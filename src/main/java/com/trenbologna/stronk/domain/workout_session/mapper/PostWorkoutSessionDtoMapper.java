package com.trenbologna.stronk.domain.workout_session.mapper;

import com.trenbologna.stronk.domain.workout_session.WorkoutSessionExerciseEntity;
import com.trenbologna.stronk.domain.workout_session.dto.PostWorkoutSessionDTO;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostWorkoutSessionDtoMapper {
    public static WorkoutSessionExerciseEntity.ExerciseSessionDetail mapFromDTO(PostWorkoutSessionDTO.ExerciseDTO exerciseDTO) {
        return WorkoutSessionExerciseEntity.ExerciseSessionDetail.builder()
                .sets(Optional.ofNullable(exerciseDTO.getSets())
                        .map(sets -> sets.stream()
                                .map(e -> WorkoutSessionExerciseEntity.ExerciseSet.builder()
                                        .intensity(e.getIntensity())
                                        .note(e.getNote())
                                        .reps(e.getReps())
                                        .rpe(e.getRpe())
                                        .weight(e.getWeight())
                                        .build())
                                .collect(Collectors.toList()))
                        .orElseGet(ArrayList::new)) // Return an empty list if sets is null
                .build();
    }

}
