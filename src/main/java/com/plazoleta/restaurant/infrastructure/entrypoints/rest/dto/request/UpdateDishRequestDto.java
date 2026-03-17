package com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Representa el cuerpo HTTP para modificar un plato.
 */
public record UpdateDishRequestDto(
        @NotNull(message = "ownerId is required")
        Long ownerId,
        @Min(value = 1, message = "price must be greater than zero")
        int price,
        @NotBlank(message = "description is required")
        String description
) {
}
