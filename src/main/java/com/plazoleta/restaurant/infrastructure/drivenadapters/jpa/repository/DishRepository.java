package com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.repository;

import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.entity.DishEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Expone el acceso JPA a la tabla de platos.
 */
public interface DishRepository extends JpaRepository<DishEntity, Long> {

    List<DishEntity> findByRestaurantIdAndActiveTrueOrderByNameAsc(Long restaurantId, Pageable pageable);

    List<DishEntity> findByRestaurantIdAndCategoryIgnoreCaseAndActiveTrueOrderByNameAsc(
            Long restaurantId,
            String category,
            Pageable pageable
    );

    long countByRestaurantIdAndActiveTrue(Long restaurantId);

    long countByRestaurantIdAndCategoryIgnoreCaseAndActiveTrue(Long restaurantId, String category);
}
