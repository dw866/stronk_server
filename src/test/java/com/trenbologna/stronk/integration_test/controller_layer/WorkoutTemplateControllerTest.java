package com.trenbologna.stronk.integration_test.controller_layer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.domain.exercise.dto.PostExerciseDTO;
import com.trenbologna.stronk.domain.exercise.repository.ExerciseRepository;
import com.trenbologna.stronk.domain.user.UserEntity;
import com.trenbologna.stronk.domain.user.dto.UserRegistrationDTO;
import com.trenbologna.stronk.domain.user.repository.UserRepository;
import com.trenbologna.stronk.domain.workout_template.dto.GetWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.dto.PostWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.dto.PutWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.repository.WorkoutTemplateExerciseRepository;
import com.trenbologna.stronk.domain.workout_template.repository.WorkoutTemplateRepository;
import com.trenbologna.stronk.integration_test.controller_layer.test_data.workout_template.PostWorkoutTemplateDtoTestData;
import com.trenbologna.stronk.integration_test.controller_layer.test_data.workout_template.PutWorkoutTemplateDtoTestData;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import utils.DatabaseUtil;
import utils.JsonUtil;
import utils.RequestMappingUtil;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorkoutTemplateControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private DataSource dataSource;
    @Autowired private UserRepository userRepository;
    @Autowired private WorkoutTemplateRepository workoutTemplateRepository;
    @Autowired private WorkoutTemplateExerciseRepository workoutTemplateExerciseRepository;
    @Autowired private ExerciseRepository exerciseRepository;

    private String workoutTemplateRequestMapping = RequestMappingUtil.workoutTemplateRequestMapping;
    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        //Create User
        UserRegistrationDTO userRegistration = UserRegistrationDTO.builder()
                .firstName("John").lastName("Smith").email("test@gmail.com").password("test")
                .build();
        jwtToken = mockMvc.perform(MockMvcRequestBuilders.post(RequestMappingUtil.userRegistrationRequestMapping)
                        .content(JsonUtil.asJsonString(userRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Create Exercises
        List<PostExerciseDTO> exerciseDTOS = new ArrayList<>(Arrays.asList(
                PostExerciseDTO.builder().id(1L).name("Pull Ups").bodyPart("Back").category("Weighted").build(),
                PostExerciseDTO.builder().id(2L).name("Push Ups").bodyPart("Chest").category("Body Weight").build()
        ));
        for (PostExerciseDTO exerciseDTO: exerciseDTOS){
            mockMvc
                    .perform(MockMvcRequestBuilders.post(RequestMappingUtil.exerciseRequestMapping)
                            .header("Authorization", "Bearer " + jwtToken)
                            .content(JsonUtil.asJsonString(exerciseDTO))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Test
    @DirtiesContext
    void postWorkoutTemplate_CreateWorkout_Successful() throws Exception {
        final Long associatedExerciseId = 1L;
        final List<Long> workoutTemplateIds = new ArrayList<>();
        final PostWorkoutTemplateDTO underTest = PostWorkoutTemplateDtoTestData
                .createWorkoutWithSingleExercise(associatedExerciseId);
        //Create Workout
        mockMvc.perform(MockMvcRequestBuilders.post(workoutTemplateRequestMapping)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(JsonUtil.asJsonString(underTest))
                        .contentType(MediaType.APPLICATION_JSON))
                //Confirm WorkoutTemplate Creation and Retrieve the ID
                .andDo(result -> {
                    System.out.println("\033[1;32m"+"Create Workout Template"+"\033[0m");
                    DatabaseUtil.printAllTables(dataSource);
                })
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    Assertions.assertFalse(workoutTemplateRepository.findAll().isEmpty());
                    workoutTemplateIds.add(workoutTemplateRepository.findAll().get(0).getId());
                });
        //Verify Content of WorkoutTemplate
        List<ExerciseEntity> associatedExercises = new ArrayList<>();
        List<Long> workoutExerciseIds = workoutTemplateExerciseRepository
                .findByWorkoutTemplateId(workoutTemplateIds.get(0)).stream().map(e->{
                    associatedExercises.add(e.getExerciseTemplate());
                    return e.getId();
                }).collect(Collectors.toList());
        GetWorkoutTemplateDTO expectedResult = PostWorkoutTemplateDtoTestData.getExpected(
                underTest.setId(workoutTemplateIds.get(0)),
                workoutExerciseIds,
                associatedExercises
        );
        String actualResult = mockMvc
                .perform(MockMvcRequestBuilders.get(workoutTemplateRequestMapping +"/"+workoutTemplateIds.get(0))
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(expectedResult,
                JsonUtil.getMapper().readValue(actualResult, GetWorkoutTemplateDTO.class));
    }
    @Test
    @DirtiesContext
    void putWorkoutTemplate_ReplaceWorkout_Successful() throws Exception {
        final Long associatedExerciseId = 1L;
        final List<Long> workoutTemplateIds = new ArrayList<>();
        final PostWorkoutTemplateDTO postWorkoutTemplateDTO = PostWorkoutTemplateDtoTestData
                .createWorkoutWithSingleExercise(associatedExerciseId);
        final PutWorkoutTemplateDTO underTest = PutWorkoutTemplateDtoTestData
                .createModifiedWorkoutWithSingleExercise(associatedExerciseId);
        //Set Up Workout
        mockMvc.perform(MockMvcRequestBuilders.post(workoutTemplateRequestMapping)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(JsonUtil.asJsonString(postWorkoutTemplateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //Confirm WorkoutTemplate Creation and Retrieve the ID
                .andDo(result -> {
                    Assertions.assertFalse(workoutTemplateRepository.findAll().isEmpty());
                    workoutTemplateIds.add(workoutTemplateRepository.findAll().get(0).getId());
                });
        //Replace Workout
        mockMvc.perform(MockMvcRequestBuilders.put(workoutTemplateRequestMapping +"/"+workoutTemplateIds.get(0))
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(JsonUtil.asJsonString(underTest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    System.out.println("\033[1;32m"+"Replace Workout Template"+"\033[0m");
                    DatabaseUtil.printAllTables(dataSource);
                })
                .andExpect(MockMvcResultMatchers.status().isOk())
                //Verify WorkoutTemplate Modification
                .andDo(putWorkoutTemplateResult -> {
                    List<ExerciseEntity> associatedExercises = new ArrayList<>();
                    List<Long> workoutExerciseIds = workoutTemplateExerciseRepository
                            .findByWorkoutTemplateId(workoutTemplateIds.get(0)).stream().map(e->{
                                associatedExercises.add(e.getExerciseTemplate());
                                return e.getId();
                            }).collect(Collectors.toList());
                    GetWorkoutTemplateDTO expectedResult = PutWorkoutTemplateDtoTestData.getExpected(
                            underTest.setId(workoutTemplateIds.get(0)),
                            workoutExerciseIds,
                            associatedExercises
                    );
                    String actualResult = mockMvc
                            .perform(MockMvcRequestBuilders.get(workoutTemplateRequestMapping +"/"+workoutTemplateIds.get(0))
                                    .header("Authorization", "Bearer " + jwtToken))
                            .andDo(print())
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn().getResponse().getContentAsString();
                    Assertions.assertEquals(expectedResult,
                            JsonUtil.getMapper().readValue(actualResult, GetWorkoutTemplateDTO.class));
                });

    }
    @Test
    @DirtiesContext
    void putWorkoutTemplate_ReplaceNonExistingWorkout_ThrowsNotFoundException() throws Exception {
        final Long associatedExerciseId = 1L;
        final Long nonExistingWorkoutTemplateId = 99999L;
        final PutWorkoutTemplateDTO putWorkoutTemplateDTO = PutWorkoutTemplateDtoTestData
                .createModifiedWorkoutWithSingleExercise(associatedExerciseId);
        //Replace Workout
        mockMvc
                .perform(MockMvcRequestBuilders.put(workoutTemplateRequestMapping +"/"+nonExistingWorkoutTemplateId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(JsonUtil.asJsonString(putWorkoutTemplateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    /**
     * Delete a WorkoutTemplateEntity
     * @result WorkoutTemplateEntity gets removed,
     *         and related WorkoutTemplateExerciseEntity gets removed
     */
    @Test
    @DirtiesContext
    void deleteWorkoutTemplate_DeleteExistingWorkout_Successful() throws Exception {
        final Long associatedExerciseId = 1L;
        final List<Long> workoutTemplateIds = new ArrayList<>();
        final PostWorkoutTemplateDTO postWorkoutTemplateDTO = PostWorkoutTemplateDtoTestData
                .createWorkoutWithSingleExercise(associatedExerciseId);
        //Create WorkoutTemplate
        mockMvc
                .perform(MockMvcRequestBuilders.post(workoutTemplateRequestMapping)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(JsonUtil.asJsonString(postWorkoutTemplateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //Confirm WorkoutTemplate Creation and Retrieve the ID
                .andDo(result -> {
                    Assertions.assertFalse(workoutTemplateRepository.findAll().isEmpty());
                    workoutTemplateIds.add(workoutTemplateRepository.findAll().get(0).getId());
                });
        //Delete WorkoutTemplate
        mockMvc
                .perform(MockMvcRequestBuilders.delete(workoutTemplateRequestMapping+"/"+workoutTemplateIds.get(0))
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(result -> {
                    System.out.println("\033[1;32m"+"Deleting Workout Template"+"\033[0m");
                    DatabaseUtil.printAllTables(dataSource);
                })
                .andExpect(MockMvcResultMatchers.status().isOk())
                //Verify WorkoutTemplateEntity and WorkoutTemplateExerciseEntity Deletion
                .andDo(result -> {
                    Assertions.assertTrue(workoutTemplateRepository.findAll().isEmpty());
                    Assertions.assertTrue(workoutTemplateExerciseRepository.findAll().isEmpty());
                });
    }
    @Test
    @DirtiesContext
    void deleteWorkoutTemplate_DeleteNonExistingWorkout_ThrowsNotFoundException() throws Exception {
        //Delete WorkoutTemplate
        mockMvc
                .perform(MockMvcRequestBuilders.delete(workoutTemplateRequestMapping+"/99999")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                //Verify WorkoutTemplateEntity and WorkoutTemplateExerciseEntity Deletion
                .andDo(result -> {
                    Assertions.assertTrue(workoutTemplateRepository.findAll().isEmpty());
                    Assertions.assertTrue(workoutTemplateExerciseRepository.findAll().isEmpty());
                });
    }


}

