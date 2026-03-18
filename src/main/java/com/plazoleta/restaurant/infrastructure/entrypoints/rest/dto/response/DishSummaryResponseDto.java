package com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response;

/**
 * Representa la respuesta resumida de plato.
 */
public record DishSummaryResponseDto(
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
