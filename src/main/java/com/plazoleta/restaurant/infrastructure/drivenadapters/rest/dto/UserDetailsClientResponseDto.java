package com.plazoleta.restaurant.infrastructure.drivenadapters.rest.dto;

/**
 * Representa la respuesta remota de consulta de usuario en ms-user.
 */
public record UserDetailsClientResponseDto(
        Long id,
        String firstName,
        String lastName,
        String documentId,
        String email,
        String role
) {
}
