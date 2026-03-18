package com.plazoleta.restaurant.infrastructure.entrypoints.rest.controller;

import com.plazoleta.common.security.AuthenticatedUserProvider;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.request.CreateRestaurantRequestDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.request.CreateDishRequestDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response.DishCreatedResponseDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response.DishSummaryResponseDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response.PagedResponseDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response.RestaurantCreatedResponseDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response.RestaurantSummaryResponseDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.mapper.CatalogRestMapper;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final CatalogRestMapper catalogRestMapper;

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

    @GetMapping("/internal/{restaurantId}")
    @Operation(
            summary = "Get internal restaurant by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Restaurant found"),
                    @ApiResponse(responseCode = "404", description = "Restaurant not found")
            }
    )
    public ResponseEntity<com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response.RestaurantOwnershipResponseDto>
    getInternalRestaurant(@PathVariable final Long restaurantId) {
        return ResponseEntity.ok(
                restaurantRestMapper.toDto(restaurantHandler.getRestaurantById(restaurantId))
        );
    }

    @GetMapping
    @Operation(
            summary = "List restaurants",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Restaurants listed")
            }
    )
    public ResponseEntity<PagedResponseDto<RestaurantSummaryResponseDto>> listRestaurants(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size
    ) {
        return ResponseEntity.ok(catalogRestMapper.toRestaurantPageDto(restaurantHandler.listRestaurants(page, size)));
    }

    @GetMapping("/{restaurantId}/dishes")
    @Operation(
            summary = "List restaurant dishes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dishes listed"),
                    @ApiResponse(responseCode = "404", description = "Restaurant not found")
            }
    )
    public ResponseEntity<PagedResponseDto<DishSummaryResponseDto>> listRestaurantDishes(
            @PathVariable final Long restaurantId,
            @RequestParam(required = false) final String category,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size
    ) {
        return ResponseEntity.ok(
                catalogRestMapper.toDishPageDto(
                        restaurantHandler.listRestaurantDishes(restaurantId, category, page, size)
                )
        );
    }
}
