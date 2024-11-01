package com.trenbologna.stronk.domain.workout_session.dto;

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
public class PostWorkoutSessionDTO {
    private String name;
    private String note;
    private Date start;
    private Date end;
    private List<ExerciseDTO> exercises;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    @ToString
    @Builder
    public static class ExerciseDTO {
        private Long id;
        private List<ExerciseSetDTO> sets;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Builder
    public static class ExerciseSetDTO {
        private Enums.Intensity intensity;
        private Float weight;
        private Integer reps;
        private Integer rpe;
        private String note;
    }
}
