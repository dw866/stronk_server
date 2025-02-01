package com.trenbologna.stronk.domain.workout_template.mapper;

import com.trenbologna.stronk.domain.workout_template.WorkoutTemplateExerciseEntity;
import com.trenbologna.stronk.domain.workout_template.dto.PutWorkoutTemplateDTO;

import java.util.stream.Collectors;

public class PutWorkoutTemplateDtoMapper {
    public static WorkoutTemplateExerciseEntity.ExerciseTemplateDetail mapFromDTO(PutWorkoutTemplateDTO.ExerciseDTO exerciseDTO){
        return WorkoutTemplateExerciseEntity.ExerciseTemplateDetail.builder()
                .restTimeIntensityMap(exerciseDTO.getRestTimeIntensityMap())
                .sets(exerciseDTO.getSets().stream().map(e->{
                    return mapFromDTO(e);
                }).collect(Collectors.toList()))
                .metric(exerciseDTO.getMetric())
                .build();
    }
    public static WorkoutTemplateExerciseEntity.ExerciseSet mapFromDTO(PutWorkoutTemplateDTO.ExerciseSetDTO exerciseSetDTO){
        return WorkoutTemplateExerciseEntity.ExerciseSet.builder()
                .note(exerciseSetDTO.getNote())
                .weight(exerciseSetDTO.getWeight())
                .rpe(exerciseSetDTO.getRpe())
                .reps(exerciseSetDTO.getReps())
                .intensity(exerciseSetDTO.getIntensity())
                .build();
    }
}
