package com.eugene.shoegame.services;

import com.eugene.shoegame.dto.UserDTO;

public interface UserService {
    public UserDTO registerUser(UserDTO userDTO);
    public UserDTO loginUser(UserDTO userDTO);
    public UserDTO getUserById(Long userId);
    public UserDTO updateUser(Long userId, UserDTO userDTO);
    public void deleteUser(Long userId);
}
