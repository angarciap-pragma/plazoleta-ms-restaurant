package com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.repository;

import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.entity.RestaurantEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Expone el acceso JPA a la tabla de restaurantes.
 */
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    boolean existsByNit(String nit);

    List<RestaurantEntity> findAllByOrderByNameAsc(Pageable pageable);
}
