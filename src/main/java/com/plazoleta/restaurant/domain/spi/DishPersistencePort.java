package com.plazoleta.restaurant.domain.spi;

import com.plazoleta.restaurant.domain.model.Dish;
import java.util.Optional;

/**
 * Define la persistencia del agregado de plato.
 */
public interface DishPersistencePort {

    Optional<Dish> findById(Long dishId);

    Dish save(Dish dish);
}
