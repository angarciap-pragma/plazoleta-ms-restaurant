package com.plazoleta.restaurant.domain.spi;

import com.plazoleta.restaurant.domain.model.Restaurant;

/**
 * Define la persistencia del agregado de restaurante.
 */
public interface RestaurantPersistencePort {

    boolean existsByNit(String nit);

    Restaurant save(Restaurant restaurant);
}
