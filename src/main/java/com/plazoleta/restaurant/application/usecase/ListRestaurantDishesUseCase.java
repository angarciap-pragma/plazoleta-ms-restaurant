package com.plazoleta.restaurant.application.usecase;

import com.plazoleta.common.exception.BadRequestException;
import com.plazoleta.common.exception.ResourceNotFoundException;
import com.plazoleta.restaurant.application.query.ListRestaurantDishesQuery;
import com.plazoleta.restaurant.application.response.DishSummaryResponse;
import com.plazoleta.restaurant.application.response.PagedResponse;
import com.plazoleta.restaurant.domain.api.ListRestaurantDishesServicePort;
import com.plazoleta.restaurant.domain.exception.RestaurantErrorCode;
import com.plazoleta.restaurant.domain.spi.DishPersistencePort;
import com.plazoleta.restaurant.domain.spi.RestaurantPersistencePort;

/**
 * Implementa el listado paginado de platos por restaurante.
 */
public class ListRestaurantDishesUseCase implements ListRestaurantDishesServicePort {

    private final RestaurantPersistencePort restaurantPersistencePort;
    private final DishPersistencePort dishPersistencePort;

    public ListRestaurantDishesUseCase(
            final RestaurantPersistencePort restaurantPersistencePort,
            final DishPersistencePort dishPersistencePort
    ) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
    }

    @Override
    public PagedResponse<DishSummaryResponse> listRestaurantDishes(final ListRestaurantDishesQuery query) {
        validatePageRequest(query.page(), query.size());
        restaurantPersistencePort.findById(query.restaurantId())
                .orElseThrow(() -> new ResourceNotFoundException(RestaurantErrorCode.RESTAURANT_NOT_FOUND));

        long totalElements = dishPersistencePort.countActiveByRestaurantId(query.restaurantId(), query.category());

        return PagedResponse.<DishSummaryResponse>builder()
                .content(dishPersistencePort.findActiveByRestaurantId(
                                query.restaurantId(),
                                query.category(),
                                query.page(),
                                query.size()
                        )
                        .stream()
                        .map(dish -> DishSummaryResponse.builder()
                                .id(dish.getId())
                                .name(dish.getName())
                                .price(dish.getPrice())
                                .description(dish.getDescription())
                                .imageUrl(dish.getImageUrl())
                                .category(dish.getCategory())
                                .active(dish.isActive())
                                .restaurantId(dish.getRestaurantId())
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
