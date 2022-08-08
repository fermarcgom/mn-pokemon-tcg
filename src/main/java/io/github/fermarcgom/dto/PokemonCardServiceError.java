package io.github.fermarcgom.dto;

import io.micronaut.core.annotation.Introspected;

@Introspected
public record PokemonCardServiceError (
        int status,
        String error,
        String message
){}
