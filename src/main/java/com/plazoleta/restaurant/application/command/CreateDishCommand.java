package com.plazoleta.restaurant.application.command;

/**
 * Representa la solicitud interna para crear un plato.
 */
public record CreateDishCommand(
        Long restaurantId,
        Long ownerId,
        String name,
        int price,
        String description,
        String imageUrl,
        String category
) {
}
