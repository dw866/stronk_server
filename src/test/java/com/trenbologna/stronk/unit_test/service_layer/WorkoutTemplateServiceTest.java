package com.trenbologna.stronk.unit_test.service_layer;

import com.trenbologna.stronk.domain.exercise.repository.ExerciseRepository;
import com.trenbologna.stronk.domain.workout_template.repository.WorkoutTemplateExerciseRepository;
import com.trenbologna.stronk.domain.workout_template.repository.WorkoutTemplateRepository;
import com.trenbologna.stronk.domain.workout_template.service.WorkoutTemplateServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class WorkoutTemplateServiceTest {
    @Mock private WorkoutTemplateRepository workoutTemplateRepository;
    @Mock private ExerciseRepository exerciseRepository;
    @Mock private WorkoutTemplateExerciseRepository workoutTemplateExerciseRepository;

    @InjectMocks private WorkoutTemplateServiceImpl underTest;


    @BeforeEach
    public void setup(){
        underTest = new WorkoutTemplateServiceImpl(
                workoutTemplateRepository,
                exerciseRepository,
                workoutTemplateExerciseRepository
        );
    }
    @AfterEach
    public void tearDown(){}


//    @Test
//    void createWorkoutTemplate_WithValidWorkoutDTO_Success() {
//        ExerciseEntity exercise = ExerciseEntity.builder().id(1L).build();
//        WorkoutTemplateEntity workout = WorkoutTemplateEntity.builder().id(1L).build();
//        WorkoutTemplateExerciseEntity workoutExercise = WorkoutTemplateExerciseEntity.builder()
//                .workout(workout)
//                .exercise(exercise)
//                .build();
//        //given
//        PostWorkoutTemplateDTO workoutDTO = PostWorkoutTemplateDTO.builder()
//                .id(workout.getId())
//                .exercises(List.of(
//                        AddExerciseToWorkoutDTO.builder().exerciseToAddId(exercise.getId()).build()))
//                .build();
//        //mock behavior
//        given(workoutTemplateRepository.save(any())).willReturn(workout);
//        given(exerciseRepository.findById(exercise.getId())).willReturn(Optional.of(exercise));
//        given(workoutTemplateExerciseRepository.saveAll(anyList())).willReturn(List.of(workoutExercise));
//        //when
//        underTest.createWorkout(workoutDTO);
//        //then
//        ArgumentCaptor<WorkoutTemplateEntity> workoutEntityArgumentCaptor = ArgumentCaptor.forClass(WorkoutTemplateEntity.class);
//        ArgumentCaptor<List<WorkoutTemplateExerciseEntity>> workoutExerciseListArgumentCaptor = ArgumentCaptor.forClass(List.class);
//        verify(workoutTemplateRepository).save(workoutEntityArgumentCaptor.capture());
//        verify(workoutTemplateExerciseRepository).saveAll(workoutExerciseListArgumentCaptor.capture());
//        assertThat(workoutEntityArgumentCaptor.getValue().getId()).isEqualTo(workout.getId());
//        assertThat(workoutExerciseListArgumentCaptor.getValue()).contains(workoutExercise);
//    }
//    @Test
//    void createWorkoutTemplate_WithDuplicateExercises_Success(){
//        //todo
//        ExerciseEntity exercise = ExerciseEntity.builder().id(1L).build();
//        WorkoutTemplateEntity workout = WorkoutTemplateEntity.builder().id(1L).build();
//        WorkoutTemplateExerciseEntity workoutExercise = WorkoutTemplateExerciseEntity.builder()
//                .workout(workout)
//                .exercise(exercise)
//                .build();
//        //given
//        PostWorkoutTemplateDTO workoutDTO = PostWorkoutTemplateDTO.builder()
//                .id(workout.getId())
//                .addExerciseToWorkoutList(List.of(
//                        AddExerciseToWorkoutDTO.builder().exerciseToAddId(exercise.getId()).build(),
//                        AddExerciseToWorkoutDTO.builder().exerciseToAddId(exercise.getId()).build()))
//                .build();
//        //mock behavior
//        given(workoutTemplateRepository.save(any())).willReturn(workout);
//        given(exerciseRepository.findById(exercise.getId())).willReturn(Optional.of(exercise));
//        given(workoutTemplateExerciseRepository.saveAll(anyList())).willReturn(List.of(workoutExercise));
//        //when
//        underTest.createWorkout(workoutDTO);
//        //then
//        ArgumentCaptor<WorkoutTemplateEntity> workoutEntityArgumentCaptor = ArgumentCaptor.forClass(WorkoutTemplateEntity.class);
//        ArgumentCaptor<List<WorkoutTemplateExerciseEntity>> workoutExerciseListArgumentCaptor = ArgumentCaptor.forClass(List.class);
//        verify(workoutTemplateRepository).save(workoutEntityArgumentCaptor.capture());
//        verify(workoutTemplateExerciseRepository).saveAll(workoutExerciseListArgumentCaptor.capture());
//        assertThat(workoutEntityArgumentCaptor.getValue().getId()).isEqualTo(workout.getId());
//        assertThat(workoutExerciseListArgumentCaptor.getValue()).contains(workoutExercise);
//    }
//    @Test
//    void createWorkoutTemplate_NonExistingExerciseInWorkoutDTO_FailureThrowError(){
//        Long invalidExerciseID = 1L;
//        WorkoutTemplateEntity workout = WorkoutTemplateEntity.builder().id(1L).build();
//        //given
//        PostWorkoutTemplateDTO workoutDTO = PostWorkoutTemplateDTO.builder()
//                .id(workout.getId())
//                .addExerciseToWorkoutList(List.of(
//                        AddExerciseToWorkoutDTO.builder().exerciseToAddId(invalidExerciseID).build()))
//                .build();
//        //mock behavior
//        given(workoutTemplateRepository.save(any())).willReturn(workout);
//        given(exerciseRepository.findById(invalidExerciseID)).willReturn(Optional.empty());
//        //when & then
//        assertThatThrownBy(() -> underTest.createWorkout(workoutDTO))
//                .isInstanceOf(ResourceNotFoundException.class)
//                .hasMessageContaining("Exercise not found with id ");
//        verify(workoutTemplateRepository, never()).save(any());
//        verify(workoutTemplateExerciseRepository, never()).saveAll(any());
//    }
    @Test
    void patchWorkoutTemplate_ModifyExercises_Success(){

    }
    @Test
    @Disabled
    void patchWorkoutTemplate_AddExercises_Success(){

    }
    @Test
    @Disabled
    void patchWorkoutTemplate_AddDuplicateExercises_Success(){

    }
    @Test
    @Disabled
    void patchWorkoutTemplate_ReplaceExercises_Success(){

    }



}
