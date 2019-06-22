package com.cardmein.drawroyale.game.persistence;

import java.util.ArrayList;
import java.util.List;

import com.cardmein.drawroyale.game.model.GameCard;

import org.springframework.stereotype.Service;

/**
 * Handles persistence of cards used in games in memory.
 */
@Service
public class GameCardRepository {

    private static Long GAME_CARD_ID_SEQ = 0L;

    private List<GameCard> gameCards = new ArrayList<>();

    /**
     * Persist a game card in memory
     * @param gameCard Game card to persist
     * @return Persisted game card
     */
    public GameCard create(GameCard gameCard) {
        if (gameCard.getId() == null) {
            gameCard.setId(++GAME_CARD_ID_SEQ);
        }

        gameCards.add(gameCard);

        return gameCard;
    }

}
