package com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response;

/**
 * Representa la respuesta HTTP de creación de restaurante.
 */
public record RestaurantCreatedResponseDto(
        Long id,
        String name,
        String nit,
        String address,
        String phoneNumber,
        String logoUrl,
        Long ownerId
) {
}
