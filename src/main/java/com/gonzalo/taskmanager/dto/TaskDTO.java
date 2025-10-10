package com.gonzalo.taskmanager.dto;

import lombok.Data;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private String userEmail;
    private String username;

    public TaskDTO(Long id, String title, String description, boolean completed, String userEmail, String username) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.userEmail = userEmail;
        this.username = username;
    }
}