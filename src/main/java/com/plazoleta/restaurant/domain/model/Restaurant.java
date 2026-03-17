package com.plazoleta.restaurant.domain.model;

import com.plazoleta.common.exception.BadRequestException;
import com.plazoleta.restaurant.domain.exception.RestaurantErrorCode;
import lombok.Builder;
import lombok.Getter;

/**
 * Representa el agregado de restaurante del microservicio.
 */
@Getter
@Builder
public class Restaurant {

    private final Long id;
    private final String name;
    private final String nit;
    private final String address;
    private final String phoneNumber;
    private final String logoUrl;
    private final Long ownerId;

    public static Restaurant create(
            final String name,
            final String nit,
            final String address,
            final String phoneNumber,
            final String logoUrl,
            final Long ownerId
    ) {
        validateName(name);
        return Restaurant.builder()
                .name(name)
                .nit(nit)
                .address(address)
                .phoneNumber(phoneNumber)
                .logoUrl(logoUrl)
                .ownerId(ownerId)
                .build();
    }

    private static void validateName(final String name) {
        if (name.matches("^\\d+$")) {
            throw new BadRequestException(RestaurantErrorCode.INVALID_RESTAURANT_NAME);
        }
    }
}
