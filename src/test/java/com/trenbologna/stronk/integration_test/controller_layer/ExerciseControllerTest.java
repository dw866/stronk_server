package com.trenbologna.stronk.integration_test.controller_layer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.domain.exercise.dto.GetExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PostExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PatchExerciseDTO;
import com.trenbologna.stronk.domain.exercise.mapper.GetExerciseDtoMapper;
import com.trenbologna.stronk.domain.exercise.repository.ExerciseRepository;
import com.trenbologna.stronk.domain.user.UserEntity;
import com.trenbologna.stronk.domain.user.dto.UserRegistrationDTO;
import com.trenbologna.stronk.domain.user.repository.UserRepository;
import com.trenbologna.stronk.domain.workout_session.dto.PostWorkoutSessionDTO;
import com.trenbologna.stronk.domain.workout_session.repository.WorkoutSessionExerciseRepository;
import com.trenbologna.stronk.domain.workout_template.dto.PostWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.repository.WorkoutTemplateExerciseRepository;
import com.trenbologna.stronk.integration_test.controller_layer.test_data.exercise.PatchExerciseDtoTestData;
import com.trenbologna.stronk.integration_test.controller_layer.test_data.exercise.PostExerciseDtoTestData;
import com.trenbologna.stronk.integration_test.controller_layer.test_data.workout_session.PostWorkoutSessionDtoTestData;
import com.trenbologna.stronk.integration_test.controller_layer.test_data.workout_template.PostWorkoutTemplateDtoTestData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import utils.DatabaseUtil;
import utils.JsonUtil;
import utils.RequestMappingUtil;

