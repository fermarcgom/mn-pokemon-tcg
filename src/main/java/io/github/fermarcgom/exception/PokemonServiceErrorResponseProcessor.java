package io.github.fermarcgom.exception;

import io.github.fermarcgom.dto.PokemonCardServiceError;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.server.exceptions.response.Error;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import jakarta.inject.Singleton;

@Singleton
public class PokemonServiceErrorResponseProcessor implements ErrorResponseProcessor<PokemonCardServiceError> {
    @Override
    public MutableHttpResponse<PokemonCardServiceError> processResponse(
            @NonNull ErrorContext errorContext,
            @NonNull MutableHttpResponse<?> response) {

        PokemonCardServiceError error;
        if (!errorContext.hasErrors()) {
            error = new PokemonCardServiceError(
                    response.getStatus().getCode(),
                    response.getStatus().name(),
                    "No custom errors found...");
        } else {
            Error firstError = errorContext.getErrors().get(0);
            error = new PokemonCardServiceError(
                    response.getStatus().getCode(),
                    response.getStatus().name(),
                    firstError.getMessage());
        }
        return response.body(error).contentType(MediaType.APPLICATION_JSON);
    }
}
