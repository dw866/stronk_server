package com.trenbologna.stronk.domain.workout_session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trenbologna.stronk.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class WorkoutSessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "note")
    private String note;

    @Column(name = "session_start")
    private Date sessionStart;

    @Column(name = "session_end")
    private Date sessionEnd;

    @OneToMany(mappedBy = "workoutSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<WorkoutSessionExerciseEntity> workoutSessionExercises;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
