package com.plazoleta.restaurant.application.response;

import lombok.Builder;

/**
 * Representa la respuesta interna de creación de plato.
 */
@Builder
public record DishCreatedResponse(
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
