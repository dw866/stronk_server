package com.trenbologna.stronk.domain.workout_template.mapper;

import com.trenbologna.stronk.domain.workout_template.WorkoutTemplateEntity;
import com.trenbologna.stronk.domain.workout_template.WorkoutTemplateExerciseEntity;
import com.trenbologna.stronk.domain.workout_template.dto.PostWorkoutTemplateDTO;

import java.util.stream.Collectors;

public class PostWorkoutTemplateDtoMapper {
    public static WorkoutTemplateExerciseEntity.ExerciseTemplateDetail mapFromDTO(PostWorkoutTemplateDTO.ExerciseDTO exerciseDTO){
        return WorkoutTemplateExerciseEntity.ExerciseTemplateDetail.builder()
                .restTimeIntensityMap(exerciseDTO.getRestTimeIntensityMap())
                .sets(exerciseDTO.getSets().stream().map(e->{
                    return mapFromDTO(e);
                }).collect(Collectors.toList()))
                .metric(exerciseDTO.getMetric())
                .build();
    }
    public static WorkoutTemplateExerciseEntity.ExerciseSet mapFromDTO(PostWorkoutTemplateDTO.ExerciseSetDTO exerciseSetDTO){
        return WorkoutTemplateExerciseEntity.ExerciseSet.builder()
                .note(exerciseSetDTO.getNote())
                .weight(exerciseSetDTO.getWeight())
                .rpe(exerciseSetDTO.getRpe())
                .reps(exerciseSetDTO.getReps())
                .intensity(exerciseSetDTO.getIntensity())
                .build();
    }
    public static WorkoutTemplateEntity mapFromDTO(PostWorkoutTemplateDTO postWorkoutTemplateDTO){
        return WorkoutTemplateEntity.builder()
                .id(postWorkoutTemplateDTO.getId())
                .name(postWorkoutTemplateDTO.getName())
                .note(postWorkoutTemplateDTO.getNote())
                .build();
    }
}
