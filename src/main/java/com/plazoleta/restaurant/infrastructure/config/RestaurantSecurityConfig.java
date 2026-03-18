package com.plazoleta.restaurant.infrastructure.config;

import com.plazoleta.common.security.BaseSecurityConfig;
import com.plazoleta.common.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Define la seguridad HTTP temporal del microservicio de restaurantes.
 */
@Configuration
@Import(BaseSecurityConfig.class)
public class RestaurantSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity httpSecurity,
            final JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/restaurants/internal/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/actuator/health",
                                "/actuator/info"
                        ).permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/restaurants").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/restaurants/*/dishes").hasRole("OWNER")
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/dishes/*").hasRole("OWNER")
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/dishes/*/status").hasRole("OWNER")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/restaurants").hasRole("CUSTOMER")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/restaurants/*/dishes").hasRole("CUSTOMER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
