package com.plazoleta.restaurant.infrastructure.drivenadapters.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.plazoleta.restaurant.domain.model.Dish;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.entity.DishEntity;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.mapper.DishEntityMapper;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.repository.DishRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;

class DishJpaAdapterTest {

    private final DishRepository dishRepository = Mockito.mock(DishRepository.class);
    private final DishEntityMapper dishEntityMapper = Mockito.mock(DishEntityMapper.class);
    private final DishJpaAdapter dishJpaAdapter = new DishJpaAdapter(dishRepository, dishEntityMapper);

    @Test
    @DisplayName("should save mapped dish")
    void shouldSaveMappedDish() {
        Dish dish = Dish.builder().name("Burger").price(20000).build();
        DishEntity entity = DishEntity.builder().name("Burger").price(20000).build();
        Dish savedDish = Dish.builder().id(5L).name("Burger").price(20000).active(true).build();

        when(dishEntityMapper.toEntity(dish)).thenReturn(entity);
        when(dishRepository.save(entity)).thenReturn(entity);
        when(dishEntityMapper.toDomain(entity)).thenReturn(savedDish);

        assertThat(dishJpaAdapter.save(dish).getId()).isEqualTo(5L);
    }

    @Test
    @DisplayName("should find mapped dish by id")
    void shouldFindMappedDishById() {
        DishEntity entity = DishEntity.builder().id(9L).name("Burger").build();
        Dish dish = Dish.builder().id(9L).name("Burger").price(20000).build();

        when(dishRepository.findById(9L)).thenReturn(Optional.of(entity));
        when(dishEntityMapper.toDomain(entity)).thenReturn(dish);

        assertThat(dishJpaAdapter.findById(9L)).isPresent();
    }

    @Test
    @DisplayName("should list active dishes without category filter")
    void shouldListActiveDishesWithoutCategoryFilter() {
        DishEntity entity = DishEntity.builder().id(9L).name("Burger").build();
        Dish dish = Dish.builder().id(9L).name("Burger").price(20000).active(true).restaurantId(1L).build();

        when(dishRepository.findByRestaurantIdAndActiveTrueOrderByNameAsc(1L, PageRequest.of(0, 10)))
                .thenReturn(List.of(entity));
        when(dishEntityMapper.toDomain(entity)).thenReturn(dish);

        assertThat(dishJpaAdapter.findActiveByRestaurantId(1L, null, 0, 10)).containsExactly(dish);
    }

    @Test
    @DisplayName("should list active dishes with category filter")
    void shouldListActiveDishesWithCategoryFilter() {
        DishEntity entity = DishEntity.builder().id(10L).name("Salad").category("HEALTHY").build();
        Dish dish = Dish.builder().id(10L).name("Salad").category("HEALTHY").price(15000).active(true).restaurantId(1L).build();

        when(dishRepository.findByRestaurantIdAndCategoryIgnoreCaseAndActiveTrueOrderByNameAsc(
                1L,
                "HEALTHY",
                PageRequest.of(0, 10)
        )).thenReturn(List.of(entity));
        when(dishEntityMapper.toDomain(entity)).thenReturn(dish);

        assertThat(dishJpaAdapter.findActiveByRestaurantId(1L, "HEALTHY", 0, 10)).containsExactly(dish);
    }

    @Test
    @DisplayName("should count active dishes without category filter")
    void shouldCountActiveDishesWithoutCategoryFilter() {
        when(dishRepository.countByRestaurantIdAndActiveTrue(1L)).thenReturn(2L);

        assertThat(dishJpaAdapter.countActiveByRestaurantId(1L, null)).isEqualTo(2L);
    }

    @Test
    @DisplayName("should count active dishes with category filter")
    void shouldCountActiveDishesWithCategoryFilter() {
        when(dishRepository.countByRestaurantIdAndCategoryIgnoreCaseAndActiveTrue(1L, "HEALTHY")).thenReturn(1L);

        assertThat(dishJpaAdapter.countActiveByRestaurantId(1L, "HEALTHY")).isEqualTo(1L);
    }
}
