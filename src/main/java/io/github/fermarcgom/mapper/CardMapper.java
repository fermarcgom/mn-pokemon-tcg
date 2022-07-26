package io.github.fermarcgom.mapper;

import io.github.fermarcgom.dto.CardPageResponse;
import io.github.fermarcgom.dto.CardResponse;
import io.github.fermarcgom.persistence.domain.Card;
import io.micronaut.data.model.Page;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class CardMapper {

    public CardResponse map(Card card) {
        return new CardResponse(card.getId(),
                card.getPokemonName(),
                card.getPokemonType(),
                card.getPokemonHp(),
                card.getEvolvedFrom(),
                card.getImageLink());
    }

    public List<CardResponse> map(List<Card> card) {
        return card.stream().map(this::map).toList();
    }

    public CardPageResponse map (Page<Card> cardPage) {
        CardPageResponse cardPageResponse = new CardPageResponse();
        cardPageResponse.setContent(map(cardPage.getContent()));
        cardPageResponse.setPage(cardPage.getPageNumber());
        cardPageResponse.setTotalPages(cardPage.getTotalPages());
        cardPageResponse.setSize(cardPage.getSize());
        cardPageResponse.setTotalSize(cardPage.getTotalSize());
        return cardPageResponse;
    }
}
