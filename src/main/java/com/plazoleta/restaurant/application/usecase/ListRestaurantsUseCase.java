package com.plazoleta.restaurant.application.usecase;

import com.plazoleta.common.exception.BadRequestException;
import com.plazoleta.restaurant.application.query.ListRestaurantsQuery;
import com.plazoleta.restaurant.application.response.PagedResponse;
import com.plazoleta.restaurant.application.response.RestaurantSummaryResponse;
import com.plazoleta.restaurant.domain.api.ListRestaurantsServicePort;
import com.plazoleta.restaurant.domain.exception.RestaurantErrorCode;
import com.plazoleta.restaurant.domain.spi.RestaurantPersistencePort;

/**
 * Implementa el listado paginado de restaurantes.
 */
public class ListRestaurantsUseCase implements ListRestaurantsServicePort {

    private final RestaurantPersistencePort restaurantPersistencePort;

    public ListRestaurantsUseCase(final RestaurantPersistencePort restaurantPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public PagedResponse<RestaurantSummaryResponse> listRestaurants(final ListRestaurantsQuery query) {
        validatePageRequest(query.page(), query.size());
        long totalElements = restaurantPersistencePort.count();

        return PagedResponse.<RestaurantSummaryResponse>builder()
                .content(restaurantPersistencePort.findAllOrderedByName(query.page(), query.size())
                        .stream()
                        .map(restaurant -> RestaurantSummaryResponse.builder()
                                .id(restaurant.getId())
                                .name(restaurant.getName())
                                .logoUrl(restaurant.getLogoUrl())
                                .build())
                        .toList())
                .page(query.page())
                .size(query.size())
                .totalElements(totalElements)
                .totalPages((int) Math.ceil((double) totalElements / query.size()))
                .build();
    }

    private void validatePageRequest(final int page, final int size) {
        if (page < 0 || size <= 0) {
            throw new BadRequestException(RestaurantErrorCode.INVALID_PAGE_REQUEST);
        }
    }
}
