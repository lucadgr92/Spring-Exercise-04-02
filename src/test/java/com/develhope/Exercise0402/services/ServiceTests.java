package com.develhope.Exercise0402.services;

import com.develhope.Exercise0402.users.UserEntity;
import com.develhope.Exercise0402.users.UserRepository;
import com.develhope.Exercise0402.users.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;



    private UserEntity createUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Mario");
        userEntity.setSurname("Rossi");
        userEntity.setUsername("MarioRossi98");

        return userEntity;
    }

    private void createAndSaveUsers() {

        userRepository.deleteAll();

        UserEntity userEntityOne = createUser();
        UserEntity userEntityTwo = createUser();
        UserEntity userEntityThree = createUser();
        UserEntity userEntityFour = createUser();
        UserEntity userEntityFive = createUser();

        userRepository.save(userEntityOne);
        userRepository.save(userEntityTwo);
        userRepository.save(userEntityThree);
        userRepository.save(userEntityFour);
        userRepository.save(userEntityFive);
    }



    @Test
    @Transactional
    void addUserSuccessfulAddition() {
        Optional<UserEntity> savedUser = userService.addUser(createUser());
        Long savedUserId = savedUser.get().getId();

        UserEntity retrievedUser = userRepository.findById(savedUserId).get();
        assertEquals(retrievedUser.getId(), savedUserId);
        assertNotNull(retrievedUser);
    }

    @Test
    @Transactional
    void addUserNullInput() {
        Optional<UserEntity> savedUser = userService.addUser(null);
        assertEquals(savedUser, Optional.empty());
    }

    @Test
    @Transactional
    void showUsersCompleteList() {
        createAndSaveUsers();

        List<UserEntity> userEntityList = userService.showUsers();
        assertEquals(userEntityList.size(), 5);
    }

    @Test
    @Transactional
    void showUserInvalidId() {
        Optional<UserEntity> userEntity = userService.showUser(Long.valueOf(-1));
        assertEquals(userEntity, Optional.empty());
    }

    @Test
    @Transactional
    void showUserValidId() {
        UserEntity savedUser = userRepository.save(createUser());
        Optional<UserEntity> foundUser = userService.showUser(Long.valueOf(savedUser.getId()));
        assertNotNull(foundUser.get());
        assertEquals(savedUser.getId(),foundUser.get().getId());
    }

    @Test
    @Transactional
    void deleteUser() {
        Long savedUserId = userRepository.save(createUser()).getId();
        userService.deleteUser(savedUserId);
        Optional<UserEntity> userToVerify = userRepository.findById(savedUserId);
        assertTrue(!userToVerify.isPresent());
    }

    @Test
    @Transactional
    void deleteUsers() {
        createAndSaveUsers();
        userService.deleteUsers();
        List<UserEntity> userList = userRepository.findAll();
        assertEquals(userList.size(), 0);
    }
}
