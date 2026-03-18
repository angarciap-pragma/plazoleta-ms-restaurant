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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import javax.crypto.SecretKey;
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

    private static final String JWT_SECRET = "this-is-a-shared-secret-key-with-safe-length-123456";
    private static final String JWT_ISSUER = "plazoleta-auth";

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

        HttpResponse<String> response = sendCreateRestaurantRequest(requestDto, buildToken(1L, "admin@plazoleta.com", "ADMIN"));
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
        ), buildToken(1L, "admin@plazoleta.com", "ADMIN"));
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
        ), buildToken(1L, "admin@plazoleta.com", "ADMIN"));
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
        ), buildToken(1L, "admin@plazoleta.com", "ADMIN"));
        Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body).containsEntry("code", "COMMON_400");
    }

    @Test
    @DisplayName("should create dish successfully")
    void shouldCreateDishSuccessfully() throws Exception {
        when(ownerUserQueryPort.getOwnerById(anyLong())).thenReturn(OwnerUser.builder().id(1L).role("OWNER").build());
        CreateRestaurantRequestDto restaurantRequest = new CreateRestaurantRequestDto(
                "Food Place",
                "999001",
                "Main street 1",
                "+573005698329",
                "https://logo.test",
                1L
        );
        HttpResponse<String> restaurantResponse = sendCreateRestaurantRequest(
                restaurantRequest,
                buildToken(1L, "admin@plazoleta.com", "ADMIN")
        );
        RestaurantCreatedResponseDto restaurant = objectMapper.readValue(restaurantResponse.body(), RestaurantCreatedResponseDto.class);

        HttpResponse<String> response = sendCreateDishRequest(restaurant.id(), """
                {
                  "name": "Burger",
                  "price": 20000,
                  "description": "Beef burger",
                  "imageUrl": "https://image.test/burger.png",
                  "category": "FAST_FOOD"
                }
                """, buildToken(1L, "owner@plazoleta.com", "OWNER"));
        Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(body).containsEntry("active", true);
    }

    @Test
    @DisplayName("should reject dish creation when owner does not match")
    void shouldRejectDishCreationWhenOwnerDoesNotMatch() throws Exception {
        when(ownerUserQueryPort.getOwnerById(anyLong())).thenReturn(OwnerUser.builder().id(1L).role("OWNER").build());
        CreateRestaurantRequestDto restaurantRequest = new CreateRestaurantRequestDto(
                "Food Place",
                "999002",
                "Main street 1",
                "+573005698330",
                "https://logo.test",
                1L
        );
        HttpResponse<String> restaurantResponse = sendCreateRestaurantRequest(
                restaurantRequest,
                buildToken(1L, "admin@plazoleta.com", "ADMIN")
        );
        RestaurantCreatedResponseDto restaurant = objectMapper.readValue(restaurantResponse.body(), RestaurantCreatedResponseDto.class);

        HttpResponse<String> response = sendCreateDishRequest(restaurant.id(), """
                {
                  "name": "Burger",
                  "price": 20000,
                  "description": "Beef burger",
                  "imageUrl": "https://image.test/burger.png",
                  "category": "FAST_FOOD"
                }
                """, buildToken(99L, "other-owner@plazoleta.com", "OWNER"));
        Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(body).containsEntry("code", "RESTAURANT_403_OWNER_MISMATCH");
    }

    @Test
    @DisplayName("should reject invalid dish price")
    void shouldRejectInvalidDishPrice() throws Exception {
        HttpResponse<String> response = sendCreateDishRequest(1L, """
                {
                  "name": "Burger",
                  "price": 0,
                  "description": "Beef burger",
                  "imageUrl": "https://image.test/burger.png",
                  "category": "FAST_FOOD"
                }
                """, buildToken(1L, "owner@plazoleta.com", "OWNER"));
        Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body).containsEntry("code", "COMMON_400");
    }

    @Test
    @DisplayName("should update dish successfully")
    void shouldUpdateDishSuccessfully() throws Exception {
        when(ownerUserQueryPort.getOwnerById(anyLong())).thenReturn(OwnerUser.builder().id(1L).role("OWNER").build());
        CreateRestaurantRequestDto restaurantRequest = new CreateRestaurantRequestDto(
                "Food Place",
                "999003",
                "Main street 1",
                "+573005698331",
                "https://logo.test",
                1L
        );
        HttpResponse<String> restaurantResponse = sendCreateRestaurantRequest(
                restaurantRequest,
                buildToken(1L, "admin@plazoleta.com", "ADMIN")
        );
        RestaurantCreatedResponseDto restaurant = objectMapper.readValue(restaurantResponse.body(), RestaurantCreatedResponseDto.class);

        HttpResponse<String> createDishResponse = sendCreateDishRequest(restaurant.id(), """
                {
                  "name": "Burger",
                  "price": 20000,
                  "description": "Beef burger",
                  "imageUrl": "https://image.test/burger.png",
                  "category": "FAST_FOOD"
                }
                """, buildToken(1L, "owner@plazoleta.com", "OWNER"));
        Map<String, Object> createdDish = objectMapper.readValue(createDishResponse.body(), new TypeReference<>() {
        });

        HttpResponse<String> response = sendUpdateDishRequest(((Number) createdDish.get("id")).longValue(), """
                {
                  "price": 25000,
                  "description": "Updated burger"
                }
                """, buildToken(1L, "owner@plazoleta.com", "OWNER"));
        Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(body).containsEntry("price", 25000);
        assertThat(body).containsEntry("description", "Updated burger");
    }

    @Test
    @DisplayName("should reject dish update when owner does not match")
    void shouldRejectDishUpdateWhenOwnerDoesNotMatch() throws Exception {
        when(ownerUserQueryPort.getOwnerById(anyLong())).thenReturn(OwnerUser.builder().id(1L).role("OWNER").build());
        CreateRestaurantRequestDto restaurantRequest = new CreateRestaurantRequestDto(
                "Food Place",
                "999004",
                "Main street 1",
                "+573005698332",
                "https://logo.test",
                1L
        );
        HttpResponse<String> restaurantResponse = sendCreateRestaurantRequest(
                restaurantRequest,
                buildToken(1L, "admin@plazoleta.com", "ADMIN")
        );
        RestaurantCreatedResponseDto restaurant = objectMapper.readValue(restaurantResponse.body(), RestaurantCreatedResponseDto.class);

        HttpResponse<String> createDishResponse = sendCreateDishRequest(restaurant.id(), """
                {
                  "name": "Burger",
                  "price": 20000,
                  "description": "Beef burger",
                  "imageUrl": "https://image.test/burger.png",
                  "category": "FAST_FOOD"
                }
                """, buildToken(1L, "owner@plazoleta.com", "OWNER"));
        Map<String, Object> createdDish = objectMapper.readValue(createDishResponse.body(), new TypeReference<>() {
        });

        HttpResponse<String> response = sendUpdateDishRequest(((Number) createdDish.get("id")).longValue(), """
                {
                  "price": 25000,
                  "description": "Updated burger"
                }
                """, buildToken(99L, "other-owner@plazoleta.com", "OWNER"));
        Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(body).containsEntry("code", "RESTAURANT_403_DISH_OWNER_MISMATCH");
    }

    @Test
    @DisplayName("should reject restaurant creation when caller is not admin")
    void shouldRejectRestaurantCreationWhenCallerIsNotAdmin() throws Exception {
        when(ownerUserQueryPort.getOwnerById(anyLong())).thenReturn(OwnerUser.builder().id(1L).role("OWNER").build());

        HttpResponse<String> response = sendCreateRestaurantRequest(new CreateRestaurantRequestDto(
                "Food Place",
                "123459",
                "Main street 1",
                "+573005698339",
                "https://logo.test",
                1L
        ), buildToken(1L, "owner@plazoleta.com", "OWNER"));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("should expose internal restaurant lookup")
    void shouldExposeInternalRestaurantLookup() throws Exception {
        when(ownerUserQueryPort.getOwnerById(anyLong())).thenReturn(OwnerUser.builder().id(1L).role("OWNER").build());
        HttpResponse<String> restaurantResponse = sendCreateRestaurantRequest(
                new CreateRestaurantRequestDto(
                        "Internal Food",
                        "999005",
                        "Main street 2",
                        "+573005698333",
                        "https://logo.test",
                        1L
                ),
                buildToken(1L, "admin@plazoleta.com", "ADMIN")
        );
        RestaurantCreatedResponseDto restaurant = objectMapper.readValue(restaurantResponse.body(), RestaurantCreatedResponseDto.class);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/restaurants/internal/" + restaurant.id()))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() { });

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(body).containsEntry("ownerId", 1);
    }

    @Test
    @DisplayName("should update dish active status successfully")
    void shouldUpdateDishActiveStatusSuccessfully() throws Exception {
        when(ownerUserQueryPort.getOwnerById(anyLong())).thenReturn(OwnerUser.builder().id(1L).role("OWNER").build());
        HttpResponse<String> restaurantResponse = sendCreateRestaurantRequest(
                new CreateRestaurantRequestDto(
                        "Food Place",
                        "999006",
                        "Main street 3",
                        "+573005698334",
                        "https://logo.test",
                        1L
                ),
                buildToken(1L, "admin@plazoleta.com", "ADMIN")
        );
        RestaurantCreatedResponseDto restaurant = objectMapper.readValue(restaurantResponse.body(), RestaurantCreatedResponseDto.class);
        HttpResponse<String> createDishResponse = sendCreateDishRequest(restaurant.id(), """
                {
                  "name": "Burger",
                  "price": 20000,
                  "description": "Beef burger",
                  "imageUrl": "https://image.test/burger.png",
                  "category": "FAST_FOOD"
                }
                """, buildToken(1L, "owner@plazoleta.com", "OWNER"));
        Map<String, Object> createdDish = objectMapper.readValue(createDishResponse.body(), new TypeReference<>() { });

        HttpResponse<String> response = sendPatchRequest(
                "/dishes/" + ((Number) createdDish.get("id")).longValue() + "/status",
                """
                {
                  "active": false
                }
                """,
                buildToken(1L, "owner@plazoleta.com", "OWNER")
        );
        Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() { });

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(body).containsEntry("active", false);
    }

    @Test
    @DisplayName("should list restaurants paginated for customer")
    void shouldListRestaurantsPaginatedForCustomer() throws Exception {
        when(ownerUserQueryPort.getOwnerById(anyLong())).thenReturn(OwnerUser.builder().id(1L).role("OWNER").build());
        sendCreateRestaurantRequest(new CreateRestaurantRequestDto(
                "Alpha Food",
                "999007",
                "Main street 4",
                "+573005698335",
                "https://logo.test/a",
                1L
        ), buildToken(1L, "admin@plazoleta.com", "ADMIN"));
        sendCreateRestaurantRequest(new CreateRestaurantRequestDto(
                "Beta Food",
                "999008",
                "Main street 5",
                "+573005698336",
                "https://logo.test/b",
                1L
        ), buildToken(1L, "admin@plazoleta.com", "ADMIN"));

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/restaurants?page=0&size=1"))
                .header("Authorization", "Bearer " + buildToken(20L, "customer@plazoleta.com", "CUSTOMER"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() { });

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(body).containsEntry("page", 0);
        assertThat(body).containsEntry("size", 1);
    }

    @Test
    @DisplayName("should list restaurant dishes paginated for customer")
    void shouldListRestaurantDishesPaginatedForCustomer() throws Exception {
        when(ownerUserQueryPort.getOwnerById(anyLong())).thenReturn(OwnerUser.builder().id(1L).role("OWNER").build());
        HttpResponse<String> restaurantResponse = sendCreateRestaurantRequest(
                new CreateRestaurantRequestDto(
                        "Gamma Food",
                        "999009",
                        "Main street 6",
                        "+573005698337",
                        "https://logo.test/g",
                        1L
                ),
                buildToken(1L, "admin@plazoleta.com", "ADMIN")
        );
        RestaurantCreatedResponseDto restaurant = objectMapper.readValue(restaurantResponse.body(), RestaurantCreatedResponseDto.class);
        sendCreateDishRequest(restaurant.id(), """
                {
                  "name": "Burger",
                  "price": 20000,
                  "description": "Beef burger",
                  "imageUrl": "https://image.test/burger.png",
                  "category": "FAST_FOOD"
                }
                """, buildToken(1L, "owner@plazoleta.com", "OWNER"));

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/restaurants/" + restaurant.id() + "/dishes?category=FAST_FOOD&page=0&size=10"))
                .header("Authorization", "Bearer " + buildToken(20L, "customer@plazoleta.com", "CUSTOMER"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() { });

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(body).containsEntry("page", 0);
        assertThat(body).containsEntry("size", 10);
    }

    private HttpResponse<String> sendCreateRestaurantRequest(
            final CreateRestaurantRequestDto requestDto,
            final String token
    ) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/restaurants"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestDto)))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendCreateDishRequest(final Long restaurantId, final String body, final String token)
            throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/restaurants/" + restaurantId + "/dishes"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendUpdateDishRequest(final Long dishId, final String body, final String token)
            throws Exception {
        return sendPatchRequest("/dishes/" + dishId, body, token);
    }

    private HttpResponse<String> sendPatchRequest(final String path, final String body, final String token)
            throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(body))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String buildToken(final Long userId, final String email, final String role) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        SecretKey secretKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(email)
                .issuer(JWT_ISSUER)
                .issuedAt(java.util.Date.from(now.toInstant()))
                .expiration(java.util.Date.from(now.plusMinutes(30).toInstant()))
                .claim("userId", userId)
                .claim("email", email)
                .claim("role", role)
                .signWith(secretKey)
                .compact();
    }
}
