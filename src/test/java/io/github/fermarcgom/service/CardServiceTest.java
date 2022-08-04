package io.github.fermarcgom.service;

import io.github.fermarcgom.dto.CardCreateRequest;
import io.github.fermarcgom.dto.CardPageResponse;
import io.github.fermarcgom.dto.CardResponse;
import io.github.fermarcgom.dto.Type;
import io.github.fermarcgom.exception.PokemonCardServiceException;
import io.github.fermarcgom.mapper.CardMapper;
import io.github.fermarcgom.persistence.dao.CardRepository;
import io.github.fermarcgom.persistence.domain.Card;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    private CardService cardService;

    @BeforeEach
    void setup() {
        cardService = new CardService(cardRepository, new CardMapper());
    }

    @Test
    void whenGetCardsThenCardsAreFound() {
        Mockito.when(cardRepository.findAll(Mockito.any(Pageable.class), Mockito.any(Type.class)))
                .thenAnswer(invocation -> getCardPage(invocation.getArgument(0), invocation.getArgument(1)));

        CardPageResponse response = cardService.getCards(0, 1, Type.DRAGON);

        assertThat(response.getSize()).isEqualTo(1);
        assertThat(response.getTotalSize()).isEqualTo(1);
        assertThat(response.getPage()).isEqualTo(0);
        assertThat(response.getTotalPages()).isEqualTo(1);
        assertThat(response.getContent()).hasSize(1);
    }

    @Test
    void whenGetCardsWithNullValuesThenDefaultValuesAreUsed() {
        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.when(cardRepository.findAll(pageableArgumentCaptor.capture(), Mockito.isNull()))
                .thenReturn(getCardPage(Pageable.from(0, 1), Type.DRAGON));

        CardPageResponse response = cardService.getCards(null, null, null);

        assertThat(response).isNotNull();
        assertThat(pageableArgumentCaptor.getValue()).isNotNull();
        assertThat(pageableArgumentCaptor.getValue().getNumber()).isEqualTo(0);
        assertThat(pageableArgumentCaptor.getValue().getSize()).isEqualTo(20);
    }

    @Test
    void whenGetCardByIdThenCorrectCardIsRetrieved() {
        Mockito.when(cardRepository.findById(Mockito.anyInt()))
                .thenAnswer(invocation -> Optional.of(getCard(invocation.getArgument(0), Type.DRAGON)));

        CardResponse response = cardService.getCard(1);

        assertThat(response.id()).isEqualTo(1);
    }

    @Test
    void whenGetCardByIdWithNonExistingIdThenExceptionIsRaised() {
        Mockito.when(cardRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> cardService.getCard(1))
                .isInstanceOf(PokemonCardServiceException.class)
                .hasMessage("There is no card with id 1");
    }

    @Test
    void whenCreateCardWithValidValuesThenCardIsCreated() {
        ArgumentCaptor<Card> cardArgumentCaptor = ArgumentCaptor.forClass(Card.class);
        Mockito.when(cardRepository.save(cardArgumentCaptor.capture()))
                .thenAnswer(invocation -> {
                    Card saved = invocation.getArgument(0);
                    saved.setId(101);
                    return saved;
                });
        Mockito.when(cardRepository.findByPokemonName("evolvedFrom"))
                .thenReturn(Optional.of(getCard(102, Type.DRAGON)));

        CardCreateRequest request = new CardCreateRequest("pokemonName", Type.DRAGON, 50, "evolvedFrom", "http://image.link.com");

        Integer id = cardService.createCard(request);

        assertThat(id).isEqualTo(101);
        assertThat(cardArgumentCaptor.getValue()).isNotNull();
        assertThat(cardArgumentCaptor.getValue().getPokemonName()).isEqualTo(request.pokemonName());
        assertThat(cardArgumentCaptor.getValue().getPokemonType()).isEqualTo(request.pokemonType());
        assertThat(cardArgumentCaptor.getValue().getPokemonHp()).isEqualTo(request.pokemonHp());
        assertThat(cardArgumentCaptor.getValue().getEvolvedFrom()).isEqualTo(request.evolvedFrom());
        assertThat(cardArgumentCaptor.getValue().getImageLink()).isEqualTo(request.imageLink());
    }

    @Test
    void whenCreateCardWithInvalidEvolvedFromThenExceptionIsRaised() {
        CardCreateRequest request = new CardCreateRequest("pokemonName", Type.DRAGON, 50, "evolvedFrom", "http://image.link.com");

        assertThatThrownBy(() -> cardService.createCard(request))
                .isInstanceOf(PokemonCardServiceException.class)
                        .hasMessage("There is no card with the pokemon name evolvedFrom to be evolved from");
    }

    @Test
    void whenDeleteCardValidIdThenCardIsDeleted() {
        Mockito.when(cardRepository.findById(1))
                .thenReturn(Optional.of(getCard(1, Type.DRAGON)));

        cardService.deleteCard(1);

        Mockito.verify(cardRepository).delete(Mockito.any());
    }

    @Test
    void whenDeleteCardNonExistingIdThenExceptionIsRaised() {
        Mockito.when(cardRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> cardService.deleteCard(1))
                .isInstanceOf(PokemonCardServiceException.class)
                .hasMessage("There is no card with id 1");
    }

    private Page<Card> getCardPage(Pageable page, Type type) {
        return Page.of(List.of(getCard(101, type)), page, 1);
    }

    private Card getCard(Integer id, Type type) {
        Card card = new Card();
        card.setPokemonType(type);
        card.setPokemonName("pokemonName");
        card.setPokemonHp(50);
        card.setEvolvedFrom("evolvedFrom");
        card.setImageLink("http://image.link.com");
        card.setId(id);

        return card;
    }
}
