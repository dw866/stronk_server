package com.trenbologna.stronk.integration_test.controller_layer.test_data.exercise;

import com.trenbologna.stronk.domain.exercise.dto.GetExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PostExerciseDTO;

public class PostExerciseDtoTestData {
    public static PostExerciseDTO createExercise(){
        return PostExerciseDTO.builder()
                .name("Pull Ups")
                .category("Weighted")
                .bodyPart("Back")
                .build();
    }
    public static GetExerciseDTO getExpected(PostExerciseDTO postExerciseDTO, Long exerciseId){
        return GetExerciseDTO.builder()
                .id(exerciseId)
                .name(postExerciseDTO.getName())
                .category(postExerciseDTO.getCategory())
                .bodyPart(postExerciseDTO.getBodyPart())
                .build();
    }
}
