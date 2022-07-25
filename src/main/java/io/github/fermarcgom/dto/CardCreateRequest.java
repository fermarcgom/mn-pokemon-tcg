package io.github.fermarcgom.dto;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Introspected
public record CardCreateRequest (
        @NotBlank
        String pokemonName,
        @NotNull
        Type pokemonType,
        @NotNull
        @Min(value = 1)
        Integer pokemonHp,
        String evolvedFrom,
        String imageLink
){}

