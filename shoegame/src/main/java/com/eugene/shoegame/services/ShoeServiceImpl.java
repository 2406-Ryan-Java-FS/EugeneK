package com.eugene.shoegame.services;

import com.eugene.shoegame.dto.ShoeDTO;
import com.eugene.shoegame.entities.ShoeEntity;
import com.eugene.shoegame.entities.UserEntity;
import com.eugene.shoegame.exceptions.shoeexceptions.ResourceNotFoundException;
import com.eugene.shoegame.exceptions.userexceptions.UserNotFoundException;
import com.eugene.shoegame.repositories.ShoeRepository;
import com.eugene.shoegame.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service //  marks the class as a service component in Spring's component scanning.
public class ShoeServiceImpl implements ShoeService {

    private final ShoeRepository shoeRepository;

    // Introducing user association to include users handling shoe's CRUD implementation
    private final UserRepository userRepository;

    @Autowired
    public ShoeServiceImpl(final ShoeRepository shoeRepository, UserRepository userRepository){
        this.shoeRepository = shoeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ShoeDTO createShoe(Long userId, ShoeDTO shoeDTO) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        ShoeEntity shoeEntity= convertShoeDTOToShoeEntity(shoeDTO);
        shoeEntity.setUserEntity(userEntity);
        ShoeEntity savedShoe = shoeRepository.save(shoeEntity);
        return convertShoeEntityToShoeDTO(savedShoe);
    }

    @Override
    public ShoeDTO getShoeById(Long userId, Long id) {
        ShoeEntity shoeEntity = shoeRepository.findByIdAndUserEntity_UserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Shoe not found, id is:" + id));

        return convertShoeEntityToShoeDTO(shoeEntity);
    }

    @Override
    public List<ShoeDTO> getAllShoesByUser(Long userId) {
        List<ShoeEntity> shoeEntities = shoeRepository.findAllByUserEntity_UserId(userId);
        List<ShoeDTO> shoeDTOs = new ArrayList<>();

        for (ShoeEntity shoeEntity : shoeEntities) {
            ShoeDTO shoeDTO = convertShoeEntityToShoeDTO(shoeEntity);
            shoeDTOs.add(shoeDTO);
        }

        return shoeDTOs;

    }

    @Override
    public ShoeDTO updateShoe(Long userId, Long id, ShoeDTO shoeDTO) {
        // First, I need to get an existing shoe.
        ShoeEntity existingShoeEntity = shoeRepository.findByIdAndUserEntity_UserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Shoe to be updated not found, id is:"+ id));


        updateShoeEntityFromShoeDTO(existingShoeEntity, shoeDTO);
        ShoeEntity updatedShoe = shoeRepository.save(existingShoeEntity);
        return convertShoeEntityToShoeDTO(updatedShoe);
    }

    @Override
    public void deleteShoe(Long userId, Long shoeId) {
        ShoeEntity shoeEntity = shoeRepository.findByIdAndUserEntity_UserId(shoeId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Shoe to be deleted not found, id is:" + shoeId));

        shoeRepository.delete(shoeEntity);
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
                .userId(shoeEntity.getUserEntity().getUserId())
                .build();
    }

    private void updateShoeEntityFromShoeDTO(ShoeEntity shoeEntity, ShoeDTO shoeDTO){
        // Update the existing shoe with new values
        shoeEntity.setName(shoeDTO.getName());
        shoeEntity.setBrand(shoeDTO.getBrand());
        shoeEntity.setSize(shoeDTO.getSize());
        shoeEntity.setColor(shoeDTO.getColor());
        shoeEntity.setPrice(shoeDTO.getPrice());
    }
}
