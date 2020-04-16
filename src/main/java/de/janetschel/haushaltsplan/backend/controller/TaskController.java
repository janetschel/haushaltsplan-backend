package de.janetschel.haushaltsplan.backend.controller;

import de.janetschel.haushaltsplan.backend.entity.TaskEntity;
import de.janetschel.haushaltsplan.backend.enums.Feedback;
import de.janetschel.haushaltsplan.backend.exception.InvalidAuthenticationTokenException;
import de.janetschel.haushaltsplan.backend.exception.LoginExpiredException;
import de.janetschel.haushaltsplan.backend.exception.UserNotLoggedInException;
import de.janetschel.haushaltsplan.backend.service.TaskService;
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
    public ResponseEntity<List<TaskEntity>> getDocuments(@RequestHeader("Auth-Token") String authToken)
            throws InvalidAuthenticationTokenException, LoginExpiredException, UserNotLoggedInException {
        return taskService.getDocuments(authToken);
    }

    @PostMapping("/addDocument")
    public ResponseEntity<String> addDocument(@RequestBody TaskEntity taskEntity, @RequestHeader("Auth-Token") String authToken)
            throws InvalidAuthenticationTokenException, LoginExpiredException, UserNotLoggedInException {
        return taskService.addDocument(taskEntity, authToken);
    }

    @PutMapping("/updateDocument")
    public ResponseEntity<String> updateDocument(@RequestBody TaskEntity taskEntity, @RequestHeader("Auth-Token") String authToken)
            throws InvalidAuthenticationTokenException, LoginExpiredException, UserNotLoggedInException {
        return taskService.updateDocument(taskEntity, authToken);
    }

    @PutMapping("/updateDocument/addFeedback/{id}")
    public ResponseEntity<String> addFeedbackToDocument(@RequestParam("feedback") Feedback feedback,
                                                        @PathVariable("id") String id,
                                                        @RequestHeader("Auth-Token") String authToken)
            throws InvalidAuthenticationTokenException, LoginExpiredException, UserNotLoggedInException {
        return taskService.addFeedbackToDocument(id, feedback, authToken);
    }

    @DeleteMapping("/deleteDocument")
    public ResponseEntity<String> deleteDocument(@RequestParam("id") String id, @RequestHeader("Auth-Token") String authToken)
            throws InvalidAuthenticationTokenException, LoginExpiredException, UserNotLoggedInException {
        return taskService.deleteDocument(id, authToken);
    }
}
