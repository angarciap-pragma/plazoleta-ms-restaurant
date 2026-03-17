package com.plazoleta.restaurant.application.response;

import lombok.Builder;

/**
 * Representa la respuesta interna de modificación de plato.
 */
@Builder
public record DishUpdatedResponse(
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
