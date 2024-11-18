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
public class Player {
    private Stats stats;
    private int mana;
    private Hero hero;
    private ArrayList<Card> hand;
    private Deck deck;
    boolean turn;

    Player() {
        stats = new Stats();
        mana = 0;
        this.hand = new ArrayList<Card>();
        turn = false;

    }


    Player(Hero hero, Deck deck) {
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

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void takeCardInHand() {
        if (deck.isEmpty()) {
            return;
        }
        hand.add(deck.drawCard());
    }

    public Card getHandCardIdx(int idx) {
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

    boolean isPlayerTurn() {
        return turn;
    }
    void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }
    public void resetPlayer() {
        this.mana = 0;
        this.hand.clear();
        this.turn = false;

    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }


}

