package com.cardmein.drawroyale.game.service;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.cardmein.drawroyale.game.model.Player;
import com.cardmein.drawroyale.game.persistence.PlayerRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlayerServiceTest {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    public void createdPlayerPersistedInRepository() {
        Long playerId = playerService.createPlayer("Bob");
        Player player = playerRepository.find(playerId);

        assertThat(player, notNullValue());
        assertThat(player.getId(), is(playerId));
        assertThat(player.getName(), is("Bob"));
    }

    @Test
    public void createdPlayerIsRetrievable() {
        Long playerId = playerService.createPlayer("Bob");
        Player player = playerService.getPlayer(playerId);

        assertThat(player, notNullValue());
        assertThat(player.getId(), is(playerId));
        assertThat(player.getName(), is("Bob"));
    }

}