package com.plazoleta.restaurant.infrastructure.service.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.plazoleta.restaurant.application.command.CreateRestaurantCommand;
import com.plazoleta.restaurant.application.response.RestaurantCreatedResponse;
import com.plazoleta.restaurant.domain.api.CreateRestaurantServicePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RestaurantHandlerTest {

    private final CreateRestaurantServicePort createRestaurantServicePort = Mockito.mock(CreateRestaurantServicePort.class);
    private final RestaurantHandler restaurantHandler = new RestaurantHandler(createRestaurantServicePort);

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
}
