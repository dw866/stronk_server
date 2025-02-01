package com.trenbologna.stronk.integration_test.controller_layer.test_data.exercise;

import com.trenbologna.stronk.domain.exercise.dto.GetExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PatchExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PostExerciseDTO;

public class PatchExerciseDtoTestData {
    public static PatchExerciseDTO patchExercise(){
        return PatchExerciseDTO.builder()
                .name("Patched Pull Ups")
                .category("Patched Weighted")
                .bodyPart("Patched Back")
                .build();
    }
    public static GetExerciseDTO getExpected(PatchExerciseDTO patchExerciseDTO, Long exerciseId){
        return GetExerciseDTO.builder()
                .id(exerciseId)
                .name(patchExerciseDTO.getName())
                .category(patchExerciseDTO.getCategory())
                .bodyPart(patchExerciseDTO.getBodyPart())
                .build();
    }
}
