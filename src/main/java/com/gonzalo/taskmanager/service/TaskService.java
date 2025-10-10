package com.gonzalo.taskmanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gonzalo.taskmanager.model.Task;
import com.gonzalo.taskmanager.model.User;
import com.gonzalo.taskmanager.repository.TaskRepository;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository repo;

    public List<Task> getAllTasks() {
        List<Task> tasks = repo.findAll();
        System.out.println("📋 Total tasks in DB: " + tasks.size());
        tasks.forEach(task -> {
            System.out.println("   Task " + task.getId() + ": '" + task.getTitle() + "' - User: " + 
                (task.getUser() != null ? task.getUser().getEmail() : "NO USER"));
        });
        return tasks;
    }

    public List<Task> getTasksByUser(User user) {
        System.out.println("🔍 Getting tasks for user: " + user.getEmail());
        List<Task> tasks = repo.findByUser(user);
        System.out.println("✅ Found " + tasks.size() + " tasks for user: " + user.getEmail());
        tasks.forEach(task -> {
            System.out.println("   Task " + task.getId() + ": '" + task.getTitle() + "'");
        });
        return tasks;
    }

    public Task saveTaskForUser(Task task, User user) {
        System.out.println("💾 SAVING TASK FOR USER: " + user.getEmail());
        System.out.println("   Task title: " + task.getTitle());
        System.out.println("   Task description: " + task.getDescription());
        System.out.println("   Task ID before save: " + task.getId());
        
        // CRÍTICO: Crear una NUEVA instancia de Task
        Task newTask = new Task();
        newTask.setTitle(task.getTitle().trim());
        newTask.setDescription(task.getDescription() != null ? task.getDescription().trim() : null);
        newTask.setCompleted(false);
        newTask.setUser(user);
        
        System.out.println("   New task before save - ID: " + newTask.getId() + ", Title: " + newTask.getTitle());
        
        Task savedTask = repo.save(newTask);
        
        System.out.println("✅ TASK SAVED SUCCESSFULLY");
        System.out.println("   Saved task ID: " + savedTask.getId());
        System.out.println("   Saved task title: " + savedTask.getTitle());
        System.out.println("   User: " + savedTask.getUser().getEmail());
        
        return savedTask;
    }

    public Task updateTaskForUser(Long id, Task taskDetails, User user) {
        System.out.println("🔄 Updating task: " + id + " for user: " + user.getEmail());
        
        Task task = repo.findByIdAndUser(id, user).orElse(null);
        if (task == null) {
            System.out.println("❌ Task not found or user doesn't have access: " + id);
            return null;
        }

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setCompleted(taskDetails.isCompleted());
        
        Task updatedTask = repo.save(task);
        System.out.println("✅ Task updated: " + updatedTask.getId());
        return updatedTask;
    }

    public boolean deleteTaskForUser(Long id, User user) {
        System.out.println("🗑️ Deleting task: " + id + " for user: " + user.getEmail());
        
        Task task = repo.findByIdAndUser(id, user).orElse(null);
        if (task == null) {
            System.out.println("❌ Task not found or user doesn't have access: " + id);
            return false;
        }
        
        repo.delete(task);
        System.out.println("✅ Task deleted: " + id);
        return true;
    }

    // ... (otros métodos se mantienen igual)
    public Task getTaskById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Task getTaskByIdAndUser(Long id, User user) {
        return repo.findByIdAndUser(id, user).orElse(null);
    }

    public boolean deleteTask(Long id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }

    public void deleteAllTasks() {
        repo.deleteAll();
        System.out.println("✅ All tasks deleted");
    }

    public long getTaskCount() {
        return repo.count();
    }
}