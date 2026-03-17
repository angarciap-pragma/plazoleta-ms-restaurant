package com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Representa el cuerpo HTTP para crear un restaurante.
 */
public record CreateRestaurantRequestDto(
        @NotBlank(message = "name is required")
        String name,
        @NotBlank(message = "nit is required")
        @Pattern(regexp = "^[0-9]+$", message = "nit must contain only digits")
        String nit,
        @NotBlank(message = "address is required")
        String address,
        @NotBlank(message = "phoneNumber is required")
        @Size(max = 13, message = "phoneNumber must contain at most 13 characters")
        @Pattern(regexp = "^\\+?[0-9]+$", message = "phoneNumber must contain only digits and optional leading plus sign")
        String phoneNumber,
        @NotBlank(message = "logoUrl is required")
        String logoUrl,
        @NotNull(message = "ownerId is required")
        Long ownerId
) {
}
