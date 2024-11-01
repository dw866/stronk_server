package com.trenbologna.stronk.integration_test.controller_layer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.domain.exercise.dto.GetExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PostExerciseDTO;
import com.trenbologna.stronk.domain.exercise.repository.ExerciseRepository;
import com.trenbologna.stronk.domain.workout_template.dto.GetWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.dto.PostWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.dto.PutWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.repository.WorkoutTemplateExerciseRepository;
import com.trenbologna.stronk.domain.workout_template.repository.WorkoutTemplateRepository;
import com.trenbologna.stronk.integration_test.controller_layer.test_data.workout_template.PostWorkoutTemplateDtoTestData;
import com.trenbologna.stronk.integration_test.controller_layer.test_data.workout_template.PutWorkoutTemplateDtoTestData;
import com.trenbologna.stronk.utils.CustomPair;
import com.trenbologna.stronk.utils.Enums;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import utils.JsonUtil;
import utils.RequestMappingUtil;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorkoutTemplateControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private EntityManager entityManager;
    @Autowired private WorkoutTemplateRepository workoutTemplateRepository;
    @Autowired private WorkoutTemplateExerciseRepository workoutTemplateExerciseRepository;
    @Autowired private ExerciseRepository exerciseRepository;

    private String workoutTemplateRequestMapping = RequestMappingUtil.workoutTemplateRequestMapping;

    @BeforeAll
    void setUp() throws Exception {
        String exerciseRequestMapping = RequestMappingUtil.exerciseRequestMapping;
        List<PostExerciseDTO> exerciseDTOS = new ArrayList<>(Arrays.asList(
                PostExerciseDTO.builder().id(1L).name("Pull Ups").bodyPart("Back").category("Weighted").build(),
                PostExerciseDTO.builder().id(2L).name("Push Ups").bodyPart("Chest").category("Body Weight").build()
        ));
        //Create Exercises
        for (PostExerciseDTO exerciseDTO: exerciseDTOS){
            mockMvc
                    .perform(MockMvcRequestBuilders.post(exerciseRequestMapping)
                            .content(asJsonString(exerciseDTO))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }
//    @BeforeEach
//    void resetSequenceGeneration(){
//        entityManager.createNativeQuery("ALTER SEQUENCE workout_template_entity_seq RESTART WITH 1").executeUpdate();
//        entityManager.createNativeQuery("ALTER SEQUENCE workout_template_exercise_entity_seq RESTART WITH 1").executeUpdate();
//    }

    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE)
    void postWorkoutTemplate_CreateWorkoutWithSingleExercise_Successful() throws Exception {
        final Long associatedExerciseId = 1L;
        final List<Long> workoutTemplateIds = new ArrayList<>();
        final PostWorkoutTemplateDTO underTest = PostWorkoutTemplateDtoTestData
                .createWorkoutWithSingleExercise(associatedExerciseId);
        //Create Workout
        mockMvc.perform(MockMvcRequestBuilders.post(workoutTemplateRequestMapping)
                        .content(asJsonString(underTest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                //Confirm WorkoutTemplate Creation and Retrieve the ID
                .andDo(result -> {
                    Assertions.assertFalse(workoutTemplateRepository.findAll().isEmpty());
                    workoutTemplateIds.add(workoutTemplateRepository.findAll().get(0).getId());
                })
                //Verify Content of WorkoutTemplate
                .andDo(result -> {
                    List<ExerciseEntity> associatedExercises = new ArrayList<>();
                    List<Long> workoutExerciseIds = workoutTemplateExerciseRepository
                            .findByWorkoutId(workoutTemplateIds.get(0)).stream().map(e->{
                                associatedExercises.add(e.getExercise());
                                return e.getId();
                            }).collect(Collectors.toList());
                    GetWorkoutTemplateDTO expectedResult = PostWorkoutTemplateDtoTestData.getExpected(
                            underTest.setId(workoutTemplateIds.get(0)),
                            workoutExerciseIds,
                            associatedExercises
                    );
                    String actualResult = mockMvc
                            .perform(MockMvcRequestBuilders.get(workoutTemplateRequestMapping +"/"+workoutTemplateIds.get(0)))
                            .andDo(print())
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn().getResponse().getContentAsString();
                    Assertions.assertEquals(expectedResult,
                            JsonUtil.getMapper().readValue(actualResult, GetWorkoutTemplateDTO.class));
                });

    }
    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE)
    void putWorkoutTemplate_ReplaceWorkout_Successful() throws Exception {
        final Long associatedExerciseId = 1L;
        final List<Long> workoutTemplateIds = new ArrayList<>();
        final PostWorkoutTemplateDTO postWorkoutTemplateDTO = PostWorkoutTemplateDtoTestData
                .createWorkoutWithSingleExercise(associatedExerciseId);
        final PutWorkoutTemplateDTO underTest = PutWorkoutTemplateDtoTestData
                .createModifiedWorkoutWithSingleExercise(associatedExerciseId);
        //Set Up Workout
        mockMvc.perform(MockMvcRequestBuilders.post(workoutTemplateRequestMapping)
                        .content(asJsonString(postWorkoutTemplateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //Confirm WorkoutTemplate Creation and Retrieve the ID
                .andDo(result -> {
                    Assertions.assertFalse(workoutTemplateRepository.findAll().isEmpty());
                    workoutTemplateIds.add(workoutTemplateRepository.findAll().get(0).getId());
                })
                //Replace Workout
                .andDo(result -> {
                    mockMvc.perform(MockMvcRequestBuilders.put(workoutTemplateRequestMapping +"/"+workoutTemplateIds.get(0))
                                    .content(asJsonString(underTest))
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            //Verify WorkoutTemplate Modification
                            .andDo(putWorkoutTemplateResult -> {
                                List<ExerciseEntity> associatedExercises = new ArrayList<>();
                                List<Long> workoutExerciseIds = workoutTemplateExerciseRepository
                                        .findByWorkoutId(workoutTemplateIds.get(0)).stream().map(e->{
                                            associatedExercises.add(e.getExercise());
                                            return e.getId();
                                        }).collect(Collectors.toList());
                                GetWorkoutTemplateDTO expectedResult = PutWorkoutTemplateDtoTestData.getExpected(
                                        underTest.setId(workoutTemplateIds.get(0)),
                                        workoutExerciseIds,
                                        associatedExercises
                                );
                                String actualResult = mockMvc
                                        .perform(MockMvcRequestBuilders.get(workoutTemplateRequestMapping +"/"+workoutTemplateIds.get(0)))
                                        .andDo(print())
                                        .andExpect(MockMvcResultMatchers.status().isOk())
                                        .andReturn().getResponse().getContentAsString();
                                Assertions.assertEquals(expectedResult,
                                        JsonUtil.getMapper().readValue(actualResult, GetWorkoutTemplateDTO.class));
                            });
                });
    }
    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE)
    void putWorkoutTemplate_ReplaceNonExistingWorkout_ThrowsNotFoundException() throws Exception {
        final Long associatedExerciseId = 1L;
        final Long nonExistingWorkoutTemplateId = 99999L;
        final PutWorkoutTemplateDTO putWorkoutTemplateDTO = PutWorkoutTemplateDtoTestData
                .createModifiedWorkoutWithSingleExercise(associatedExerciseId);
        //Replace Workout
        mockMvc
                .perform(MockMvcRequestBuilders.put(workoutTemplateRequestMapping +"/"+nonExistingWorkoutTemplateId)
                        .content(asJsonString(putWorkoutTemplateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    /**
     * Delete a WorkoutTemplateEntity
     * @result WorkoutTemplateEntity gets removed,
     *         and related WorkoutTemplateExerciseEntity gets removed
     */
    @Test
    @Transactional
    void deleteWorkoutTemplate_DeleteExistingWorkout_Successful() throws Exception {
        final Long associatedExerciseId = 1L;
        final List<Long> workoutTemplateIds = new ArrayList<>();
        final PostWorkoutTemplateDTO postWorkoutTemplateDTO = PostWorkoutTemplateDtoTestData
                .createWorkoutWithSingleExercise(associatedExerciseId);
        //Create WorkoutTemplate
        mockMvc
                .perform(MockMvcRequestBuilders.post(workoutTemplateRequestMapping)
                        .content(asJsonString(postWorkoutTemplateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //Confirm WorkoutTemplate Creation and Retrieve the ID
                .andDo(result -> {
                    Assertions.assertFalse(workoutTemplateRepository.findAll().isEmpty());
                    workoutTemplateIds.add(workoutTemplateRepository.findAll().get(0).getId());
                })
                //Delete WorkoutTemplate
                .andDo(result -> {
                    mockMvc
                            .perform(MockMvcRequestBuilders.delete(workoutTemplateRequestMapping+"/"+workoutTemplateIds.get(0)))
                            .andExpect(MockMvcResultMatchers.status().isOk());
                    //Verify WorkoutTemplateEntity and WorkoutTemplateExerciseEntity Deletion
                    Assertions.assertFalse(workoutTemplateRepository.findById(workoutTemplateIds.get(0)).isPresent());
                    Assertions.assertTrue(workoutTemplateExerciseRepository.findByWorkoutId(workoutTemplateIds.get(0)).isEmpty());
                });
    }
    @Test
    @Disabled
    void deleteWorkoutTemplate_DeleteNonExistingWorkout_ThrowsNotFoundException(){

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

