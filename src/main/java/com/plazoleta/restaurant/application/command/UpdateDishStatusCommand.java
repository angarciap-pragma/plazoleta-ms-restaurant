package com.plazoleta.restaurant.application.command;

/**
 * Representa la solicitud interna para activar o inactivar un plato.
 */
public record UpdateDishStatusCommand(
        Long dishId,
        Long ownerId,
        boolean active
) {
}
