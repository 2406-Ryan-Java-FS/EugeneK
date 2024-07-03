package com.eugene.shoegame.controllers;


import com.eugene.shoegame.dto.UserDTO;
import com.eugene.shoegame.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/shoegame/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;

    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO){
        UserDTO registeredUser = userService.registerUser(userDTO);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody UserDTO userDTO){
        UserDTO loggedUser = userService.loginUser(userDTO);
        return ResponseEntity.ok(loggedUser);
    }

    // get user by specifying their id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId){
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // update user
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO>updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO){
        UserDTO updatedUser = userService.updateUser(userId, userDTO);
        return ResponseEntity.ok(updatedUser);

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }






}
