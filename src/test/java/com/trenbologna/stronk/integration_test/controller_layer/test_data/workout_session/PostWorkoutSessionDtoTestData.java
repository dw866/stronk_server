package com.trenbologna.stronk.integration_test.controller_layer.test_data.workout_session;

import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.domain.workout_session.dto.GetWorkoutSessionDTO;
import com.trenbologna.stronk.domain.workout_session.dto.PostWorkoutSessionDTO;
import com.trenbologna.stronk.domain.workout_template.dto.GetWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.dto.PostWorkoutTemplateDTO;
import com.trenbologna.stronk.utils.CustomPair;
import com.trenbologna.stronk.utils.Enums;

import java.util.*;
import java.util.stream.Collectors;

public class PostWorkoutSessionDtoTestData {
    public static PostWorkoutSessionDTO createWorkoutSession(Long associatedExerciseId){
        List<PostWorkoutSessionDTO.ExerciseSetDTO> sets = Arrays.asList(
                PostWorkoutSessionDTO.ExerciseSetDTO.builder().weight(10.0F).rpe(5).reps(10).note("Strong as fuck boi").intensity(Enums.Intensity.WARM_UP).build(),
                PostWorkoutSessionDTO.ExerciseSetDTO.builder().weight(40.0F).rpe(0).reps(10).note("Strong as fuck boi").intensity(Enums.Intensity.FAILURE).build());
        Map<Enums.Intensity, Integer> restTimeIntensity = new HashMap<Enums.Intensity, Integer>() {{
            put(Enums.Intensity.WARM_UP, 60);
            put(Enums.Intensity.FAILURE, 120);
        }};
        return PostWorkoutSessionDTO.builder()
                .name("Push/Pull Workout")
                .note("Workout With Single Exercise")
                .exercises(Arrays.asList(
                        PostWorkoutSessionDTO.ExerciseDTO.builder()
                                .id(associatedExerciseId)
                                .sets(sets)
                                .build()
                )).build();
    }
//    public static GetWorkoutSessionDTO getExpected(PostWorkoutSessionDTO underTest, List<Long> workoutExerciseIds, List<ExerciseEntity> exercises){
//        List<CustomPair<Long, GetWorkoutSessionDTO.ExerciseDTO>> expectedExercises = new ArrayList<>();
//        for (int i = 0; i < workoutExerciseIds.size(); i++){
//            expectedExercises.add(CustomPair.of(workoutExerciseIds.get(i), getExpectedExercise(underTest.getExercises().get(i), exercises.get(i))));
//        }
//        return GetWorkoutSessionDTO.builder()
//                .id(underTest.getId())
//                .name(underTest.getName())
//                .note(underTest.getNote())
//                .exercises(expectedExercises)
//                .build();
//    }
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
