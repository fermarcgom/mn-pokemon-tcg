package io.github.fermarcgom.exception;

import io.github.fermarcgom.dto.PokemonCardServiceError;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
public class PokemonCardExceptionHandler implements ExceptionHandler<PokemonCardServiceException, HttpResponse<PokemonCardServiceError>>{
    @Override
    public HttpResponse<PokemonCardServiceError> handle(HttpRequest request, PokemonCardServiceException exception) {
        return HttpResponse.badRequest()
                .body(new PokemonCardServiceError(
                        exception.getStatus().getCode(),
                        exception.getError(),
                        exception.getMessage()));
    }
}
