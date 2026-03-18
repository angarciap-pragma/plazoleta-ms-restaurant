package com.plazoleta.restaurant.infrastructure.drivenadapters.jpa;

import com.plazoleta.restaurant.domain.model.Dish;
import com.plazoleta.restaurant.domain.spi.DishPersistencePort;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.mapper.DishEntityMapper;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.repository.DishRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Implementa la persistencia de platos usando JPA.
 */
@Component
@RequiredArgsConstructor
public class DishJpaAdapter implements DishPersistencePort {

    private final DishRepository dishRepository;
    private final DishEntityMapper dishEntityMapper;

    @Override
    public Optional<Dish> findById(final Long dishId) {
        return dishRepository.findById(dishId).map(dishEntityMapper::toDomain);
    }

    @Override
    public List<Dish> findActiveByRestaurantId(
            final Long restaurantId,
            final String category,
            final int page,
            final int size
    ) {
        if (StringUtils.hasText(category)) {
            return dishRepository.findByRestaurantIdAndCategoryIgnoreCaseAndActiveTrueOrderByNameAsc(
                            restaurantId,
                            category,
                            PageRequest.of(page, size)
                    )
                    .stream()
                    .map(dishEntityMapper::toDomain)
                    .toList();
        }

        return dishRepository.findByRestaurantIdAndActiveTrueOrderByNameAsc(restaurantId, PageRequest.of(page, size))
                .stream()
                .map(dishEntityMapper::toDomain)
                .toList();
    }

    @Override
    public long countActiveByRestaurantId(final Long restaurantId, final String category) {
        if (StringUtils.hasText(category)) {
            return dishRepository.countByRestaurantIdAndCategoryIgnoreCaseAndActiveTrue(restaurantId, category);
        }
        return dishRepository.countByRestaurantIdAndActiveTrue(restaurantId);
    }

    @Override
    public Dish save(final Dish dish) {
        return dishEntityMapper.toDomain(dishRepository.save(dishEntityMapper.toEntity(dish)));
    }
}
