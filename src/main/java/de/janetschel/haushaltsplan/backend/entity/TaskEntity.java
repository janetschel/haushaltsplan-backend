package de.janetschel.haushaltsplan.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tasks")
public class TaskEntity {
    @Id
    private String id;

    private String day;
    private String chore;
    private String pic;
    private String blame;
    private boolean done;

    public TaskEntity(String day, String chore, String pic, String blame, boolean done) {
        this.day = day;
        this.chore = chore;
        this.pic = pic;
        this.blame = blame;
        this.done = done;
    }
}
