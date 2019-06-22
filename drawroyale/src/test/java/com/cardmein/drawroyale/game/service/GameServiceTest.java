package com.cardmein.drawroyale.game.service;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.persistence.GameRepository;

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

}