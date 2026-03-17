package com.plazoleta.restaurant.domain.exception;

import com.plazoleta.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Define los códigos de error propios del microservicio de restaurantes.
 */
public enum RestaurantErrorCode implements ErrorCode {
    NIT_ALREADY_EXISTS("RESTAURANT_409_NIT_ALREADY_EXISTS", "Restaurant NIT already exists", HttpStatus.CONFLICT),
    INVALID_OWNER_ROLE("RESTAURANT_400_INVALID_OWNER_ROLE", "Owner user must have OWNER role", HttpStatus.BAD_REQUEST),
    INVALID_RESTAURANT_NAME("RESTAURANT_400_INVALID_NAME", "Restaurant name cannot contain only numbers", HttpStatus.BAD_REQUEST),
    RESTAURANT_NOT_FOUND("RESTAURANT_404_NOT_FOUND", "Restaurant was not found", HttpStatus.NOT_FOUND),
    RESTAURANT_OWNER_MISMATCH("RESTAURANT_403_OWNER_MISMATCH", "Only the restaurant owner can create dishes", HttpStatus.FORBIDDEN),
    INVALID_DISH_PRICE("RESTAURANT_400_INVALID_DISH_PRICE", "Dish price must be greater than zero", HttpStatus.BAD_REQUEST),
    DISH_NOT_FOUND("RESTAURANT_404_DISH_NOT_FOUND", "Dish was not found", HttpStatus.NOT_FOUND),
    DISH_OWNER_MISMATCH("RESTAURANT_403_DISH_OWNER_MISMATCH", "Only the restaurant owner can modify dishes", HttpStatus.FORBIDDEN);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus status;

    RestaurantErrorCode(final String code, final String defaultMessage, final HttpStatus status) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.status = status;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String defaultMessage() {
        return defaultMessage;
    }

    @Override
    public HttpStatus status() {
        return status;
    }
}
