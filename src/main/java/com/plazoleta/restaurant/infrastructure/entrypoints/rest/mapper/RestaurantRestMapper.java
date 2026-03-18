package com.plazoleta.restaurant.infrastructure.entrypoints.rest.mapper;

import com.plazoleta.restaurant.application.command.CreateRestaurantCommand;
import com.plazoleta.restaurant.application.response.RestaurantOwnershipResponse;
import com.plazoleta.restaurant.application.response.RestaurantCreatedResponse;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response.RestaurantOwnershipResponseDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.request.CreateRestaurantRequestDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response.RestaurantCreatedResponseDto;
import org.mapstruct.Mapper;

/**
 * Mapea contratos HTTP hacia comandos y respuestas de aplicación.
 */
@Mapper(componentModel = "spring")
public interface RestaurantRestMapper {

    CreateRestaurantCommand toCommand(CreateRestaurantRequestDto requestDto);

    RestaurantCreatedResponseDto toDto(RestaurantCreatedResponse response);

    RestaurantOwnershipResponseDto toDto(RestaurantOwnershipResponse response);
}
