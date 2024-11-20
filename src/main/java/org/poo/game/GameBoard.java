package org.poo.game;

import java.util.ArrayList;

public class GameBoard {
    private ArrayList<ArrayList<Card>> board;
    private Player player1;
    private Player player2;
    public static final int COLS = 5;
    public static final int ROWS = 4;
    public static final int FRONT_ONE = 2;
    public static final int BACK_ONE = 3;
    public static final int FRONT_TWO = 1;
    public static final int BACK_TWO = 0;


    public GameBoard(final Player player1, final Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new ArrayList<ArrayList<Card>>(ROWS);
        for (int i = 0; i < ROWS; i++) {
            board.add(new ArrayList<Card>(COLS));
        }
    }

    public final ArrayList<ArrayList<Card>> getBoard() {
        return board;
    }


    public final Player getPlayer1() {
        return player1;
    }

    public final Player getPlayer2() {
        return player2;
    }

    public final void setBoard(final ArrayList<ArrayList<Card>> board) {
        this.board = board;
    }

    public final void setPlayer1(final Player player1) {
        this.player1 = player1;
    }

    public final void setPlayer2(final Player player2) {
        this.player2 = player2;
    }

    /**
     * Method that place a card on the table
     * @param playerIdx the index of the current player
     * @param cardIdx the index of the card that
     *                will be placed on the table
     * @return error messages (if it is an error),
     *         or null
     */
    public final String placeCard(final int playerIdx, final int cardIdx) {
        Player currentPlayer;
        int front;
        int back;
        if (playerIdx == 1) {
            currentPlayer = player1;
            front = FRONT_ONE;
            back = BACK_ONE;

        } else {
            currentPlayer = player2;
            front = FRONT_TWO;
            back = BACK_TWO;
        }
        if (currentPlayer.isPlayerTurn()) {
            if (currentPlayer.getMana() >= currentPlayer.getHand().get(cardIdx).getMana()) {
                Card card = currentPlayer.getHand().get(cardIdx);
                if (card.isFrontRowCard()) {
                    if (board.get(front).size() < COLS) {
                        board.get(front).add(card);
                        currentPlayer.setMana(currentPlayer.getMana() - card.getMana());
                        currentPlayer.getHand().remove(card);
                    } else {
                        return "Cannot place card on table since row is full.";
                    }
                } else {
                    if (board.get(back).size() < COLS) {
                        board.get(back).add(card);
                        currentPlayer.setMana(currentPlayer.getMana() - card.getMana());
                        currentPlayer.getHand().remove(card);
                    } else {
                        return "Cannot place card on table since row is full.";
                    }
                }
            } else {
                return "Not enough mana to place card on table.";
            }
        }
        return null;
    }

    /**
     * Method that performs the attack of the card
     * @param xAttacker the "x" coordinate of the attacker card
     * @param yAttacker the "y" coordinate of the attacker card
     * @param xAttacked the "x" coordinate of the attacked card
     * @param yAttacked the "y" coordinate of the attacked card
     * @return error messages (if they exist), or null
     */
    final String cardAttack(final int xAttacker,
                            final int yAttacker,
                            final int xAttacked,
                            final int yAttacked) {
        boolean tank = false;
        if (xAttacker == BACK_TWO || xAttacker == FRONT_TWO) {
            if (xAttacked == BACK_TWO || xAttacked == FRONT_TWO) {
                return "Attacked card does not belong to the enemy.";
            }
            for (Card card: board.get(FRONT_ONE)) {
                if (card.isTank()) {
                    tank = true;
                }
            }
        }
        if (xAttacker == FRONT_ONE || xAttacker == BACK_ONE) {
            if (xAttacked == FRONT_ONE || xAttacked == BACK_ONE) {
                return "Attacked card does not belong to the enemy.";
            }
            for (Card card: board.get(FRONT_TWO)) {
                if (card.isTank()) {
                    tank = true;
                }
            }
        }
        Card attacker = board.get(xAttacker).get(yAttacker);
        Card attacked = board.get(xAttacked).get(yAttacked);

        if (attacker.isUsed()) {
            return "Attacker card has already attacked this turn.";
        }
        if (attacker.isFrozen()) {
            return "Attacker card is frozen.";
        }
        if (tank) {
            if (!attacked.isTank()) {
                return "Attacked card is not of type 'Tank'.";
            }
        }
        attacker.setUsed(true);
        attacked.setHealth(attacked.getHealth() - attacker.getAttackDamage());
        if (attacked.getHealth() <= 0) {
            board.get(xAttacked).remove(attacked);
        }
        return null;
    }

