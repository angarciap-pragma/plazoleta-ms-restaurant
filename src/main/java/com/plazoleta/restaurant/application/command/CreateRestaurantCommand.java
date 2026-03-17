package com.plazoleta.restaurant.application.command;

/**
 * Representa la solicitud interna para crear un restaurante.
 */
public record CreateRestaurantCommand(
        String name,
        String nit,
        String address,
        String phoneNumber,
        String logoUrl,
        Long ownerId
) {
}
