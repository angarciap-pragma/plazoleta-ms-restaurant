package com.plazoleta.restaurant.domain.spi;

import com.plazoleta.restaurant.domain.model.Restaurant;
import java.util.Optional;

/**
 * Define la persistencia del agregado de restaurante.
 */
public interface RestaurantPersistencePort {

    boolean existsByNit(String nit);

    Optional<Restaurant> findById(Long restaurantId);

    Restaurant save(Restaurant restaurant);
}
