package com.plazoleta.restaurant.infrastructure.entrypoints.rest.mapper;

import com.plazoleta.restaurant.application.response.DishSummaryResponse;
import com.plazoleta.restaurant.application.response.PagedResponse;
import com.plazoleta.restaurant.application.response.RestaurantSummaryResponse;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response.DishSummaryResponseDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response.PagedResponseDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response.RestaurantSummaryResponseDto;
import org.mapstruct.Mapper;

/**
 * Mapea respuestas de catálogo hacia contratos HTTP.
 */
@Mapper(componentModel = "spring")
public interface CatalogRestMapper {

    RestaurantSummaryResponseDto toDto(RestaurantSummaryResponse response);

    DishSummaryResponseDto toDto(DishSummaryResponse response);

    default PagedResponseDto<RestaurantSummaryResponseDto> toRestaurantPageDto(
            final PagedResponse<RestaurantSummaryResponse> response
    ) {
        return new PagedResponseDto<>(
                response.content().stream().map(this::toDto).toList(),
                response.page(),
                response.size(),
                response.totalElements(),
                response.totalPages()
        );
    }

    default PagedResponseDto<DishSummaryResponseDto> toDishPageDto(
            final PagedResponse<DishSummaryResponse> response
    ) {
        return new PagedResponseDto<>(
                response.content().stream().map(this::toDto).toList(),
                response.page(),
                response.size(),
                response.totalElements(),
                response.totalPages()
        );
    }
}
