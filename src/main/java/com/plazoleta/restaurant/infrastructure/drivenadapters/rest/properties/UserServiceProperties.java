package com.plazoleta.restaurant.infrastructure.drivenadapters.rest.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Representa las propiedades del cliente REST hacia ms-user.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "restaurant.clients.user-service")
public class UserServiceProperties {

    private String baseUrl;
}
