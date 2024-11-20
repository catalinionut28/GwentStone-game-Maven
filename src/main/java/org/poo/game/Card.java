package org.poo.game;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import org.poo.fileio.CardInput;

public class Card {
    private final int mana;
    private int health;
    private int attackDamage;
    private final String description;
    private ArrayList<String> colors;
    private final String name;
    protected boolean frozen;
    private boolean used;

    public Card(final CardInput card) {
        this.mana = card.getMana();
        this.attackDamage = card.getAttackDamage();
        this.health = card.getHealth();
        this.description = card.getDescription();
        this.colors = card.getColors();
        this.name = card.getName();
        this.frozen = false;
        this.used = false;

    }
    public Card(final int mana, final int health,
                final int attackDamage,
                final String description, final ArrayList<String> colors,
                final String name) {
        this.mana = mana;
        this.health = health;
        this.attackDamage = attackDamage;
        this.description = description;
        this.colors = colors;
        this.name = name;
        frozen = false;
        used = false;
    }

    /**
     * Getter for health field
     * @return the card's health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Getter for the name field
     * @return the card's name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the attackDamage field
     * @return the card's AD
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * Getter for the description field
     * @return the card's description
     */

    public String getDescription() {
        return description;
    }

    /**
     * Getter for the colors field
     * @return the card's colors
     */

    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     * Getter for the mana field
     * @return the card's mana
     */
    public int getMana() {
        return mana;
    }

    /**
     * Setter for the health field
     * @param health the health value to be set for the health field
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     * Setter for the attackDamage field
     * @param attackDamage the AD value to be set for the AD field
     */

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public final boolean isFrozen() {
        return frozen;
    }

    public final boolean isUsed() {
        return used;
    }

    public final void setUsed(final boolean used) {
        this.used = used;
    }

    public final void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }

    public final boolean isFrontRowCard() {
        return (name.equals("Warden")
                || name.equals("Goliath")
                || name.equals("The Ripper")
                || name.equals("Miraj"));
    }

    public final boolean isTank() {
        return (name.equals("Warden") || name.equals("Goliath"));
    }
}

class Hero extends Card {

    // A hero cannot be frozen, so it is initialized to false and remains final
    private final boolean frozen;
    public static final int HERO_HEALTH = 30;

    Hero(final int mana, final int health,
                final int attackDamage,
                final String description,
                final ArrayList<String> colors,
                final String name) {
        super(mana, HERO_HEALTH, 0, description, colors, name);
        this.frozen = false;
        this.setUsed(false);
    }


     Hero(final CardInput card) {
        super(card);
        this.setHealth(HERO_HEALTH);
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
    private final ArrayList<Card> cards;

    Deck(final ArrayList<CardInput> cardsInput) {
        ArrayList<Card> cardsList = new ArrayList<>();
        for (CardInput cardInput: cardsInput) {
            cardsList.add(new Card(cardInput));
        }
        this.cards = cardsList;
    }

    public void shuffle(final long seed) {
        Random random = new Random(seed);
        Collections.shuffle(cards, random);
    }

    public void addCardInDeck(final Card card) {
        cards.add(card);
    }

    public void removeCardFromDeck(final Card card) {
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

    public ArrayList<Card> getCards() {
        return cards;
    }
}

