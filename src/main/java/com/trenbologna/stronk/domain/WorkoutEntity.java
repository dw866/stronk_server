package com.trenbologna.stronk.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "WORKOUT_TABLE")
public class WorkoutEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "note")
    private String note;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private List<ExerciseEntity> exercises;

    public WorkoutEntity(){

    }
    public WorkoutEntity(String name, String note){
        this.name = name;
        this.note = note;
    }

}
