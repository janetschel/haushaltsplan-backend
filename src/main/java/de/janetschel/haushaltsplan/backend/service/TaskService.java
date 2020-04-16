package de.janetschel.haushaltsplan.backend.service;

import de.janetschel.haushaltsplan.backend.entity.TaskEntity;
import de.janetschel.haushaltsplan.backend.enums.Feedback;
import de.janetschel.haushaltsplan.backend.exception.InvalidAuthenticationTokenException;
import de.janetschel.haushaltsplan.backend.exception.LoginExpiredException;
import de.janetschel.haushaltsplan.backend.exception.UserNotLoggedInException;
import de.janetschel.haushaltsplan.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    public final TaskRepository taskRepository;

    @Value("${authentication.user}")
    private String authtoken;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public ResponseEntity<List<TaskEntity>> getDocuments(String authToken)
            throws InvalidAuthenticationTokenException, LoginExpiredException, UserNotLoggedInException {

        LoginService.checkIfLoginIsExpired();

        if (!authToken.equals(authtoken)) {
            throw new InvalidAuthenticationTokenException();
        }

        return ResponseEntity.ok(taskRepository.findAll());
    }

    public ResponseEntity<String> addDocument(TaskEntity taskEntity, String authToken)
            throws InvalidAuthenticationTokenException, LoginExpiredException, UserNotLoggedInException {

        LoginService.checkIfLoginIsExpired();

        if (!authToken.equals(authtoken)) {
            throw new InvalidAuthenticationTokenException();
        }

        String id = taskEntity.getId();
        id = id == null ? "0" : id;

        TaskEntity documentToAdd = taskRepository.findById(id).orElse(null);

        if (documentToAdd != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Task with ID '" + id + "' does already exist and could therefore not be created");
        }

        taskRepository.insert(taskEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body("Task added successfully");
    }

    public ResponseEntity<String> updateDocument(TaskEntity taskEntity, String authToken)
            throws InvalidAuthenticationTokenException, LoginExpiredException, UserNotLoggedInException {

        LoginService.checkIfLoginIsExpired();

        if (!authToken.equals(authtoken)) {
            throw new InvalidAuthenticationTokenException();
        }

        String id = taskEntity.getId();
        TaskEntity documentToUpdate = taskRepository.findById(id).orElse(null);

        if (documentToUpdate == null) {
            addDocument(taskEntity, authToken);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Task with ID '" + id + "' does not exist and could therefore not be updated. It was created instead");
        }

        documentToUpdate.setId(null);
        documentToUpdate.setDay(taskEntity.getDay());
        documentToUpdate.setPic(taskEntity.getPic());
        documentToUpdate.setBlame(taskEntity.getBlame());
        documentToUpdate.setDone(taskEntity.isDone());
        documentToUpdate.setFeedback(taskEntity.getFeedback());

        taskRepository.deleteById(id);
        taskRepository.save(documentToUpdate);

        return ResponseEntity.ok("Task updated successfully");
    }

    public ResponseEntity<String> addFeedbackToDocument(String id, Feedback feedback, String authToken)
            throws InvalidAuthenticationTokenException, LoginExpiredException, UserNotLoggedInException {

        LoginService.checkIfLoginIsExpired();

        if (!authToken.equals(authtoken)) {
            throw new InvalidAuthenticationTokenException();
        }

        TaskEntity documentToAddFeedback = taskRepository.findById(id).orElse(null);

        if (documentToAddFeedback == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Could not add feedback to task. Reason: Task with ID '" + id + "' does not exist");
        }

        if (!documentToAddFeedback.isDone()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body("Could not add feedback to task. Reason: Task with ID '" + id + "' is not completed yet");
        }

        documentToAddFeedback.setFeedback(feedback);

        taskRepository.deleteById(id);
        taskRepository.save(documentToAddFeedback);

        return ResponseEntity.ok("Feedback successfully added to task");
    }

    public ResponseEntity<String> deleteDocument(String id, String authToken)
            throws InvalidAuthenticationTokenException, LoginExpiredException, UserNotLoggedInException {

        LoginService.checkIfLoginIsExpired();

        if (!authToken.equals(authtoken)) {
            throw new InvalidAuthenticationTokenException();
        }

        TaskEntity documentToDelete = taskRepository.findById(id).orElse(null);

        if (documentToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Task with ID '" + id + "' does not exist and could therefore not be deleted");
        }

        taskRepository.delete(documentToDelete);
        return ResponseEntity.ok("Task deleted successfully");
    }
}
