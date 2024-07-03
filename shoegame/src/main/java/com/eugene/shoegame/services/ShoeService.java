package com.eugene.shoegame.services;

import com.eugene.shoegame.dto.ShoeDTO;

import java.util.List;

public interface ShoeService {

    public ShoeDTO createShoe(Long userId, ShoeDTO shoeDTO);
    public ShoeDTO getShoeById(Long userId, Long id);
    public List<ShoeDTO> getAllShoesByUser(Long userId);
    public ShoeDTO updateShoe(Long userId, Long id, ShoeDTO shoeDTO);
    public void deleteShoe(Long userId, Long id);
}
