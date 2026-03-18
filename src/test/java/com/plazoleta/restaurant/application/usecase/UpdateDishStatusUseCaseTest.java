package com.plazoleta.restaurant.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.plazoleta.common.exception.ForbiddenException;
import com.plazoleta.common.exception.ResourceNotFoundException;
import com.plazoleta.restaurant.application.command.UpdateDishStatusCommand;
import com.plazoleta.restaurant.domain.model.Dish;
import com.plazoleta.restaurant.domain.model.Restaurant;
import com.plazoleta.restaurant.domain.spi.DishPersistencePort;
import com.plazoleta.restaurant.domain.spi.RestaurantPersistencePort;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UpdateDishStatusUseCaseTest {

    private final DishPersistencePort dishPersistencePort = Mockito.mock(DishPersistencePort.class);
    private final RestaurantPersistencePort restaurantPersistencePort = Mockito.mock(RestaurantPersistencePort.class);
    private final UpdateDishStatusUseCase useCase = new UpdateDishStatusUseCase(dishPersistencePort, restaurantPersistencePort);

    @Test
    @DisplayName("should update dish active status successfully")
    void shouldUpdateDishActiveStatusSuccessfully() {
        Dish existingDish = Dish.builder()
                .id(1L)
                .name("Burger")
                .price(20000)
                .description("Burger")
                .imageUrl("https://image.test/burger.png")
                .category("FAST_FOOD")
                .active(true)
                .restaurantId(1L)
                .build();
        Dish updatedDish = existingDish.updateActiveStatus(false);

        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(existingDish));
        when(restaurantPersistencePort.findById(1L)).thenReturn(Optional.of(Restaurant.builder().id(1L).ownerId(2L).build()));
        when(dishPersistencePort.save(Mockito.any(Dish.class))).thenReturn(updatedDish);

        var response = useCase.updateDishStatus(new UpdateDishStatusCommand(1L, 2L, false));

        assertThat(response.active()).isFalse();
    }

    @Test
    @DisplayName("should reject missing dish on status update")
    void shouldRejectMissingDishOnStatusUpdate() {
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.updateDishStatus(new UpdateDishStatusCommand(1L, 2L, false)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("should reject missing restaurant on status update")
    void shouldRejectMissingRestaurantOnStatusUpdate() {
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(Dish.builder().id(1L).restaurantId(1L).build()));
        when(restaurantPersistencePort.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.updateDishStatus(new UpdateDishStatusCommand(1L, 2L, false)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("should reject status update when owner does not match")
    void shouldRejectStatusUpdateWhenOwnerDoesNotMatch() {
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(Dish.builder().id(1L).restaurantId(1L).build()));
        when(restaurantPersistencePort.findById(1L)).thenReturn(Optional.of(Restaurant.builder().id(1L).ownerId(3L).build()));

        assertThatThrownBy(() -> useCase.updateDishStatus(new UpdateDishStatusCommand(1L, 2L, false)))
                .isInstanceOf(ForbiddenException.class);
    }
}
