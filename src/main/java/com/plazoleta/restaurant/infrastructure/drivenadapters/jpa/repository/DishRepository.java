package com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.repository;

import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Expone el acceso JPA a la tabla de platos.
 */
public interface DishRepository extends JpaRepository<DishEntity, Long> {
}
