package com.heroku.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@Document(collection = "tasks")
public class TaskEntity {
    @Id
    public String id;

    public String day;
    public String chore;
    public String pic;
    public String blame;
    public boolean done;

    public TaskEntity(String day, String chore, String pic, String blame, boolean done) {
        this.day = day;
        this.chore = chore;
        this.pic = pic;
        this.blame = blame;
        this.done = done;
    }
}
