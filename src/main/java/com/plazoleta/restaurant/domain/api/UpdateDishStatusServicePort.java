package com.plazoleta.restaurant.domain.api;

import com.plazoleta.restaurant.application.command.UpdateDishStatusCommand;
import com.plazoleta.restaurant.application.response.DishUpdatedResponse;

/**
 * Define el cambio de estado activo del plato.
 */
public interface UpdateDishStatusServicePort {

    DishUpdatedResponse updateDishStatus(UpdateDishStatusCommand command);
}
