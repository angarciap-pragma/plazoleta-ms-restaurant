package com.plazoleta.restaurant.application.query;

/**
 * Representa la consulta paginada de restaurantes.
 */
public record ListRestaurantsQuery(
        int page,
        int size
) {
}
