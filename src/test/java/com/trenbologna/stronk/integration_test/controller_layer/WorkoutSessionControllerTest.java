package com.trenbologna.stronk.integration_test.controller_layer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trenbologna.stronk.domain.workout_session.dto.PostWorkoutSessionDTO;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Calendar;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorkoutSessionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private String workoutTemplateRequestMapping = "/api/v1/workout-templates";




    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private class DTOFactory {
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
                                    .id(1L)//exercise id
//                                    .sets()
                            .build()))
                    .build();
        }
    }
}
