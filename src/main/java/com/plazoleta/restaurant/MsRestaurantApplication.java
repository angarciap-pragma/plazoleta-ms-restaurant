package com.plazoleta.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del microservicio de restaurantes.
 */
@SpringBootApplication
public class MsRestaurantApplication {

    public static void main(final String[] args) {
        SpringApplication.run(MsRestaurantApplication.class, args);
    }
}
