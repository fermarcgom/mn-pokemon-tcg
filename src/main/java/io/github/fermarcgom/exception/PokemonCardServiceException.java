package io.github.fermarcgom.exception;

import io.micronaut.http.HttpStatus;

public class PokemonCardServiceException extends RuntimeException{

    private HttpStatus status;
    private String error;

    public PokemonCardServiceException(HttpStatus status, String error, String message) {
        super(message);
        this.status = status;
        this.error = error;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }
}
