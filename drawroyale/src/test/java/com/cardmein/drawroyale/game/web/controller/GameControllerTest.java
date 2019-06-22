package com.cardmein.drawroyale.game.web.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.cardmein.drawroyale.game.model.CardSuit;
import com.cardmein.drawroyale.game.model.CardValue;
import com.cardmein.drawroyale.game.model.DeckType;
import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.model.GameCard;
import com.cardmein.drawroyale.game.model.PlayerGame;
import com.cardmein.drawroyale.game.model.PlayerGameState;
import com.cardmein.drawroyale.game.model.Shoe;
import com.cardmein.drawroyale.game.model.ShoeState;
import com.cardmein.drawroyale.game.service.DeckService;
import com.cardmein.drawroyale.game.service.GameService;
import com.cardmein.drawroyale.game.service.PlayerService;
import com.cardmein.drawroyale.game.web.model.Card;
import com.cardmein.drawroyale.game.web.model.GameAddDeckResource;
import com.cardmein.drawroyale.game.web.model.GameAddPlayerResource;
import com.cardmein.drawroyale.game.web.model.GameCreateResource;
import com.cardmein.drawroyale.game.web.model.GameLeaderboardResource;
import com.cardmein.drawroyale.game.web.model.GameResource;
import com.cardmein.drawroyale.game.web.model.LeaderboardPlayerGameResource;
import com.cardmein.drawroyale.game.web.model.PlayerGameResource;
import com.cardmein.drawroyale.game.web.model.PlayerHandResource;
import com.cardmein.drawroyale.game.web.model.ShoeCardCount;
import com.cardmein.drawroyale.game.web.model.ShoeCardCountStatsResource;
import com.cardmein.drawroyale.game.web.model.ShoeResource;
import com.cardmein.drawroyale.game.web.model.ShoeSuitCount;
import com.cardmein.drawroyale.game.web.model.ShoeSuitStatsResource;

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
    public void putShoeShuffleStateChangesDeckOrder() {
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

    @Test
    public void putPlayerGameDrawingCardStateDrawsCardFromShoe() throws Exception {
        Long gameId = gameService.createGame("Battle Royale");
        Long playerId = playerService.createPlayer("Bob");
        Long deckId = deckService.createDeck(DeckType.STANDARD);

        PlayerGame playerGame = gameService.addPlayer(gameId, playerId);
        gameService.addDeck(gameId, deckId);

        HttpEntity<String> drawingEntity = new HttpEntity<String>(PlayerGameState.DRAWING_CARD.name(), headers);
        ResponseEntity<PlayerGameResource> drawingResponse =
            restTemplate.exchange(createURLWithPort("/games/" + gameId + "/players/" + playerGame.getId() + "/state"), HttpMethod.PUT, drawingEntity, PlayerGameResource.class);

        Game game = gameService.getGame(gameId);
        Shoe shoe = game.getShoe();

        assertThat(drawingResponse.getBody().getHand().size(), is(1));
        assertThat(shoe.getCards().size(), is(51));
    }

    @Test
    public void putPlayerGameDrawingCardStateWithEmptyShoeFails() throws Exception {
        Long gameId = gameService.createGame("Battle Royale");
        Long playerId = playerService.createPlayer("Bob");
        Long deckId = deckService.createDeck(DeckType.STANDARD);

        PlayerGame playerGame = gameService.addPlayer(gameId, playerId);
        gameService.addDeck(gameId, deckId);

        Game game = gameService.getGame(gameId);
        Shoe shoe = game.getShoe();

        int totalCards = shoe.getCards().size();

        HttpEntity<String> drawingEntity = null;
        ResponseEntity<PlayerGameResource> drawingResponse = null;
        for (int i = 0; i < totalCards; i++) {
            drawingEntity = new HttpEntity<String>(PlayerGameState.DRAWING_CARD.name(), headers);
            drawingResponse =
                restTemplate.exchange(createURLWithPort("/games/" + gameId + "/players/" + playerGame.getId() + "/state"), HttpMethod.PUT, drawingEntity, PlayerGameResource.class);
    
        }

        game = gameService.getGame(gameId);
        shoe = game.getShoe();

        assertThat(drawingResponse.getBody().getHand().size(), is(totalCards));
        assertThat(shoe.getCards().size(), is(0));

        drawingEntity = new HttpEntity<String>(PlayerGameState.DRAWING_CARD.name(), headers);
        ResponseEntity<String> emptyDrawingResponse =
            restTemplate.exchange(createURLWithPort("/games/" + gameId + "/players/" + playerGame.getId() + "/state"), HttpMethod.PUT, drawingEntity, String.class);

        assertThat(emptyDrawingResponse.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void getPlayerHand() throws Exception {
        Long gameId = gameService.createGame("Battle Royale");
        Long playerId = playerService.createPlayer("Bob");
        Long deckId = deckService.createDeck(DeckType.STANDARD);

        PlayerGame playerGame = gameService.addPlayer(gameId, playerId);
        gameService.addDeck(gameId, deckId);

        playerGame = gameService.drawPlayerCard(playerGame.getId());
        playerGame = gameService.drawPlayerCard(playerGame.getId());

        ResponseEntity<PlayerHandResource> response =
                restTemplate.exchange(createURLWithPort("/games/" + gameId + "/players/" + playerGame.getId() + "/hand"),
                        HttpMethod.GET, null, PlayerHandResource.class);

        PlayerHandResource hand = response.getBody();

        // By default, standard deck will generate clubs first starting with 2 and 3
        assertThat(hand.getCards().size(), is(2));
        assertThat(hand.getCards().get(0).getSuit(), is(CardSuit.CLUB));
        assertThat(hand.getCards().get(0).getValue(), is(CardValue.TWO));
        assertThat(hand.getCards().get(1).getSuit(), is(CardSuit.CLUB));
        assertThat(hand.getCards().get(1).getValue(), is(CardValue.THREE));
    }

    @Test
    public void getLeaderboard() throws Exception {
        // Generate a 2 player game with player 1 drawing 2 and 3 of clubs, then player 2 drawing 4 and 5 of clubs
        // Player 2 is ranked first with 9 points and player 1 is secon with 5 points
        Long gameId = gameService.createGame("Battle Royale");
        Long player1Id = playerService.createPlayer("Bob");
        Long player2Id = playerService.createPlayer("Jim");
        Long deckId = deckService.createDeck(DeckType.STANDARD);

        PlayerGame playerGame1 = gameService.addPlayer(gameId, player1Id);
        PlayerGame playerGame2 = gameService.addPlayer(gameId, player2Id);
        gameService.addDeck(gameId, deckId);

        playerGame1 = gameService.drawPlayerCard(playerGame1.getId());
        playerGame1 = gameService.drawPlayerCard(playerGame1.getId());

        playerGame2 = gameService.drawPlayerCard(playerGame2.getId());
        playerGame2 = gameService.drawPlayerCard(playerGame2.getId());

        ResponseEntity<GameLeaderboardResource> response =
            restTemplate.exchange(createURLWithPort("/games/" + gameId + "/leaderboard"), HttpMethod.GET, null, GameLeaderboardResource.class);

        GameLeaderboardResource leaderboardResource = response.getBody();

        LeaderboardPlayerGameResource rank1Player = leaderboardResource.getLeaderboard().get(0);
        LeaderboardPlayerGameResource rank2Player = leaderboardResource.getLeaderboard().get(1);
        assertThat(rank1Player.getObjectId(), is(playerGame2.getId()));
        assertThat(rank1Player.getScore(), is(9));
        assertThat(rank1Player.getName(), is ("Jim"));

        assertThat(rank2Player.getObjectId(), is(playerGame1.getId()));
        assertThat(rank2Player.getScore(), is(5));
        assertThat(rank2Player.getName(), is ("Bob"));
    }

    @Test
    public void getShoeStatsRemainingSuits() throws Exception {
        // Deal the 2 and 3 of clubs to the player
        Long gameId = gameService.createGame("Battle Royale");
        Long playerId = playerService.createPlayer("Bob");
        Long deckId = deckService.createDeck(DeckType.STANDARD);

        PlayerGame playerGame = gameService.addPlayer(gameId, playerId);
        gameService.addDeck(gameId, deckId);

        playerGame = gameService.drawPlayerCard(playerGame.getId());
        playerGame = gameService.drawPlayerCard(playerGame.getId());

        ResponseEntity<ShoeSuitStatsResource> response =
                restTemplate.exchange(createURLWithPort("/games/" + gameId + "/shoe/stats/suits"),
                        HttpMethod.GET, null, ShoeSuitStatsResource.class);

        ShoeSuitStatsResource stats = response.getBody();
        
        boolean clubExists = false;
        boolean diamondExists = false;
        boolean heartExists = false;
        boolean spadeExists = false;
        for (ShoeSuitCount count : stats.getSuitCounts()) {
            if (CardSuit.CLUB.equals(count.getSuit())) {
                assertThat(count.getCount(), is(11));
                clubExists = true;

            } else if (CardSuit.DIAMOND.equals(count.getSuit())) {
                assertThat(count.getCount(), is(13));
                diamondExists = true;

            } else if (CardSuit.HEART.equals(count.getSuit())) {
                assertThat(count.getCount(), is(13));
                heartExists = true;
                
            } else if (CardSuit.SPADE.equals(count.getSuit())) {
                assertThat(count.getCount(), is(13));
                spadeExists = true;
            }

        }

        assertTrue(clubExists);
        assertTrue(diamondExists);
        assertTrue(heartExists);
        assertTrue(spadeExists);

    }

    @Test
    public void getShoeStatsRemainingCards() throws Exception {
        // Create a game with 2 decks and deal the 2 and 3 of clubs to the player
        // All cards will have a remaining count of 2 except these 2 cards
        Long gameId = gameService.createGame("Battle Royale");
        Long playerId = playerService.createPlayer("Bob");
        Long deck1Id = deckService.createDeck(DeckType.STANDARD);
        Long deck2Id = deckService.createDeck(DeckType.STANDARD);

        PlayerGame playerGame = gameService.addPlayer(gameId, playerId);
        gameService.addDeck(gameId, deck1Id);
        gameService.addDeck(gameId, deck2Id);

        playerGame = gameService.drawPlayerCard(playerGame.getId());
        playerGame = gameService.drawPlayerCard(playerGame.getId());

        ResponseEntity<ShoeCardCountStatsResource> response =
                restTemplate.exchange(createURLWithPort("/games/" + gameId + "/shoe/stats/cards"),
                        HttpMethod.GET, null, ShoeCardCountStatsResource.class);

        ShoeCardCountStatsResource stats = response.getBody();

        // Order should be Hearts, Spades, Clubs then Diamonds, then sorted from King to Ace(1)
        assertThat(stats.getCardCounts().size(), is(52));
        
        ShoeCardCount stat = stats.getCardCounts().get(0);
        assertThat(stat.getSuit(), is(CardSuit.HEART));
        assertThat(stat.getValue(), is(CardValue.KING));
        assertThat(stat.getCount(), is(2));
        
        stat = stats.getCardCounts().get(2);
        assertThat(stat.getSuit(), is(CardSuit.HEART));
        assertThat(stat.getValue(), is(CardValue.JACK));
        assertThat(stat.getCount(), is(2));
        
        stat = stats.getCardCounts().get(13);
        assertThat(stat.getSuit(), is(CardSuit.SPADE));
        assertThat(stat.getValue(), is(CardValue.KING));
        assertThat(stat.getCount(), is(2));
        
        stat = stats.getCardCounts().get(18);
        assertThat(stat.getSuit(), is(CardSuit.SPADE));
        assertThat(stat.getValue(), is(CardValue.EIGHT));
        assertThat(stat.getCount(), is(2));
        
        stat = stats.getCardCounts().get(36);
        assertThat(stat.getSuit(), is(CardSuit.CLUB));
        assertThat(stat.getValue(), is(CardValue.THREE));
        assertThat(stat.getCount(), is(1));
        
        stat = stats.getCardCounts().get(37);
        assertThat(stat.getSuit(), is(CardSuit.CLUB));
        assertThat(stat.getValue(), is(CardValue.TWO));
        assertThat(stat.getCount(), is(1));

        stat = stats.getCardCounts().get(51);
        assertThat(stat.getSuit(), is(CardSuit.DIAMOND));
        assertThat(stat.getValue(), is(CardValue.ACE));
        assertThat(stat.getCount(), is(2));
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