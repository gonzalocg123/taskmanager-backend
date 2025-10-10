package com.gonzalo.taskmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gonzalo.taskmanager.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Buscar usuario por email (lo usaremos para el login con JWT)
    Optional<User> findByEmail(String email);

    // Opcional: buscar usuario por username si lo necesitas más adelante
    Optional<User> findByUsername(String username);
}
