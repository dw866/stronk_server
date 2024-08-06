package com.trenbologna.stronk.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "EXERCISE_TABLE")
public class ExerciseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "bodyPart")
    private String bodyPart;

    @Column(name = "category")
    private String category;

    public ExerciseEntity(){

    }
    public ExerciseEntity(String name, String bodyPart, String category){
        this.name = name;
        this.bodyPart = bodyPart;
        this.category = category;
    }

}

