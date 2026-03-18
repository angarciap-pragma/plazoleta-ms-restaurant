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

    @Test
    @DisplayName("should update only price and description")
    void shouldUpdateOnlyPriceAndDescription() {
        Dish updatedDish = Dish.builder()
                .id(1L)
                .name("Burger")
                .price(20000)
                .description("Original")
                .imageUrl("https://image.test/burger.png")
                .category("FAST_FOOD")
                .active(true)
                .restaurantId(1L)
                .build()
                .updatePriceAndDescription(25000, "Updated");

        assertThat(updatedDish.getPrice()).isEqualTo(25000);
        assertThat(updatedDish.getDescription()).isEqualTo("Updated");
        assertThat(updatedDish.getName()).isEqualTo("Burger");
    }

    @Test
    @DisplayName("should update active status")
    void shouldUpdateActiveStatus() {
        Dish updatedDish = Dish.builder()
                .id(1L)
                .name("Burger")
                .price(20000)
                .description("Original")
                .imageUrl("https://image.test/burger.png")
                .category("FAST_FOOD")
                .active(true)
                .restaurantId(1L)
                .build()
                .updateActiveStatus(false);

        assertThat(updatedDish.isActive()).isFalse();
        assertThat(updatedDish.getName()).isEqualTo("Burger");
    }
}
