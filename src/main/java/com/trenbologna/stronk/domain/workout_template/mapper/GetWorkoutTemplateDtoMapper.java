package com.trenbologna.stronk.domain.workout_template.mapper;

import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.domain.workout_template.WorkoutTemplateEntity;
import com.trenbologna.stronk.domain.workout_template.WorkoutTemplateExerciseEntity;
import com.trenbologna.stronk.domain.workout_template.dto.GetWorkoutTemplateDTO;
import com.trenbologna.stronk.utils.CustomPair;

import java.util.List;
import java.util.stream.Collectors;

public class GetWorkoutTemplateDtoMapper {
    public static GetWorkoutTemplateDTO mapToDTO(WorkoutTemplateEntity workoutTemplate, List<WorkoutTemplateExerciseEntity> workoutTemplateExercises){
        return GetWorkoutTemplateDTO.builder()
                .id(workoutTemplate.getId())
                .name(workoutTemplate.getName())
                .note(workoutTemplate.getNote())
                .exercises(workoutTemplateExercises.stream().map(wte->{
                    ExerciseEntity exercise = wte.getExerciseTemplate();
                    WorkoutTemplateExerciseEntity.ExerciseTemplateDetail exerciseDetail = wte.getExerciseDetail();
                    return CustomPair.of(
                            wte.getId(),
                            GetWorkoutTemplateDTO.ExerciseDTO.builder()
                                    .id(exercise.getId())
                                    .name(exercise.getName())
                                    .bodyPart(exercise.getBodyPart())
                                    .category(exercise.getCategory())
                                    .sets(exerciseDetail.getSets().stream().map(e->{
                                        return GetWorkoutTemplateDTO.ExerciseSetDTO.builder()
                                                .intensity(e.getIntensity())
                                                .reps(e.getReps())
                                                .rpe(e.getRpe())
                                                .weight(e.getWeight())
                                                .note(e.getNote())
                                                .build();
                                    }).collect(Collectors.toList()))
                                    .restTimeIntensityMap(exerciseDetail.getRestTimeIntensityMap())
                                    .metric(exerciseDetail.getMetric())
                                    .build());
                }).collect(Collectors.toList()))
                .build();

    }

}
