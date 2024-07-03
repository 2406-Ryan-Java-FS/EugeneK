package com.eugene.shoegame.repositories;

import com.eugene.shoegame.dto.UserDTO;
import com.eugene.shoegame.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /* The Optional class helps avoid NullPointerExceptions in case a user is not found.*/
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}