    /**
     * Method that checks for the existence of a card at position (x, y)
     * @param x the "x" coordinate of the card
     * @param y the "y" coordinate of the card
     * @return an error (if the card doesn't exist), or null
     */
    final String getCardAtPosition(final int x, final int y) {
        if (y >= board.get(x).size()) {
            return "No card available at that position.";
        }
        return null;
    }

    /**
     * Method that performs an ability of a card
     * @param xAttacker the "x" coordinate of the attacker card
     * @param yAttacker the "y" coordinate of the attacker card
     * @param xAttacked the "x" coordinate of the attacked card
     * @param yAttacked the "y" coordinate of the attacked card
     * @return error message (if any), or null
     */
    final String useAbility(final int xAttacker,
                            final int yAttacker,
                            final int xAttacked, final
                            int yAttacked) {
        boolean tank = false;
        Card attackerCard = board.get(xAttacker).get(yAttacker);
        Card attackedCard = board.get(xAttacked).get(yAttacked);
        if (attackerCard.isFrozen()) {
            return "Attacker card is frozen.";
        }
        if (attackerCard.isUsed()) {
            return "Attacker card has already attacked this turn.";
        }
        if (attackerCard.getName().equals("Disciple")) {
            if (xAttacker == BACK_TWO) {
                if (xAttacked == BACK_TWO || xAttacked == FRONT_TWO) {
                    attackedCard.setHealth(attackedCard.getHealth() + 2);
                } else {
                    return "Attacked card does not belong to the current player.";
                }
            }
            if (xAttacker == BACK_ONE) {
                if (xAttacked == FRONT_ONE || xAttacked == BACK_ONE) {
                    attackedCard.setHealth(attackedCard.getHealth() + 2);
                } else {
                    return "Attacked card does not belong to the current player.";
                }
            }
        }
        if (attackerCard.getName().equals("The Ripper")
                || attackerCard.getName().equals("Miraj")
                || attackerCard.getName().equals("The Cursed One")) {
            if (xAttacker == BACK_TWO || xAttacker == FRONT_TWO) {
                if (xAttacked == BACK_TWO || xAttacked == FRONT_TWO) {
                    return "Attacked card does not belong to the enemy.";
                }
                for (Card card: board.get(2)) {
                    if (card.isTank()) {
                        tank = true;
                    }
                }
            }
            if (xAttacker == FRONT_ONE || xAttacker == BACK_ONE) {
                if (xAttacked == FRONT_ONE || xAttacked == BACK_ONE) {
                    return "Attacked card does not belong to the enemy.";
                }
                for (Card card: board.get(FRONT_TWO)) {
                    if (card.isTank()) {
                        tank = true;
                    }
                }
            }
            if (tank) {
                if (!attackedCard.isTank()) {
                    return "Attacked card is not of type 'Tank'.";
                }
            }
            switch (attackerCard.getName()) {
                case "The Ripper":
                    if (attackedCard.getAttackDamage() <= 2) {
                        attackedCard.setAttackDamage(0);
                    } else {
                        attackedCard.setAttackDamage(attackedCard.getAttackDamage() - 2);
                    }
                    break;
                case "Miraj":
                    int tmp;
                    tmp = attackedCard.getHealth();
                    attackedCard.setHealth(attackerCard.getHealth());
                    attackerCard.setHealth(tmp);
                    break;
                case "The Cursed One":
                    int aux;
                    aux = attackedCard.getAttackDamage();
                    attackedCard.setAttackDamage(attackedCard.getHealth());
                    attackedCard.setHealth(aux);
                    if (attackedCard.getHealth() <= 0) {
                        board.get(xAttacked).remove(attackedCard);
                    }
                    break;
                default:
                    break;
            }
        }
        attackerCard.setUsed(true);
        return null;
    }

