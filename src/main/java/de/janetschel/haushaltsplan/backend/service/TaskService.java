package de.janetschel.haushaltsplan.backend.service;

import de.janetschel.haushaltsplan.backend.entity.TaskEntity;
import de.janetschel.haushaltsplan.backend.exception.InvalidAuthenticationTokenException;
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

    public ResponseEntity<List<TaskEntity>> getDocuments(String authToken) throws InvalidAuthenticationTokenException {
        if (!authToken.equals(authtoken)) {
            throw new InvalidAuthenticationTokenException();
        }

        return ResponseEntity.ok(taskRepository.findAll());
    }

    public ResponseEntity<String> addDocument(TaskEntity taskEntity, String authToken) throws InvalidAuthenticationTokenException {
        if (!authToken.equals(authtoken)) {
            throw new InvalidAuthenticationTokenException();
        }

        String id = taskEntity.getId();
        id = id == null ? "0" : id;

        String message = "Task with ID '" + id + "' does already exist and could therefore not be created";

        TaskEntity documentToAdd = taskRepository.findById(id).orElse(null);

        if (documentToAdd != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }

        taskRepository.insert(taskEntity);

        message = "Task added successfully";
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    public ResponseEntity<String> updateDocument(TaskEntity taskEntity, String authToken) throws InvalidAuthenticationTokenException {
        if (!authToken.equals(authtoken)) {
            throw new InvalidAuthenticationTokenException();
        }

        String id = taskEntity.getId();
        String message =
                "Task with ID '" + id + "' does not exist and could therefore not be updated. It was created instead";

        TaskEntity documentToUpdate = taskRepository.findById(id).orElse(null);

        if (documentToUpdate == null) {
            addDocument(taskEntity, authToken);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
        }

        documentToUpdate.setId(null);
        documentToUpdate.setDay(taskEntity.getDay());
        documentToUpdate.setPic(taskEntity.getPic());
        documentToUpdate.setBlame(taskEntity.getBlame());
        documentToUpdate.setDone(taskEntity.isDone());

        taskRepository.deleteById(id);
        taskRepository.save(documentToUpdate);

        message = "Task updated successfully";
        return ResponseEntity.ok(message);
    }

    public ResponseEntity<String> deleteDocument(String id, String authToken) throws InvalidAuthenticationTokenException {
        if (!authToken.equals(authtoken)) {
            throw new InvalidAuthenticationTokenException();
        }

        String message = "Task with ID '" + id + "' does not exist and could therefore not be deleted";

        TaskEntity documentToDelete = taskRepository.findById(id).orElse(null);

        if (documentToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }

        taskRepository.delete(documentToDelete);

        message = "Task deleted successfully";
        return ResponseEntity.ok(message);
    }
}
