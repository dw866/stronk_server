package com.trenbologna.stronk.unit_test.service_layer;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {

//    @Mock private ExerciseRepository exerciseRepository;
//
//    @InjectMocks private ExerciseServiceImpl underTest;
//
//    @BeforeEach
//    public void setup(){
//        underTest = new ExerciseServiceImpl(exerciseRepository);
//    }
//
//    @AfterEach
//    public void tearDown(){}
//
//    @Test
//    @Disabled
//    void getExercise_Exercise(){
//
//    }
//
//    @Test
//    void createExercise_ExerciseDoesNotExist_ExerciseCreationSuccess(){
//        //given
//        ExerciseEntity exercise = ExerciseEntity.builder().id(1L).build();
//        given(exerciseRepository.findById(exercise.getId())).willReturn(Optional.empty());
//        //when
//        underTest.createExercise(exercise);
//        //then
//        ArgumentCaptor<ExerciseEntity> exerciseEntityArgumentCaptor = ArgumentCaptor.forClass(ExerciseEntity.class);
//        verify(exerciseRepository).save(exerciseEntityArgumentCaptor.capture());
//        assertThat(exerciseEntityArgumentCaptor.getValue()).isEqualTo(exercise);
//    }
//
//    @Test
//    @Disabled
//    void createExercise_ExerciseExists_ExerciseCreationFailed(){
//        //given
//        ExerciseEntity exercise = ExerciseEntity.builder().id(1L).build();
//        given(exerciseRepository.findById(exercise.getId())).willReturn(Optional.of(exercise));
//        //when & then
//        verify(exerciseRepository, never()).save(any());
//        assertThatThrownBy(() -> underTest.createExercise(exercise))
//                .isInstanceOf(ResponseStatusException.class)
//                .hasMessageContaining(String.valueOf(HttpStatus.CONFLICT))
//                .hasMessageContaining("Exercise Entity Already Exists");
//    }
//    @Test
//    @Disabled
//    void patchExercise_ExerciseExists_ExercisePatchSuccessful(){
//
//
//    }
//    @Test
//    @Disabled
//    void patchExercise_ExerciseDoesNotExists_ExercisePatchFailed(){
//
//    }
}
