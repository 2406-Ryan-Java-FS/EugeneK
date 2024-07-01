package com.eugene.shoegame.repositories;

import com.eugene.shoegame.entities.ShoeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShoeRepository extends JpaRepository<ShoeEntity, Long> {
}
