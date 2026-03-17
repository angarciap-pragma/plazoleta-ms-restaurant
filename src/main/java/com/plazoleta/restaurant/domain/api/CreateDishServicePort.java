package com.plazoleta.restaurant.domain.api;

import com.plazoleta.restaurant.application.command.CreateDishCommand;
import com.plazoleta.restaurant.application.response.DishCreatedResponse;

/**
 * Define la operación de creación de platos.
 */
public interface CreateDishServicePort {

    DishCreatedResponse createDish(CreateDishCommand command);
}
