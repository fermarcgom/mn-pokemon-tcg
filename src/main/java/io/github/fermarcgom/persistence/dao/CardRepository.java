package io.github.fermarcgom.persistence.dao;

import io.github.fermarcgom.persistence.domain.Card;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends CrudRepository<Card, Integer> {

    @Override
    List<Card> findAll();

    Optional<Card> findByPokemonName(String pokemonName);
}
