package com.eugene.shoegame.controllers;

import com.eugene.shoegame.dto.ShoeDTO;
import com.eugene.shoegame.services.ShoeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoegame/users/{userId}/shoes")
public class ShoeController {

    private final ShoeService shoeService;

    @Autowired
    public ShoeController(ShoeService shoeService){
        this.shoeService = shoeService;
    }

    @PostMapping
    public ResponseEntity<ShoeDTO> createShoe(@PathVariable Long userId, @RequestBody ShoeDTO shoeDTO){
        ShoeDTO createdShoe = shoeService.createShoe(userId, shoeDTO);
        return new ResponseEntity<>(createdShoe, HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoeDTO> getShoeById(@PathVariable Long userId, @PathVariable Long id){
        ShoeDTO shoeDTO = shoeService.getShoeById(userId, id);
        return ResponseEntity.ok(shoeDTO);
    }

    @GetMapping
    public ResponseEntity<List<ShoeDTO>> getAllShoesByUser(@PathVariable Long userId){
        List<ShoeDTO> allShoes = shoeService.getAllShoesByUser(userId);
        return ResponseEntity.ok(allShoes);
    }

    @PutMapping("/{id}")
    // the @PathVariable defines the shoe id passed in the endpoint URL.
    // The @RequestBody defines the updated set of data needed to be passed as JSON to update an existing shoe's data.
    public ResponseEntity<ShoeDTO> updateShoe(@PathVariable Long userId, @PathVariable Long id, @RequestBody ShoeDTO shoeDTO){
        ShoeDTO updatedShoe = shoeService.updateShoe(userId, id, shoeDTO);
        return ResponseEntity.ok(updatedShoe);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ShoeDTO> deleteShoe(@PathVariable Long userId, @PathVariable Long id){
        shoeService.deleteShoe(userId, id);
        return ResponseEntity.noContent().build();
    }

}

