package com.itgate.ecommerce.controllers;



import com.itgate.ecommerce.models.ERole;
import com.itgate.ecommerce.models.Provider;
import com.itgate.ecommerce.models.Role;
import com.itgate.ecommerce.models.User;
import com.itgate.ecommerce.payload.request.SignupRequest;
import com.itgate.ecommerce.payload.response.MessageResponse;
import com.itgate.ecommerce.repository.ProviderRepository;
import com.itgate.ecommerce.repository.RoleRepository;
import com.itgate.ecommerce.repository.UserRepository;
import com.itgate.ecommerce.service.impl.ProviderServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/provider")
@CrossOrigin("*")
public class ProviderController {
    @Autowired
    ProviderServiceImpl providerService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;
    @Autowired
     ProviderRepository providerRepository;

    @GetMapping("/{id}")
    public Provider getProviderById(@PathVariable Long id){
        return providerService.findProvierById(id);
    }

    @GetMapping
    public List<Provider> getAllProviders(){
        return providerService.findProviders();
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteProvider(@PathVariable Long id){
        Map<String, Boolean> response = new HashMap<>();
        providerService.deleteProvider(id);
        try{
            response.put("Deleted", Boolean.TRUE);
            return response;
        }catch(Exception e){
            response.put("failed to delete", Boolean.FALSE);
            return response;
        }
    }

    @PostMapping
    public Provider addProvider(@RequestBody Provider provider){
        return providerService.createProvider(provider);
    }

    @PutMapping("/{id}")
    public Provider updateProvider(@PathVariable Long id, @RequestBody Provider provider){
        Provider provider1 = providerService.findProvierById(id);
        if (provider1 != null)
            return providerService.updateProvider(provider);
        else
            throw new RuntimeException("Failed to update");
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
        Provider provider = new Provider(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getMatricule(),
                signUpRequest.getService(),
                signUpRequest.getCompany());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            Role providerRole = roleRepository.findByName(ERole.ROLE_PROVIDER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(providerRole);
        }

        provider.setRoles(roles);
        provider.setConfirm(true);
        providerRepository.save(provider);

        return ResponseEntity.ok(new MessageResponse("Provider registered successfully!"));
    }
}
