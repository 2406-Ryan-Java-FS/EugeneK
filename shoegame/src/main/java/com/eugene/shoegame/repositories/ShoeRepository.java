package com.eugene.shoegame.repositories;

import com.eugene.shoegame.entities.ShoeEntity;
import com.eugene.shoegame.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ShoeRepository extends JpaRepository<ShoeEntity, Long> {
    Optional<ShoeEntity> findByIdAndUserEntity_UserId(Long id, Long userId);
    List<ShoeEntity> findAllByUserEntity_UserId(Long userId);

}
