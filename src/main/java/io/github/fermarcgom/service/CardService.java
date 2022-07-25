package io.github.fermarcgom.service;

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
}
