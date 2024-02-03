package com.develhope.Exercise0402.users;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/newuser")
    public @ResponseBody ResponseEntity<UserEntity> addUser(@RequestBody UserEntity userEntity) {
        Optional<UserEntity> serviceUser = userService.addUser(userEntity);
        if(serviceUser.isPresent()) {
            return ResponseEntity.ok(serviceUser.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/users")
    public @ResponseBody List<UserEntity> showUsers() {
        return userService.showUsers();
    }

    @GetMapping("/user/{id}")
    public @ResponseBody ResponseEntity<UserEntity> showUser(@PathVariable long id) {
        Optional<UserEntity> user = userService.showUser(id);
        if(user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delusers")
    public void deleteUsers() {
        userService.deleteUsers();
    }

    @DeleteMapping("/deluser/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }

}
