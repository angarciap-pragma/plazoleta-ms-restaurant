package com.plazoleta.restaurant.domain.api;

import com.plazoleta.restaurant.application.response.RestaurantOwnershipResponse;

/**
 * Define la consulta interna de restaurante por identificador.
 */
public interface GetRestaurantByIdServicePort {

    RestaurantOwnershipResponse getRestaurantById(Long restaurantId);
}
