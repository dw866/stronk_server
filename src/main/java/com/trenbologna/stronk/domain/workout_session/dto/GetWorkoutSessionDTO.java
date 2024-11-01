package com.trenbologna.stronk.domain.workout_session.dto;

import com.trenbologna.stronk.domain.workout_session.WorkoutSessionExerciseEntity;
import com.trenbologna.stronk.utils.Enums;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class GetWorkoutSessionDTO {
    private String name;
    private String note;
    private Date start;
    private Date end;
    private List<GetWorkoutSessionDTO.ExerciseSessionDTO> exercises;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    @ToString
    @Builder
    public static class ExerciseSessionDTO {
        private Long exerciseId;
        private List<GetWorkoutSessionDTO.ExerciseSessionSetDTO> sets;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    @ToString
    @Builder
    public static class ExerciseSessionSetDTO {
        private Enums.Intensity intensity;
        private Float weight;
        private Integer reps;
        private Integer rpe;
        private String note;
    }
}
