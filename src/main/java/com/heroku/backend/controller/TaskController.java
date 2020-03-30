package com.heroku.backend.controller;

import com.heroku.backend.entity.TaskEntity;
import com.heroku.backend.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/addDocument")
    public ResponseEntity<String> addDocument(@RequestBody TaskEntity taskEntity) {
        return taskService.addDocument(taskEntity);
    }

    @PutMapping("/updateDocument")
    public ResponseEntity<String> updateDocument(@RequestBody TaskEntity taskEntity) {
        return taskService.updateDocument(taskEntity);
    }

    @DeleteMapping("/deleteDocument")
    public ResponseEntity<String> deleteDocument(@RequestParam("id") String id) {
        return taskService.deleteDocument(id);
    }
}
