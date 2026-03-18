package com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Representa el cuerpo HTTP para modificar un plato.
 */
public record UpdateDishRequestDto(
        @Min(value = 1, message = "price must be greater than zero")
        int price,
        @NotBlank(message = "description is required")
        String description
) {
}
