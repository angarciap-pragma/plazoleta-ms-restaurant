package com.plazoleta.restaurant.infrastructure.entrypoints.rest.mapper;

import com.plazoleta.restaurant.application.command.CreateDishCommand;
import com.plazoleta.restaurant.application.response.DishCreatedResponse;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.request.CreateDishRequestDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response.DishCreatedResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapea contratos HTTP de platos hacia comandos y respuestas de aplicación.
 */
@Mapper(componentModel = "spring")
public interface DishRestMapper {

    @Mapping(target = "restaurantId", source = "restaurantId")
    @Mapping(target = "ownerId", source = "ownerId")
    CreateDishCommand toCommand(Long restaurantId, Long ownerId, CreateDishRequestDto requestDto);

    DishCreatedResponseDto toDto(DishCreatedResponse response);
}
