package com.plazoleta.restaurant.application.response;

import lombok.Builder;

/**
 * Representa la respuesta interna mínima de restaurante para integraciones.
 */
@Builder
public record RestaurantOwnershipResponse(
        Long id,
        Long ownerId
) {
}
