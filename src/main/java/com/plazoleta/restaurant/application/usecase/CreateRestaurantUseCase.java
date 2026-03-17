package com.plazoleta.restaurant.application.usecase;

import com.plazoleta.common.exception.BadRequestException;
import com.plazoleta.common.exception.ConflictException;
import com.plazoleta.restaurant.application.command.CreateRestaurantCommand;
import com.plazoleta.restaurant.application.response.RestaurantCreatedResponse;
import com.plazoleta.restaurant.domain.api.CreateRestaurantServicePort;
import com.plazoleta.restaurant.domain.exception.RestaurantErrorCode;
import com.plazoleta.restaurant.domain.model.OwnerUser;
import com.plazoleta.restaurant.domain.model.Restaurant;
import com.plazoleta.restaurant.domain.spi.OwnerUserQueryPort;
import com.plazoleta.restaurant.domain.spi.RestaurantPersistencePort;

/**
 * Implementa el caso de uso para crear restaurantes.
 */
public class CreateRestaurantUseCase implements CreateRestaurantServicePort {

    private static final String OWNER_ROLE = "OWNER";

    private final RestaurantPersistencePort restaurantPersistencePort;
    private final OwnerUserQueryPort ownerUserQueryPort;

    public CreateRestaurantUseCase(
            final RestaurantPersistencePort restaurantPersistencePort,
            final OwnerUserQueryPort ownerUserQueryPort
    ) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.ownerUserQueryPort = ownerUserQueryPort;
    }

    @Override
    public RestaurantCreatedResponse createRestaurant(final CreateRestaurantCommand command) {
        if (restaurantPersistencePort.existsByNit(command.nit())) {
            throw new ConflictException(RestaurantErrorCode.NIT_ALREADY_EXISTS);
        }

        OwnerUser ownerUser = ownerUserQueryPort.getOwnerById(command.ownerId());
        if (!OWNER_ROLE.equals(ownerUser.getRole())) {
            throw new BadRequestException(RestaurantErrorCode.INVALID_OWNER_ROLE);
        }

        Restaurant savedRestaurant = restaurantPersistencePort.save(Restaurant.create(
                command.name(),
                command.nit(),
                command.address(),
                command.phoneNumber(),
                command.logoUrl(),
                command.ownerId()
        ));

        return RestaurantCreatedResponse.builder()
                .id(savedRestaurant.getId())
                .name(savedRestaurant.getName())
                .nit(savedRestaurant.getNit())
                .address(savedRestaurant.getAddress())
                .phoneNumber(savedRestaurant.getPhoneNumber())
                .logoUrl(savedRestaurant.getLogoUrl())
                .ownerId(savedRestaurant.getOwnerId())
                .build();
    }
}
