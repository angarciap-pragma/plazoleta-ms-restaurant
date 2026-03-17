package com.plazoleta.restaurant.infrastructure.service.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.plazoleta.restaurant.application.command.CreateDishCommand;
import com.plazoleta.restaurant.application.response.DishCreatedResponse;
import com.plazoleta.restaurant.application.command.UpdateDishCommand;
import com.plazoleta.restaurant.application.response.DishUpdatedResponse;
import com.plazoleta.restaurant.application.command.CreateRestaurantCommand;
import com.plazoleta.restaurant.application.response.RestaurantCreatedResponse;
import com.plazoleta.restaurant.domain.api.CreateDishServicePort;
import com.plazoleta.restaurant.domain.api.CreateRestaurantServicePort;
import com.plazoleta.restaurant.domain.api.UpdateDishServicePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RestaurantHandlerTest {

    private final CreateRestaurantServicePort createRestaurantServicePort = Mockito.mock(CreateRestaurantServicePort.class);
    private final CreateDishServicePort createDishServicePort = Mockito.mock(CreateDishServicePort.class);
    private final UpdateDishServicePort updateDishServicePort = Mockito.mock(UpdateDishServicePort.class);
    private final RestaurantHandler restaurantHandler = new RestaurantHandler(
            createRestaurantServicePort,
            createDishServicePort,
            updateDishServicePort
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
}
