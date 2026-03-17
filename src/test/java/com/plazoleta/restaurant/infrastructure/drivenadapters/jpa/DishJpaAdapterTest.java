package com.plazoleta.restaurant.infrastructure.drivenadapters.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.plazoleta.restaurant.domain.model.Dish;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.entity.DishEntity;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.mapper.DishEntityMapper;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.repository.DishRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
}
