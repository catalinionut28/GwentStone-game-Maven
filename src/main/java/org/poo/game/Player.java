package org.poo.game;

import java.util.ArrayList;

class Stats {
    private int wins;
    private int totalGamesPlayed;

    Stats() {
        wins = 0;
        totalGamesPlayed = 0;
    }

    public int getWins() {
        return wins;
    }

     public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void increaseWins() {
        wins++;
    }

    public void increaseTotalGamesPlayed() {
        totalGamesPlayed++;
    }
}
public final class Player {
    private Stats stats;
    private int mana;
    private Hero hero;
    private ArrayList<Card> hand;
    private Deck deck;
    private boolean turn;

    Player() {
        stats = new Stats();
        mana = 0;
        this.hand = new ArrayList<Card>();
        turn = false;

    }


    Player(final Hero hero, final Deck deck) {
        this.deck = deck;
        this.hero = hero;
        this.mana = 1;
        this.hand = new ArrayList<Card>();
        stats = new Stats();
        this.turn = false;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * method that adds a card to a player's hand
     */
    public void takeCardInHand() {
        if (deck.isEmpty()) {
            return;
        }
        hand.add(deck.drawCard());
    }

    /**
     * Method that returns the card from the hand at the specified index.
     * In case of an empty deck, the function will return null.
     * @param idx the index of the card to return
     * @return the card from the hand at the specified index,
     * or null if the deck is empty"
     */
    public Card getHandCardIdx(final int idx) {
        if (deck.isEmpty()) {
            return null;
        }
        Card card = hand.get(idx);
        return card;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public Deck getDeck() {
        return deck;
    }

    public Hero getHero() {
        return hero;
    }

    public Stats getStats() {
        return stats;
    }

    /**
     * Method that indicates if it is the player's turn
     * @return the turn field
     */
    boolean isPlayerTurn() {
        return turn;
    }
    void setTurn(final boolean turn) {
        this.turn = turn;
    }

    public void setDeck(final Deck deck) {
        this.deck = deck;
    }

    public void setHero(final Hero hero) {
        this.hero = hero;
    }

    public void setHand(final ArrayList<Card> hand) {
        this.hand = hand;
    }

    /**
     * Method that reset the player after the game has ended
     */
    public void resetPlayer() {
        this.mana = 0;
        this.hand.clear();
        this.turn = false;

    }

    public void setStats(final Stats stats) {
        this.stats = stats;
    }


}

