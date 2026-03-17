package com.plazoleta.restaurant.infrastructure.entrypoints.rest.mapper;

import com.plazoleta.restaurant.application.command.UpdateDishCommand;
import com.plazoleta.restaurant.application.response.DishUpdatedResponse;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.request.UpdateDishRequestDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response.DishUpdatedResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapea contratos HTTP de modificación de platos hacia comandos y respuestas.
 */
@Mapper(componentModel = "spring")
public interface UpdateDishRestMapper {

    @Mapping(target = "dishId", source = "dishId")
    UpdateDishCommand toCommand(Long dishId, UpdateDishRequestDto requestDto);

    DishUpdatedResponseDto toDto(DishUpdatedResponse response);
}
