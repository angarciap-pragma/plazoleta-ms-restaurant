package com.plazoleta.restaurant.infrastructure.entrypoints.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plazoleta.restaurant.domain.model.OwnerUser;
import com.plazoleta.restaurant.domain.spi.OwnerUserQueryPort;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.request.CreateRestaurantRequestDto;
import com.plazoleta.restaurant.infrastructure.entrypoints.rest.dto.response.RestaurantCreatedResponseDto;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
        properties = "spring.profiles.active=test",
        webEnvironment = WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class RestaurantControllerTest {

    @LocalServerPort
    private int port;

    @MockitoBean
    private OwnerUserQueryPort ownerUserQueryPort;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("should create restaurant successfully")
    void shouldCreateRestaurantSuccessfully() throws Exception {
        when(ownerUserQueryPort.getOwnerById(anyLong())).thenReturn(OwnerUser.builder().id(1L).role("OWNER").build());
        CreateRestaurantRequestDto requestDto = new CreateRestaurantRequestDto(
                "Food Place",
                "123456",
                "Main street 1",
                "+573005698325",
                "https://logo.test",
                1L
        );

        HttpResponse<String> response = sendCreateRestaurantRequest(requestDto);
        RestaurantCreatedResponseDto body = objectMapper.readValue(response.body(), RestaurantCreatedResponseDto.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(body.id()).isNotNull();
        assertThat(body.nit()).isEqualTo("123456");
    }

    @Test
    @DisplayName("should reject invalid owner role")
    void shouldRejectInvalidOwnerRole() throws Exception {
        when(ownerUserQueryPort.getOwnerById(anyLong())).thenReturn(OwnerUser.builder().id(1L).role("CUSTOMER").build());
        HttpResponse<String> response = sendCreateRestaurantRequest(new CreateRestaurantRequestDto(
                "Food Place",
                "123457",
                "Main street 1",
                "+573005698326",
                "https://logo.test",
                1L
        ));
        Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body).containsEntry("code", "RESTAURANT_400_INVALID_OWNER_ROLE");
    }

    @Test
    @DisplayName("should reject numeric only restaurant name")
    void shouldRejectNumericOnlyRestaurantName() throws Exception {
        when(ownerUserQueryPort.getOwnerById(anyLong())).thenReturn(OwnerUser.builder().id(1L).role("OWNER").build());
        HttpResponse<String> response = sendCreateRestaurantRequest(new CreateRestaurantRequestDto(
                "123456",
                "123458",
                "Main street 1",
                "+573005698327",
                "https://logo.test",
                1L
        ));
        Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body).containsEntry("code", "RESTAURANT_400_INVALID_NAME");
    }

    @Test
    @DisplayName("should reject invalid nit format")
    void shouldRejectInvalidNitFormat() throws Exception {
        HttpResponse<String> response = sendCreateRestaurantRequest(new CreateRestaurantRequestDto(
                "Food Place",
                "ABC123",
                "Main street 1",
                "+573005698328",
                "https://logo.test",
                1L
        ));
        Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body).containsEntry("code", "COMMON_400");
    }

    private HttpResponse<String> sendCreateRestaurantRequest(final CreateRestaurantRequestDto requestDto) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/restaurants"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestDto)))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
