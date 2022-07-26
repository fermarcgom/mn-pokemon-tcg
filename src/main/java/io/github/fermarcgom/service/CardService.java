package io.github.fermarcgom.service;

import io.github.fermarcgom.dto.CardCreateRequest;
import io.github.fermarcgom.dto.CardPageResponse;
import io.github.fermarcgom.dto.CardResponse;
import io.github.fermarcgom.dto.Type;
import io.github.fermarcgom.exception.PokemonCardServiceException;
import io.github.fermarcgom.mapper.CardMapper;
import io.github.fermarcgom.persistence.dao.CardRepository;
import io.github.fermarcgom.persistence.domain.Card;
import io.micronaut.core.util.StringUtils;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpStatus;
import jakarta.inject.Singleton;

@Singleton
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    public CardService(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    public CardPageResponse getCards(Integer page, Integer size, Type type) {
        int pageRequested = page == null ? 0 : page;
        int sizeRequested = size == null ? 20 : size;
        return cardMapper.map(cardRepository.findAll(Pageable.from(pageRequested, sizeRequested)));
    }

    public CardResponse getCard(Integer id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new PokemonCardServiceException(HttpStatus.NOT_FOUND,
                        "ERR_CARD_NOT_FOUND",
                        String.format("There is no card with id %d", id)));
        return cardMapper.map(card);
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
                            String.format("There is no card with the pokemon name %s to be evolved from", evolvedFrom)));
        }
    }
}
