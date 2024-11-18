package org.poo.game;

import java.util.ArrayList;

public class GameBoard {
    private ArrayList<ArrayList<Card>> board;
    private Player player1;
    private Player player2;
    // array that contains the last index available of each row


    public GameBoard(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new ArrayList<ArrayList<Card>>(4);
        for(int i = 0; i < 4; i++) {
            board.add(new ArrayList<Card>(5));
        }


    }

    public ArrayList<ArrayList<Card>> getBoard() {
        return board;
    }


    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setBoard(ArrayList<ArrayList<Card>> board) {
        this.board = board;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public String placeCard(int playerIdx, int cardIdx) {
        Player currentPlayer;
        int front;
        int back;
        if (playerIdx == 1) {
            currentPlayer = player1;
            front = 2;
            back = 3;

        }
        else {
            currentPlayer = player2;
            front = 1;
            back = 0;
        }
        if (currentPlayer.isPlayerTurn()) {
            if (currentPlayer.getMana() >= currentPlayer.getHand().get(cardIdx).getMana()) {
                Card card = currentPlayer.getHand().get(cardIdx);
//                System.out.println(card.getName() + ": " + card.getMana());
//                System.out.println("Player Turn: " + getTurn());
//                System.out.println("Player Mana: " + currentPlayer.getMana());
                if (card.isFrontRowCard()) {
                    if (board.get(front).size() < 5) {
                        board.get(front).add(card);
                        currentPlayer.setMana(currentPlayer.getMana() - card.getMana());
                        currentPlayer.getHand().remove(card);
                    }
                    else return "Cannot place card on table since row is full.";
                }
                else {
                    if (board.get(back).size() < 5) {
                        board.get(back).add(card);
                        currentPlayer.setMana(currentPlayer.getMana() - card.getMana());
                        currentPlayer.getHand().remove(card);
                    }
                    else return "Cannot place card on table since row is full.";
                }
            }
            else return "Not enough mana to place card on table.";

        }
        return null;
    }

    String cardAttack(int xAttacker, int yAttacker, int xAttacked, int yAttacked) {
        boolean tank = false;
        if (xAttacker == 0 || xAttacker == 1) {
            if (xAttacked == 0 || xAttacked == 1) {
                return "Attacked card does not belong to the enemy.";
            }
            for (Card card: board.get(2)) {
                if (card.isTank()) {
                    tank = true;
                }
            }

        }
        if (xAttacker == 2 || xAttacker == 3) {
            if (xAttacked == 2 || xAttacked == 3) {
                return "Attacked card does not belong to the enemy.";
            }
            for (Card card: board.get(1)) {
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
        System.out.println(attacker.getAttackDamage());
        if (attacked.getHealth() <= 0) {
            board.get(xAttacked).remove(attacked);
        }
        return null;
    }

    String getCardAtPosition(int x, int y) {
        if (y >= board.get(x).size()) {
            return "No card available at that position.";
        }
        return null;
    }

    String useAbility(int xAttacker, int yAttacker, int xAttacked, int yAttacked) {
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
            if (xAttacker == 0) {
                if (xAttacked == 0 || xAttacked == 1) {
                    attackedCard.setHealth(attackedCard.getHealth() + 2);
                } else return "Attacked card does not belong to the current player.";
            }
            if (xAttacker == 3) {
                if (xAttacked == 2 || xAttacked == 3) {
                    attackedCard.setHealth(attackedCard.getHealth() + 2);
                } else return "Attacked card does not belong to the current player.";
            }
        }
        if (attackerCard.getName().equals("The Ripper") ||
                attackerCard.getName().equals("Miraj") ||
                attackerCard.getName().equals("The Cursed One")) {
            if (xAttacker == 0 || xAttacker == 1) {
                if (xAttacked == 0 || xAttacked == 1) {
                    return "Attacked card does not belong to the enemy.";
                }
                for (Card card: board.get(2)) {
                    if (card.isTank()) {
                        tank = true;
                    }
                }

            }
            if (xAttacker == 2 || xAttacker == 3) {
                if (xAttacked == 2 || xAttacked == 3) {
                    return "Attacked card does not belong to the enemy.";
                }
                for (Card card: board.get(1)) {
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
                    }
                    else attackedCard.setAttackDamage(attackedCard.getAttackDamage() - 2);
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

            }
        }
        attackerCard.setUsed(true);
        return null;
    }

    public String cardAttacksHero(int x, int y) {
        boolean tank = false;
        Hero heroAttacked = null;
        Card card = board.get(x).get(y);
        if (card.isFrozen()) {
            return "Attacker card is frozen.";
        }
        if (card.isUsed()) {
            return "Attacker card has already attacked this turn.";
        }
        if (x == 0 || x == 1) {
            heroAttacked = player1.getHero();
            for (Card c: board.get(2)) {
                if (c.isTank()) {
                    tank = true;
                    break;
                }
            }
        }
        if (x == 2 || x == 3) {
            heroAttacked = player2.getHero();
            for (Card c: board.get(1)) {
                if (c.isTank()) {
                    tank = true;
                    break;
                }
            }
        }
        System.out.println(tank);
        if (tank) {
            return "Attacked card is not of type 'Tank'.";
        }
        int newHealth = heroAttacked.getHealth() - card.getAttackDamage();
        heroAttacked.setHealth(newHealth);
        if (!heroAttacked.isAlive()) {
            player1.getStats().increaseTotalGamesPlayed();
            player2.getStats().increaseTotalGamesPlayed();
            switch (x) {
                case 0:
                    player2.getStats().increaseWins();
                    return "Player two killed the enemy hero.";
                case 1:
                    player2.getStats().increaseWins();
                    return "Player two killed the enemy hero.";
                case 2:
                    player1.getStats().increaseWins();
                    return "Player one killed the enemy hero.";
                case 3:
                    player1.getStats().increaseWins();
                    return "Player one killed the enemy hero.";
            }
        }
        card.setUsed(true);
        return null;
    }

   private void subZero(int affectedRow) {
        for (Card card: board.get(affectedRow)) {
            card.setFrozen(true);
        }
    }

    private void lowBlow(int affectedRow) {
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

    private void earthBorn(int affectedRow) {
        for (Card card: board.get(affectedRow)) {
            card.setHealth(card.getHealth() + 1);
        }
    }

    private void bloodThirst(int affectedRow) {
        for (Card card: board.get(affectedRow)) {
            card.setAttackDamage(card.getAttackDamage() + 1);
        }
    }

    public String heroAbility(int affectedRow) {
        int playerIdx = getTurn();
        Player currentPlayer = null;
        Hero hero = null;
        switch (playerIdx) {
            case 1:
                currentPlayer = player1;
                hero = player1.getHero();
                System.out.println("caz 1");
                break;
            case 2:
                System.out.println("caz 2");
                currentPlayer = player2;
                hero = player2.getHero();
                break;
        }
        if (hero.getMana() > currentPlayer.getMana()) {
            return "Not enough mana to use hero's ability.";
        }
        System.out.println("Player 1 hero: " + player1.getHero().getName());
        System.out.println("Player 2 hero: " + player2.getHero().getName());
        System.out.println("Player" + playerIdx + hero.getName() + " " + hero.isUsed());
        if (hero.isUsed()) {
            return "Hero has already attacked this turn.";
        }
        if (hero.getName().equals("Lord Royce") || hero.getName().equals("Empress Thorina")) {
            switch (playerIdx) {
                case 1:
                    if (affectedRow == 2 || affectedRow == 3) {
                        return "Selected row does not belong to the enemy.";
                    } else break;
                case 2:
                    if (affectedRow == 0 || affectedRow == 1) {
                        return "Selected row does not belong to the enemy.";
                    } else break;
            }
        }
        if (hero.getName().equals("General Kocioraw") || hero.getName().equals("King Mudface")) {
            switch (playerIdx) {
                case 1:
                    if (affectedRow == 0 || affectedRow == 1) {
                        return "Selected row does not belong to the current player.";
                    } else break;
                case 2:
                    if (affectedRow == 2 || affectedRow == 3) {
                        return "Selected row does not belong to the current player.";
                    } else break;
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
        }
        hero.setUsed(true);
        currentPlayer.setMana(currentPlayer.getMana() - hero.getMana());
        return null;
    }



   public int getTurn() {
        return (player1.isPlayerTurn() && !player2.isPlayerTurn()) ? 1 : 2;
    }

}