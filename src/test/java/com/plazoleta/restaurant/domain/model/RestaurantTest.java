package com.plazoleta.restaurant.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.plazoleta.common.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantTest {

    @Test
    @DisplayName("should create restaurant successfully")
    void shouldCreateRestaurantSuccessfully() {
        Restaurant restaurant = Restaurant.create(
                "Food 123",
                "123456",
                "Main street 1",
                "+573005698325",
                "https://logo.test",
                1L
        );

        assertThat(restaurant.getName()).isEqualTo("Food 123");
    }

    @Test
    @DisplayName("should reject numeric only restaurant name")
    void shouldRejectNumericOnlyRestaurantName() {
        assertThatThrownBy(() -> Restaurant.create(
                "123456",
                "123456",
                "Main street 1",
                "+573005698325",
                "https://logo.test",
                1L
        )).isInstanceOf(BadRequestException.class);
    }
}
