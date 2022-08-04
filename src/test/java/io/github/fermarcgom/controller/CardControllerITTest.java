package io.github.fermarcgom.controller;

import io.github.fermarcgom.dto.CardCreateRequest;
import io.github.fermarcgom.dto.CardPageResponse;
import io.github.fermarcgom.dto.CardResponse;
import io.github.fermarcgom.dto.PokemonCardServiceError;
import io.github.fermarcgom.dto.Type;
import io.github.fermarcgom.persistence.dao.CardRepository;
import io.github.fermarcgom.persistence.dao.UserRepository;
import io.github.fermarcgom.persistence.domain.User;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

@Testcontainers
@MicronautTest
class CardControllerITTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    CardRepository cardRepository;

    @Inject
    UserRepository userRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
        cardRepository.deleteAll();
        User logUser = new User();
        logUser.setPassword("pass");
        logUser.setEmail("testUser@test.com");
        userRepository.save(logUser);
    }

    @Test
    void whenNotAuthorizedAndCreateCardRequestThenError() {
        CardCreateRequest cardCreateRequest = new CardCreateRequest("Charmander",
                Type.FIRE,
                60,
                null,
                "http://site.pokemon.com/charmander.jpg");
        HttpRequest<CardCreateRequest> request = HttpRequest.POST("/card", cardCreateRequest);
        Assertions.assertThatExceptionOfType(HttpClientException.class)
                .isThrownBy(() -> client.toBlocking().exchange(request, PokemonCardServiceError.class))
                .withMessageContaining("Unauthorized");
    }

    @Test
    void whenAuthorizedAndGetCardsRequestThenRetrieveSuccessfully() {
        String accessToken = logIn().getAccessToken();
        HttpResponse<Integer> createResponse = createCard(accessToken);

        Assertions.assertThat(createResponse.getBody()).isNotEmpty();

        HttpRequest<Object> getRequest = HttpRequest.GET("/card")
                .bearerAuth(accessToken);
        HttpResponse<CardPageResponse> getResponse = client.toBlocking().exchange(getRequest, CardPageResponse.class);

        Assertions.assertThat(Optional.ofNullable(getResponse.getStatus())).isPresent().get().isEqualTo(HttpStatus.OK);
        Assertions.assertThat(getResponse.getBody()).isNotEmpty();
        CardPageResponse cardPage = getResponse.getBody().get();
        Assertions.assertThat(cardPage.getPage()).isEqualTo(0);
        Assertions.assertThat(cardPage.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(cardPage.getSize()).isEqualTo(20);
        Assertions.assertThat(cardPage.getTotalSize()).isEqualTo(1);
        Assertions.assertThat(cardPage.getContent()).hasSize(1);
    }

    @Test
    void whenAuthorizedAndGetByIdRequestThenRetrieveSuccessfully() {
        CardCreateRequest cardCreateRequest = getCardCreateRequest();
        String accessToken = logIn().getAccessToken();
        HttpResponse<Integer> createResponse = createCard(accessToken, cardCreateRequest);

        Assertions.assertThat(createResponse.getBody()).isNotEmpty();

        HttpRequest<Object> getByIdRequest = HttpRequest.GET("/card/" + createResponse.getBody().get())
                .bearerAuth(accessToken);
        HttpResponse<CardResponse> getByIdResponse = client.toBlocking().exchange(getByIdRequest, CardResponse.class);

        Assertions.assertThat(Optional.ofNullable(getByIdResponse.getStatus())).isPresent().get().isEqualTo(HttpStatus.OK);
        Assertions.assertThat(getByIdResponse.getBody()).isNotEmpty();
        CardResponse card = getByIdResponse.getBody().get();
        Assertions.assertThat(card.pokemonName()).isEqualTo(cardCreateRequest.pokemonName());
        Assertions.assertThat(card.evolvedFrom()).isEqualTo(cardCreateRequest.evolvedFrom());
        Assertions.assertThat(card.imageLink()).isEqualTo(cardCreateRequest.imageLink());
        Assertions.assertThat(card.pokemonHp()).isEqualTo(cardCreateRequest.pokemonHp());
        Assertions.assertThat(card.pokemonType()).isEqualTo(cardCreateRequest.pokemonType());
    }

    @Test
    void whenAuthorizedAndCreateCardRequestThenCardIsCreated() {
        HttpResponse<Integer> response = createCard(logIn().getAccessToken());


        Assertions.assertThat(response.getBody()).isNotEmpty();
        Assertions.assertThat(cardRepository.findAll()).hasSize(1);
        Assertions.assertThat(cardRepository.findById(response.getBody().get())).isNotEmpty();
    }

    @Test
    void whenAuthorizedAndDeleteCardRequestThenCardIsDeleted() {
        String accessToken = logIn().getAccessToken();
        HttpResponse<Integer> createResponse = createCard(accessToken);

        Assertions.assertThat(createResponse.getBody()).isNotEmpty();

        HttpRequest<Object> deleteRequest = HttpRequest.DELETE("/card/" + createResponse.getBody().get())
                .bearerAuth(accessToken);
        HttpResponse<Void> deleteResponse = client.toBlocking().exchange(deleteRequest);

        Assertions.assertThat(Optional.ofNullable(deleteResponse.getStatus())).isPresent().get().isEqualTo(HttpStatus.OK);
        Assertions.assertThat(cardRepository.findAll()).isEmpty();
    }

    @Test
    void whenErrorIsRaisedThenResponseIsParsed() {
        String accessToken = logIn().getAccessToken();

        HttpRequest<Object> deleteRequest = HttpRequest.DELETE("/card/1")
                .bearerAuth(accessToken);

        HttpClientResponseException exception = Assertions.catchThrowableOfType(
                () -> client.toBlocking().exchange(deleteRequest, Argument.of(Void.class), Argument.of(PokemonCardServiceError.class)),
                HttpClientResponseException.class);

        Optional<PokemonCardServiceError> error = exception.getResponse().getBody(PokemonCardServiceError.class);
        Assertions.assertThat(error).isPresent();
        Assertions.assertThat(error.get().status()).isEqualTo(404);
    }

    private BearerAccessRefreshToken logIn() {
        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("testUser@test.com", "pass");
        HttpRequest<UsernamePasswordCredentials> request = HttpRequest.POST("/login", credentials);
        HttpResponse<BearerAccessRefreshToken> response =  client.toBlocking().exchange(request, BearerAccessRefreshToken.class);
        return response.body();
    }

    private HttpResponse<Integer> createCard(String accessToken) {
        CardCreateRequest cardCreateRequest = getCardCreateRequest();
        return createCard(accessToken, cardCreateRequest);
    }

    private HttpResponse<Integer> createCard(String accessToken, CardCreateRequest cardCreateRequest) {
        HttpRequest<CardCreateRequest> request = HttpRequest.POST("/card", cardCreateRequest)
                .bearerAuth(accessToken);
        HttpResponse<Integer> response = client.toBlocking().exchange(request, Integer.class);

        Assertions.assertThat(Optional.ofNullable(response.getStatus())).isPresent().get().isEqualTo(HttpStatus.CREATED);

        return response;
    }

    @NotNull
    private CardCreateRequest getCardCreateRequest() {
        return new CardCreateRequest("Charmander",
                Type.FIRE,
                60,
                null,
                "http://site.pokemon.com/charmander.jpg");
    }
}
