package com.trenbologna.stronk.domain.exercise.mapper;

import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.domain.exercise.dto.GetExerciseDTO;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GetExerciseDtoMapper {
    public static List<GetExerciseDTO> mapToDTO(List<ExerciseEntity> exercises){
        return exercises.stream().map(e->{
                    return GetExerciseDTO.builder()
                            .id(e.getId())
                            .name(e.getName())
                            .category(e.getCategory())
                            .bodyPart(e.getBodyPart())
                            .build();
                }).collect(Collectors.toList());

    }
    public static GetExerciseDTO mapToDTO(ExerciseEntity exercise){
        return GetExerciseDTO.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .category(exercise.getCategory())
                .bodyPart(exercise.getBodyPart())
                .build();
    }
}
