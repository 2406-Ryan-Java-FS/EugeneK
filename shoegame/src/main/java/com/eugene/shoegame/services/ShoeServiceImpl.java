package com.eugene.shoegame.services;

import com.eugene.shoegame.dto.ShoeDTO;
import com.eugene.shoegame.entities.ShoeEntity;
import com.eugene.shoegame.exceptions.ResourceNotFoundException;
import com.eugene.shoegame.repositories.ShoeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service //  marks the class as a service component in Spring's component scanning.
public class ShoeServiceImpl implements ShoeService {
    private final ShoeRepository shoeRepository;

    @Autowired
    public ShoeServiceImpl(final ShoeRepository shoeRepository){
        this.shoeRepository = shoeRepository;
    }

    @Override
    public ShoeDTO createShoe(ShoeDTO shoeDTO) {
        ShoeEntity shoeEntity= convertShoeDTOToShoeEntity(shoeDTO);
        ShoeEntity savedShoe = shoeRepository.save(shoeEntity);
        return convertShoeEntityToShoeDTO(savedShoe);
    }

    private ShoeEntity convertShoeDTOToShoeEntity(ShoeDTO shoeDTO){
        return ShoeEntity.builder()
                .name(shoeDTO.getName())
                .brand(shoeDTO.getBrand())
                .size(shoeDTO.getSize())
                .color(shoeDTO.getColor())
                .price(shoeDTO.getPrice())
                .build();
    }

    private ShoeDTO convertShoeEntityToShoeDTO(ShoeEntity shoeEntity){
        return ShoeDTO.builder()
                .id(shoeEntity.getId())
                .name(shoeEntity.getName())
                .brand(shoeEntity.getBrand())
                .size(shoeEntity.getSize())
                .color(shoeEntity.getColor())
                .price(shoeEntity.getPrice())
                .build();
    }


    @Override
    public ShoeDTO getShoeById(Long id) {
        ShoeEntity shoe = shoeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shoe not found, id is:" + id));

        return convertShoeEntityToShoeDTO(shoe);
    }


    @Override
    public List<ShoeDTO> getAllShoes() {
        List<ShoeEntity> shoeEntities = shoeRepository.findAll();
        List<ShoeDTO> shoeDTOs = new ArrayList<>();

        for (ShoeEntity shoeEntity : shoeEntities) {
            ShoeDTO shoeDTO = convertShoeEntityToShoeDTO(shoeEntity);
            shoeDTOs.add(shoeDTO);
        }

        return shoeDTOs;

    }

    @Override
    public ShoeDTO updateShoe(Long id, ShoeDTO shoeDTO) {
        // First, I need to get an existing shoe.
        ShoeEntity existingShoe = shoeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shoe to be updated not found, id is:"+ id));

        // Update the existing shoe with new values
        existingShoe.setName(shoeDTO.getName());
        existingShoe.setBrand(shoeDTO.getBrand());
        existingShoe.setSize(shoeDTO.getSize());
        existingShoe.setColor(shoeDTO.getColor());
        existingShoe.setPrice(shoeDTO.getPrice());

        ShoeEntity updatedShoe = shoeRepository.save(existingShoe);
        return convertShoeEntityToShoeDTO(updatedShoe);
    }

    @Override
    public void deleteShoe(Long id) {
        ShoeEntity shoe = shoeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shoe to be deleted not found, id is:" + id));

        shoeRepository.delete(shoe);
    }
}
