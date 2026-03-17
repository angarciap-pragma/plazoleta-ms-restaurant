package com.plazoleta.restaurant.infrastructure.drivenadapters.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.plazoleta.restaurant.domain.model.Restaurant;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.entity.RestaurantEntity;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.mapper.RestaurantEntityMapper;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.repository.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RestaurantJpaAdapterTest {

    private final RestaurantRepository restaurantRepository = Mockito.mock(RestaurantRepository.class);
    private final RestaurantEntityMapper restaurantEntityMapper = Mockito.mock(RestaurantEntityMapper.class);
    private final RestaurantJpaAdapter restaurantJpaAdapter =
            new RestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper);

    @Test
    @DisplayName("should delegate nit existence check")
    void shouldDelegateNitExistenceCheck() {
        when(restaurantRepository.existsByNit("123456")).thenReturn(true);

        assertThat(restaurantJpaAdapter.existsByNit("123456")).isTrue();
    }

    @Test
    @DisplayName("should save mapped restaurant")
    void shouldSaveMappedRestaurant() {
        Restaurant restaurant = Restaurant.builder().name("Food Place").nit("123456").build();
        RestaurantEntity entity = RestaurantEntity.builder().name("Food Place").nit("123456").build();
        Restaurant savedRestaurant = Restaurant.builder().id(1L).name("Food Place").nit("123456").build();

        when(restaurantEntityMapper.toEntity(restaurant)).thenReturn(entity);
        when(restaurantRepository.save(entity)).thenReturn(entity);
        when(restaurantEntityMapper.toDomain(entity)).thenReturn(savedRestaurant);

        assertThat(restaurantJpaAdapter.save(restaurant).getId()).isEqualTo(1L);
    }
}
