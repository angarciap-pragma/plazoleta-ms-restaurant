package com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.mapper;

import com.plazoleta.restaurant.domain.model.Dish;
import com.plazoleta.restaurant.infrastructure.drivenadapters.jpa.entity.DishEntity;
import org.mapstruct.Mapper;

/**
 * Mapea entre el agregado de dominio y la entidad JPA del plato.
 */
@Mapper(componentModel = "spring")
public interface DishEntityMapper {

    default DishEntity toEntity(final Dish dish) {
        return DishEntity.builder()
                .id(dish.getId())
                .name(dish.getName())
                .price(dish.getPrice())
                .description(dish.getDescription())
                .imageUrl(dish.getImageUrl())
                .category(dish.getCategory())
                .active(dish.isActive())
                .restaurantId(dish.getRestaurantId())
                .build();
    }

    default Dish toDomain(final DishEntity entity) {
        return Dish.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .imageUrl(entity.getImageUrl())
                .category(entity.getCategory())
                .active(entity.isActive())
                .restaurantId(entity.getRestaurantId())
                .build();
    }
}
