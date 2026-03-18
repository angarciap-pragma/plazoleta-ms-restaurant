package com.plazoleta.restaurant.application.response;

import lombok.Builder;

/**
 * Representa la respuesta interna mínima de plato para integraciones.
 */
@Builder
public record DishInternalResponse(
        Long id,
        Long restaurantId,
        int price,
        String category,
        boolean active
) {
}
