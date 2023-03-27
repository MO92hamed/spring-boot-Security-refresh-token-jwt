package com.itgate.ecommerce.controllers;


import com.itgate.ecommerce.models.Customer;
import com.itgate.ecommerce.models.ERole;
import com.itgate.ecommerce.models.Role;
import com.itgate.ecommerce.models.User;
import com.itgate.ecommerce.payload.request.SignupRequest;
import com.itgate.ecommerce.payload.response.MessageResponse;
import com.itgate.ecommerce.repository.CustomerRepository;
import com.itgate.ecommerce.repository.RoleRepository;
import com.itgate.ecommerce.repository.UserRepository;
import com.itgate.ecommerce.service.impl.CustomerServiceImpl;
import com.itgate.ecommerce.utils.StorageService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CustomerController {

    private final CustomerServiceImpl customerService;

    private final Path rootLocation = Paths.get("upload-dir");
    @Autowired
    UserRepository userRepository;
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private StorageService storageService;
    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id){
        return customerService.findCustomerById(id);
    }

    @GetMapping
    public List<Customer> getAllCustomers(){
        return customerService.findCustomers();
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteCustomer(@PathVariable Long id){
        Map<String, Boolean> response = new HashMap<>();
        customerService.deleteCustomer(id);
        try{
            response.put("Deleted", Boolean.TRUE);
            return response;
        }catch(Exception e){
            response.put("failed to delete", Boolean.FALSE);
            return response;
        }
    }

    @PostMapping
    public Customer addCustomer(@RequestBody Customer customer){
        return customerService.createCustomer(customer);
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer){
        Customer c1 = customerService.findCustomerById(id);

        if (c1 != null)
            return customerService.updateCustomer(customer);
        else
            throw new RuntimeException("Failed to update ");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid  SignupRequest signUpRequest, @RequestParam("file") MultipartFile file) throws MessagingException {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Customer customer = new Customer(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getAddress(), signUpRequest.getCity());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            Role customerRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(customerRole);
        }

        try {
            String fileName = Integer.toString(new Random().nextInt(1000000000));
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'), file.getOriginalFilename().length());
            String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'));
            String original = name + fileName + ext;
            Files.copy(file.getInputStream(), this.rootLocation.resolve(original));
            customer.setImage(original);
        } catch (Exception e) {
            throw new RuntimeException("FAIL File Problem BackEnd !");
        }

        customer.setRoles(roles);
        customer.setConfirm(false);

        String from = "admin@gmail.com";
        String to = signUpRequest.getEmail();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Complete Registration");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setText("<HTML><body> <a href=\"http://localhost:8081/customer/updateconfirm?email="
                + signUpRequest.getEmail()+ "\">VERIFY<a/></body></HTML>", true);

        mailSender.send(message);
        customerRepository.save(customer);
        return ResponseEntity.ok(new MessageResponse("Customer registered successfully!"));
    }

    @GetMapping ("/updateconfirm")
    public String updateConfirm( String email){
        Customer customer = customerRepository.findByEmail(email);
        System.out.println(customer);
        if (customer != null){
            customer.setConfirm(true);
            customerRepository.saveAndFlush(customer);
            return "Congratulation email confirm. WELCOME TO MY APP..!";
        }else {
            throw new RuntimeException("FAIL");
        }
    }
}
