package com.trenbologna.stronk.domain.workout_session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.utils.Enums;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString(exclude = {"workoutSession", "exerciseSession"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class WorkoutSessionExerciseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    @JsonIgnore
    private WorkoutSessionEntity workoutSession;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    @JsonIgnore
    private ExerciseEntity exerciseSession;

    @Embedded
    private WorkoutSessionExerciseEntity.ExerciseSessionDetail exerciseSessionDetail;

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Embeddable
    public static class ExerciseSessionDetail {
        @ElementCollection
        @CollectionTable(name="exercise_session_sets", joinColumns=@JoinColumn(name="exercise_id"))
        private List<WorkoutSessionExerciseEntity.ExerciseSet> sets;
    }

    @Getter
    @Setter
    @Embeddable
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExerciseSet{
        //TODO: add annotations for validation. eg min max and size
        @Enumerated(EnumType.STRING)
        private Enums.Intensity intensity;

        @Min(0)
        private Float weight;

        @Min(0)
        private Integer reps;

        @Min(1)
        @Max(10)
        private Integer rpe;

        @Size(max = 255)
        private String note;

    }
}
