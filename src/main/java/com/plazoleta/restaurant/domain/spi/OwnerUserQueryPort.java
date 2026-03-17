package com.plazoleta.restaurant.domain.spi;

import com.plazoleta.restaurant.domain.model.OwnerUser;

/**
 * Define la consulta remota de propietarios en el microservicio de usuarios.
 */
public interface OwnerUserQueryPort {

    OwnerUser getOwnerById(Long ownerId);
}
