package com.plazoleta.restaurant.infrastructure.entrypoints.rest.controller;

import com.plazoleta.common.security.AuthenticatedUserProvider;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.request.CreateRestaurantRequestDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.request.CreateDishRequestDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response.DishCreatedResponseDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response.RestaurantCreatedResponseDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.mapper.DishRestMapper;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.mapper.RestaurantRestMapper;
import com.plazoleta.restaurant.infrastructure.service.handler.RestaurantHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone los endpoints HTTP del microservicio de restaurantes.
 */
@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurants", description = "Endpoints for restaurant management")
public class RestaurantController {

    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final RestaurantHandler restaurantHandler;
    private final RestaurantRestMapper restaurantRestMapper;
    private final DishRestMapper dishRestMapper;

    @PostMapping
    @Operation(
            summary = "Create restaurant",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Restaurant created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "409", description = "Duplicated NIT")
            }
    )
    public ResponseEntity<RestaurantCreatedResponseDto> createRestaurant(
            @Valid @RequestBody final CreateRestaurantRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restaurantRestMapper.toDto(
                        restaurantHandler.createRestaurant(restaurantRestMapper.toCommand(requestDto))
                ));
    }

    @PostMapping("/{restaurantId}/dishes")
    @Operation(
            summary = "Create dish",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Dish created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "403", description = "Owner mismatch"),
                    @ApiResponse(responseCode = "404", description = "Restaurant not found")
            }
    )
    public ResponseEntity<DishCreatedResponseDto> createDish(
            @PathVariable final Long restaurantId,
            @Valid @RequestBody final CreateDishRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dishRestMapper.toDto(
                        restaurantHandler.createDish(dishRestMapper.toCommand(
                                restaurantId,
                                authenticatedUserProvider.getCurrentUser().userId(),
                                requestDto
                        ))
                ));
    }
}
