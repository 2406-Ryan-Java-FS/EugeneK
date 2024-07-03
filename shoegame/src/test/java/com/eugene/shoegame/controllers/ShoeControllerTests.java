package com.eugene.shoegame.controllers;

import com.eugene.shoegame.dto.ShoeDTO;
import com.eugene.shoegame.exceptions.shoeexceptions.ResourceNotFoundException;
import com.eugene.shoegame.services.ShoeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
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

    private static final Long USER_ID = 1L;
    private static final Long SHOE_ID = 1L;

    @Test
    void testCreateShoe_Success() throws Exception {
        ShoeDTO inputShoeDTO = ShoeDTO.builder()
                .name("New Shoe")
                .brand("New Brand")
                .size(10.0)
                .color("Black")
                .price(100.0)
                .build();

        ShoeDTO createdShoeDTO = ShoeDTO.builder()
                .id(SHOE_ID)
                .name("New Shoe")
                .brand("New Brand")
                .size(10.0)
                .color("Black")
                .price(100.0)
                .userId(USER_ID)
                .build();

        when(shoeService.createShoe(eq(USER_ID), any(ShoeDTO.class))).thenReturn(createdShoeDTO);

        mockMvc.perform(post("/shoegame/users/{userId}/shoes", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputShoeDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(SHOE_ID))
                .andExpect(jsonPath("$.name").value("New Shoe"))
                .andExpect(jsonPath("$.brand").value("New Brand"))
                .andExpect(jsonPath("$.size").value(10.0))
                .andExpect(jsonPath("$.color").value("Black"))
                .andExpect(jsonPath("$.price").value(100.0));
    }

    @Test
    void testGetShoeById_Success() throws Exception {
        ShoeDTO shoeDTO = ShoeDTO.builder()
                .id(SHOE_ID)
                .name("Test Shoe")
                .brand("Test Brand")
                .size(10.0)
                .color("Black")
                .price(100.0)
                .userId(USER_ID)
                .build();

        when(shoeService.getShoeById(USER_ID, SHOE_ID)).thenReturn(shoeDTO);

        mockMvc.perform(get("/shoegame/users/{userId}/shoes/{id}", USER_ID, SHOE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(SHOE_ID))
                .andExpect(jsonPath("$.name").value("Test Shoe"))
                .andExpect(jsonPath("$.brand").value("Test Brand"))
                .andExpect(jsonPath("$.size").value(10.0))
                .andExpect(jsonPath("$.color").value("Black"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.userId").value(USER_ID));
    }

    @Test
    void testGetShoeById_NotFound() throws Exception {
        when(shoeService.getShoeById(USER_ID, SHOE_ID))
                .thenThrow(new ResourceNotFoundException("Shoe not found"));

        mockMvc.perform(get("/shoegame/users/{userId}/shoes/{id}", USER_ID, SHOE_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Shoe not found"));
    }

    @Test
    void testGetAllShoesByUser_Success() throws Exception {
        List<ShoeDTO> shoes = Arrays.asList(
                ShoeDTO.builder().id(1L).name("Shoe 1").brand("Brand 1").size(10.0).color("Black").price(100.0).userId(USER_ID).build(),
                ShoeDTO.builder().id(2L).name("Shoe 2").brand("Brand 2").size(11.0).color("White").price(110.0).userId(USER_ID).build()
        );

        when(shoeService.getAllShoesByUser(USER_ID)).thenReturn(shoes);

        mockMvc.perform(get("/shoegame/users/{userId}/shoes", USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Shoe 1"))
                .andExpect(jsonPath("$[1].name").value("Shoe 2"));
    }

    @Test
    void testUpdateShoe_Success() throws Exception {
        ShoeDTO inputShoeDTO = ShoeDTO.builder()
                .name("Updated Shoe")
                .brand("Updated Brand")
                .size(11.0)
                .color("Red")
                .price(120.0)
                .build();

        ShoeDTO updatedShoeDTO = ShoeDTO.builder()
                .id(SHOE_ID)
                .name("Updated Shoe")
                .brand("Updated Brand")
                .size(11.0)
                .color("Red")
                .price(120.0)
                .userId(USER_ID)
                .build();

        when(shoeService.updateShoe(eq(USER_ID), eq(SHOE_ID), any(ShoeDTO.class))).thenReturn(updatedShoeDTO);

        mockMvc.perform(put("/shoegame/users/{userId}/shoes/{id}", USER_ID, SHOE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputShoeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(SHOE_ID))
                .andExpect(jsonPath("$.name").value("Updated Shoe"))
                .andExpect(jsonPath("$.brand").value("Updated Brand"))
                .andExpect(jsonPath("$.size").value(11.0))
                .andExpect(jsonPath("$.color").value("Red"))
                .andExpect(jsonPath("$.price").value(120.0));
    }

    @Test
    void testUpdateShoe_NotFound() throws Exception {
        ShoeDTO inputShoeDTO = ShoeDTO.builder()
                .name("Updated Shoe")
                .brand("Updated Brand")
                .size(11.0)
                .color("Red")
                .price(120.0)
                .build();

        when(shoeService.updateShoe(eq(USER_ID), eq(SHOE_ID), any(ShoeDTO.class)))
                .thenThrow(new ResourceNotFoundException("Shoe not found"));

        mockMvc.perform(put("/shoegame/users/{userId}/shoes/{id}", USER_ID, SHOE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputShoeDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Shoe not found"));
    }

    @Test
    void testDeleteShoe_Success() throws Exception {
        doNothing().when(shoeService).deleteShoe(USER_ID, SHOE_ID);

        mockMvc.perform(delete("/shoegame/users/{userId}/shoes/{id}", USER_ID, SHOE_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteShoe_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Shoe not found"))
                .when(shoeService).deleteShoe(USER_ID, SHOE_ID);

        mockMvc.perform(delete("/shoegame/users/{userId}/shoes/{id}", USER_ID, SHOE_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Shoe not found"));
    }
}
