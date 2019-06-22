package com.cardmein.drawroyale.game.web.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import com.cardmein.drawroyale.game.model.DeckType;
import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.model.GameCard;
import com.cardmein.drawroyale.game.model.PlayerGame;
import com.cardmein.drawroyale.game.model.ShoeState;
import com.cardmein.drawroyale.game.service.DeckService;
import com.cardmein.drawroyale.game.service.GameService;
import com.cardmein.drawroyale.game.service.PlayerService;
import com.cardmein.drawroyale.game.web.model.Card;
import com.cardmein.drawroyale.game.web.model.GameAddDeckResource;
import com.cardmein.drawroyale.game.web.model.GameAddPlayerResource;
import com.cardmein.drawroyale.game.web.model.GameCreateResource;
import com.cardmein.drawroyale.game.web.model.GameResource;
import com.cardmein.drawroyale.game.web.model.PlayerGameResource;
import com.cardmein.drawroyale.game.web.model.ShoeResource;

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

    @Autowired
    private PlayerService playerService;

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

    @Test
    public void postShoeShuffleStateChangesDeckOrder() {
        Long gameId = gameService.createGame("Battle Royale");
        Long deckId = deckService.createDeck(DeckType.STANDARD);

        GameAddDeckResource body = new GameAddDeckResource();
        body.setObjectId(deckId);
        HttpEntity<GameAddDeckResource> addDeckEntity = new HttpEntity<GameAddDeckResource>(body, headers);

        ResponseEntity<GameResource> addDeckResponse =
            restTemplate.exchange(createURLWithPort("/games/" + gameId + "/decks"), HttpMethod.POST, addDeckEntity, GameResource.class);

        ShoeResource shoe = addDeckResponse.getBody().getShoe();
        long beforeShuffleHash = shoe.getCards().stream()
                .map(Card::getObjectId)
                .reduce((x, y) -> x * 31l + y)
                .get();

        HttpEntity<String> shuffleEntity = new HttpEntity<String>(ShoeState.SHUFFLING.name(), headers);
        ResponseEntity<ShoeResource> shuffleResponse =
            restTemplate.exchange(createURLWithPort("/games/" + gameId + "/shoe/state"), HttpMethod.PUT, shuffleEntity, ShoeResource.class);
        
        assertThat(shuffleResponse.getStatusCode(), is(HttpStatus.OK));

        shoe = shuffleResponse.getBody();

        long afterShuffleHash = shoe.getCards().stream()
                .map(Card::getObjectId)
                .reduce((x, y) -> x * 31l + y)
                .get();

        assertThat(afterShuffleHash, not(beforeShuffleHash));

    }

    @Test
    public void post1PlayerToGame() {
        Long gameId = gameService.createGame("Battle Royale");
        Long playerId = playerService.createPlayer("Bob");

        GameAddPlayerResource body = new GameAddPlayerResource();
        body.setPlayerId(playerId);
        HttpEntity<GameAddPlayerResource> entity = new HttpEntity<GameAddPlayerResource>(body, headers);

        ResponseEntity<PlayerGameResource> response =
            restTemplate.exchange(createURLWithPort("/games/" + gameId + "/players"), HttpMethod.POST, entity, PlayerGameResource.class);

        PlayerGameResource playerGameResource = response.getBody();
        PlayerGame playerGame = gameService.getPlayerGame(playerGameResource.getObjectId());
        List<PlayerGame> playerGames = gameService.getAllPlayers(gameId);

        assertThat(playerGames.size(), is(1));
        assertThat(playerGames.get(0).getPlayer().getId(), is(playerId));
        validateResourceEqualsModel(playerGameResource, playerGame);

    }

    @Test
    public void postExistingPlayerToGameFails() {
        Long gameId = gameService.createGame("Battle Royale");
        Long playerId = playerService.createPlayer("Bob");

        GameAddPlayerResource body = new GameAddPlayerResource();
        body.setPlayerId(playerId);
        HttpEntity<GameAddPlayerResource> entity = new HttpEntity<GameAddPlayerResource>(body, headers);

        ResponseEntity<String> response =
            restTemplate.exchange(createURLWithPort("/games/" + gameId + "/players"), HttpMethod.POST, entity, String.class);

        response =
            restTemplate.exchange(createURLWithPort("/games/" + gameId + "/players"), HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        
    }

    @Test
    public void deleteExistingPlayerFromGame() throws Exception {
        Long gameId = gameService.createGame("Battle Royale");
        Long playerId = playerService.createPlayer("Bob");

        PlayerGame playerGame = gameService.addPlayer(gameId, playerId);
        
        ResponseEntity<GameResource> response =
            restTemplate.exchange(createURLWithPort("/games/" + gameId + "/players/" + playerGame.getId()), HttpMethod.DELETE, null, GameResource.class);

        playerGame = gameService.getPlayerGame(playerGame.getId());

        assertThat(playerGame, nullValue());
    }

    @Test
    public void deleteInexistingPlayerFromGameFails() throws Exception {
        Long gameId = gameService.createGame("Battle Royale");
        Long playerId = playerService.createPlayer("Bob");

        ResponseEntity<String> response =
            restTemplate.exchange(createURLWithPort("/games/" + gameId + "/players/1"), HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));

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

    private void validateResourceEqualsModel(PlayerGameResource playerGameResource, PlayerGame playerGame) {
        assertThat(playerGameResource.getObjectId(), is(playerGame.getId()));
        assertThat(playerGameResource.getName(), is(playerGame.getPlayer().getName()));
    }

    private boolean cardAndResourceEqual(GameCard card, com.cardmein.drawroyale.game.web.model.Card cardResource) {
        return card.getId().equals(cardResource.getObjectId()) &&
                card.getCard().getSuit().equals(cardResource.getSuit()) &&
                card.getCard().getValue().equals(cardResource.getValue());
    }

}