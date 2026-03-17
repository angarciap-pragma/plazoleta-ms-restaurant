package com.plazoleta.restaurant.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.plazoleta.common.exception.BadRequestException;
import com.plazoleta.common.exception.ForbiddenException;
import com.plazoleta.common.exception.ResourceNotFoundException;
import com.plazoleta.restaurant.application.command.UpdateDishCommand;
import com.plazoleta.restaurant.domain.model.Dish;
import com.plazoleta.restaurant.domain.model.Restaurant;
import com.plazoleta.restaurant.domain.spi.DishPersistencePort;
import com.plazoleta.restaurant.domain.spi.RestaurantPersistencePort;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UpdateDishUseCaseTest {

    private final DishPersistencePort dishPersistencePort = Mockito.mock(DishPersistencePort.class);
    private final RestaurantPersistencePort restaurantPersistencePort = Mockito.mock(RestaurantPersistencePort.class);
    private final UpdateDishUseCase updateDishUseCase = new UpdateDishUseCase(dishPersistencePort, restaurantPersistencePort);

    @Test
    @DisplayName("should update dish successfully")
    void shouldUpdateDishSuccessfully() {
        UpdateDishCommand command = new UpdateDishCommand(3L, 10L, 25000, "Updated description");
        Dish existingDish = Dish.builder()
                .id(3L)
                .name("Burger")
                .price(20000)
                .description("Original")
                .imageUrl("https://image.test/burger.png")
                .category("FAST_FOOD")
                .active(true)
                .restaurantId(1L)
                .build();

        when(dishPersistencePort.findById(3L)).thenReturn(Optional.of(existingDish));
        when(restaurantPersistencePort.findById(1L)).thenReturn(Optional.of(Restaurant.builder().id(1L).ownerId(10L).build()));
        when(dishPersistencePort.save(Mockito.any(Dish.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertThat(updateDishUseCase.updateDish(command).price()).isEqualTo(25000);
    }

    @Test
    @DisplayName("should fail when dish does not exist")
    void shouldFailWhenDishDoesNotExist() {
        when(dishPersistencePort.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateDishUseCase.updateDish(new UpdateDishCommand(3L, 10L, 25000, "Updated")))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("should fail when owner does not match")
    void shouldFailWhenOwnerDoesNotMatch() {
        Dish existingDish = Dish.builder()
                .id(3L)
                .name("Burger")
                .price(20000)
                .description("Original")
                .imageUrl("https://image.test/burger.png")
                .category("FAST_FOOD")
                .active(true)
                .restaurantId(1L)
                .build();

        when(dishPersistencePort.findById(3L)).thenReturn(Optional.of(existingDish));
        when(restaurantPersistencePort.findById(1L)).thenReturn(Optional.of(Restaurant.builder().id(1L).ownerId(99L).build()));

        assertThatThrownBy(() -> updateDishUseCase.updateDish(new UpdateDishCommand(3L, 10L, 25000, "Updated")))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("should fail when restaurant linked to dish does not exist")
    void shouldFailWhenRestaurantLinkedToDishDoesNotExist() {
        Dish existingDish = Dish.builder()
                .id(3L)
                .name("Burger")
                .price(20000)
                .description("Original")
                .imageUrl("https://image.test/burger.png")
                .category("FAST_FOOD")
                .active(true)
                .restaurantId(1L)
                .build();

        when(dishPersistencePort.findById(3L)).thenReturn(Optional.of(existingDish));
        when(restaurantPersistencePort.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateDishUseCase.updateDish(new UpdateDishCommand(3L, 10L, 25000, "Updated")))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("should fail when price is invalid")
    void shouldFailWhenPriceIsInvalid() {
        Dish existingDish = Dish.builder()
                .id(3L)
                .name("Burger")
                .price(20000)
                .description("Original")
                .imageUrl("https://image.test/burger.png")
                .category("FAST_FOOD")
                .active(true)
                .restaurantId(1L)
                .build();

        when(dishPersistencePort.findById(3L)).thenReturn(Optional.of(existingDish));
        when(restaurantPersistencePort.findById(1L)).thenReturn(Optional.of(Restaurant.builder().id(1L).ownerId(10L).build()));

        assertThatThrownBy(() -> updateDishUseCase.updateDish(new UpdateDishCommand(3L, 10L, 0, "Updated")))
                .isInstanceOf(BadRequestException.class);
    }
}
