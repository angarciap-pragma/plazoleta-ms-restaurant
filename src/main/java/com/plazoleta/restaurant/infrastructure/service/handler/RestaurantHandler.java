package com.plazoleta.restaurant.infrastructure.service.handler;

import com.plazoleta.restaurant.application.command.CreateRestaurantCommand;
import com.plazoleta.restaurant.application.response.RestaurantCreatedResponse;
import com.plazoleta.restaurant.domain.api.CreateRestaurantServicePort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Orquesta las solicitudes HTTP del módulo de restaurantes.
 */
@Component
@RequiredArgsConstructor
public class RestaurantHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantHandler.class);

    private final CreateRestaurantServicePort createRestaurantServicePort;

    public RestaurantCreatedResponse createRestaurant(final CreateRestaurantCommand command) {
        LOGGER.info("Creating restaurant with nit {}", command.nit());
        return createRestaurantServicePort.createRestaurant(command);
    }
}
