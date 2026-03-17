package com.plazoleta.restaurant.domain.model;

import com.plazoleta.common.exception.BadRequestException;
import com.plazoleta.restaurant.domain.exception.RestaurantErrorCode;
import lombok.Builder;
import lombok.Getter;

/**
 * Representa el agregado de plato del microservicio.
 */
@Getter
@Builder
public class Dish {

    private final Long id;
    private final String name;
    private final int price;
    private final String description;
    private final String imageUrl;
    private final String category;
    private final boolean active;
    private final Long restaurantId;

    public static Dish create(
            final String name,
            final int price,
            final String description,
            final String imageUrl,
            final String category,
            final Long restaurantId
    ) {
        if (price <= 0) {
            throw new BadRequestException(RestaurantErrorCode.INVALID_DISH_PRICE);
        }

        return Dish.builder()
                .name(name)
                .price(price)
                .description(description)
                .imageUrl(imageUrl)
                .category(category)
                .active(true)
                .restaurantId(restaurantId)
                .build();
    }
}
