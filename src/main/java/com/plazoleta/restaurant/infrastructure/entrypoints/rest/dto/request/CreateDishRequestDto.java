package com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Representa el cuerpo HTTP para crear un plato.
 */
public record CreateDishRequestDto(
        @NotNull(message = "ownerId is required")
        Long ownerId,
        @NotBlank(message = "name is required")
        String name,
        @Min(value = 1, message = "price must be greater than zero")
        int price,
        @NotBlank(message = "description is required")
        String description,
        @NotBlank(message = "imageUrl is required")
        String imageUrl,
        @NotBlank(message = "category is required")
        String category
) {
}
