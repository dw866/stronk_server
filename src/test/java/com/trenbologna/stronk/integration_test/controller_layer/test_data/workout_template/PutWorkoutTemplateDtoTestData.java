package com.trenbologna.stronk.integration_test.controller_layer.test_data.workout_template;

import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.domain.workout_template.dto.GetWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.dto.PostWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.dto.PutWorkoutTemplateDTO;
import com.trenbologna.stronk.utils.CustomPair;
import com.trenbologna.stronk.utils.Enums;

import java.util.*;
import java.util.stream.Collectors;

public class PutWorkoutTemplateDtoTestData {
    public static PutWorkoutTemplateDTO createModifiedWorkoutWithSingleExercise(Long associatedExerciseId){
        List<PutWorkoutTemplateDTO.ExerciseSetDTO> sets = Arrays.asList(
                PutWorkoutTemplateDTO.ExerciseSetDTO.builder().weight(99999.0F).rpe(0).reps(99999).note("Strong as fuck boi").intensity(Enums.Intensity.FAILURE).build(),
                PutWorkoutTemplateDTO.ExerciseSetDTO.builder().weight(99999.0F).rpe(0).reps(99999).note("Strong as fuck boi").intensity(Enums.Intensity.FAILURE).build());
        Map<Enums.Intensity, Integer> restTimeIntensity = new HashMap<Enums.Intensity, Integer>(){{
            put(Enums.Intensity.FAILURE, 99999);
            put(Enums.Intensity.FAILURE, 99999);
        }};
        return PutWorkoutTemplateDTO.builder()
                .name("Modified Pull/Push Workout")
                .note("Modified Workout With Single Exercise")
                .exercises(Arrays.asList(
                    PutWorkoutTemplateDTO.ExerciseDTO.builder()
                            .id(associatedExerciseId)
                            .metric(Enums.WeightUnit.IMPERIAL)
                            .restTimeIntensityMap(restTimeIntensity)
                            .sets(sets)
                            .build()
                ))
                .build();
    }

    public static GetWorkoutTemplateDTO getExpected(PutWorkoutTemplateDTO underTest, List<Long> workoutExerciseIds, List<ExerciseEntity> exercises){
        List<CustomPair<Long, GetWorkoutTemplateDTO.ExerciseDTO>> expectedExercises = new ArrayList<>();
        for (int i = 0; i < workoutExerciseIds.size(); i++){
            expectedExercises.add(CustomPair.of(workoutExerciseIds.get(i), getExpectedExercise(underTest.getExercises().get(i), exercises.get(i))));
        }
        return GetWorkoutTemplateDTO.builder()
                .id(underTest.getId())
                .name(underTest.getName())
                .note(underTest.getNote())
                .exercises(expectedExercises)
                .build();
    }
    private static GetWorkoutTemplateDTO.ExerciseDTO getExpectedExercise(PutWorkoutTemplateDTO.ExerciseDTO exerciseDetail, ExerciseEntity exercise){
        return GetWorkoutTemplateDTO.ExerciseDTO.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .bodyPart(exercise.getBodyPart())
                .category(exercise.getCategory())
                .sets(exerciseDetail.getSets().stream().map(set->{
                    return GetWorkoutTemplateDTO.ExerciseSetDTO.builder()
                            .intensity(set.getIntensity())
                            .reps(set.getReps())
                            .rpe(set.getRpe())
                            .weight(set.getWeight())
                            .note(set.getNote())
                            .build();
                }).collect(Collectors.toList()))
                .restTimeIntensityMap(exerciseDetail.getRestTimeIntensityMap())
                .metric(exerciseDetail.getMetric())
                .build();
    }
}
