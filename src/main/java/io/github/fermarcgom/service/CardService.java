package io.github.fermarcgom.service;

import io.github.fermarcgom.dto.CardCreateRequest;
import io.github.fermarcgom.exception.PokemonCardServiceException;
import io.github.fermarcgom.persistence.dao.CardRepository;
import io.github.fermarcgom.persistence.domain.Card;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpStatus;
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

    public void createCard(CardCreateRequest cardCreateRequest) {
        validateEvolvedFrom(cardCreateRequest.evolvedFrom());

        Card card = new Card();
        card.setPokemonName(cardCreateRequest.pokemonName());
        card.setPokemonType(cardCreateRequest.pokemonType());
        card.setPokemonHp(cardCreateRequest.pokemonHp());
        card.setEvolvedFrom(cardCreateRequest.evolvedFrom());
        card.setImageLink(cardCreateRequest.imageLink());

        cardRepository.save(card);
    }

    private void validateEvolvedFrom(String evolvedFrom) {
        if (StringUtils.isNotEmpty(evolvedFrom)) {
            cardRepository.findByPokemonName(evolvedFrom)
                    .orElseThrow(() -> new PokemonCardServiceException(HttpStatus.BAD_REQUEST,
                            "ERR_EVOLVED_FROM",
                            String.format("There is no card with the pokemon name '%s' to be evolved from", evolvedFrom)));
        }
    }
}
