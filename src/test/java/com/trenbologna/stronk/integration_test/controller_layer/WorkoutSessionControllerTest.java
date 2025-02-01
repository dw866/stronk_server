package com.trenbologna.stronk.integration_test.controller_layer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trenbologna.stronk.domain.exercise.dto.PostExerciseDTO;
import com.trenbologna.stronk.domain.exercise.repository.ExerciseRepository;
import com.trenbologna.stronk.domain.user.UserEntity;
import com.trenbologna.stronk.domain.user.dto.UserRegistrationDTO;
import com.trenbologna.stronk.domain.user.repository.UserRepository;
import com.trenbologna.stronk.domain.workout_session.dto.PostWorkoutSessionDTO;
import com.trenbologna.stronk.domain.workout_session.repository.WorkoutSessionExerciseRepository;
import com.trenbologna.stronk.domain.workout_session.repository.WorkoutSessionRepository;
import com.trenbologna.stronk.domain.workout_template.repository.WorkoutTemplateExerciseRepository;
import com.trenbologna.stronk.domain.workout_template.repository.WorkoutTemplateRepository;
import com.trenbologna.stronk.integration_test.controller_layer.test_data.workout_session.PostWorkoutSessionDtoTestData;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utils.DatabaseUtil;
import utils.JsonUtil;
import utils.RequestMappingUtil;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorkoutSessionControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private DataSource dataSource;
    @Autowired private WorkoutTemplateRepository workoutTemplateRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private WorkoutSessionRepository workoutSessionRepository;
    @Autowired private WorkoutSessionExerciseRepository workoutSessionExerciseRepository;
    @Autowired private WorkoutTemplateExerciseRepository workoutTemplateExerciseRepository;
    @Autowired private ExerciseRepository exerciseRepository;
    private String workoutSessionRequestMapping = "/api/v1/workout-sessions";
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
    void postWorkoutSession_CreateWorkoutSession_Successful() throws Exception {
        final Long associatedExerciseId = 1L;
        final PostWorkoutSessionDTO postWorkoutSessionDTO = PostWorkoutSessionDtoTestData
                .createWorkoutSession(associatedExerciseId);
        mockMvc.perform(MockMvcRequestBuilders.post(workoutSessionRequestMapping)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(JsonUtil.asJsonString(postWorkoutSessionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    System.out.println("\033[1;32m"+"Create Workout Template"+"\033[0m");
                    DatabaseUtil.printAllTables(dataSource);
                })
                .andExpect(MockMvcResultMatchers.status().isOk())
                //Confirm WorkoutTemplate Creation and Retrieve the ID
                .andDo(result -> {
                    Assertions.assertFalse(workoutSessionRepository.findAll().isEmpty());

                });
    }
    @Test
    @Disabled
    void getWorkoutSession(){
        
    }


}
