package com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response;

/**
 * Representa la respuesta resumida de restaurante.
 */
public record RestaurantSummaryResponseDto(
        Long id,
        String name,
        String logoUrl
) {
}
