package com.plazoleta.restaurant.infrastructure.service.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.plazoleta.restaurant.application.command.CreateDishCommand;
import com.plazoleta.restaurant.application.response.DishCreatedResponse;
import com.plazoleta.restaurant.application.command.UpdateDishCommand;
import com.plazoleta.restaurant.application.response.DishUpdatedResponse;
import com.plazoleta.restaurant.application.command.CreateRestaurantCommand;
import com.plazoleta.restaurant.application.command.UpdateDishStatusCommand;
import com.plazoleta.restaurant.application.response.DishSummaryResponse;
import com.plazoleta.restaurant.application.response.PagedResponse;
import com.plazoleta.restaurant.application.response.RestaurantOwnershipResponse;
import com.plazoleta.restaurant.application.response.RestaurantCreatedResponse;
import com.plazoleta.restaurant.application.response.RestaurantSummaryResponse;
import com.plazoleta.restaurant.domain.api.CreateDishServicePort;
import com.plazoleta.restaurant.domain.api.CreateRestaurantServicePort;
import com.plazoleta.restaurant.domain.api.GetDishByIdServicePort;
import com.plazoleta.restaurant.domain.api.GetRestaurantByIdServicePort;
import com.plazoleta.restaurant.domain.api.ListRestaurantDishesServicePort;
import com.plazoleta.restaurant.domain.api.ListRestaurantsServicePort;
import com.plazoleta.restaurant.domain.api.UpdateDishStatusServicePort;
import com.plazoleta.restaurant.domain.api.UpdateDishServicePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RestaurantHandlerTest {

    private final CreateRestaurantServicePort createRestaurantServicePort = Mockito.mock(CreateRestaurantServicePort.class);
    private final CreateDishServicePort createDishServicePort = Mockito.mock(CreateDishServicePort.class);
    private final UpdateDishServicePort updateDishServicePort = Mockito.mock(UpdateDishServicePort.class);
    private final GetDishByIdServicePort getDishByIdServicePort = Mockito.mock(GetDishByIdServicePort.class);
    private final GetRestaurantByIdServicePort getRestaurantByIdServicePort =
            Mockito.mock(GetRestaurantByIdServicePort.class);
    private final UpdateDishStatusServicePort updateDishStatusServicePort =
            Mockito.mock(UpdateDishStatusServicePort.class);
    private final ListRestaurantsServicePort listRestaurantsServicePort =
            Mockito.mock(ListRestaurantsServicePort.class);
    private final ListRestaurantDishesServicePort listRestaurantDishesServicePort =
            Mockito.mock(ListRestaurantDishesServicePort.class);
    private final RestaurantHandler restaurantHandler = new RestaurantHandler(
            createRestaurantServicePort,
            createDishServicePort,
            updateDishServicePort,
            getDishByIdServicePort,
            getRestaurantByIdServicePort,
            updateDishStatusServicePort,
            listRestaurantsServicePort,
            listRestaurantDishesServicePort
    );

    @Test
    @DisplayName("should delegate restaurant creation to service port")
    void shouldDelegateRestaurantCreationToServicePort() {
        CreateRestaurantCommand command = new CreateRestaurantCommand(
                "Food Place", "123456", "Main street 1", "+573005698325", "https://logo.test", 1L
        );
        RestaurantCreatedResponse response = RestaurantCreatedResponse.builder().id(1L).name("Food Place").build();
        when(createRestaurantServicePort.createRestaurant(command)).thenReturn(response);

        assertThat(restaurantHandler.createRestaurant(command).id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("should delegate dish creation to service port")
    void shouldDelegateDishCreationToServicePort() {
        CreateDishCommand command = new CreateDishCommand(
                1L, 10L, "Burger", 20000, "Beef burger", "https://image.test/burger.png", "FAST_FOOD"
        );
        DishCreatedResponse response = DishCreatedResponse.builder().id(8L).name("Burger").active(true).build();
        when(createDishServicePort.createDish(command)).thenReturn(response);

        assertThat(restaurantHandler.createDish(command).id()).isEqualTo(8L);
    }

    @Test
    @DisplayName("should delegate dish update to service port")
    void shouldDelegateDishUpdateToServicePort() {
        UpdateDishCommand command = new UpdateDishCommand(8L, 10L, 25000, "Updated");
        DishUpdatedResponse response = DishUpdatedResponse.builder().id(8L).price(25000).description("Updated").build();
        when(updateDishServicePort.updateDish(command)).thenReturn(response);

        assertThat(restaurantHandler.updateDish(command).price()).isEqualTo(25000);
    }

    @Test
    @DisplayName("should delegate internal restaurant lookup to service port")
    void shouldDelegateInternalRestaurantLookupToServicePort() {
        RestaurantOwnershipResponse response = RestaurantOwnershipResponse.builder().id(1L).ownerId(2L).build();
        when(getRestaurantByIdServicePort.getRestaurantById(1L)).thenReturn(response);

        assertThat(restaurantHandler.getRestaurantById(1L).ownerId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("should delegate dish active status update to service port")
    void shouldDelegateDishActiveStatusUpdateToServicePort() {
        UpdateDishStatusCommand command = new UpdateDishStatusCommand(8L, 10L, false);
        DishUpdatedResponse response = DishUpdatedResponse.builder().id(8L).active(false).build();
        when(updateDishStatusServicePort.updateDishStatus(command)).thenReturn(response);

        assertThat(restaurantHandler.updateDishStatus(command).active()).isFalse();
    }

    @Test
    @DisplayName("should delegate restaurant listing to service port")
    void shouldDelegateRestaurantListingToServicePort() {
        PagedResponse<RestaurantSummaryResponse> response = PagedResponse.<RestaurantSummaryResponse>builder()
                .content(java.util.List.of(RestaurantSummaryResponse.builder().id(1L).name("Food").build()))
                .page(0)
                .size(10)
                .totalElements(1)
                .totalPages(1)
                .build();
        when(listRestaurantsServicePort.listRestaurants(new com.plazoleta.restaurant.application.query.ListRestaurantsQuery(0, 10)))
                .thenReturn(response);

        assertThat(restaurantHandler.listRestaurants(0, 10).content()).hasSize(1);
    }

    @Test
    @DisplayName("should delegate dish listing to service port")
    void shouldDelegateDishListingToServicePort() {
        PagedResponse<DishSummaryResponse> response = PagedResponse.<DishSummaryResponse>builder()
                .content(java.util.List.of(DishSummaryResponse.builder().id(1L).name("Burger").build()))
                .page(0)
                .size(10)
                .totalElements(1)
                .totalPages(1)
                .build();
        when(listRestaurantDishesServicePort.listRestaurantDishes(
                new com.plazoleta.restaurant.application.query.ListRestaurantDishesQuery(1L, "FAST_FOOD", 0, 10)
        )).thenReturn(response);

        assertThat(restaurantHandler.listRestaurantDishes(1L, "FAST_FOOD", 0, 10).content()).hasSize(1);
    }
}
