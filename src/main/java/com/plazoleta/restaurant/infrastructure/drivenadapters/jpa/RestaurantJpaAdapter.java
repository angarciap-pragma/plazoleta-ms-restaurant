package com.plazoleta.restaurant.infrastructure.drivenadapters.jpa;

import com.plazoleta.restaurant.domain.model.Restaurant;
import com.plazoleta.restaurant.domain.spi.RestaurantPersistencePort;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.mapper.RestaurantEntityMapper;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.repository.RestaurantRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    public Optional<Restaurant> findById(final Long restaurantId) {
        return restaurantRepository.findById(restaurantId).map(restaurantEntityMapper::toDomain);
    }

    @Override
    public List<Restaurant> findAllOrderedByName(final int page, final int size) {
        return restaurantRepository.findAllByOrderByNameAsc(PageRequest.of(page, size))
                .stream()
                .map(restaurantEntityMapper::toDomain)
                .toList();
    }

    @Override
    public long count() {
        return restaurantRepository.count();
    }

    @Override
    public Restaurant save(final Restaurant restaurant) {
        return restaurantEntityMapper.toDomain(
                restaurantRepository.save(restaurantEntityMapper.toEntity(restaurant))
        );
    }
}
