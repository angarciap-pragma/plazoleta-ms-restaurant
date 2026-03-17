package com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.mapper;

import com.plazoleta.restaurant.domain.model.Restaurant;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.entity.RestaurantEntity;
import org.mapstruct.Mapper;

/**
 * Mapea entre el agregado de dominio y la entidad JPA.
 */
@Mapper(componentModel = "spring")
public interface RestaurantEntityMapper {

    default RestaurantEntity toEntity(final Restaurant restaurant) {
        return RestaurantEntity.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .nit(restaurant.getNit())
                .address(restaurant.getAddress())
                .phoneNumber(restaurant.getPhoneNumber())
                .logoUrl(restaurant.getLogoUrl())
                .ownerId(restaurant.getOwnerId())
                .build();
    }

    default Restaurant toDomain(final RestaurantEntity entity) {
        return Restaurant.builder()
                .id(entity.getId())
                .name(entity.getName())
                .nit(entity.getNit())
                .address(entity.getAddress())
                .phoneNumber(entity.getPhoneNumber())
                .logoUrl(entity.getLogoUrl())
                .ownerId(entity.getOwnerId())
                .build();
    }
}
