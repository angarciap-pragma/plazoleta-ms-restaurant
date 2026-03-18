package com.plazoleta.restaurant.infrastructure.service.handler;

import com.plazoleta.restaurant.application.command.CreateRestaurantCommand;
import com.plazoleta.restaurant.application.command.CreateDishCommand;
import com.plazoleta.restaurant.application.command.UpdateDishStatusCommand;
import com.plazoleta.restaurant.application.command.UpdateDishCommand;
import com.plazoleta.restaurant.application.response.DishCreatedResponse;
import com.plazoleta.restaurant.application.response.DishInternalResponse;
import com.plazoleta.restaurant.application.response.DishSummaryResponse;
import com.plazoleta.restaurant.application.response.DishUpdatedResponse;
import com.plazoleta.restaurant.application.response.PagedResponse;
import com.plazoleta.restaurant.application.response.RestaurantOwnershipResponse;
import com.plazoleta.restaurant.application.response.RestaurantCreatedResponse;
import com.plazoleta.restaurant.application.response.RestaurantSummaryResponse;
import com.plazoleta.restaurant.application.query.ListRestaurantDishesQuery;
import com.plazoleta.restaurant.application.query.ListRestaurantsQuery;
import com.plazoleta.restaurant.domain.api.CreateDishServicePort;
import com.plazoleta.restaurant.domain.api.CreateRestaurantServicePort;
import com.plazoleta.restaurant.domain.api.GetDishByIdServicePort;
import com.plazoleta.restaurant.domain.api.GetRestaurantByIdServicePort;
import com.plazoleta.restaurant.domain.api.ListRestaurantDishesServicePort;
import com.plazoleta.restaurant.domain.api.ListRestaurantsServicePort;
import com.plazoleta.restaurant.domain.api.UpdateDishStatusServicePort;
import com.plazoleta.restaurant.domain.api.UpdateDishServicePort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Orquesta las solicitudes HTTP del módulo de restaurantes.
 */
@Component
@RequiredArgsConstructor
public class RestaurantHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantHandler.class);

    private final CreateRestaurantServicePort createRestaurantServicePort;
    private final CreateDishServicePort createDishServicePort;
    private final UpdateDishServicePort updateDishServicePort;
    private final GetDishByIdServicePort getDishByIdServicePort;
    private final GetRestaurantByIdServicePort getRestaurantByIdServicePort;
    private final UpdateDishStatusServicePort updateDishStatusServicePort;
    private final ListRestaurantsServicePort listRestaurantsServicePort;
    private final ListRestaurantDishesServicePort listRestaurantDishesServicePort;

    public RestaurantCreatedResponse createRestaurant(final CreateRestaurantCommand command) {
        LOGGER.info("Creating restaurant with nit {}", command.nit());
        return createRestaurantServicePort.createRestaurant(command);
    }

    public DishCreatedResponse createDish(final CreateDishCommand command) {
        LOGGER.info("Creating dish {} for restaurant {}", command.name(), command.restaurantId());
        return createDishServicePort.createDish(command);
    }

    public DishUpdatedResponse updateDish(final UpdateDishCommand command) {
        LOGGER.info("Updating dish {}", command.dishId());
        return updateDishServicePort.updateDish(command);
    }

    public RestaurantOwnershipResponse getRestaurantById(final Long restaurantId) {
        LOGGER.info("Fetching restaurant {}", restaurantId);
        return getRestaurantByIdServicePort.getRestaurantById(restaurantId);
    }

    public DishInternalResponse getDishById(final Long dishId) {
        LOGGER.info("Fetching internal dish {}", dishId);
        return getDishByIdServicePort.getDishById(dishId);
    }

    public DishUpdatedResponse updateDishStatus(final UpdateDishStatusCommand command) {
        LOGGER.info("Updating active status for dish {}", command.dishId());
        return updateDishStatusServicePort.updateDishStatus(command);
    }

    public PagedResponse<RestaurantSummaryResponse> listRestaurants(final int page, final int size) {
        LOGGER.info("Listing restaurants page {} size {}", page, size);
        return listRestaurantsServicePort.listRestaurants(new ListRestaurantsQuery(page, size));
    }

    public PagedResponse<DishSummaryResponse> listRestaurantDishes(
            final Long restaurantId,
            final String category,
            final int page,
            final int size
    ) {
        LOGGER.info("Listing dishes for restaurant {} page {} size {}", restaurantId, page, size);
        return listRestaurantDishesServicePort.listRestaurantDishes(
                new ListRestaurantDishesQuery(restaurantId, category, page, size)
        );
    }
}
