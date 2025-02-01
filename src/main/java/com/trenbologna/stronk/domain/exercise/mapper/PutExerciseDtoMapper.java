package com.trenbologna.stronk.domain.exercise.mapper;

import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.domain.exercise.dto.PatchExerciseDTO;

public class PutExerciseDtoMapper {
    public static ExerciseEntity mapFromDTO(PatchExerciseDTO patchExerciseDTO){
        return ExerciseEntity.builder()
                .name(patchExerciseDTO.getName())
                .bodyPart(patchExerciseDTO.getBodyPart())
                .category(patchExerciseDTO.getCategory())
                .build();
    }
}
