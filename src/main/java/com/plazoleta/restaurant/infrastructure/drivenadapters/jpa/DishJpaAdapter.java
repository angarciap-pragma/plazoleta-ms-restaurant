package com.plazoleta.restaurant.infrastructure.drivenadapters.jpa;

import com.plazoleta.restaurant.domain.model.Dish;
import com.plazoleta.restaurant.domain.spi.DishPersistencePort;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.mapper.DishEntityMapper;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementa la persistencia de platos usando JPA.
 */
@Component
@RequiredArgsConstructor
public class DishJpaAdapter implements DishPersistencePort {

    private final DishRepository dishRepository;
    private final DishEntityMapper dishEntityMapper;

    @Override
    public Dish save(final Dish dish) {
        return dishEntityMapper.toDomain(dishRepository.save(dishEntityMapper.toEntity(dish)));
    }
}
