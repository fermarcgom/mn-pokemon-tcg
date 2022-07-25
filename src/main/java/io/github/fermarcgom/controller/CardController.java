package io.github.fermarcgom.controller;

import io.github.fermarcgom.dto.CardCreateRequest;
import io.github.fermarcgom.persistence.domain.Card;
import io.github.fermarcgom.service.CardService;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
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

    @Get
    public List<Card> get() {
        return cardService.getCards();
    }

    @Post
    @Status(HttpStatus.CREATED)
    public void createCard(@Body CardCreateRequest card) {
        cardService.createCard(card);
    }
}
