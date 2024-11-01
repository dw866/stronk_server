package com.trenbologna.stronk.domain.exercise;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trenbologna.stronk.domain.workout_template.WorkoutTemplateExerciseEntity;
import com.trenbologna.stronk.domain.workout_session.WorkoutSessionExerciseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ExerciseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "bodyPart")
    private String bodyPart;

    @Column(name = "category")
    private String category;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<WorkoutTemplateExerciseEntity> workoutExercises;

    @OneToMany(mappedBy = "exerciseSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<WorkoutSessionExerciseEntity> workoutSessionExercises;
}

