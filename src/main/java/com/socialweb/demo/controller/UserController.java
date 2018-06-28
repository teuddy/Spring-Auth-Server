package com.socialweb.demo.controller;


import com.socialweb.demo.exception.AppException;
import com.socialweb.demo.model.RoleNames;
import com.socialweb.demo.model.Roles;
import com.socialweb.demo.model.User;
import com.socialweb.demo.payload.SignUpRequest;
import com.socialweb.demo.payload.SignupUserResponse;
import com.socialweb.demo.repository.RoleRepository;
import com.socialweb.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @PostMapping("/createuser")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signupRequest) {


        if(userRepository.existsByUsername(signupRequest.getUsername())) {
            return new ResponseEntity(new SignupUserResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signupRequest.getEmail())) {
            return new ResponseEntity(new SignupUserResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }



        // Creating user's account
        User user = new User(signupRequest.getName(), signupRequest.getUsername(),// se crea un objeto User con el Objeto tipo SignUp
                signupRequest.getEmail(), signupRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));// encripta la contrasena


        Roles userRole = roleRepository.findByName(RoleNames.ROLE_USER)//se busca en el repositorio el rol User
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));// se le pone ese rol a el usuario

        User result = userRepository.save(user);//se guarda el usuario

        URI location = ServletUriComponentsBuilder// se manda un uri para llevar al usuario al link donde se creo
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new SignupUserResponse(true, "User registered successfully"));
    }



}

