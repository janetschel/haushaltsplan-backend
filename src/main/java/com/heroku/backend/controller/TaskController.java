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

    @GetMapping("/addDocument")
    public ResponseEntity<String> addDocument(@RequestParam("day") String day,
                                              @RequestParam("chore") String chore,
                                              @RequestParam("pic") String pic,
                                              @RequestParam("blame") String blame,
                                              @RequestParam("done") boolean done,
                                              @RequestParam("token") String authToken) throws InvalidAuthenticationTokenException {
        TaskEntity taskEntity = new TaskEntity(day, chore, pic, blame, done);
        return taskService.addDocument(taskEntity, authToken);
    }

    @GetMapping("/updateDocument")
    public ResponseEntity<String> updateDocument(@RequestParam("id") String id,
                                                 @RequestParam("day") String day,
                                                 @RequestParam("chore") String chore,
                                                 @RequestParam("pic") String pic,
                                                 @RequestParam("blame") String blame,
                                                 @RequestParam("done") boolean done,
                                                 @RequestParam("token") String authToken) throws InvalidAuthenticationTokenException {
        TaskEntity taskEntity = new TaskEntity(id, day, chore, pic, blame, done);
        return taskService.updateDocument(taskEntity, authToken);
    }

    @GetMapping("/deleteDocument")
    public ResponseEntity<String> deleteDocument(@RequestParam("id") String id,
                                                 @RequestParam("token") String authToken) throws InvalidAuthenticationTokenException {
        return taskService.deleteDocument(id, authToken);
    }
}
