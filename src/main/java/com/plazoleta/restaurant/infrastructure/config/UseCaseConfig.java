package com.plazoleta.restaurant.infrastructure.config;

import com.plazoleta.common.exception.GlobalExceptionHandler;
import com.plazoleta.common.logging.TraceIdFilter;
import com.plazoleta.restaurant.application.usecase.CreateDishUseCase;
import com.plazoleta.restaurant.application.usecase.CreateRestaurantUseCase;
import com.plazoleta.restaurant.application.usecase.GetRestaurantByIdUseCase;
import com.plazoleta.restaurant.application.usecase.ListRestaurantDishesUseCase;
import com.plazoleta.restaurant.application.usecase.ListRestaurantsUseCase;
import com.plazoleta.restaurant.application.usecase.UpdateDishStatusUseCase;
import com.plazoleta.restaurant.application.usecase.UpdateDishUseCase;
import com.plazoleta.restaurant.domain.api.CreateDishServicePort;
import com.plazoleta.restaurant.domain.api.CreateRestaurantServicePort;
import com.plazoleta.restaurant.domain.api.GetRestaurantByIdServicePort;
import com.plazoleta.restaurant.domain.api.ListRestaurantDishesServicePort;
import com.plazoleta.restaurant.domain.api.ListRestaurantsServicePort;
import com.plazoleta.restaurant.domain.api.UpdateDishStatusServicePort;
import com.plazoleta.restaurant.domain.api.UpdateDishServicePort;
import com.plazoleta.restaurant.domain.spi.DishPersistencePort;
import com.plazoleta.restaurant.domain.spi.OwnerUserQueryPort;
import com.plazoleta.restaurant.domain.spi.RestaurantPersistencePort;
import com.plazoleta.restaurant.infrastructure.drivenadapters.rest.properties.UserServiceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestClient;

/**
 * Registra los beans de aplicación del microservicio de restaurantes.
 */
@Configuration
@Import(GlobalExceptionHandler.class)
@EnableConfigurationProperties(UserServiceProperties.class)
public class UseCaseConfig {

    @Bean
    public CreateRestaurantServicePort createRestaurantServicePort(
            final RestaurantPersistencePort restaurantPersistencePort,
            final OwnerUserQueryPort ownerUserQueryPort
    ) {
        return new CreateRestaurantUseCase(restaurantPersistencePort, ownerUserQueryPort);
    }

    @Bean
    public CreateDishServicePort createDishServicePort(
            final RestaurantPersistencePort restaurantPersistencePort,
            final DishPersistencePort dishPersistencePort
    ) {
        return new CreateDishUseCase(restaurantPersistencePort, dishPersistencePort);
    }

    @Bean
    public UpdateDishServicePort updateDishServicePort(
            final DishPersistencePort dishPersistencePort,
            final RestaurantPersistencePort restaurantPersistencePort
    ) {
        return new UpdateDishUseCase(dishPersistencePort, restaurantPersistencePort);
    }

    @Bean
    public GetRestaurantByIdServicePort getRestaurantByIdServicePort(
            final RestaurantPersistencePort restaurantPersistencePort
    ) {
        return new GetRestaurantByIdUseCase(restaurantPersistencePort);
    }

    @Bean
    public UpdateDishStatusServicePort updateDishStatusServicePort(
            final DishPersistencePort dishPersistencePort,
            final RestaurantPersistencePort restaurantPersistencePort
    ) {
        return new UpdateDishStatusUseCase(dishPersistencePort, restaurantPersistencePort);
    }

    @Bean
    public ListRestaurantsServicePort listRestaurantsServicePort(
            final RestaurantPersistencePort restaurantPersistencePort
    ) {
        return new ListRestaurantsUseCase(restaurantPersistencePort);
    }

    @Bean
    public ListRestaurantDishesServicePort listRestaurantDishesServicePort(
            final RestaurantPersistencePort restaurantPersistencePort,
            final DishPersistencePort dishPersistencePort
    ) {
        return new ListRestaurantDishesUseCase(restaurantPersistencePort, dishPersistencePort);
    }

    @Bean
    public TraceIdFilter traceIdFilter() {
        return new TraceIdFilter();
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }
}