import javax.sql.DataSource;
import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExerciseControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private DataSource dataSource;
    @Autowired private UserRepository userRepository;
    @Autowired private ExerciseRepository exerciseRepository;
    @Autowired private WorkoutSessionExerciseRepository workoutSessionExerciseRepository;
    @Autowired private WorkoutTemplateExerciseRepository workoutTemplateExerciseRepository;

    private String exerciseRequestMapping = RequestMappingUtil.exerciseRequestMapping;
    private String workoutTemplateRequestMapping = RequestMappingUtil.workoutTemplateRequestMapping;
    private String workoutSessionRequestMapping = RequestMappingUtil.workoutSessionRequestMapping;
    private String jwtToken;
    private UserEntity user;
    @BeforeEach
    public void setUp() throws Exception {
        UserRegistrationDTO userRegistration = UserRegistrationDTO.builder()
                .firstName("John").lastName("Smith").email("test@gmail.com").password("test")
                .build();
        jwtToken = mockMvc.perform(MockMvcRequestBuilders.post(RequestMappingUtil.userRegistrationRequestMapping)
                        .content(JsonUtil.asJsonString(userRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        user = userRepository.findByEmail(userRegistration.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("Username not found with email: " + userRegistration.getEmail()));
    }

    @Test
    @DirtiesContext
    public void getExercises_Successful() throws Exception {
        //Insert Exercises into database
        final List<ExerciseEntity> exercises = new ArrayList<>(Arrays.asList(
                ExerciseEntity.builder().name("Pull Ups").bodyPart("Back").category("Weighted").user(user).build(),
                ExerciseEntity.builder().name("Push Ups").bodyPart("Chest").category("Body Weight").user(user).build()
        ));
        exerciseRepository.saveAll(exercises);
        //Retrieve Exercises
        String actualResult = mockMvc
                .perform(MockMvcRequestBuilders.get(exerciseRequestMapping)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //Retrieve Exercises in Repository
                .andDo(result -> {
                    DatabaseUtil.printAllTables(dataSource);
                    Assertions.assertFalse(exerciseRepository.findAll().isEmpty());
                })
                .andReturn().getResponse().getContentAsString();
        //Verify Content of Exercises Retrieved
        List<GetExerciseDTO> expectedResult = GetExerciseDtoMapper.mapToDTO(exercises);
        Assertions.assertEquals(expectedResult, GetExerciseDtoMapper.mapToDTO(
                JsonUtil.getMapper().readValue(actualResult, new TypeReference<List<ExerciseEntity>>() {}))
        );
    }
    @Test
    @DirtiesContext
    public void postExercise_Successful() throws Exception {
        final List<Long> exerciseIds = new ArrayList<>();
        final PostExerciseDTO underTest = PostExerciseDTO.builder()
                .name("Pull Ups")
                .category("Weighted")
                .bodyPart("Back")
                .build();
        //Create Exercise
        mockMvc.perform(MockMvcRequestBuilders.post(exerciseRequestMapping)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(JsonUtil.asJsonString(underTest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                //Confirm Exercise Creation and Retrieve the ID
                .andDo(result -> {
                    DatabaseUtil.printAllTables(dataSource);
                    Assertions.assertFalse(exerciseRepository.findAll().isEmpty());
                    exerciseIds.add(exerciseRepository.findAll().get(0).getId());
                });
        //Verify Content of Exercise
        GetExerciseDTO expectedResult = GetExerciseDTO.builder()
                .id(exerciseIds.get(0))
                .name(underTest.getName())
                .category(underTest.getCategory())
                .bodyPart(underTest.getBodyPart())
                .build();
        String actualResult = mockMvc
                .perform(MockMvcRequestBuilders.get(exerciseRequestMapping)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(expectedResult, GetExerciseDtoMapper
                .mapToDTO(JsonUtil.getMapper().readValue(actualResult, new TypeReference<List<ExerciseEntity>>(){}).get(0)));

    }
    @Test
    @DirtiesContext
    public void patchExercise_Successful() throws Exception {
        final List<Long> exerciseIds = new ArrayList<>();
        final PostExerciseDTO postExerciseDTO = PostExerciseDTO.builder()
                .name("Pull Ups").category("Weighted").bodyPart("Back")
                .build();;
        final PatchExerciseDTO underTest = PatchExerciseDTO.builder()
                .name("Patched Pull Ups").category("Patched Weighted").bodyPart("Patched Back")
                .build();;
        //Create Exercise
        mockMvc.perform(MockMvcRequestBuilders.post(exerciseRequestMapping)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(JsonUtil.asJsonString(postExerciseDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //Confirm Exercise Creation and Retrieve the ID
                .andDo(result -> {
                    Assertions.assertFalse(exerciseRepository.findAll().isEmpty());
                    exerciseIds.add(exerciseRepository.findAll().get(0).getId());
                });
        //Modify Exercise
        mockMvc.perform(MockMvcRequestBuilders.patch(exerciseRequestMapping+"/"+exerciseIds.get(0))
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(JsonUtil.asJsonString(underTest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    DatabaseUtil.printAllTables(dataSource);
                })
                .andExpect(MockMvcResultMatchers.status().isOk());
        //Verify Exercise Modification
        GetExerciseDTO expectedResult = GetExerciseDTO.builder()
                .id(exerciseIds.get(0)).name(underTest.getName()).category(underTest.getCategory()).bodyPart(underTest.getBodyPart())
                .build();
        String actualResult = mockMvc
                .perform(MockMvcRequestBuilders.get(exerciseRequestMapping)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(expectedResult, GetExerciseDtoMapper
                .mapToDTO(JsonUtil.getMapper().readValue(actualResult, new TypeReference<List<ExerciseEntity>>(){}).get(0)));
    }

    @Test
    @DirtiesContext
    public void deleteExercise_ExerciseExist_Successful() throws Exception {
        final PostExerciseDTO exercise = PostExerciseDTO.builder()
                .id(1L)
                .name("Pull Ups").category("Weighted").bodyPart("Back")
                .build();
        final PostWorkoutTemplateDTO postWorkoutTemplateDTO = PostWorkoutTemplateDtoTestData
                .createWorkoutWithSingleExercise(exercise.getId());
        final PostWorkoutSessionDTO postWorkoutSessionDTO = PostWorkoutSessionDtoTestData
                .createWorkoutSession(exercise.getId());
        //Create Exercise
        mockMvc.perform(MockMvcRequestBuilders.post(exerciseRequestMapping)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(JsonUtil.asJsonString(exercise))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                //Confirm Exercise Creation and Retrieve the ID
                .andDo(result -> {
                    System.out.println("\033[1;32m"+"Create Exercise"+"\033[0m");
                    DatabaseUtil.printAllTables(dataSource);
                });
        //Create Workout Template
        mockMvc.perform(MockMvcRequestBuilders.post(workoutTemplateRequestMapping)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(JsonUtil.asJsonString(postWorkoutTemplateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    System.out.println("\033[1;32m"+"Create Workout Template"+"\033[0m");
                    DatabaseUtil.printAllTables(dataSource);
                })
                .andExpect(MockMvcResultMatchers.status().isOk());
        //Create Workout Session
        mockMvc.perform(MockMvcRequestBuilders.post(workoutSessionRequestMapping)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(JsonUtil.asJsonString(postWorkoutSessionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    System.out.println("\033[1;32m"+"Create Workout Session"+"\033[0m");
                    DatabaseUtil.printAllTables(dataSource);
                })
                .andExpect(MockMvcResultMatchers.status().isOk());
        //Delete Exercise
        mockMvc.perform(MockMvcRequestBuilders.delete(exerciseRequestMapping+"/"+exercise.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    System.out.println("\033[1;32m"+"Delete Exercise"+"\033[0m");
                    DatabaseUtil.printAllTables(dataSource);
                })
                .andExpect(MockMvcResultMatchers.status().isOk())
                //Verify that Exercise, WorkoutSessionExercise, and WorkoutTemplateExercise do not exist
                .andDo(result -> {
                    Assertions.assertTrue(exerciseRepository.findAll().isEmpty());
                    Assertions.assertTrue(workoutTemplateExerciseRepository.findAll().isEmpty());
                    Assertions.assertTrue(workoutSessionExerciseRepository.findAll().isEmpty());
                });
    }
}

