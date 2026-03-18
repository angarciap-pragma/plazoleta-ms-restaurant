package com.plazoleta.restaurant.domain.api;

import com.plazoleta.restaurant.application.query.ListRestaurantsQuery;
import com.plazoleta.restaurant.application.response.PagedResponse;
import com.plazoleta.restaurant.application.response.RestaurantSummaryResponse;

/**
 * Define el listado paginado de restaurantes.
 */
public interface ListRestaurantsServicePort {

    PagedResponse<RestaurantSummaryResponse> listRestaurants(ListRestaurantsQuery query);
}
