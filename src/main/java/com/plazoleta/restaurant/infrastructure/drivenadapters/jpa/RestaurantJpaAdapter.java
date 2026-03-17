package com.plazoleta.restaurant.infrastructure.drivenadapters.jpa;

import com.plazoleta.restaurant.domain.model.Restaurant;
import com.plazoleta.restaurant.domain.spi.RestaurantPersistencePort;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.mapper.RestaurantEntityMapper;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementa la persistencia de restaurantes usando JPA.
 */
@Component
@RequiredArgsConstructor
public class RestaurantJpaAdapter implements RestaurantPersistencePort {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;

    @Override
    public boolean existsByNit(final String nit) {
        return restaurantRepository.existsByNit(nit);
    }

    @Override
    public Restaurant save(final Restaurant restaurant) {
        return restaurantEntityMapper.toDomain(
                restaurantRepository.save(restaurantEntityMapper.toEntity(restaurant))
        );
    }
}
