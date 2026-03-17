package com.plazoleta.restaurant.domain.spi;

import com.plazoleta.restaurant.domain.model.Dish;

/**
 * Define la persistencia del agregado de plato.
 */
public interface DishPersistencePort {

    Dish save(Dish dish);
}
