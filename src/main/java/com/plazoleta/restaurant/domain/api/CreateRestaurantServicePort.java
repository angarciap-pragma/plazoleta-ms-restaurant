package com.plazoleta.restaurant.domain.api;

import com.plazoleta.restaurant.application.command.CreateRestaurantCommand;
import com.plazoleta.restaurant.application.response.RestaurantCreatedResponse;

/**
 * Define la operación de creación de restaurantes.
 */
public interface CreateRestaurantServicePort {

    RestaurantCreatedResponse createRestaurant(CreateRestaurantCommand command);
}
