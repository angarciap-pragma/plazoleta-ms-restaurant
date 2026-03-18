package com.plazoleta.restaurant.application.response;

import lombok.Builder;

/**
 * Representa la respuesta resumida de restaurante para catálogo.
 */
@Builder
public record RestaurantSummaryResponse(
        Long id,
        String name,
        String logoUrl
) {
}
