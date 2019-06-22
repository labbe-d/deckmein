package com.cardmein.drawroyale.game.service;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;

import com.cardmein.drawroyale.game.model.Deck;
import com.cardmein.drawroyale.game.model.DeckType;
import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.model.Shoe;
import com.cardmein.drawroyale.game.persistence.GameRepository;
import com.cardmein.drawroyale.game.service.exception.DuplicateDeckException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private DeckService deckService;

    @Test
    public void createdGamePersistedInRepository() {
        Long gameId = gameService.createGame("Battle Royale");
        Game game = gameRepository.find(gameId);

        assertThat(game, notNullValue());
        assertThat(game.getId(), is(gameId));
        assertThat(game.getName(), is("Battle Royale"));
    }

    @Test
    public void createdGameIsRetrievable() {
        Long gameId = gameService.createGame("Battle Royale");
        Game game = gameService.getGame(gameId);

        assertThat(game, notNullValue());
        assertThat(game.getId(), is(gameId));
        assertThat(game.getName(), is("Battle Royale"));
    }

    @Test
    public void deletedGameIsRemovedFromRepository() {
        Long gameId = gameService.createGame("Battle Royale");
        Game game = gameRepository.find(gameId);

        assertThat(game, notNullValue());

        gameService.deleteGame(gameId);
        game = gameRepository.find(gameId);

        assertThat(game, nullValue());

    }

    @Test
    public void addDeckToGame() throws Exception {
        Long gameId = gameService.createGame("Battle Royale");
        Long deckId = deckService.createDeck(DeckType.STANDARD);

        gameService.addDeck(gameId, deckId);

        Game game = gameService.getGame(gameId);

        assertThat(game.getDecks().size(), is(1));
        assertThat(game.getDecks().get(0).getId(), is(deckId));

        Deck deck = deckService.getDeck(deckId);
        Shoe shoe = game.getShoe();
        assertThat(shoe.getCards().size(), is(52));

        deck.getCards().forEach(c -> {
            if (shoe.getCards().stream().noneMatch(s -> s.getCard().getId().equals(c.getId()))) {
                fail("Missing card in shoe suit " + c.getSuit().name() + " value " + c.getValue().name());
            }
        });

    }

    @Test(expected = DuplicateDeckException.class)
    public void addExistingDeckToGameThrows() throws Exception {
        Long gameId = gameService.createGame("Battle Royale");
        Long deckId = deckService.createDeck(DeckType.STANDARD);

        gameService.addDeck(gameId, deckId);
        gameService.addDeck(gameId, deckId);

    }

    @Test
    public void add2DecksToGameContainsAllCards() throws Exception {
        Long gameId = gameService.createGame("Battle Royale");
        Long deck1Id = deckService.createDeck(DeckType.STANDARD);
        Long deck2Id = deckService.createDeck(DeckType.STANDARD);

        gameService.addDeck(gameId, deck1Id);
        gameService.addDeck(gameId, deck2Id);

        Game game = gameService.getGame(gameId);

        assertThat(game.getDecks().size(), is(2));
        assertThat(game.getDecks().get(0).getId(), isIn(Arrays.asList(deck1Id, deck2Id)));
        assertThat(game.getDecks().get(1).getId(), isIn(Arrays.asList(deck1Id, deck2Id)));

        Deck deck1 = deckService.getDeck(deck1Id);
        Deck deck2 = deckService.getDeck(deck2Id);
        Shoe shoe = game.getShoe();
        assertThat(shoe.getCards().size(), is(104));

        deck1.getCards().forEach(c -> {
            if (shoe.getCards().stream().noneMatch(s -> s.getCard().getId().equals(c.getId()))) {
                fail("Missing card in shoe suit " + c.getSuit().name() + " value " + c.getValue().name());
            }
        });

        deck2.getCards().forEach(c -> {
            if (shoe.getCards().stream().noneMatch(s -> s.getCard().getId().equals(c.getId()))) {
                fail("Missing card in shoe suit " + c.getSuit().name() + " value " + c.getValue().name());
            }
        });
    }

}