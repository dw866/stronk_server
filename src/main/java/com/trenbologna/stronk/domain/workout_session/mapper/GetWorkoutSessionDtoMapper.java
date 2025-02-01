package com.trenbologna.stronk.domain.workout_session.mapper;

import com.trenbologna.stronk.domain.workout_session.WorkoutSessionEntity;
import com.trenbologna.stronk.domain.workout_session.dto.GetWorkoutSessionDTO;
import com.trenbologna.stronk.domain.workout_session.dto.PostWorkoutSessionDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

public class GetWorkoutSessionDtoMapper {
    public static List<GetWorkoutSessionDTO> mapToDTO(List<WorkoutSessionEntity> workoutSessions){
        return workoutSessions.stream().map(e->{
            return mapToDTO(e);
        }).collect(Collectors.toList());
    }
    public static GetWorkoutSessionDTO mapToDTO(WorkoutSessionEntity workout){
        return GetWorkoutSessionDTO.builder()
                .start(workout.getSessionStart())
                .end(workout.getSessionEnd())
                .name(workout.getName())
                .note(workout.getName())
                .exercises(workout.getWorkoutSessionExercises().stream().map(wse->{
                    return GetWorkoutSessionDTO.ExerciseSessionDTO.builder()
                            .exerciseId(wse.getId())
                            .sets(wse.getExerciseSessionDetail().getSets().stream().map(e->{
                                return GetWorkoutSessionDTO.ExerciseSessionSetDTO.builder()
                                        .intensity(e.getIntensity())
                                        .reps(e.getReps())
                                        .rpe(e.getRpe())
                                        .weight(e.getWeight())
                                        .note(e.getNote())
                                        .build();
                            }).collect(Collectors.toList()))
                            .build();
                }).collect(Collectors.toList()))
                .build();
    }
    public static WorkoutSessionEntity mapFromDTO(PostWorkoutSessionDTO postWorkoutSessionDTO) {
        return WorkoutSessionEntity.builder()
                .name(postWorkoutSessionDTO.getName())
                .note(postWorkoutSessionDTO.getNote())
                .sessionStart(postWorkoutSessionDTO.getStart())
                .sessionEnd(postWorkoutSessionDTO.getEnd())
                .build();
    }
}
