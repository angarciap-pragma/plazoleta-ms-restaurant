package com.plazoleta.restaurant.domain.api;

import com.plazoleta.restaurant.application.query.ListRestaurantDishesQuery;
import com.plazoleta.restaurant.application.response.DishSummaryResponse;
import com.plazoleta.restaurant.application.response.PagedResponse;

/**
 * Define el listado paginado de platos por restaurante.
 */
public interface ListRestaurantDishesServicePort {

    PagedResponse<DishSummaryResponse> listRestaurantDishes(ListRestaurantDishesQuery query);
}
