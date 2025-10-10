package com.gonzalo.taskmanager.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gonzalo.taskmanager.dto.TaskDTO;
import com.gonzalo.taskmanager.model.Task;
import com.gonzalo.taskmanager.model.User;
import com.gonzalo.taskmanager.service.TaskService;
import com.gonzalo.taskmanager.service.UserService;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    // Obtener tareas del usuario actual
    @GetMapping
    public List<TaskDTO> getUserTasks(@RequestParam String userEmail) {
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userEmail));
        
        List<Task> tasks = taskService.getTasksByUser(user);
        
        // Convertir a DTO para evitar referencia circular
        return tasks.stream()
                .map(task -> new TaskDTO(
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.isCompleted(),
                    task.getUser().getEmail(),
                    task.getUser().getUsername()
                ))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody Task task, @RequestParam String userEmail) {
        try {
            User user = userService.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userEmail));
            
            // Asegurarnos de que la tarea no tenga ID previo
            task.setId(null);
            task.setCompleted(false);
            
            Task savedTask = taskService.saveTaskForUser(task, user);
            
            // Convertir a DTO antes de retornar
            TaskDTO taskDTO = new TaskDTO(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.isCompleted(),
                savedTask.getUser().getEmail(),
                savedTask.getUser().getUsername()
            );
            
            return ResponseEntity.ok(taskDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody Task taskDetails, @RequestParam String userEmail) {
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userEmail));
        
        Task updatedTask = taskService.updateTaskForUser(id, taskDetails, user);
        
        if (updatedTask != null) {
            // Convertir a DTO antes de retornar
            TaskDTO taskDTO = new TaskDTO(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.isCompleted(),
                updatedTask.getUser().getEmail(),
                updatedTask.getUser().getUsername()
            );
            return ResponseEntity.ok(taskDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @RequestParam String userEmail) {
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userEmail));
        
        boolean deleted = taskService.deleteTaskForUser(id, user);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Endpoint para obtener información del usuario
    @GetMapping("/user-info")
public ResponseEntity<User> getCurrentUserInfo(@RequestParam String userEmail) {
    User user = userService.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userEmail));
    
    // No enviar la contraseña por seguridad
    user.setPassword(null);
    
    // Debug: verificar qué se está enviando
    System.out.println("User info for: " + userEmail);
    System.out.println("Username: " + user.getUsername());
    System.out.println("Email: " + user.getEmail());
    
    return ResponseEntity.ok(user);
}

    // Endpoint de prueba
    @GetMapping("/test")
    public String test() {
        return "Tasks API is working!";
    }

    // Endpoints de debug (solo para desarrollo)
    
    @GetMapping("/debug/all")
    public List<Task> getAllTasksDebug() {
        return taskService.getAllTasks();
    }

    @GetMapping("/debug/stats")
    public String getTaskStats() {
        List<Task> allTasks = taskService.getAllTasks();
        return "Total tasks in database: " + allTasks.size();
    }

    @DeleteMapping("/debug/clear-all")
    public ResponseEntity<String> clearAllTasks() {
        try {
            List<Task> allTasks = taskService.getAllTasks();
            taskService.deleteAllTasks();
            return ResponseEntity.ok("Deleted " + allTasks.size() + " tasks");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}