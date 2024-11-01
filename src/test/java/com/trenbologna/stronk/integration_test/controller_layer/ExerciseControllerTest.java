package com.trenbologna.stronk.integration_test.controller_layer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.domain.exercise.dto.GetExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PostExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PatchExerciseDTO;
import com.trenbologna.stronk.domain.exercise.mapper.GetExerciseDtoMapper;
import com.trenbologna.stronk.domain.exercise.repository.ExerciseRepository;
import com.trenbologna.stronk.domain.workout_session.dto.PostWorkoutSessionDTO;
import com.trenbologna.stronk.domain.workout_session.repository.WorkoutSessionExerciseRepository;
import com.trenbologna.stronk.domain.workout_template.dto.PostWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.repository.WorkoutTemplateExerciseRepository;
import com.trenbologna.stronk.integration_test.controller_layer.test_data.exercise.PatchExerciseDtoTestData;
import com.trenbologna.stronk.integration_test.controller_layer.test_data.exercise.PostExerciseDtoTestData;
import com.trenbologna.stronk.integration_test.controller_layer.test_data.workout_template.PostWorkoutTemplateDtoTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import utils.JsonUtil;
import utils.RequestMappingUtil;

import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExerciseControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ExerciseRepository exerciseRepository;
    @Autowired private WorkoutSessionExerciseRepository workoutSessionExerciseRepository;
    @Autowired private WorkoutTemplateExerciseRepository workoutTemplateExerciseRepository;

    private String exerciseRequestMapping = RequestMappingUtil.exerciseRequestMapping;
    private String workoutTemplateRequestMapping = RequestMappingUtil.workoutTemplateRequestMapping;
    private String workoutSessionRequestMapping = RequestMappingUtil.workoutSessionRequestMapping;

    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void getExercises_Successful() throws Exception {
        final List<ExerciseEntity> exercises = new ArrayList<>();
        //Insert Exercises into database
        exerciseRepository.saveAll(Arrays.asList(
                ExerciseEntity.builder().name("Pull Ups").bodyPart("Back").category("Weighted").build(),
                ExerciseEntity.builder().name("Push Ups").bodyPart("Chest").category("Body Weight").build()
        ));
        //Retrieve Exercises
        String actualResult = mockMvc
                .perform(MockMvcRequestBuilders.get(exerciseRequestMapping))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                //Retrieve Exercises in Repository
                .andDo(result -> {
                    Assertions.assertFalse(exerciseRepository.findAll().isEmpty());
                    exercises.addAll(exerciseRepository.findAll());
                })
                .andReturn().getResponse().getContentAsString();
        //Verify Content of Exercises Retrieved
        List<GetExerciseDTO> expectedResult = GetExerciseDtoMapper.mapToDTO(exercises);
        Assertions.assertEquals(expectedResult, GetExerciseDtoMapper.mapToDTO(
                JsonUtil.getMapper().readValue(actualResult, new TypeReference<List<ExerciseEntity>>() {}))
        );
    }
    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void postExercise_Successful() throws Exception {
        final List<Long> exerciseIds = new ArrayList<>();
        final PostExerciseDTO underTest = PostExerciseDtoTestData.createExercise();
        //Create Exercise
        mockMvc.perform(MockMvcRequestBuilders.post(exerciseRequestMapping)
                        .content(asJsonString(underTest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                //Confirm Exercise Creation and Retrieve the ID
                .andDo(result -> {
                    Assertions.assertFalse(exerciseRepository.findAll().isEmpty());
                    exerciseIds.add(exerciseRepository.findAll().get(0).getId());
                })
                //Verify Content of Exercise
                .andDo(result -> {
                    GetExerciseDTO expectedResult = PostExerciseDtoTestData.getExpected(underTest, exerciseIds.get(0));
                    String actualResult = mockMvc
                            .perform(MockMvcRequestBuilders.get(exerciseRequestMapping +"/"+exerciseIds.get(0)))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn().getResponse().getContentAsString();
                    Assertions.assertEquals(expectedResult, GetExerciseDtoMapper
                            .mapToDTO(JsonUtil.getMapper().readValue(actualResult, ExerciseEntity.class)));
                });
    }
    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void patchExercise_Successful() throws Exception {
        final List<Long> exerciseIds = new ArrayList<>();
        final PostExerciseDTO postExerciseDTO = PostExerciseDtoTestData.createExercise();
        final PatchExerciseDTO underTest = PatchExerciseDtoTestData.patchExercise();
        //Create Exercise
        mockMvc.perform(MockMvcRequestBuilders.post(exerciseRequestMapping)
                        .content(asJsonString(postExerciseDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //Confirm Exercise Creation and Retrieve the ID
                .andDo(result -> {
                    Assertions.assertFalse(exerciseRepository.findAll().isEmpty());
                    exerciseIds.add(exerciseRepository.findAll().get(0).getId());
                })
                //Modify Exercise
                .andDo(result -> {
                    mockMvc.perform(MockMvcRequestBuilders.patch(exerciseRequestMapping+"/"+exerciseIds.get(0))
                                    .content(asJsonString(underTest))
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andDo(MockMvcResultHandlers.print())
                            .andExpect(MockMvcResultMatchers.status().isOk());
                })
                //Verify Exercise Modification
                .andDo(result -> {
                    GetExerciseDTO expectedResult = PatchExerciseDtoTestData.getExpected(underTest, exerciseIds.get(0));
                    String actualResult = mockMvc
                            .perform(MockMvcRequestBuilders.get(exerciseRequestMapping +"/1"))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn().getResponse().getContentAsString();
                    Assertions.assertEquals(expectedResult, GetExerciseDtoMapper
                            .mapToDTO(JsonUtil.getMapper().readValue(actualResult, ExerciseEntity.class)));
                });
    }

    /**
     * Delete an ExerciseEntity
     * @result ExerciseEntity gets removed,
     *         and related WorkoutSessionExerciseEntity gets removed
     *         and related WorkoutTemplateExerciseEntity gets removed
     */
    //todo
//    @Test
//    @Transactional(isolation = Isolation.SERIALIZABLE)
//    public void deleteExercise_ExerciseExist_Successful() throws Exception {
//        //Create Exercise, WorkoutTemplate, and WorkoutSession
//        ExerciseEntity exercise = exerciseRepository.save(ExerciseEntity.builder().name("Pull Ups").bodyPart("Back").category("Weighted").build());
//        final List<Long> exerciseIds = new ArrayList<>();
//        final PostWorkoutTemplateDTO postWorkoutTemplateDTO = PostWorkoutTemplateDtoTestData
//                .createWorkoutWithSingleExercise(exercise.getId());
//        PostWorkoutSessionDTO postWorkoutSessionDTO = DTOFactory.postWorkoutSessionDTO;
//
//        mockMvc.perform(MockMvcRequestBuilders.post(workoutTemplateRequestMapping)
//                        .content(asJsonString(postWorkoutTemplateDTO))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(result -> {
//                    mockMvc.perform(MockMvcRequestBuilders.post(workoutSessionRequestMapping)
//                                    .content(asJsonString(postWorkoutSessionDTO))
//                                    .contentType(MediaType.APPLICATION_JSON))
//                            .andExpect(MockMvcResultMatchers.status().isOk());
//                })
//                //Delete Exercise
//                .andDo(result -> {
//                    mockMvc.perform(MockMvcRequestBuilders.delete(exerciseRequestMapping+"/1")
//                                    .contentType(MediaType.APPLICATION_JSON))
//                            .andExpect(MockMvcResultMatchers.status().isOk());
//                });
//
//
//        //Verify that Exercise, WorkoutSessionExercise, and WorkoutTemplateExercise do not exist
//        Assertions.assertThrows(ResourceNotFoundException.class, ()->
//                exerciseRepository.findById(1L).orElseThrow(()->new ResourceNotFoundException())
//        );
//        Assertions.assertThrows(ResourceNotFoundException.class, ()->
//                workoutSessionExerciseRepository.findById(1L).orElseThrow(()->new ResourceNotFoundException())
//        );
//        Assertions.assertThrows(ResourceNotFoundException.class, ()->
//                workoutTemplateExerciseRepository.findById(1L).orElseThrow(()->new ResourceNotFoundException())
//        );
//    }
    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteExercise_ExerciseDoesNotExist_Failure() throws Exception {
        //Delete Exercise
        mockMvc
                .perform(MockMvcRequestBuilders.delete(exerciseRequestMapping+"/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private class DTOFactory {
        //For Exercise
        static final PostExerciseDTO postExerciseDTO;
        static final PatchExerciseDTO patchExerciseDTO;
        static {
            postExerciseDTO = PostExerciseDTO.builder()
                    .name("Pull Ups")
                    .bodyPart("Back")
                    .category("Weighted")
                    .build();
            patchExerciseDTO = PatchExerciseDTO.builder()
                    .name("Push Ups")
                    .bodyPart("Chest")
                    .category("Weighted")
                    .build();

        }
        //For WorkoutSession
        static final PostWorkoutSessionDTO postWorkoutSessionDTO;
        static{
            Calendar past = Calendar.getInstance();
            Calendar future = Calendar.getInstance();
            past.set(1700, 01, 01);
            future.set(2077, 01, 01);
            postWorkoutSessionDTO = PostWorkoutSessionDTO.builder()
                    .name("")
                    .note("")
                    .start(past.getTime())
                    .end(future.getTime())
                    .exercises(Arrays.asList(
                            PostWorkoutSessionDTO.ExerciseDTO.builder()
                                    .id(1L)
                                    .build()))
                    .build();
        }

}

