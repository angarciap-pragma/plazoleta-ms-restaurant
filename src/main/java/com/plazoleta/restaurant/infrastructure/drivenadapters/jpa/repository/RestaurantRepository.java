package com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.repository;

import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Expone el acceso JPA a la tabla de restaurantes.
 */
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    boolean existsByNit(String nit);
}
