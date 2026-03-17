package com.plazoleta.restaurant.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.plazoleta.common.exception.BadRequestException;
import com.plazoleta.common.exception.ForbiddenException;
import com.plazoleta.common.exception.ResourceNotFoundException;
import com.plazoleta.restaurant.application.command.CreateDishCommand;
import com.plazoleta.restaurant.domain.model.Dish;
import com.plazoleta.restaurant.domain.model.Restaurant;
import com.plazoleta.restaurant.domain.spi.DishPersistencePort;
import com.plazoleta.restaurant.domain.spi.RestaurantPersistencePort;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CreateDishUseCaseTest {

    private final RestaurantPersistencePort restaurantPersistencePort = Mockito.mock(RestaurantPersistencePort.class);
    private final DishPersistencePort dishPersistencePort = Mockito.mock(DishPersistencePort.class);
    private final CreateDishUseCase createDishUseCase = new CreateDishUseCase(restaurantPersistencePort, dishPersistencePort);

    @Test
    @DisplayName("should create dish successfully")
    void shouldCreateDishSuccessfully() {
        CreateDishCommand command = buildCommand(1L, 10L, 20000);
        when(restaurantPersistencePort.findById(1L)).thenReturn(Optional.of(Restaurant.builder().id(1L).ownerId(10L).build()));
        when(dishPersistencePort.save(Mockito.any(Dish.class))).thenReturn(Dish.builder()
                .id(7L)
                .name("Burger")
                .price(20000)
                .description("Beef burger")
                .imageUrl("https://image.test/burger.png")
                .category("FAST_FOOD")
                .active(true)
                .restaurantId(1L)
                .build());

        assertThat(createDishUseCase.createDish(command).active()).isTrue();
    }

    @Test
    @DisplayName("should fail when restaurant does not exist")
    void shouldFailWhenRestaurantDoesNotExist() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createDishUseCase.createDish(buildCommand(1L, 10L, 20000)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("should fail when owner id does not match restaurant owner")
    void shouldFailWhenOwnerIdDoesNotMatchRestaurantOwner() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(Optional.of(Restaurant.builder().id(1L).ownerId(20L).build()));

        assertThatThrownBy(() -> createDishUseCase.createDish(buildCommand(1L, 10L, 20000)))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("should fail when price is invalid")
    void shouldFailWhenPriceIsInvalid() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(Optional.of(Restaurant.builder().id(1L).ownerId(10L).build()));

        assertThatThrownBy(() -> createDishUseCase.createDish(buildCommand(1L, 10L, 0)))
                .isInstanceOf(BadRequestException.class);
    }

    private CreateDishCommand buildCommand(final Long restaurantId, final Long ownerId, final int price) {
        return new CreateDishCommand(
                restaurantId,
                ownerId,
                "Burger",
                price,
                "Beef burger",
                "https://image.test/burger.png",
                "FAST_FOOD"
        );
    }
}
