package com.plazoleta.restaurant.application.response;

import lombok.Builder;

/**
 * Representa la respuesta resumida de plato para catálogo.
 */
@Builder
public record DishSummaryResponse(
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
