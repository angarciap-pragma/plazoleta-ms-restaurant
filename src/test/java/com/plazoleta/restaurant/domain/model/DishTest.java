package com.plazoleta.restaurant.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.plazoleta.common.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DishTest {

    @Test
    @DisplayName("should create dish with active true by default")
    void shouldCreateDishWithActiveTrueByDefault() {
        Dish dish = Dish.create(
                "Burger",
                20000,
                "Beef burger",
                "https://image.test/burger.png",
                "FAST_FOOD",
                1L
        );

        assertThat(dish.isActive()).isTrue();
        assertThat(dish.getRestaurantId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("should reject non positive price")
    void shouldRejectNonPositivePrice() {
        assertThatThrownBy(() -> Dish.create(
                "Burger",
                0,
                "Beef burger",
                "https://image.test/burger.png",
                "FAST_FOOD",
                1L
        )).isInstanceOf(BadRequestException.class);
    }
}
