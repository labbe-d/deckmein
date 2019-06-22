package com.cardmein.drawroyale.game.service;

import java.util.ArrayList;
import java.util.List;

import com.cardmein.drawroyale.game.model.Card;
import com.cardmein.drawroyale.game.model.CardSuit;
import com.cardmein.drawroyale.game.model.CardValue;

import org.springframework.stereotype.Component;

/**
 * Deck building implementation for a standard deck of 52 cards of 4 suits and 13 faces from Ace to King.
 */
@Component
public class StandardDeckBuilder implements DeckBuilder {

    @Override
    public List<Card> buildDeck() {
        List<Card> deck = new ArrayList<Card>();
        for (CardSuit suit : CardSuit.values()) {
            for (CardValue value : CardValue.values()) {
                deck.add(new Card(suit, value));
            }
        }
        return deck;
    }
}