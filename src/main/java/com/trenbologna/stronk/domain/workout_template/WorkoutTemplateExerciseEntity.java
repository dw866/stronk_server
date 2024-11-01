package com.trenbologna.stronk.domain.workout_template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.utils.Enums;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@ToString(exclude = {"workout", "exercise"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class WorkoutTemplateExerciseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    @JsonIgnore
    private WorkoutTemplateEntity workout;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    @JsonIgnore
    private ExerciseEntity exercise;

    @Embedded
    private ExerciseTemplateDetail exerciseDetail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkoutTemplateExerciseEntity)) return false;
        WorkoutTemplateExerciseEntity that = (WorkoutTemplateExerciseEntity) o;
        return Objects.equals(exerciseDetail, that.exerciseDetail);
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Embeddable
    public static class ExerciseTemplateDetail {
        @ElementCollection
        @CollectionTable(name="exercise_sets", joinColumns=@JoinColumn(name="exercise_id"))
        private List<ExerciseSet> sets;

        @ElementCollection
        @MapKeyColumn(name="intensity")
        @Column(name="time")
        @CollectionTable(name="rest_time",
                joinColumns=@JoinColumn(name="set_id"))
        private Map<Enums.Intensity, Integer> restTimeIntensityMap;

        @Enumerated(EnumType.STRING)
        private Enums.WeightUnit metric;

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
