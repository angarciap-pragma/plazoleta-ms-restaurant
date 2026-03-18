package com.plazoleta.restaurant.infrastructure.entrypoints.rest.controller;

import com.plazoleta.common.security.AuthenticatedUserProvider;
import com.plazoleta.restaurant.application.command.UpdateDishStatusCommand;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.request.UpdateDishRequestDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.request.UpdateDishStatusRequestDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response.DishUpdatedResponseDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.mapper.UpdateDishRestMapper;
import com.plazoleta.restaurant.infrastructure.service.handler.RestaurantHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;

/**
 * Expone los endpoints HTTP del módulo de platos.
 */
@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
@Tag(name = "Dishes", description = "Endpoints for dish management")
public class DishController {

    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final RestaurantHandler restaurantHandler;
    private final UpdateDishRestMapper updateDishRestMapper;

    @PatchMapping("/{dishId}")
    @Operation(
            summary = "Update dish",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dish updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "403", description = "Owner mismatch"),
                    @ApiResponse(responseCode = "404", description = "Dish or restaurant not found")
            }
    )
    public ResponseEntity<DishUpdatedResponseDto> updateDish(
            @PathVariable final Long dishId,
            @Valid @RequestBody final UpdateDishRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                updateDishRestMapper.toDto(
                        restaurantHandler.updateDish(updateDishRestMapper.toCommand(
                                dishId,
                                authenticatedUserProvider.getCurrentUser().userId(),
                                requestDto
                        ))
                )
        );
    }

    @PatchMapping("/{dishId}/status")
    @Operation(
            summary = "Update dish active status",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dish status updated"),
                    @ApiResponse(responseCode = "403", description = "Owner mismatch"),
                    @ApiResponse(responseCode = "404", description = "Dish or restaurant not found")
            }
    )
    public ResponseEntity<DishUpdatedResponseDto> updateDishStatus(
            @PathVariable final Long dishId,
            @Valid @RequestBody final UpdateDishStatusRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                updateDishRestMapper.toDto(
                        restaurantHandler.updateDishStatus(
                                new UpdateDishStatusCommand(
                                        dishId,
                                        authenticatedUserProvider.getCurrentUser().userId(),
                                        requestDto.active()
                                )
                        )
                )
        );
    }
}
