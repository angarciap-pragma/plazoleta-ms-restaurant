package com.plazoleta.restaurant.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.plazoleta.common.exception.BadRequestException;
import com.plazoleta.restaurant.application.query.ListRestaurantsQuery;
import com.plazoleta.restaurant.application.response.RestaurantSummaryResponse;
import com.plazoleta.restaurant.domain.model.Restaurant;
import com.plazoleta.restaurant.domain.spi.RestaurantPersistencePort;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ListRestaurantsUseCaseTest {

    private final RestaurantPersistencePort restaurantPersistencePort = Mockito.mock(RestaurantPersistencePort.class);
    private final ListRestaurantsUseCase useCase = new ListRestaurantsUseCase(restaurantPersistencePort);

    @Test
    @DisplayName("should list restaurants with pagination")
    void shouldListRestaurantsWithPagination() {
        when(restaurantPersistencePort.count()).thenReturn(2L);
        when(restaurantPersistencePort.findAllOrderedByName(0, 1)).thenReturn(List.of(
                Restaurant.builder().id(1L).name("A Food").logoUrl("https://logo.test/a.png").build()
        ));

        var response = useCase.listRestaurants(new ListRestaurantsQuery(0, 1));

        assertThat(response.content()).hasSize(1);
        assertThat(response.totalPages()).isEqualTo(2);
        assertThat(response.content().getFirst()).isEqualTo(
                RestaurantSummaryResponse.builder().id(1L).name("A Food").logoUrl("https://logo.test/a.png").build()
        );
    }

    @Test
    @DisplayName("should reject invalid restaurant page request")
    void shouldRejectInvalidRestaurantPageRequest() {
        assertThatThrownBy(() -> useCase.listRestaurants(new ListRestaurantsQuery(-1, 0)))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("should reject zero restaurant page size")
    void shouldRejectZeroRestaurantPageSize() {
        assertThatThrownBy(() -> useCase.listRestaurants(new ListRestaurantsQuery(0, 0)))
                .isInstanceOf(BadRequestException.class);
    }
}
