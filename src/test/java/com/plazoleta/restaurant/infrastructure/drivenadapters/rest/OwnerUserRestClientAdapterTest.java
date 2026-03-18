package com.plazoleta.restaurant.infrastructure.drivenadapters.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.plazoleta.common.exception.ResourceNotFoundException;
import com.plazoleta.restaurant.infrastructure.drivenadapters.rest.properties.UserServiceProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

class OwnerUserRestClientAdapterTest {

    @Test
    @DisplayName("should fetch owner from ms-user")
    void shouldFetchOwnerFromMsUser() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        UserServiceProperties properties = new UserServiceProperties();
        properties.setBaseUrl("http://localhost:8082");
        OwnerUserRestClientAdapter adapter = new OwnerUserRestClientAdapter(builder.build(), properties);

        server.expect(requestTo("http://localhost:8082/users/internal/1"))
                .andRespond(withSuccess(
                        "{\"id\":1,\"firstName\":\"Andrea\",\"lastName\":\"Garcia\",\"documentId\":\"123\",\"email\":\"owner@plazoleta.com\",\"role\":\"OWNER\"}",
                        MediaType.APPLICATION_JSON
                ));

        assertThat(adapter.getOwnerById(1L).getRole()).isEqualTo("OWNER");
    }

    @Test
    @DisplayName("should fail when owner is not found in ms-user")
    void shouldFailWhenOwnerIsNotFoundInMsUser() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        UserServiceProperties properties = new UserServiceProperties();
        properties.setBaseUrl("http://localhost:8082");
        OwnerUserRestClientAdapter adapter = new OwnerUserRestClientAdapter(builder.build(), properties);

        server.expect(requestTo("http://localhost:8082/users/internal/99"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThatThrownBy(() -> adapter.getOwnerById(99L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("should fail when ms-user returns empty body")
    void shouldFailWhenMsUserReturnsEmptyBody() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        UserServiceProperties properties = new UserServiceProperties();
        properties.setBaseUrl("http://localhost:8082");
        OwnerUserRestClientAdapter adapter = new OwnerUserRestClientAdapter(builder.build(), properties);

        server.expect(requestTo("http://localhost:8082/users/internal/1"))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> adapter.getOwnerById(1L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("should rethrow non not-found client errors")
    void shouldRethrowNonNotFoundClientErrors() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        UserServiceProperties properties = new UserServiceProperties();
        properties.setBaseUrl("http://localhost:8082");
        OwnerUserRestClientAdapter adapter = new OwnerUserRestClientAdapter(builder.build(), properties);

        server.expect(requestTo("http://localhost:8082/users/internal/5"))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        assertThatThrownBy(() -> adapter.getOwnerById(5L)).isInstanceOf(HttpClientErrorException.class);
    }
}
