package com.plazoleta.restaurant.application.usecase;

import com.plazoleta.common.exception.ForbiddenException;
import com.plazoleta.common.exception.ResourceNotFoundException;
import com.plazoleta.restaurant.application.command.CreateDishCommand;
import com.plazoleta.restaurant.application.response.DishCreatedResponse;
import com.plazoleta.restaurant.domain.api.CreateDishServicePort;
import com.plazoleta.restaurant.domain.exception.RestaurantErrorCode;
import com.plazoleta.restaurant.domain.model.Dish;
import com.plazoleta.restaurant.domain.model.Restaurant;
import com.plazoleta.restaurant.domain.spi.DishPersistencePort;
import com.plazoleta.restaurant.domain.spi.RestaurantPersistencePort;

/**
 * Implementa el caso de uso para crear platos.
 */
public class CreateDishUseCase implements CreateDishServicePort {

    private final RestaurantPersistencePort restaurantPersistencePort;
    private final DishPersistencePort dishPersistencePort;

    public CreateDishUseCase(
            final RestaurantPersistencePort restaurantPersistencePort,
            final DishPersistencePort dishPersistencePort
    ) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
    }

    @Override
    public DishCreatedResponse createDish(final CreateDishCommand command) {
        Restaurant restaurant = restaurantPersistencePort.findById(command.restaurantId())
                .orElseThrow(() -> new ResourceNotFoundException(RestaurantErrorCode.RESTAURANT_NOT_FOUND));

        if (!restaurant.getOwnerId().equals(command.ownerId())) {
            throw new ForbiddenException(RestaurantErrorCode.RESTAURANT_OWNER_MISMATCH);
        }

        Dish savedDish = dishPersistencePort.save(Dish.create(
                command.name(),
                command.price(),
                command.description(),
                command.imageUrl(),
                command.category(),
                command.restaurantId()
        ));

        return DishCreatedResponse.builder()
                .id(savedDish.getId())
                .name(savedDish.getName())
                .price(savedDish.getPrice())
                .description(savedDish.getDescription())
                .imageUrl(savedDish.getImageUrl())
                .category(savedDish.getCategory())
                .active(savedDish.isActive())
                .restaurantId(savedDish.getRestaurantId())
                .build();
    }
}
