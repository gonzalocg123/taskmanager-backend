package com.gonzalo.taskmanager.controller;

import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gonzalo.taskmanager.model.User;
import com.gonzalo.taskmanager.repository.UserRepository;
import com.gonzalo.taskmanager.security.JwtService;

@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user) {
        System.out.println("=== REGISTER ENDPOINT CALLED ===");
        System.out.println("User received: " + user.getUsername() + " - " + user.getEmail());
        
        // Verificar si el usuario ya existe
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        User savedUser = userRepository.save(user);
        
        String token = jwtService.generateToken(user.getEmail());
        System.out.println("User registered successfully: " + savedUser.getId());
        
        // CORREGIDO: Enviar el username correcto, no el email
        return Map.of(
            "token", token, 
            "message", "Usuario registrado exitosamente",
            "username", savedUser.getUsername() // ← Cambiado de getEmail() a getUsername()
        );
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {
        System.out.println("=== LOGIN ENDPOINT CALLED ===");
        System.out.println("Login attempt for email: " + user.getEmail());
        
        User dbUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        
        String token = jwtService.generateToken(dbUser.getEmail());
        System.out.println("Login successful for user: " + dbUser.getUsername());
        
        // CORREGIDO: Enviar el username correcto
        return Map.of(
            "token", token, 
            "message", "Login exitoso",
            "username", dbUser.getUsername() // ← Cambiado de getEmail() a getUsername()
        );
    }

    // Endpoint de prueba
    @GetMapping("/test")
    public String authTest() {
        return "Auth endpoint working!";
    }
}