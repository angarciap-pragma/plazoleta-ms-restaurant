package com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response;

/**
 * Representa la respuesta HTTP interna de plato para integraciones.
 */
public record DishInternalResponseDto(
        Long id,
        Long restaurantId,
        int price,
        String category,
        boolean active
) {
}
