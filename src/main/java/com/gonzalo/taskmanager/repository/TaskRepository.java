package com.gonzalo.taskmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gonzalo.taskmanager.model.Task;
import com.gonzalo.taskmanager.model.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // Encontrar todas las tareas de un usuario
    List<Task> findByUser(User user);
    
    // Encontrar una tarea específica de un usuario
    Optional<Task> findByIdAndUser(Long id, User user);
    
    // Contar tareas por usuario y estado
    long countByUserAndCompleted(User user, boolean completed);
}