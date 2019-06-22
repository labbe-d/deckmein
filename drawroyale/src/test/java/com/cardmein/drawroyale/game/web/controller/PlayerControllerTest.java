package com.cardmein.drawroyale.game.web.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.cardmein.drawroyale.game.model.Player;
import com.cardmein.drawroyale.game.service.PlayerService;
import com.cardmein.drawroyale.game.web.model.PlayerCreateResource;
import com.cardmein.drawroyale.game.web.model.PlayerResource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class PlayerControllerTest extends BaseControllerTest {

    @Autowired
    private PlayerService playerService;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Test
    public void getExistingPlayer() {
        Long playerId = playerService.createPlayer("Bob");
        Player player = playerService.getPlayer(playerId);

        ResponseEntity<PlayerResource> response =
            restTemplate.exchange(createURLWithPort("/players/" + playerId), HttpMethod.GET, null, PlayerResource.class);

        PlayerResource playerResource = response.getBody();

        validateResourceEqualsModel(playerResource, player);
        
    }

    @Test
    public void postStandardDeck() {
        PlayerCreateResource body = new PlayerCreateResource();
        body.setName("Bob");
        HttpEntity<PlayerCreateResource> entity = new HttpEntity<PlayerCreateResource>(body, headers);

        ResponseEntity<PlayerResource> response =
            restTemplate.exchange(createURLWithPort("/players/"), HttpMethod.POST, entity, PlayerResource.class);

        PlayerResource playerResource = response.getBody();
        assertThat(playerResource, notNullValue());
        assertThat(playerResource.getObjectId(), notNullValue());

        Player player = playerService.getPlayer(playerResource.getObjectId());

        validateResourceEqualsModel(playerResource, player);

    }

    private void validateResourceEqualsModel(PlayerResource playerResource, Player player) {
        assertThat(playerResource.getObjectId(), is(player.getId()));
        assertThat(playerResource.getName(), is(player.getName()));

    }

}