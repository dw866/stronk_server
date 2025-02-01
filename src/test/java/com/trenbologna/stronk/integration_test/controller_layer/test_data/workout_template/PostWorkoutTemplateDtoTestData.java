package com.trenbologna.stronk.integration_test.controller_layer.test_data.workout_template;

import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.domain.workout_template.dto.GetWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.dto.PostWorkoutTemplateDTO;
import com.trenbologna.stronk.utils.CustomPair;
import com.trenbologna.stronk.utils.Enums;

import java.util.*;
import java.util.stream.Collectors;

public class PostWorkoutTemplateDtoTestData {
    public static PostWorkoutTemplateDTO createWorkoutWithSingleExercise(Long associatedExerciseId){
        List<PostWorkoutTemplateDTO.ExerciseSetDTO> sets = Arrays.asList(
                PostWorkoutTemplateDTO.ExerciseSetDTO.builder().weight(10.0F).rpe(5).reps(10).note("Strong as fuck boi").intensity(Enums.Intensity.WARM_UP).build(),
                PostWorkoutTemplateDTO.ExerciseSetDTO.builder().weight(40.0F).rpe(0).reps(10).note("Strong as fuck boi").intensity(Enums.Intensity.FAILURE).build());
        Map<Enums.Intensity, Integer> restTimeIntensity = new HashMap<Enums.Intensity, Integer>() {{
            put(Enums.Intensity.WARM_UP, 60);
            put(Enums.Intensity.FAILURE, 120);
        }};
        return PostWorkoutTemplateDTO.builder()
                .name("Push/Pull Workout")
                .note("Workout With Single Exercise")
                .exercises(Arrays.asList(
                        PostWorkoutTemplateDTO.ExerciseDTO.builder()
                                .id(associatedExerciseId)
                                .restTimeIntensityMap(restTimeIntensity)
                                .sets(sets)
                                .metric(Enums.WeightUnit.METRIC)
                                .build()
                )).build();
    }
    public static GetWorkoutTemplateDTO getExpected(PostWorkoutTemplateDTO underTest, List<Long> workoutExerciseIds, List<ExerciseEntity> exercises){
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
    private static GetWorkoutTemplateDTO.ExerciseDTO getExpectedExercise(PostWorkoutTemplateDTO.ExerciseDTO exerciseDetail, ExerciseEntity exercise){
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
