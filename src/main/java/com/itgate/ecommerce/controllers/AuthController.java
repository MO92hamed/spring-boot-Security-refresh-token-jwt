package com.itgate.ecommerce.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.itgate.ecommerce.exception.TokenRefreshException;
import com.itgate.ecommerce.repository.RoleRepository;
import com.itgate.ecommerce.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.itgate.ecommerce.models.ERole;
import com.itgate.ecommerce.models.RefreshToken;
import com.itgate.ecommerce.models.Role;
import com.itgate.ecommerce.models.User;
import com.itgate.ecommerce.payload.request.LoginRequest;
import com.itgate.ecommerce.payload.request.SignupRequest;
import com.itgate.ecommerce.payload.request.TokenRefreshRequest;
import com.itgate.ecommerce.payload.response.JwtResponse;
import com.itgate.ecommerce.payload.response.MessageResponse;
import com.itgate.ecommerce.payload.response.TokenRefreshResponse;
import com.itgate.ecommerce.security.jwt.JwtUtils;
import com.itgate.ecommerce.security.services.RefreshTokenService;
import com.itgate.ecommerce.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  RefreshTokenService refreshTokenService;

  @Autowired
  private JavaMailSender mailSender;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());
    if (user.get().getConfirm() == true){
      SecurityContextHolder.getContext().setAuthentication(authentication);

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      String jwt = jwtUtils.generateJwtToken(userDetails);

      List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
              .collect(Collectors.toList());

      RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

      return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
              userDetails.getUsername(), userDetails.getEmail(), roles));
    }else {
      return new ResponseEntity<>("User in not confirmed",HttpStatus.NOT_FOUND);
    }

  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);
    }

    user.setRoles(roles);
    user.setConfirm(true);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(user -> {
          String token = jwtUtils.generateTokenFromUsername(user.getUsername());
          return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
        })
        .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
            "Refresh token is not in database!"));
  }
  
  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = userDetails.getId();
    refreshTokenService.deleteByUserId(userId);
    return ResponseEntity.ok(new MessageResponse("Log out successful!"));
  }

  @PostMapping("/forgetpassword")
  public HashMap<String, String> resetPassword(String email) throws MessagingException {
    HashMap message = new HashMap();
    User userExistiong = userRepository.findByEmail(email);
    if (userExistiong == null){
      message.put("User", "user not found");
      return message;
    }

    UUID token = UUID.randomUUID();
    userExistiong.setPasswordResetToken(token.toString());
    userExistiong.setId(userExistiong.getId());

    //Mail
    MimeMessage message1 = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message1);
    helper.setSubject("forget password");
    helper.setFrom("admin@gmail.com");
    helper.setTo(userExistiong.getEmail());
    helper.setText("<HTML><body> <a href=\"http://localhost:4200/savepassword/"
            + userExistiong.getPasswordResetToken()+ "\">Forget Password<a/></body></HTML>", true);
    mailSender.send(message1);

    userRepository.saveAndFlush(userExistiong);
    message.put("User", "user found and mail is send");
    return  message;
  }

  @PostMapping("/resetpassword/{passwordResetToken}")
  public HashMap<String, String> savePassword(@PathVariable String passwordResetToken, String newPassword){
    HashMap message = new HashMap();
    User userExisting = userRepository.findByPasswordResetToken(passwordResetToken);

    if (userExisting != null){
      userExisting.setId(userExisting.getId());
      userExisting.setPassword(new BCryptPasswordEncoder().encode(newPassword));
      userExisting.setPasswordResetToken(null);
      userRepository.save(userExisting);
      message.put("Reset Password", "Processed");
      return message;
    }else {
      message.put("Reset Password", "Failed");
      return message;
    }
  }

}
