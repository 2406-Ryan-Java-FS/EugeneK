package com.eugene.shoegame.services;

import com.eugene.shoegame.dto.UserDTO;
import com.eugene.shoegame.entities.UserEntity;
import com.eugene.shoegame.exceptions.userexceptions.InvalidPasswordException;
import com.eugene.shoegame.exceptions.userexceptions.UserNotFoundException;
import com.eugene.shoegame.exceptions.userexceptions.UsernameAlreadyExistsException;
import com.eugene.shoegame.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService{

    // initiate UserRepository
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        if(userRepository.findByUsername(userDTO.getUsername()).isPresent()){
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        UserEntity userEntity = UserEntity.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .build();

        UserEntity savedUser = userRepository.save(userEntity);
        return convertUserEntityToUserDTO(savedUser);
    }


    @Override
    public UserDTO loginUser(UserDTO userDTO) {
        // The findByUsername() returns an Optional<UserEntity>
        UserEntity userEntity = userRepository.findByUsername(userDTO.getUsername()).orElse(null);
        //.orElseThrow(() -> new UserNotFoundException("User not found"));

        if(userEntity == null){
            throw new UserNotFoundException("User not found");
        }

        if (!userEntity.getPassword().equals(userDTO.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }
        return convertUserEntityToUserDTO(userEntity);
    }

    // This method retrieves a user by their ID. Helpful for User management.
    @Override
    public UserDTO getUserById(Long userId){
        UserEntity userEntity = userRepository.findById(userId).orElse(null);

        if (userEntity == null) {
                throw new UserNotFoundException("User not found");
        }
        return convertUserEntityToUserDTO(userEntity);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO){
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
                //.orElseThrow(() -> new UserNotFoundException("User not found"));

        if (userEntity == null) {
            throw new UserNotFoundException("User not found");
        }

        if (!userEntity.getUsername().equals(userDTO.getUsername()) &&
                userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        userEntity.setUsername(userDTO.getUsername());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            userEntity.setPassword(userDTO.getPassword());
        }

        UserEntity updatedUser = userRepository.save(userEntity);
        return convertUserEntityToUserDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }


    private UserDTO convertUserEntityToUserDTO(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        return UserDTO.builder()
                .userId(userEntity.getUserId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .build();

    }

}
