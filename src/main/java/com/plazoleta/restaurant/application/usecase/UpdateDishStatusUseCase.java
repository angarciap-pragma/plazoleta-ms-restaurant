package com.plazoleta.restaurant.application.usecase;

import com.plazoleta.common.exception.ForbiddenException;
import com.plazoleta.common.exception.ResourceNotFoundException;
import com.plazoleta.restaurant.application.command.UpdateDishStatusCommand;
import com.plazoleta.restaurant.application.response.DishUpdatedResponse;
import com.plazoleta.restaurant.domain.api.UpdateDishStatusServicePort;
import com.plazoleta.restaurant.domain.exception.RestaurantErrorCode;
import com.plazoleta.restaurant.domain.model.Dish;
import com.plazoleta.restaurant.domain.model.Restaurant;
import com.plazoleta.restaurant.domain.spi.DishPersistencePort;
import com.plazoleta.restaurant.domain.spi.RestaurantPersistencePort;

/**
 * Implementa la activación e inactivación de platos.
 */
public class UpdateDishStatusUseCase implements UpdateDishStatusServicePort {

    private final DishPersistencePort dishPersistencePort;
    private final RestaurantPersistencePort restaurantPersistencePort;

    public UpdateDishStatusUseCase(
            final DishPersistencePort dishPersistencePort,
            final RestaurantPersistencePort restaurantPersistencePort
    ) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public DishUpdatedResponse updateDishStatus(final UpdateDishStatusCommand command) {
        Dish dish = dishPersistencePort.findById(command.dishId())
                .orElseThrow(() -> new ResourceNotFoundException(RestaurantErrorCode.DISH_NOT_FOUND));

        Restaurant restaurant = restaurantPersistencePort.findById(dish.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException(RestaurantErrorCode.RESTAURANT_NOT_FOUND));

        if (!restaurant.getOwnerId().equals(command.ownerId())) {
            throw new ForbiddenException(RestaurantErrorCode.DISH_OWNER_MISMATCH);
        }

        Dish updatedDish = dishPersistencePort.save(dish.updateActiveStatus(command.active()));

        return DishUpdatedResponse.builder()
                .id(updatedDish.getId())
                .name(updatedDish.getName())
                .price(updatedDish.getPrice())
                .description(updatedDish.getDescription())
                .imageUrl(updatedDish.getImageUrl())
                .category(updatedDish.getCategory())
                .active(updatedDish.isActive())
                .restaurantId(updatedDish.getRestaurantId())
                .build();
    }
}
