package com.plazoleta.restaurant.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.plazoleta.common.exception.BadRequestException;
import com.plazoleta.common.exception.ConflictException;
import com.plazoleta.restaurant.application.command.CreateRestaurantCommand;
import com.plazoleta.restaurant.domain.model.OwnerUser;
import com.plazoleta.restaurant.domain.model.Restaurant;
import com.plazoleta.restaurant.domain.spi.OwnerUserQueryPort;
import com.plazoleta.restaurant.domain.spi.RestaurantPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CreateRestaurantUseCaseTest {

    private final RestaurantPersistencePort restaurantPersistencePort = Mockito.mock(RestaurantPersistencePort.class);
    private final OwnerUserQueryPort ownerUserQueryPort = Mockito.mock(OwnerUserQueryPort.class);
    private final CreateRestaurantUseCase createRestaurantUseCase =
            new CreateRestaurantUseCase(restaurantPersistencePort, ownerUserQueryPort);

    @Test
    @DisplayName("should create restaurant successfully")
    void shouldCreateRestaurantSuccessfully() {
        CreateRestaurantCommand command = buildCommand("Food Place", "123456", 1L);
        when(restaurantPersistencePort.existsByNit("123456")).thenReturn(false);
        when(ownerUserQueryPort.getOwnerById(1L)).thenReturn(OwnerUser.builder().id(1L).role("OWNER").build());
        when(restaurantPersistencePort.save(Mockito.any(Restaurant.class))).thenReturn(Restaurant.builder()
                .id(1L)
                .name("Food Place")
                .nit("123456")
                .address("Main street 1")
                .phoneNumber("+573005698325")
                .logoUrl("https://logo.test")
                .ownerId(1L)
                .build());

        assertThat(createRestaurantUseCase.createRestaurant(command).id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("should reject duplicated nit")
    void shouldRejectDuplicatedNit() {
        when(restaurantPersistencePort.existsByNit("123456")).thenReturn(true);

        assertThatThrownBy(() -> createRestaurantUseCase.createRestaurant(buildCommand("Food Place", "123456", 1L)))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("should reject invalid owner role")
    void shouldRejectInvalidOwnerRole() {
        when(restaurantPersistencePort.existsByNit("123456")).thenReturn(false);
        when(ownerUserQueryPort.getOwnerById(1L)).thenReturn(OwnerUser.builder().id(1L).role("CUSTOMER").build());

        assertThatThrownBy(() -> createRestaurantUseCase.createRestaurant(buildCommand("Food Place", "123456", 1L)))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("should reject numeric only name from domain rule")
    void shouldRejectNumericOnlyNameFromDomainRule() {
        when(restaurantPersistencePort.existsByNit("123456")).thenReturn(false);
        when(ownerUserQueryPort.getOwnerById(1L)).thenReturn(OwnerUser.builder().id(1L).role("OWNER").build());

        assertThatThrownBy(() -> createRestaurantUseCase.createRestaurant(buildCommand("123456", "123456", 1L)))
                .isInstanceOf(BadRequestException.class);
    }

    private CreateRestaurantCommand buildCommand(final String name, final String nit, final Long ownerId) {
        return new CreateRestaurantCommand(
                name,
                nit,
                "Main street 1",
                "+573005698325",
                "https://logo.test",
                ownerId
        );
    }
}