    /**
     * Method that performs the attack of the card against a hero
     * @param x the "x" coordinate of the attacker card
     * @param y the "y" coordinate of the attacker card
     * @return error messages (if any), or null
     */
    public final String cardAttacksHero(final int x, final int y) {
        boolean tank = false;
        Hero heroAttacked = null;
        Card card = board.get(x).get(y);
        if (card.isFrozen()) {
            return "Attacker card is frozen.";
        }
        if (card.isUsed()) {
            return "Attacker card has already attacked this turn.";
        }
        if (x == BACK_TWO || x == FRONT_TWO) {
            heroAttacked = player1.getHero();
            for (Card c: board.get(FRONT_ONE)) {
                if (c.isTank()) {
                    tank = true;
                    break;
                }
            }
        }
        if (x == BACK_ONE || x == FRONT_ONE) {
            heroAttacked = player2.getHero();
            for (Card c: board.get(FRONT_TWO)) {
                if (c.isTank()) {
                    tank = true;
                    break;
                }
            }
        }
        if (tank) {
            return "Attacked card is not of type 'Tank'.";
        }
        int newHealth = heroAttacked.getHealth() - card.getAttackDamage();
        heroAttacked.setHealth(newHealth);
        if (!heroAttacked.isAlive()) {
            player1.getStats().increaseTotalGamesPlayed();
            player2.getStats().increaseTotalGamesPlayed();
            switch (x) {
                case BACK_TWO:
                    player2.getStats().increaseWins();
                    return "Player two killed the enemy hero.";
                case FRONT_TWO:
                    player2.getStats().increaseWins();
                    return "Player two killed the enemy hero.";
                case FRONT_ONE:
                    player1.getStats().increaseWins();
                    return "Player one killed the enemy hero.";
                case BACK_ONE:
                    player1.getStats().increaseWins();
                    return "Player one killed the enemy hero.";
                default:
                    break;
            }
        }
        card.setUsed(true);
        return null;
    }

    /**
     * Method that performs subZero ability
     * @param affectedRow the affected row by the ability
     */
   private void subZero(final int affectedRow) {
        for (Card card: board.get(affectedRow)) {
            card.setFrozen(true);
        }
    }
    /**
     * Method that performs lowBlow ability
     * @param affectedRow the affected row by the ability
     */
    private void lowBlow(final int affectedRow) {
        Card affectedCard = null;
        int maxHealth = -1;
        for (Card card: board.get(affectedRow)) {
            if (card.getHealth() > maxHealth) {
                affectedCard = card;
                maxHealth = card.getHealth();
            }
        }
        board.get(affectedRow).remove(affectedCard);
    }

    /**
     * Method that performs earthBorn ability
     * @param affectedRow the affected row by the ability
     */
    private void earthBorn(final int affectedRow) {
        for (Card card: board.get(affectedRow)) {
            card.setHealth(card.getHealth() + 1);
        }
    }

    /**
     * Method that performs bloodThirst ability
     * @param affectedRow the affected row by the ability
     */

    private void bloodThirst(final int affectedRow) {
        for (Card card: board.get(affectedRow)) {
            card.setAttackDamage(card.getAttackDamage() + 1);
        }
    }

    /**
     * Method that performs every hero's ability
     * @param affectedRow the affected row by the ability
     * @return error messages (if any), or null
     */
    public final String heroAbility(final int affectedRow) {
        int playerIdx = getTurn();
        Player currentPlayer = null;
        Hero hero = null;
        switch (playerIdx) {
            case 1:
                currentPlayer = player1;
                hero = player1.getHero();
                break;
            case 2:
                currentPlayer = player2;
                hero = player2.getHero();
                break;
            default:
                break;
        }
        if (hero.getMana() > currentPlayer.getMana()) {
            return "Not enough mana to use hero's ability.";
        }
        if (hero.isUsed()) {
            return "Hero has already attacked this turn.";
        }
        if (hero.getName().equals("Lord Royce")
                || hero.getName().equals("Empress Thorina")) {
            switch (playerIdx) {
                case 1:
                    if (affectedRow == FRONT_ONE || affectedRow == BACK_ONE) {
                        return "Selected row does not belong to the enemy.";
                    } else  {
                        break;
                    }
                case 2:
                    if (affectedRow == FRONT_TWO || affectedRow == BACK_TWO) {
                        return "Selected row does not belong to the enemy.";
                    } else {
                        break;
                    }
                default:
                    break;
            }
        }
        if (hero.getName().equals("General Kocioraw")
                || hero.getName().equals("King Mudface")) {
            switch (playerIdx) {
                case 1:
                    if (affectedRow == BACK_TWO || affectedRow == FRONT_TWO) {
                        return "Selected row does not belong to the current player.";
                    } else {
                        break;
                    }
                case 2:
                    if (affectedRow == FRONT_ONE || affectedRow == BACK_ONE) {
                        return "Selected row does not belong to the current player.";
                    } else {
                        break;
                    }
                default:
                    break;
            }
        }

        switch (hero.getName()) {
            case "Lord Royce":
                subZero(affectedRow);
                break;
            case "Empress Thorina":
                lowBlow(affectedRow);
                break;
            case "King Mudface":
                earthBorn(affectedRow);
                break;
            case "General Kocioraw":
                bloodThirst(affectedRow);
                break;
            default:
                break;
        }
        hero.setUsed(true);
        currentPlayer.setMana(currentPlayer.getMana() - hero.getMana());
        return null;
    }



   public final int getTurn() {
        return (player1.isPlayerTurn() && !player2.isPlayerTurn()) ? 1 : 2;
    }

}
