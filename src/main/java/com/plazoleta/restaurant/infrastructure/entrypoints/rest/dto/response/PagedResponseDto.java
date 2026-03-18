package com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response;

import java.util.List;

/**
 * Representa una respuesta HTTP paginada.
 */
public record PagedResponseDto<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
