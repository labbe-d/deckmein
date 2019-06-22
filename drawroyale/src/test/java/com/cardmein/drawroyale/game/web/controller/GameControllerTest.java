package com.cardmein.drawroyale.game.web.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.service.GameService;
import com.cardmein.drawroyale.game.web.model.GameCreateResource;
import com.cardmein.drawroyale.game.web.model.GameResource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class GameControllerTest extends BaseControllerTest {

    @Autowired
    private GameService gameService;

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
        body.setName("Bob");
        HttpEntity<GameCreateResource> entity = new HttpEntity<GameCreateResource>(body, headers);

        ResponseEntity<GameResource> response =
            restTemplate.exchange(createURLWithPort("/games/"), HttpMethod.POST, entity, GameResource.class);

        GameResource gameResource = response.getBody();
        assertThat(gameResource, notNullValue());
        assertThat(gameResource.getObjectId(), notNullValue());

        Game game = gameService.getGame(gameResource.getObjectId());

        validateResourceEqualsModel(gameResource, game);

    }

    private void validateResourceEqualsModel(GameResource gameResource, Game game) {
        assertThat(gameResource.getObjectId(), is(game.getId()));
        assertThat(gameResource.getName(), is(game.getName()));

    }

}