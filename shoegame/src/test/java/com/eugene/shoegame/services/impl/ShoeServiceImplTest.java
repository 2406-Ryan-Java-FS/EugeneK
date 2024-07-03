package com.eugene.shoegame.services.impl;

import com.eugene.shoegame.dto.ShoeDTO;
import com.eugene.shoegame.entities.ShoeEntity;
import com.eugene.shoegame.entities.UserEntity;
import com.eugene.shoegame.exceptions.shoeexceptions.ResourceNotFoundException;
import com.eugene.shoegame.repositories.ShoeRepository;
import com.eugene.shoegame.repositories.UserRepository;
import com.eugene.shoegame.services.ShoeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
public class ShoeServiceImplTest {
    @Autowired
    private ShoeService shoeService;

    @Autowired
    private ShoeRepository shoeRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity testUser;

    // I added a before each method that clears the DB before each test runs.
    @BeforeEach
    void setUp() {
        shoeRepository.deleteAll();
        userRepository.deleteAll();
        testUser = userRepository.save(new UserEntity(null, "testuser", "password"));
    }

    @Test
    public void testThatShoeIsCreated(){
        //Long userId = 1L;
        ShoeDTO shoeDTO = ShoeDTO.builder()
                .name("Test Shoe")
                .brand("Test Brand")
                .size(10.0)
                .color("Black")
                .price(100.0)
                .build();

        ShoeDTO createdShoe = shoeService.createShoe(testUser.getUserId(), shoeDTO);
        //ShoeDTO createdShoe = shoeService.createShoe(userId, shoeDTO);

        assertNotNull(createdShoe.getUserId());
        assertEquals(shoeDTO.getName(), createdShoe.getName());
        assertEquals(shoeDTO.getBrand(), createdShoe.getBrand());
        assertEquals(shoeDTO.getSize(), createdShoe.getSize());
        assertEquals(shoeDTO.getColor(), createdShoe.getColor());
        assertEquals(shoeDTO.getPrice(), createdShoe.getPrice());
        assertEquals(testUser.getUserId(), createdShoe.getUserId());
        //assertEquals(userId, createdShoe.getUserId());

        // Verify the shoe was actually saved to the database
        Optional<ShoeEntity> savedShoe = shoeRepository.findById(createdShoe.getUserId());
        //Optional<ShoeEntity> savedShoe = shoeRepository.findById(createdShoe.getId());
        assertTrue(savedShoe.isPresent(), "The shoe should be found in the database");
        assertEquals(shoeDTO.getName(), savedShoe.get().getName(), "The name in the database should match");
    }

    @Test
    public void testGetShoeById(){
        ShoeEntity shoeEntity = ShoeEntity.builder()
                .name("Test Shoe")
                .brand("Test Brand")
                .size(10.0)
                .color("Black")
                .price(100.0)
                .userEntity(testUser)
                .build();

        ShoeEntity savedShoe = shoeRepository.save(shoeEntity);

        ShoeDTO foundShoe = shoeService.getShoeById(testUser.getUserId(), savedShoe.getId());

        assertNotNull(foundShoe);
        assertEquals(savedShoe.getId(), foundShoe.getId());
        assertEquals(savedShoe.getName(), foundShoe.getName());
        assertEquals(savedShoe.getBrand(), foundShoe.getBrand());
        assertEquals(savedShoe.getSize(), foundShoe.getSize());
        assertEquals(savedShoe.getColor(), foundShoe.getColor());
        assertEquals(savedShoe.getPrice(), foundShoe.getPrice());
        assertEquals(testUser.getUserId(), foundShoe.getUserId());
    }

    @Test
    public void testShoeByIdNotFound(){
        Long wrongShoeId = 200L;

        assertThrows(ResourceNotFoundException.class, () ->{
            shoeService.getShoeById(testUser.getUserId(), wrongShoeId);
        });
    }

    @Test
    public void testGetAllShoes(){
        ShoeEntity shoe1 = ShoeEntity.builder()
                .name("Test Shoe 1")
                .brand("Test Brand 1")
                .size(12.0)
                .color("Black")
                .price(99.99)
                .userEntity(testUser)
                .build();

        ShoeEntity shoe2 = ShoeEntity.builder()
                .name("Test Shoe 2")
                .brand("Test Brand 2")
                .size(11.0)
                .color("White")
                .price(89.99)
                .userEntity(testUser)
                .build();

        shoeRepository.saveAll(Arrays.asList(shoe1, shoe2));

        List<ShoeDTO> foundShoes = shoeService.getAllShoesByUser(testUser.getUserId());

        assertNotNull(foundShoes);
        assertEquals(2, foundShoes.size(), "Should only find 2 shoes in the Database");

        // Verify the contents of the returned shoes
        assertTrue(foundShoes.stream().allMatch(s -> s.getUserId().equals(testUser.getUserId())));
        assertTrue(foundShoes.stream().anyMatch(s -> s.getName().equals("Test Shoe 1")));
        assertTrue(foundShoes.stream().anyMatch(s -> s.getName().equals("Test Shoe 2")));
    }

    @Test
    public void testUpdateShoe(){
        ShoeEntity existingShoe = ShoeEntity.builder()
                .name("Old Shoe")
                .brand("Old Brand")
                .size(8.0)
                .color("Grey")
                .price(20.0)
                .userEntity(testUser)
                .build();

        existingShoe = shoeRepository.save(existingShoe);

        ShoeDTO updatedShoeDTO = ShoeDTO.builder()
                .name("Updated Shoe")
                .brand("Updated Brand")
                .size(9.0)
                .color("White")
                .price(150.0)
                .build();

        ShoeDTO updatedShoe = shoeService.updateShoe(testUser.getUserId(), existingShoe.getId(), updatedShoeDTO);

        assertNotNull(updatedShoe);
        assertEquals(existingShoe.getId(), updatedShoe.getId());
        assertEquals("Updated Shoe", updatedShoe.getName());
        assertEquals("Updated Brand", updatedShoe.getBrand());
        assertEquals(9.0, updatedShoe.getSize());
        assertEquals("White", updatedShoe.getColor());
        assertEquals(150.0, updatedShoe.getPrice());
        assertEquals(testUser.getUserId(), updatedShoe.getUserId());

        // Verify the update in the repository
        ShoeEntity savedShoe = shoeRepository.findById(existingShoe.getId()).orElse(null);
        assertNotNull(savedShoe);
        assertEquals("Updated Shoe", savedShoe.getName());
        assertEquals("Updated Brand", savedShoe.getBrand());
        assertEquals(9.0, savedShoe.getSize());
        assertEquals("White", savedShoe.getColor());
        assertEquals(150.0, savedShoe.getPrice());
        assertEquals(testUser.getUserId(), savedShoe.getUserEntity().getUserId());

    }

    @Test
    public void testDeleteShoe(){
        ShoeEntity shoe = ShoeEntity.builder()
                .name("Test Shoe")
                .brand("Test Brand")
                .size(6.0)
                .color("Green")
                .price(1200.99)
                .userEntity(testUser)
                .build();

        shoe = shoeRepository.save(shoe);
        Long shoeId = shoe.getId();

        shoeService.deleteShoe(testUser.getUserId(), shoeId);

        // The existsById() method belongs to the CRUDRepository interface that is implemented by the shoeRepository instance.
        assertFalse(shoeRepository.existsById(shoeId), "The Shoe is be deleted");
    }
}
