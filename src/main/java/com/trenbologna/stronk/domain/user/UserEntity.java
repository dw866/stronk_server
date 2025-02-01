package com.trenbologna.stronk.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.domain.workout_session.WorkoutSessionEntity;
import com.trenbologna.stronk.domain.workout_template.WorkoutTemplateEntity;
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
public class UserEntity {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "middleName")
    private String middleName;

    @Column(name = "lastName")
    private String lastName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ExerciseEntity> exercises;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<WorkoutTemplateEntity> workoutTemplates;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<WorkoutSessionEntity> workoutSessions;
}
