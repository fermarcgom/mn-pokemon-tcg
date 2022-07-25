package io.github.fermarcgom.service;

import io.github.fermarcgom.dto.CardCreateRequest;
import io.github.fermarcgom.persistence.dao.CardRepository;
import io.github.fermarcgom.persistence.domain.Card;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class CardService {

    private CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getCards() {
        return cardRepository.findAll();
    }

    public void createCard(CardCreateRequest card) {
        Card toCreate = new Card();
        toCreate.setPokemonName(card.pokemonName());
        toCreate.setPokemonType(card.pokemonType());
        toCreate.setPokemonHp(card.pokemonHp());
        toCreate.setEvolvedFrom(card.evolvedFrom());
        toCreate.setImageLink(card.imageLink());

        cardRepository.save(toCreate);
    }
}
