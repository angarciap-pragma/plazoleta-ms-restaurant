package com.plazoleta.restaurant.application.usecase;

import com.plazoleta.common.exception.ResourceNotFoundException;
import com.plazoleta.restaurant.application.response.RestaurantOwnershipResponse;
import com.plazoleta.restaurant.domain.api.GetRestaurantByIdServicePort;
import com.plazoleta.restaurant.domain.exception.RestaurantErrorCode;
import com.plazoleta.restaurant.domain.model.Restaurant;
import com.plazoleta.restaurant.domain.spi.RestaurantPersistencePort;

/**
 * Implementa la consulta interna de restaurante por id.
 */
public class GetRestaurantByIdUseCase implements GetRestaurantByIdServicePort {

    private final RestaurantPersistencePort restaurantPersistencePort;

    public GetRestaurantByIdUseCase(final RestaurantPersistencePort restaurantPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public RestaurantOwnershipResponse getRestaurantById(final Long restaurantId) {
        Restaurant restaurant = restaurantPersistencePort.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(RestaurantErrorCode.RESTAURANT_NOT_FOUND));

        return RestaurantOwnershipResponse.builder()
                .id(restaurant.getId())
                .ownerId(restaurant.getOwnerId())
                .build();
    }
}
