package com.plazoleta.restaurant.application.response;

import lombok.Builder;

/**
 * Representa la respuesta interna de creación de restaurante.
 */
@Builder
public record RestaurantCreatedResponse(
        Long id,
        String name,
        String nit,
        String address,
        String phoneNumber,
        String logoUrl,
        Long ownerId
) {
}
