package com.mafemad.agregadordeinvestimentos.controller;

import com.mafemad.agregadordeinvestimentos.entity.User;
import com.mafemad.agregadordeinvestimentos.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto) {
        var userID =userService.createUser(createUserDto);
        return ResponseEntity.created(URI.create("/v1/users/" + userID.toString())).build();
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        var users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public  ResponseEntity<User> getUserById(@PathVariable("userId") String userId) {
        var user = userService.getUserById(userId);

        if(user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable("userId") String userId,
                                           @RequestBody UpdateUserDto updateUserDto) {
        userService.UpdateUserById(userId, updateUserDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteByID(@PathVariable("userId") String userId){
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }



}
