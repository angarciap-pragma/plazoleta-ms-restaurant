package com.plazoleta.restaurant.application.response;

import java.util.List;
import lombok.Builder;

/**
 * Representa una respuesta paginada de aplicación.
 */
@Builder
public record PagedResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
