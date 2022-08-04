package com.example.user.management.microserivce.controller;

import com.example.user.management.microserivce.model.Role;
import com.example.user.management.microserivce.model.User;
import com.example.user.management.microserivce.repository.UserRepository;
import com.example.user.management.microserivce.service.UserService;
import com.example.user.management.microserivce.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
//@RequestMapping("api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private Environment env;

    @Value("${spring.application.name}")
    private String serviceId;

    @GetMapping("/service/port")
    private String getPorts() {
        return "Service port number:" + env.getProperty("local.server.port");
    }

    @GetMapping("service/instances")
    public ResponseEntity<?> getInstances() {
        return new ResponseEntity<>(discoveryClient.getInstances(serviceId),HttpStatus.OK);
    }

    //get services by eureka
    @GetMapping("/service/services")
    public List<String> getServices() {
        System.out.println("discoveryClient = " + discoveryClient.getServices());
        return discoveryClient.getServices();
    }

    @PostMapping("service/registration")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        user.setRole(Role.USER);
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/service/login")
    public ResponseEntity<?> getUser(Principal principal) {

        if (principal == null || principal.getName() == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return ResponseEntity.ok(userService.findByUsername(principal.getName()));
    }

    @PostMapping("service/names")
    public ResponseEntity<?> getNamesOfUsers(@RequestBody List<Long> idList) {
        return ResponseEntity.ok(userService.findUsers(idList));
    }

    @GetMapping("service/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("it is working");
    }

    @GetMapping("service")
    public List<User> getU() {
        return userRepository.findAll();
    }

}
