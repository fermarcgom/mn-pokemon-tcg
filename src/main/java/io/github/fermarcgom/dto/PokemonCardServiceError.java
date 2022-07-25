package io.github.fermarcgom.dto;

public record PokemonCardServiceError (
        int status,
        String error,
        String message
){}
