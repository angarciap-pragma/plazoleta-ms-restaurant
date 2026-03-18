package com.plazoleta.restaurant.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.plazoleta.common.exception.ResourceNotFoundException;
import com.plazoleta.restaurant.domain.model.Restaurant;
import com.plazoleta.restaurant.domain.spi.RestaurantPersistencePort;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GetRestaurantByIdUseCaseTest {

    private final RestaurantPersistencePort restaurantPersistencePort = Mockito.mock(RestaurantPersistencePort.class);
    private final GetRestaurantByIdUseCase useCase = new GetRestaurantByIdUseCase(restaurantPersistencePort);

    @Test
    @DisplayName("should get restaurant ownership response")
    void shouldGetRestaurantOwnershipResponse() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(Optional.of(
                Restaurant.builder().id(1L).ownerId(2L).build()
        ));

        assertThat(useCase.getRestaurantById(1L).ownerId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("should reject missing restaurant on internal lookup")
    void shouldRejectMissingRestaurantOnInternalLookup() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.getRestaurantById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
