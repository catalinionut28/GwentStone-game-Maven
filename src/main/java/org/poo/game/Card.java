package org.poo.game;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import org.poo.fileio.*;

public class Card {
    private final int mana;
    private int health;
    private int attackDamage;
    private final String description;
    private ArrayList<String> colors;
    public final String name;
    protected boolean frozen;
    private boolean used;

    public Card(CardInput card) {
        this.mana = card.getMana();
        this.attackDamage = card.getAttackDamage();
        this.health = card.getHealth();
        this.description = card.getDescription();
        this.colors = card.getColors();
        this.name = card.getName();
        this.frozen = false;
        this.used = false;

    }
    public Card(int mana, int health, int attackDamage,
         String description, ArrayList<String> colors, String name) {
        this.mana = mana;
        this.health = health;
        this.attackDamage = attackDamage;
        this.description = description;
        this.colors = colors;
        this.name = name;
        frozen = false;
        used = false;
    }

    public int getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public int getMana() {
        return mana;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public boolean isFrontRowCard() {
        return (name.equals("Warden") || name.equals("Goliath") ||
                name.equals("The Ripper") || name.equals("Miraj"));
    }

    public boolean isTank() {
        return (name.equals("Warden") || name.equals("Goliath"));
    }
}

class Hero extends Card {

    // A hero cannot be frozen, so it is initialized to false and remains final
    private final boolean frozen;

    public Hero(int mana, int health, int attackDamage, String description, ArrayList<String> colors, String name) {
        super(mana, 30, 0, description, colors, name);
        this.frozen = false;
        this.setUsed(false);
    }


    public Hero(CardInput card) {
        super(card);
        this.setHealth(30);
        this.frozen = false;
    }

    public boolean isAlive() {
        if (getHealth() > 0) {
            return true;
        }
        return false;
    }

}

class Deck {
    ArrayList<Card> cards;

    Deck(ArrayList<CardInput> cardsInput) {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (CardInput cardInput: cardsInput) {
            cards.add(new Card(cardInput));
        }
        this.cards = cards;
    }

    public void shuffle(long seed) {
        Random random = new Random(seed);
        Collections.shuffle(cards, random);
    }

    public void addCardInDeck(Card card) {
        cards.add(card);
    }

    public void removeCardFromDeck(Card card) {
        cards.remove(card);
    }

    public Card drawCard() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        }
        return null;
    }
    public boolean isEmpty() {
        return cards.isEmpty();
    }



}

