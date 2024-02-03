package com.develhope.Exercise0402.controllers;

import com.develhope.Exercise0402.users.UserController;
import com.develhope.Exercise0402.users.UserEntity;
import com.develhope.Exercise0402.users.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTests {

    @Autowired
    private UserController userController;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private UserEntity createUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Mario");
        userEntity.setSurname("Rossi");
        userEntity.setUsername("MarioRossi98");

        return userEntity;
    }

    private void createAndSaveFourUsers() {
        userRepository.deleteAll();

        UserEntity user = createUser();
        UserEntity userTwo = createUser();
        UserEntity userThree = createUser();
        UserEntity userFour = createUser();

        userRepository.save(user);
        userRepository.save(userTwo);
        userRepository.save(userThree);
        userRepository.save(userFour);

    }

    @Test
    void addUser() throws Exception {
        UserEntity user = createUser();
        String studentJSON = objectMapper.writeValueAsString(user);
        MvcResult result = this.mockMvc.perform(post("/newuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        UserEntity userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserEntity.class);
    }

    @Test
    void showUsers() throws Exception {
        createAndSaveFourUsers();

        MvcResult result = mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<UserEntity> usersFromResponse = objectMapper.readValue(result.getResponse()
                .getContentAsString(),List.class);
        assertEquals(usersFromResponse,4);
        System.out.println("Users in DB are: " + usersFromResponse.size());
    }

    @Test
    void showUser() throws Exception {
        userRepository.deleteAll();
        UserEntity user = createUser();

        MvcResult result = this.mockMvc.perform(get("/user/" + user.getId()))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        UserEntity userFromResponse = objectMapper.readValue(result.getResponse()
                .getContentAsString(), UserEntity.class);
        assertEquals(userFromResponse.getId(),user.getId());
    }

    @Test
    void deleteUser() throws Exception {
        userRepository.deleteAll();
        UserEntity user = createUser();

        UserEntity savedUser = userRepository.save(user);

        this.mockMvc.perform(delete("/user/"+savedUser.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Optional<UserEntity> studentEntity = userRepository.findById(savedUser.getId());
        assertEquals(studentEntity,Optional.empty());
    }

    @Test
    void deleteUsers() throws Exception {
        createAndSaveFourUsers();
        this.mockMvc.perform(delete("/delusers/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(userEntityList.size(),0);
    }
}


