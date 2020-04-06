package com.heroku.backend.controller;

import com.heroku.backend.entity.TaskEntity;
import com.heroku.backend.exception.InvalidAuthenticationTokenException;
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
    public ResponseEntity<List<TaskEntity>> getDocuments(@RequestParam("token") String authToken) throws InvalidAuthenticationTokenException {
        return taskService.getDocuments(authToken);
    }

    @PostMapping("/addDocument")
    public ResponseEntity<String> addDocument(@RequestBody TaskEntity taskEntity,
                                              @RequestParam("token") String authToken) throws InvalidAuthenticationTokenException {
        return taskService.addDocument(taskEntity, authToken);
    }

    @PutMapping("/updateDocument")
    public ResponseEntity<String> updateDocument(@RequestBody TaskEntity taskEntity,
                                                 @RequestParam("token") String authToken) throws InvalidAuthenticationTokenException {
        return taskService.updateDocument(taskEntity, authToken);
    }

    @DeleteMapping("/deleteDocument")
    public ResponseEntity<String> deleteDocument(@RequestParam("id") String id,
                                                 @RequestParam("token") String authToken) throws InvalidAuthenticationTokenException {
        return taskService.deleteDocument(id, authToken);
    }
}
