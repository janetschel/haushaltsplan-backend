package com.heroku.backend.controller;

import com.heroku.backend.entity.TaskEntity;
import com.heroku.backend.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController {
    public final TaskService taskService;

    public TaskController(TaskService userService) {
        this.taskService = userService;
    }

    @GetMapping("/getDocuments")
    public ResponseEntity<List<TaskEntity>> getDocuments() {
        return taskService.getDocuments();
    }
}
