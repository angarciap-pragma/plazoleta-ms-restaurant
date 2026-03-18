package com.plazoleta.restaurant.infrastructure.drivenadapters.rest;

import com.plazoleta.common.exception.ResourceNotFoundException;
import com.plazoleta.restaurant.domain.model.OwnerUser;
import com.plazoleta.restaurant.domain.spi.OwnerUserQueryPort;
import com.plazoleta.restaurant.infrastructure.drivenadapters.rest.dto.UserDetailsClientResponseDto;
import com.plazoleta.restaurant.infrastructure.drivenadapters.rest.properties.UserServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

/**
 * Consulta usuarios propietarios en ms-user mediante REST.
 */
@Component
@RequiredArgsConstructor
public class OwnerUserRestClientAdapter implements OwnerUserQueryPort {

    private final RestClient restClient;
    private final UserServiceProperties userServiceProperties;

    @Override
    public OwnerUser getOwnerById(final Long ownerId) {
        try {
            UserDetailsClientResponseDto response = restClient.get()
                    .uri(userServiceProperties.getBaseUrl() + "/users/internal/{id}", ownerId)
                    .retrieve()
                    .body(UserDetailsClientResponseDto.class);

            if (response == null) {
                throw new ResourceNotFoundException(RestaurantRemoteErrorCode.OWNER_NOT_FOUND);
            }

            return OwnerUser.builder()
                    .id(response.id())
                    .role(response.role())
                    .build();
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException(RestaurantRemoteErrorCode.OWNER_NOT_FOUND);
            }
            throw exception;
        }
    }
}
