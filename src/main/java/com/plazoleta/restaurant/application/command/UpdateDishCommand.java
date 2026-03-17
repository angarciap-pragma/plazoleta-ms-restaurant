package com.plazoleta.restaurant.application.command;

/**
 * Representa la solicitud interna para modificar un plato.
 */
public record UpdateDishCommand(
        Long dishId,
        Long ownerId,
        int price,
        String description
) {
}
