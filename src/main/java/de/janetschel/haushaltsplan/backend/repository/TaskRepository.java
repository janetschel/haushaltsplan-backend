package de.janetschel.haushaltsplan.backend.repository;

import de.janetschel.haushaltsplan.backend.entity.TaskEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<TaskEntity, String> {

}
