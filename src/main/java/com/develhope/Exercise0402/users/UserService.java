package com.develhope.Exercise0402.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<UserEntity> addUser(UserEntity userEntity) {
        if(userEntity == null) {
            return Optional.empty();
        } else {
            return Optional.of(userRepository.saveAndFlush(userEntity));
        }
    }

    public List<UserEntity> showUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> showUser (Long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if(!user.isPresent()) {
            return Optional.empty();
        } else {
            return user;
        }
    }

    public void deleteUsers() {
        userRepository.deleteAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
