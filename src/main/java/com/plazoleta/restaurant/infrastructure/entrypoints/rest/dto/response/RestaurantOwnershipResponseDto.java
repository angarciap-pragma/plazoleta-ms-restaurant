package com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response;

/**
 * Representa la respuesta HTTP interna mínima de restaurante.
 */
public record RestaurantOwnershipResponseDto(
        Long id,
        Long ownerId
) {
}
