package com.plazoleta.restaurant.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.plazoleta.common.exception.BadRequestException;
import com.plazoleta.common.exception.ResourceNotFoundException;
import com.plazoleta.restaurant.application.query.ListRestaurantDishesQuery;
import com.plazoleta.restaurant.domain.model.Dish;
import com.plazoleta.restaurant.domain.model.Restaurant;
import com.plazoleta.restaurant.domain.spi.DishPersistencePort;
import com.plazoleta.restaurant.domain.spi.RestaurantPersistencePort;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ListRestaurantDishesUseCaseTest {

    private final RestaurantPersistencePort restaurantPersistencePort = Mockito.mock(RestaurantPersistencePort.class);
    private final DishPersistencePort dishPersistencePort = Mockito.mock(DishPersistencePort.class);
    private final ListRestaurantDishesUseCase useCase =
            new ListRestaurantDishesUseCase(restaurantPersistencePort, dishPersistencePort);

    @Test
    @DisplayName("should list restaurant dishes with pagination and category")
    void shouldListRestaurantDishesWithPaginationAndCategory() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(Optional.of(Restaurant.builder().id(1L).build()));
        when(dishPersistencePort.countActiveByRestaurantId(1L, "FAST_FOOD")).thenReturn(2L);
        when(dishPersistencePort.findActiveByRestaurantId(1L, "FAST_FOOD", 0, 1)).thenReturn(List.of(
                Dish.builder()
                        .id(1L)
                        .name("Burger")
                        .price(20000)
                        .description("Beef burger")
                        .imageUrl("https://image.test/burger.png")
                        .category("FAST_FOOD")
                        .active(true)
                        .restaurantId(1L)
                        .build()
        ));

        var response = useCase.listRestaurantDishes(new ListRestaurantDishesQuery(1L, "FAST_FOOD", 0, 1));

        assertThat(response.content()).hasSize(1);
        assertThat(response.totalPages()).isEqualTo(2);
        assertThat(response.content().getFirst().name()).isEqualTo("Burger");
    }

    @Test
    @DisplayName("should reject invalid dish page request")
    void shouldRejectInvalidDishPageRequest() {
        assertThatThrownBy(() -> useCase.listRestaurantDishes(new ListRestaurantDishesQuery(1L, null, -1, 0)))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("should reject zero dish page size")
    void shouldRejectZeroDishPageSize() {
        assertThatThrownBy(() -> useCase.listRestaurantDishes(new ListRestaurantDishesQuery(1L, null, 0, 0)))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("should reject missing restaurant when listing dishes")
    void shouldRejectMissingRestaurantWhenListingDishes() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.listRestaurantDishes(new ListRestaurantDishesQuery(1L, null, 0, 10)))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
