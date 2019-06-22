package com.cardmein.drawroyale.game.web.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import com.cardmein.drawroyale.game.model.DeckType;
import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.model.GameCard;
import com.cardmein.drawroyale.game.service.DeckService;
import com.cardmein.drawroyale.game.service.GameService;
import com.cardmein.drawroyale.game.web.model.GameAddDeckResource;
import com.cardmein.drawroyale.game.web.model.GameCreateResource;
import com.cardmein.drawroyale.game.web.model.GameResource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GameControllerTest extends BaseControllerTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private DeckService deckService;

    @Test
    public void getExistingGame() {
        Long gameId = gameService.createGame("Battle Royale");
        Game game = gameService.getGame(gameId);

        ResponseEntity<GameResource> response =
            restTemplate.exchange(createURLWithPort("/games/" + gameId), HttpMethod.GET, null, GameResource.class);

        GameResource gameResource = response.getBody();

        validateResourceEqualsModel(gameResource, game);
        
    }

    @Test
    public void postNewGame() {
        GameCreateResource body = new GameCreateResource();
        body.setName("Battle Royale");
        HttpEntity<GameCreateResource> entity = new HttpEntity<GameCreateResource>(body, headers);

        ResponseEntity<GameResource> response =
            restTemplate.exchange(createURLWithPort("/games/"), HttpMethod.POST, entity, GameResource.class);

        GameResource gameResource = response.getBody();
        assertThat(gameResource, notNullValue());
        assertThat(gameResource.getObjectId(), notNullValue());

        Game game = gameService.getGame(gameResource.getObjectId());

        validateResourceEqualsModel(gameResource, game);

    }

    @Test
    public void deleteGame() {
        Long gameId = gameService.createGame("Battle Royale");
        Game game = gameService.getGame(gameId);

        assertThat(game, notNullValue());

        restTemplate.exchange(createURLWithPort("/games/" + gameId), HttpMethod.DELETE, null, Object.class);

        game = gameService.getGame(gameId);

        assertThat(game, nullValue());
    }

    @Test
    public void post1DeckToGame() {
        Long gameId = gameService.createGame("Battle Royale");
        Long deckId = deckService.createDeck(DeckType.STANDARD);

        GameAddDeckResource body = new GameAddDeckResource();
        body.setObjectId(deckId);
        HttpEntity<GameAddDeckResource> entity = new HttpEntity<GameAddDeckResource>(body, headers);

        ResponseEntity<GameResource> response =
            restTemplate.exchange(createURLWithPort("/games/" + gameId + "/decks"), HttpMethod.POST, entity, GameResource.class);

        GameResource gameResource = response.getBody();
        Game game = gameService.getGame(gameResource.getObjectId());

        assertThat(game.getDecks().size(), is(1));
        assertThat(game.getDecks().get(0).getId(), is(deckId));
        validateResourceEqualsModel(gameResource, game);
    }

    @Test
    public void postExistingDeckToGameFails() {
        Long gameId = gameService.createGame("Battle Royale");
        Long deckId = deckService.createDeck(DeckType.STANDARD);

        GameAddDeckResource body = new GameAddDeckResource();
        body.setObjectId(deckId);
        HttpEntity<GameAddDeckResource> entity = new HttpEntity<GameAddDeckResource>(body, headers);

        ResponseEntity<String> response =
            restTemplate.exchange(createURLWithPort("/games/" + gameId + "/decks"), HttpMethod.POST, entity, String.class);

        response =
            restTemplate.exchange(createURLWithPort("/games/" + gameId + "/decks"), HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    private void validateResourceEqualsModel(GameResource gameResource, Game game) {
        assertThat(gameResource.getObjectId(), is(game.getId()));
        assertThat(gameResource.getName(), is(game.getName()));
        assertThat(gameResource.getShoe().getCards().size(), is(game.getShoe().getCards().size()));

        game.getShoe().getCards().forEach(c -> {
            if (gameResource.getShoe().getCards().stream().noneMatch(s -> cardAndResourceEqual(c, s))) {
                fail("Missing card in shoe suit " + c.getCard().getSuit().name() + " value " + c.getCard().getValue().name());
            }
        });
    }

    private boolean cardAndResourceEqual(GameCard card, com.cardmein.drawroyale.game.web.model.Card cardResource) {
        return card.getId().equals(cardResource.getObjectId()) &&
                card.getCard().getSuit().equals(cardResource.getSuit()) &&
                card.getCard().getValue().equals(cardResource.getValue());
    }

}