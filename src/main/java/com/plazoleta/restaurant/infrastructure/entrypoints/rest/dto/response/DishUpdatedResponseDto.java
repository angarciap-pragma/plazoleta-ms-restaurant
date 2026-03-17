package com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response;

/**
 * Representa la respuesta HTTP de modificación de plato.
 */
public record DishUpdatedResponseDto(
        Long id,
        String name,
        int price,
        String description,
        String imageUrl,
        String category,
        boolean active,
        Long restaurantId
) {
}
