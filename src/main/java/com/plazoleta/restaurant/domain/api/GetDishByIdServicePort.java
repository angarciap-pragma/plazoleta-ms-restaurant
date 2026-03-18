package com.plazoleta.restaurant.domain.api;

import com.plazoleta.restaurant.application.response.DishInternalResponse;

/**
 * Define la consulta interna de platos por identificador.
 */
public interface GetDishByIdServicePort {

    DishInternalResponse getDishById(Long dishId);
}
