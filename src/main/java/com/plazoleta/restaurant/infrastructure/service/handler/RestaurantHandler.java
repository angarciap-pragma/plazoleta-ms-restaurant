package com.plazoleta.restaurant.infrastructure.service.handler;

import com.plazoleta.restaurant.application.command.CreateRestaurantCommand;
import com.plazoleta.restaurant.application.command.CreateDishCommand;
import com.plazoleta.restaurant.application.command.UpdateDishCommand;
import com.plazoleta.restaurant.application.response.DishCreatedResponse;
import com.plazoleta.restaurant.application.response.DishUpdatedResponse;
import com.plazoleta.restaurant.application.response.RestaurantCreatedResponse;
import com.plazoleta.restaurant.domain.api.CreateDishServicePort;
import com.plazoleta.restaurant.domain.api.CreateRestaurantServicePort;
import com.plazoleta.restaurant.domain.api.UpdateDishServicePort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Orquesta las solicitudes HTTP del módulo de restaurantes.
 */
@Component
@RequiredArgsConstructor
public class RestaurantHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantHandler.class);

    private final CreateRestaurantServicePort createRestaurantServicePort;
    private final CreateDishServicePort createDishServicePort;
    private final UpdateDishServicePort updateDishServicePort;

    public RestaurantCreatedResponse createRestaurant(final CreateRestaurantCommand command) {
        LOGGER.info("Creating restaurant with nit {}", command.nit());
        return createRestaurantServicePort.createRestaurant(command);
    }

    public DishCreatedResponse createDish(final CreateDishCommand command) {
        LOGGER.info("Creating dish {} for restaurant {}", command.name(), command.restaurantId());
        return createDishServicePort.createDish(command);
    }

    public DishUpdatedResponse updateDish(final UpdateDishCommand command) {
        LOGGER.info("Updating dish {}", command.dishId());
        return updateDishServicePort.updateDish(command);
    }
}
