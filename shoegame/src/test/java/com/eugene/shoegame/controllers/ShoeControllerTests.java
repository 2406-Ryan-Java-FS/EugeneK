package com.eugene.shoegame.controllers;

import com.eugene.shoegame.controllers.ShoeController;
import com.eugene.shoegame.dto.ShoeDTO;
import com.eugene.shoegame.exceptions.ResourceNotFoundException;
import com.eugene.shoegame.repositories.ShoeRepository;
import com.eugene.shoegame.services.ShoeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

// Using Mockito to test my controller methods.

@WebMvcTest(ShoeController.class)
public class ShoeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoeService shoeService;

    @Autowired
    private ObjectMapper objectMapper;

    // TESTING FOR THE getShoeById() controller method
    @Test
    void testGetShoeById_Success() throws Exception {
        Long shoeId = 1L;
        ShoeDTO shoeDTO = new ShoeDTO(shoeId, "Test Shoe", "Test Brand", 10.0, "Black", 100.0);
        when(shoeService.getShoeById(shoeId)).thenReturn(shoeDTO);

        mockMvc.perform(get("/api/shoes/{id}", shoeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(shoeId))
                .andExpect(jsonPath("$.name").value("Test Shoe"))
                .andExpect(jsonPath("$.brand").value("Test Brand"))
                .andExpect(jsonPath("$.size").value(10.0))
                .andExpect(jsonPath("$.color").value("Black"))
                .andExpect(jsonPath("$.price").value(100.0));
    }

    @Test
    void testGetShoeById_NotFound() throws Exception {
        Long nonExistentId = 999L;
        when(shoeService.getShoeById(nonExistentId))
                .thenThrow(new ResourceNotFoundException("Shoe with id " + nonExistentId + " not found"));

        mockMvc.perform(get("/api/shoes/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Shoe with id 999 not found"));
    }


    // TESTING THE updateShoe() controller method.
    @Test
    void testUpdateShoe_Success() throws Exception {

        Long shoeId = 1L;
        ShoeDTO inputShoeDTO = new ShoeDTO(null, "Updated Shoe", "Updated Brand", 11.0, "Red", 120.0);
        ShoeDTO updatedShoeDTO = new ShoeDTO(shoeId, "Updated Shoe", "Updated Brand", 11.0, "Red", 120.0);

        when(shoeService.updateShoe(eq(shoeId), any(ShoeDTO.class))).thenReturn(updatedShoeDTO);


        mockMvc.perform(put("/api/shoes/{id}", shoeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputShoeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(shoeId))
                .andExpect(jsonPath("$.name").value("Updated Shoe"))
                .andExpect(jsonPath("$.brand").value("Updated Brand"))
                .andExpect(jsonPath("$.size").value(11.0))
                .andExpect(jsonPath("$.color").value("Red"))
                .andExpect(jsonPath("$.price").value(120.0));
    }

    @Test
    void testUpdateShoe_NotFound() throws Exception {

        Long nonExistentId = 999L;
        ShoeDTO inputShoeDTO = new ShoeDTO(null, "Updated Shoe", "Updated Brand", 11.0, "Red", 120.0);

        when(shoeService.updateShoe(eq(nonExistentId), any(ShoeDTO.class)))
                .thenThrow(new ResourceNotFoundException("Shoe with id " + nonExistentId + " not found"));

        mockMvc.perform(put("/api/shoes/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputShoeDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Shoe with id 999 not found"));
    }

    // TESTING THE deleteShoe() controller method
    @Test
    void testDeleteShoe_Success() throws Exception {

        Long shoeId = 1L;
        doNothing().when(shoeService).deleteShoe(shoeId);


        mockMvc.perform(delete("/api/shoes/{id}", shoeId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteShoe_NotFound() throws Exception {

        Long nonExistentId = 999L;
        doThrow(new ResourceNotFoundException("Shoe with id " + nonExistentId + " not found"))
                .when(shoeService).deleteShoe(nonExistentId);


        mockMvc.perform(delete("/api/shoes/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Shoe with id 999 not found"));
    }

}
