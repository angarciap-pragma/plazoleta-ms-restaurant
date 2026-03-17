package com.plazoleta.restaurant.domain.api;

import com.plazoleta.restaurant.application.command.UpdateDishCommand;
import com.plazoleta.restaurant.application.response.DishUpdatedResponse;

/**
 * Define la operación de modificación de platos.
 */
public interface UpdateDishServicePort {

    DishUpdatedResponse updateDish(UpdateDishCommand command);
}
