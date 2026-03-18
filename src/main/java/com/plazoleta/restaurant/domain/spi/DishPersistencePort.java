package com.plazoleta.restaurant.domain.spi;

import com.plazoleta.restaurant.domain.model.Dish;
import java.util.List;
import java.util.Optional;

/**
 * Define la persistencia del agregado de plato.
 */
public interface DishPersistencePort {

    Optional<Dish> findById(Long dishId);

    List<Dish> findActiveByRestaurantId(Long restaurantId, String category, int page, int size);

    long countActiveByRestaurantId(Long restaurantId, String category);

    Dish save(Dish dish);
}
