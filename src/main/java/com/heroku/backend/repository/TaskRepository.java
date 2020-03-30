package com.heroku.backend.repository;

import com.heroku.backend.entity.TaskEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<TaskEntity, String> {

}
