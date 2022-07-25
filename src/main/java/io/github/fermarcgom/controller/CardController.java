package io.github.fermarcgom.controller;

import io.github.fermarcgom.persistence.domain.Card;
import io.github.fermarcgom.service.CardService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public List<Card> get() {
        return cardService.getCards();
    }
}
