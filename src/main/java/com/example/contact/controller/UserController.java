package com.example.contact.controller;

import com.example.contact.dto.TokenDTO;
import com.example.contact.dto.UserDTO;
import com.example.contact.model.User;
import com.example.contact.service.TokenGenerator;
import com.example.contact.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.registerUser(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO){
        User user = userService.findUserByUsername(userDTO.getUsername());
        if(user!=null && !passwordEncoder.matches(userDTO.getPassword(), user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
        String token = tokenGenerator.generateToken();
        user.setToken(token);
        userService.updateUser(user);
        return ResponseEntity.ok(new TokenDTO(token));
    }
}
