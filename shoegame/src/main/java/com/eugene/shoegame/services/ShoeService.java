package com.eugene.shoegame.services;

import com.eugene.shoegame.dto.ShoeDTO;

import java.util.List;

public interface ShoeService {

    public ShoeDTO createShoe(ShoeDTO shoeDTO);
    public ShoeDTO getShoeById(Long id);
    public List<ShoeDTO> getAllShoes();
    public ShoeDTO updateShoe(Long id, ShoeDTO shoeDTO);
    public void deleteShoe(Long id);
}
