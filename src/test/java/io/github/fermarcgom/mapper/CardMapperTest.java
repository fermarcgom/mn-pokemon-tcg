package io.github.fermarcgom.mapper;

import io.github.fermarcgom.dto.CardPageResponse;
import io.github.fermarcgom.dto.CardResponse;
import io.github.fermarcgom.dto.Type;
import io.github.fermarcgom.persistence.domain.Card;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CardMapperTest {

    CardMapper mapper = new CardMapper();

    @Test
    void whenMappingACardThenCorrectCardResponseIsCreated() {
        Card card = new Card();
        card.setImageLink("http://image.link.com");
        card.setEvolvedFrom("evolvedFrom");
        card.setPokemonHp(50);
        card.setPokemonName("pokemonName");
        card.setPokemonType(Type.LIGHTNING);

        CardResponse cardResponse = mapper.map(card);

        Assertions.assertThat(cardResponse.pokemonType()).isEqualTo(card.getPokemonType());
        Assertions.assertThat(cardResponse.evolvedFrom()).isEqualTo(card.getEvolvedFrom());
        Assertions.assertThat(cardResponse.pokemonHp()).isEqualTo(card.getPokemonHp());
        Assertions.assertThat(cardResponse.imageLink()).isEqualTo(card.getImageLink());
        Assertions.assertThat(cardResponse.pokemonName()).isEqualTo(card.getPokemonName());
    }

    @Test
    void whenMappingACardListThenCorrectCardResponseListIsCreated() {
        Card card = new Card();
        card.setImageLink("http://image.link.com");
        card.setEvolvedFrom("evolvedFrom");
        card.setPokemonHp(50);
        card.setPokemonName("pokemonName");
        card.setPokemonType(Type.LIGHTNING);

        List<CardResponse> cardResponseList = mapper.map(List.of(card));

        Assertions.assertThat(cardResponseList).hasSize(1);
    }

    @Test
    void whenMappingACardPageThenCorrectCardResponsePageIsCreated() {
        Card card = new Card();
        card.setImageLink("http://image.link.com");
        card.setEvolvedFrom("evolvedFrom");
        card.setPokemonHp(50);
        card.setPokemonName("pokemonName");
        card.setPokemonType(Type.LIGHTNING);

        CardPageResponse cardResponsePage = mapper.map(Page.of(List.of(card), Pageable.from(0, 2), 1));

        Assertions.assertThat(cardResponsePage.getPage()).isEqualTo(0);
        Assertions.assertThat(cardResponsePage.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(cardResponsePage.getContent()).hasSize(1);
        Assertions.assertThat(cardResponsePage.getTotalSize()).isEqualTo(1);
        Assertions.assertThat(cardResponsePage.getSize()).isEqualTo(2);
    }
}
