package com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * Representa el cuerpo HTTP para activar o inactivar un plato.
 */
public record UpdateDishStatusRequestDto(
        @NotNull(message = "active is required")
        Boolean active
) {
}
