package com.plazoleta.restaurant.application.query;

/**
 * Representa la consulta paginada de platos por restaurante.
 */
public record ListRestaurantDishesQuery(
        Long restaurantId,
        String category,
        int page,
        int size
) {
}
