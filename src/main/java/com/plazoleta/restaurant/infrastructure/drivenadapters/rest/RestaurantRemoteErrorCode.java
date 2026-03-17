package com.plazoleta.restaurant.infrastructure.drivenadapters.rest;

import com.plazoleta.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Define los errores remotos manejados por las integraciones REST.
 */
public enum RestaurantRemoteErrorCode implements ErrorCode {
    OWNER_NOT_FOUND("RESTAURANT_404_OWNER_NOT_FOUND", "Owner user was not found", HttpStatus.NOT_FOUND);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus status;

    RestaurantRemoteErrorCode(final String code, final String defaultMessage, final HttpStatus status) {
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
