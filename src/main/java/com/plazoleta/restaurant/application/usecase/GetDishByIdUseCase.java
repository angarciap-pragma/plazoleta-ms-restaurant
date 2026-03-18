package com.plazoleta.restaurant.application.usecase;

import com.plazoleta.common.exception.ResourceNotFoundException;
import com.plazoleta.restaurant.application.response.DishInternalResponse;
import com.plazoleta.restaurant.domain.api.GetDishByIdServicePort;
import com.plazoleta.restaurant.domain.exception.RestaurantErrorCode;
import com.plazoleta.restaurant.domain.model.Dish;
import com.plazoleta.restaurant.domain.spi.DishPersistencePort;

/**
 * Implementa la consulta interna de platos por id.
 */
public class GetDishByIdUseCase implements GetDishByIdServicePort {

    private final DishPersistencePort dishPersistencePort;

    public GetDishByIdUseCase(final DishPersistencePort dishPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
    }

    @Override
    public DishInternalResponse getDishById(final Long dishId) {
        Dish dish = dishPersistencePort.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException(RestaurantErrorCode.DISH_NOT_FOUND));

        return DishInternalResponse.builder()
                .id(dish.getId())
                .restaurantId(dish.getRestaurantId())
                .price(dish.getPrice())
                .category(dish.getCategory())
                .active(dish.isActive())
                .build();
    }
}
