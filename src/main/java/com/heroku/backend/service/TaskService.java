package com.heroku.backend.service;

import com.heroku.backend.MongoDBConfiguration;
import com.heroku.backend.ValueConfig;
import com.heroku.backend.entity.TaskEntity;
import com.heroku.backend.exception.InvalidAuthenticationTokenException;
import com.heroku.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    public final TaskRepository taskRepository;
    private MongoOperations mongoOperations;

    @Value("${authentication.user}")
    private String userAuthentication;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MongoDBConfiguration.class);
        mongoOperations = (MongoOperations) applicationContext.getBean("mongoTemplate");
    }

    public ResponseEntity<List<TaskEntity>> getDocuments(String authToken) throws InvalidAuthenticationTokenException {
        if (!authToken.equals(ValueConfig.authenticationToken)) {
            throw new InvalidAuthenticationTokenException();
        }

        return ResponseEntity.ok(taskRepository.findAll());
    }

    public ResponseEntity<String> addDocument(TaskEntity taskEntity, String authToken) throws InvalidAuthenticationTokenException {
        if (!authToken.equals(ValueConfig.authenticationToken)) {
            throw new InvalidAuthenticationTokenException();
        }

        String id = taskEntity.getId();
        id = id == null ? "0" : id;

        String message = "Task with ID '" + id + "' does already exist and could therefore not be created";

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        TaskEntity documentToAdd = mongoOperations.findOne(query, TaskEntity.class);

        if (documentToAdd != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }

        taskRepository.insert(taskEntity);

        message = "Task added successfully";
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    public ResponseEntity<String> updateDocument(TaskEntity taskEntity, String authToken) throws InvalidAuthenticationTokenException {
        if (!authToken.equals(ValueConfig.authenticationToken)) {
            throw new InvalidAuthenticationTokenException();
        }

        String id = taskEntity.getId();
        String message =
                "Task with ID '" + id + "' does not exist and could therefore not be updated. It was created instead";

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        TaskEntity documentToUpdate = mongoOperations.findOne(query, TaskEntity.class);

        if (documentToUpdate == null) {
            addDocument(taskEntity, authToken);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
        }

        documentToUpdate.setBlame(taskEntity.getBlame());
        documentToUpdate.setPic(taskEntity.getPic());
        documentToUpdate.setDone(taskEntity.isDone());
        mongoOperations.save(documentToUpdate);

        message = "Task updated successfully";
        return ResponseEntity.ok(message);
    }

    public ResponseEntity<String> deleteDocument(String id, String authToken) throws InvalidAuthenticationTokenException {
        if (!authToken.equals(userAuthentication)) {
            throw new InvalidAuthenticationTokenException();
        }

        String message = "Task with ID '" + id + "' does not exist and could therefore not be deleted";

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        TaskEntity documentToDelete = mongoOperations.findOne(query, TaskEntity.class);

        if (documentToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }

        mongoOperations.remove(documentToDelete);

        message = "Task deleted successfully";
        return ResponseEntity.ok(message);
    }
}
