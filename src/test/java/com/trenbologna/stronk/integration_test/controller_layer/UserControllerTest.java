package com.trenbologna.stronk.integration_test.controller_layer;

import com.trenbologna.stronk.domain.user.dto.UserLoginDTO;
import com.trenbologna.stronk.domain.user.dto.UserRegistrationDTO;
import com.trenbologna.stronk.domain.user.repository.UserRepository;
import com.trenbologna.stronk.domain.user.auth.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import utils.JsonUtil;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    private final String userRegistrationRequestMapping = "/api/v1/users/register";
    private final String userLoginRequestMapping = "/api/v1/users/login";
    private final String userLogoutRequestMapping = "/api/v1/users/logout";
    private final String userProfileRequestMapping = "/api/v1/users/profile";

    @Test
    @DirtiesContext
    void registerUser() throws Exception {
        final UserRegistrationDTO underTest = UserRegistrationDTO.builder()
                .firstName("John")
                .lastName("Smith")
                .email("test@gmail.com")
                .password("test")
                .build();
        final String jwtToken = jwtService.generateToken(underTest.getEmail());
        //Create user
        String actualResult = mockMvc.perform(MockMvcRequestBuilders.post(userRegistrationRequestMapping)
                        .content(JsonUtil.asJsonString(underTest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(jwtToken, actualResult);
        //Verify that jwtToken can be used
        mockMvc.perform(MockMvcRequestBuilders.get(userProfileRequestMapping)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    @DirtiesContext
    /**
     * Upon user logout, jwtToken should be blacklisted.
     * This means that future requests made with that
     * jwtToken is invalid.
     * **/
    void logoutUser() throws Exception {
        final UserRegistrationDTO createUser = UserRegistrationDTO.builder()
                .firstName("John")
                .lastName("Smith")
                .email("test@gmail.com")
                .password("test")
                .build();
        //Set up new user
        String jwtToken = mockMvc.perform(MockMvcRequestBuilders.post(userRegistrationRequestMapping)
                        .content(JsonUtil.asJsonString(createUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
        //Logout of account
        mockMvc.perform(MockMvcRequestBuilders.post(userLogoutRequestMapping)
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
        //Perform authorized request with blacklisted jwtToken
        mockMvc.perform(MockMvcRequestBuilders.post(userProfileRequestMapping)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    @Test
    @DirtiesContext
    void loginUser() throws Exception {
        final UserRegistrationDTO createUser = UserRegistrationDTO.builder()
                .firstName("John")
                .lastName("Smith")
                .email("test@gmail.com")
                .password("test")
                .build();
        final UserLoginDTO loginCredentials = UserLoginDTO.builder()
                .email(createUser.getEmail())
                .password(createUser.getPassword())
                .build();
        //Set up new user
        String jwtToken = mockMvc.perform(MockMvcRequestBuilders.post(userRegistrationRequestMapping)
                        .content(JsonUtil.asJsonString(createUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
        //Logout of account
        mockMvc.perform(MockMvcRequestBuilders.post(userLogoutRequestMapping)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Thread.sleep(1000);
        //Login to account
        String newJwtToken = mockMvc.perform(MockMvcRequestBuilders.post(userLoginRequestMapping)
                        .content(JsonUtil.asJsonString(loginCredentials))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
        //Verify that jwtToken can be used
        mockMvc.perform(MockMvcRequestBuilders.get(userProfileRequestMapping)
                        .header("Authorization", "Bearer " + newJwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk());


    }
    @Test
    @Disabled
    @DirtiesContext
    void deleteUser(){

    }


}
