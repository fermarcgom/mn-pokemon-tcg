package io.github.fermarcgom.dto;

import io.micronaut.core.annotation.Introspected;

@Introspected
public record CardResponse (
        Integer id,
        String pokemonName,
        Type pokemonType,
        Integer pokemonHp,
        String evolvedFrom,
        String imageLink
) {}
