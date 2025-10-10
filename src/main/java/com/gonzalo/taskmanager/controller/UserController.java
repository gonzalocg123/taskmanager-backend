package com.gonzalo.taskmanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gonzalo.taskmanager.model.User;
import com.gonzalo.taskmanager.security.JwtService;
import com.gonzalo.taskmanager.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User newUser = userService.register(user);
            
            // Genera el token JWT
            String token = jwtService.generateToken(newUser.getEmail());
            
            // Crea respuesta con token y usuario
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", newUser);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Verifica credenciales del usuario
            User authenticatedUser = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            
            // Genera el token JWT
            String token = jwtService.generateToken(authenticatedUser.getEmail());
            
            // Crea respuesta con token y usuario
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", authenticatedUser);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Clase DTO para el login
    public static class LoginRequest {
        private String email;
        private String password;
        
        public String getEmail() { 
            return email; 
        }
        
        public void setEmail(String email) { 
            this.email = email; 
        }
        
        public String getPassword() { 
            return password; 
        }
        
        public void setPassword(String password) { 
            this.password = password; 
        }
    }
}