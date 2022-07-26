package io.github.fermarcgom.controller;

import io.github.fermarcgom.dto.CardCreateRequest;
import io.github.fermarcgom.dto.CardPageResponse;
import io.github.fermarcgom.dto.CardResponse;
import io.github.fermarcgom.dto.Type;
import io.github.fermarcgom.service.CardService;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Status;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import javax.validation.constraints.Min;
import java.util.Optional;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Get("{?page,size,type}")
    public CardPageResponse get(@QueryValue Optional<Integer> page,
                                @QueryValue Optional<Integer> size,
                                @QueryValue Optional<Type> type) {
        return cardService.getCards(page.orElse(0), size.orElse(20), type.orElse(null));
    }

    @Get("{id}")
    public CardResponse getById(@PathVariable @Min(value = 0) Integer id) {
        return cardService.getCard(id);
    }

    @Post
    @Status(HttpStatus.CREATED)
    public void createCard(@Body CardCreateRequest card) {
        cardService.createCard(card);
    }
}
