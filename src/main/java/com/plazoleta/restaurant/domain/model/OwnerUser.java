package com.plazoleta.restaurant.domain.model;

import lombok.Builder;
import lombok.Getter;

/**
 * Representa la vista mínima del propietario consultada en ms-user.
 */
@Getter
@Builder
public class OwnerUser {

    private final Long id;
    private final String role;
}
