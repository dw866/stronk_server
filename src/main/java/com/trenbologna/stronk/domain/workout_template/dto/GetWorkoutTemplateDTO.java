package com.trenbologna.stronk.domain.workout_template.dto;

import com.trenbologna.stronk.utils.CustomPair;
import com.trenbologna.stronk.utils.Enums;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class GetWorkoutTemplateDTO {
    private Long id;
    private String name;
    private String note;
    private List<CustomPair<Long, GetWorkoutTemplateDTO.ExerciseDTO>> exercises;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    @ToString
    @Builder
    public static class ExerciseDTO {
        private Long id;
        private String name;
        private String bodyPart;
        private String category;
        private List<GetWorkoutTemplateDTO.ExerciseSetDTO> sets;
        private Map<Enums.Intensity, Integer> restTimeIntensityMap;
        private Enums.WeightUnit metric;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
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
