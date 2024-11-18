package org.poo.game;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Game {
    private GameBoard gameBoard;
    private int round;
    private int startingPlayer;

    public Game() {

    }

    public Game(long shuffleSeed, Player player1, Player player2,
                int playerOneDeckIdx, int playerTwoDeckIdx, Hero playerOneHero, Hero playerTwoHero,
                ArrayList<Deck> playerOneDecks, ArrayList<Deck> playerTwoDecks) {
        this.gameBoard = new GameBoard(player1, player2);
        this.round = 1;
        gameBoard.getPlayer1().setMana(1);
        gameBoard.getPlayer2().setMana(1);
        gameBoard.getPlayer1().setDeck(playerOneDecks.get(playerOneDeckIdx));
        gameBoard.getPlayer2().setDeck(playerTwoDecks.get(playerTwoDeckIdx));
        gameBoard.getPlayer1().setHero(playerOneHero);
        gameBoard.getPlayer2().setHero(playerTwoHero);
        gameBoard.getPlayer1().getDeck().shuffle(shuffleSeed);
        gameBoard.getPlayer2().getDeck().shuffle(shuffleSeed);
        gameBoard.getPlayer1().takeCardInHand();
        gameBoard.getPlayer2().takeCardInHand();
    }


    public void setTurnForPlayer(int playerIdx) {
        if (playerIdx == 1) {
            gameBoard.getPlayer1().setTurn(true);
        } else gameBoard.getPlayer2().setTurn(true);
    }

    public void endTurn() {
        int playerIdx = gameBoard.getTurn();
        if (playerIdx != startingPlayer) {
            gameBoard.getPlayer1().getHero().setUsed(false);
            gameBoard.getPlayer2().getHero().setUsed(false);
            for(ArrayList<Card> row: gameBoard.getBoard()) {
                for (Card card: row) {
                    card.setUsed(false);
                }
            }
            if (round >= 10) {
                round++;
                gameBoard.getPlayer1().setMana(gameBoard.getPlayer1().getMana() + 10);
                gameBoard.getPlayer2().setMana(gameBoard.getPlayer2().getMana() + 10);
                gameBoard.getPlayer1().takeCardInHand();
                gameBoard.getPlayer2().takeCardInHand();
            } else {
                round++;
                gameBoard.getPlayer1().setMana(gameBoard.getPlayer1().getMana() + round);
                gameBoard.getPlayer2().setMana(gameBoard.getPlayer2().getMana() + round);
                gameBoard.getPlayer1().takeCardInHand();
                gameBoard.getPlayer2().takeCardInHand();
            }
        }
        if (playerIdx == 1) {
            gameBoard.getPlayer1().setTurn(false);
            gameBoard.getPlayer2().setTurn(true);
            for (Card card: gameBoard.getBoard().get(3)) {
                card.setFrozen(false);
            }
            for (Card card: gameBoard.getBoard().get(2)) {
                card.setFrozen(false);
            }
        }
        else {
            gameBoard.getPlayer2().setTurn(false);
            gameBoard.getPlayer1().setTurn(true);
            for (Card card: gameBoard.getBoard().get(0)) {
                card.setFrozen(false);
            }
            for (Card card: gameBoard.getBoard().get(1)) {
                card.setFrozen(false);
            }
        }

    }

    public void getPlayerTurn(ArrayNode output, ObjectMapper objectMapper) {
        int playerIdx;
        if (gameBoard.getPlayer1().isPlayerTurn()) {
            playerIdx = 1;
        } else playerIdx = 2;
        ObjectNode playerTurnNode = objectMapper.createObjectNode();
        playerTurnNode.put("command", "getPlayerTurn");
        playerTurnNode.put("output", playerIdx);
        output.add(playerTurnNode);

    }

    public void getPlayerDeck(ArrayNode output, ObjectMapper objectMapper, int playerIdx) {
        Player player;
        if (playerIdx == 1) {
            player = gameBoard.getPlayer1();
        } else player = gameBoard.getPlayer2();
        ObjectNode deckNode = objectMapper.createObjectNode();
        deckNode.put("command", "getPlayerDeck");
        deckNode.put("playerIdx", playerIdx);
        ArrayNode deckArray = objectMapper.createArrayNode();
        for (Card card: player.getDeck().cards) {
            if (card == null) {
                break;
            }
            ObjectNode cardNode = objectMapper.createObjectNode();
            cardNode.put("mana", card.getMana());
            cardNode.put("attackDamage", card.getAttackDamage());
            cardNode.put("description", card.getDescription());
            cardNode.put("health", card.getHealth());
            ArrayNode colorsNode = cardNode.putArray("colors");
            for (String color: card.getColors()) {
                colorsNode.add(color);
            }
            cardNode.put("name", card.getName());
            deckArray.add(cardNode);
        }
        deckNode.set("output", deckArray);
        output.add(deckNode);
    }

    public void getPlayerHero(ArrayNode output, ObjectMapper objectMapper, int playerIdx) {
        Player player;
        if (playerIdx == 1) {
            player = gameBoard.getPlayer1();
        } else player = gameBoard.getPlayer2();
        ObjectNode heroNode = objectMapper.createObjectNode();
        heroNode.put("command", "getPlayerHero");
        heroNode.put("playerIdx", playerIdx);
        ObjectNode cardNode = objectMapper.createObjectNode();
        cardNode.put("mana", player.getHero().getMana());
        cardNode.put("description", player.getHero().getDescription());
        cardNode.put("health", player.getHero().getHealth());
        ArrayNode colorsNode = cardNode.putArray("colors");
        for (String color: player.getHero().getColors()) {
            colorsNode.add(color);
        }
        cardNode.put("name", player.getHero().getName());
        heroNode.set("output", cardNode);
        output.add(heroNode);
    }

    /* Method that place card on the game board, in case of error, it writes the error in output*/

    public void placeCardOnGameBoard(int playerIdx, int cardIdx, ArrayNode output, ObjectMapper objectMapper) {

        String error = gameBoard.placeCard(playerIdx, cardIdx);
        System.out.println(error);
        if (error != null) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("command", "placeCard");
            errorNode.put("handIdx", cardIdx);
            errorNode.put("error", error);
            output.add(errorNode);
        }
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void getPlayerHand(int playerIdx, ArrayNode output, ObjectMapper objectMapper) {
        Player player;
        if (playerIdx == 1) {
            player = gameBoard.getPlayer1();
        } else player = gameBoard.getPlayer2();
        ObjectNode handNode = objectMapper.createObjectNode();
        handNode.put("command", "getCardsInHand");
        handNode.put("playerIdx", playerIdx);
        ArrayNode handArray = objectMapper.createArrayNode();
        for (Card card: player.getHand()) {
            if (card == null) {
                break;
            }
            ObjectNode cardNode = objectMapper.createObjectNode();
            cardNode.put("mana", card.getMana());
            cardNode.put("attackDamage", card.getAttackDamage());
            cardNode.put("description", card.getDescription());
            cardNode.put("health", card.getHealth());
            ArrayNode colorsNode = cardNode.putArray("colors");
            for (String color: card.getColors()) {
                colorsNode.add(color);
            }
            cardNode.put("name", card.getName());
            handArray.add(cardNode);
        }
        handNode.set("output", handArray);
        output.add(handNode);
    }

    public void getPlayerMana(int playerIdx, ArrayNode output, ObjectMapper objectMapper) {
        Player player;
        if (playerIdx == 1) {
            player = gameBoard.getPlayer1();
        } else if  (playerIdx == 2) {
            player = gameBoard.getPlayer2();
        }
        else return;
        ObjectNode manaNode = objectMapper.createObjectNode();
        manaNode.put("command", "getPlayerMana");
        manaNode.put("playerIdx", playerIdx);
        manaNode.put("output", player.getMana());

        output.add(manaNode);
    }

    public void getCardsOnTable(ArrayNode output, ObjectMapper objectMapper) {
        ObjectNode tableNode = objectMapper.createObjectNode();
        tableNode.put("command", "getCardsOnTable");
        ArrayNode tableArray = objectMapper.createArrayNode();
        for (int i = 0; i < 4; i++) {
            ArrayNode rowArray = objectMapper.createArrayNode();
            if (gameBoard.getBoard().get(i).size() == 0) {
                tableArray.add(rowArray);
                continue;
            }
            for (int j = 0; j < gameBoard.getBoard().get(i).size(); j++) {
                ObjectNode cardNode = objectMapper.createObjectNode();
                cardNode.put("mana", gameBoard.getBoard().get(i).get(j).getMana());
                cardNode.put("attackDamage", gameBoard.getBoard().get(i).get(j).getAttackDamage());
                cardNode.put("description", gameBoard.getBoard().get(i).get(j).getDescription());
                cardNode.put("health", gameBoard.getBoard().get(i).get(j).getHealth());
                ArrayNode colorsNode = cardNode.putArray("colors");
                for (String color: gameBoard.getBoard().get(i).get(j).getColors()) {
                    colorsNode.add(color);
                }
                cardNode.put("name", gameBoard.getBoard().get(i).get(j).getName());
                rowArray.add(cardNode);
            }
            tableArray.add(rowArray);
        }
        tableNode.set("output", tableArray);
        output.add(tableNode);
    }

    public void cardUsesAttack(int xAttacker, int yAttacker, int xAttacked, int yAttacked, ArrayNode output, ObjectMapper objectMapper) {
        String error = gameBoard.cardAttack(xAttacker, yAttacker, xAttacked, yAttacked);
        if (error != null) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("command", "cardUsesAttack");
            ObjectNode cardAttackerNode = objectMapper.createObjectNode();
            cardAttackerNode.put("x", xAttacker);
            cardAttackerNode.put("y", yAttacker);
            errorNode.set("cardAttacker", cardAttackerNode);
            ObjectNode cardAttackedNode = objectMapper.createObjectNode();
            cardAttackedNode.put("x", xAttacked);
            cardAttackedNode.put("y", yAttacked);
            errorNode.set("cardAttacked", cardAttackedNode);
            errorNode.put("error", error);
            output.add(errorNode);
        }
    }

    public void getCardAtPosition(int x, int y, ArrayNode output, ObjectMapper objectMapper) {
        String out = gameBoard.getCardAtPosition(x, y);
        ObjectNode outputNode = objectMapper.createObjectNode();
        if (out != null) {
            outputNode.put("command", "getCardAtPosition");
            outputNode.put("output", out);
            outputNode.put("x", x);
            outputNode.put("y", y);
        }
        else {
            outputNode.put("command", "getCardAtPosition");
            ObjectNode cardNode = objectMapper.createObjectNode();
            cardNode.put("mana", gameBoard.getBoard().get(x).get(y).getMana());
            cardNode.put("attackDamage", gameBoard.getBoard().get(x).get(y).getAttackDamage());
            cardNode.put("description", gameBoard.getBoard().get(x).get(y).getDescription());
            cardNode.put("health", gameBoard.getBoard().get(x).get(y).getHealth());
            ArrayNode colorsNode = cardNode.putArray("colors");
            for (String color: gameBoard.getBoard().get(x).get(y).getColors()) {
                colorsNode.add(color);
            }
            cardNode.put("name", gameBoard.getBoard().get(x).get(y).getName());
            outputNode.set("output", cardNode);
            outputNode.put("x", x);
            outputNode.put("y", y);
        }
        output.add(outputNode);
    }

    public void cardUsesAbility(int xAttacker, int yAttacker, int xAttacked, int yAttacked, ArrayNode output, ObjectMapper objectMapper) {
        String error = gameBoard.useAbility(xAttacker, yAttacker, xAttacked, yAttacked);
        if (error != null) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("command", "cardUsesAbility");
            ObjectNode cardAttackerNode = objectMapper.createObjectNode();
            cardAttackerNode.put("x", xAttacker);
            cardAttackerNode.put("y", yAttacker);
            errorNode.set("cardAttacker", cardAttackerNode);
            ObjectNode cardAttackedNode = objectMapper.createObjectNode();
            cardAttackedNode.put("x", xAttacked);
            cardAttackedNode.put("y", yAttacked);
            errorNode.set("cardAttacked", cardAttackedNode);
            errorNode.put("error", error);
            output.add(errorNode);
        }
    }

    public void useAttackHero(int x, int y, ArrayNode output, ObjectMapper objectMapper) {
        String out = gameBoard.cardAttacksHero(x, y);
        System.out.println("output: " + out);
        if (out != null) {
            if (out.equals("Player one killed the enemy hero.") || out.equals("Player two killed the enemy hero.")) {
                ObjectNode gameEndedNode = objectMapper.createObjectNode();
                gameEndedNode.put("gameEnded", out);
                output.add(gameEndedNode);

            }
            else {
                ObjectNode errorNode = objectMapper.createObjectNode();
                errorNode.put("command", "useAttackHero");
                ObjectNode coordsNode = objectMapper.createObjectNode();
                coordsNode.put("x", x);
                coordsNode.put("y", y);
                errorNode.set("cardAttacker", coordsNode);
                errorNode.put("error", out);
                output.add(errorNode);
            }
        }

    }

    public void useHeroAbility(int affectedRow, ArrayNode output, ObjectMapper objectMapper) {
        String error = gameBoard.heroAbility(affectedRow);
        if (error != null) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("command", "useHeroAbility");
            errorNode.put("affectedRow", affectedRow);
            errorNode.put("error", error);
            output.add(errorNode);
        }
    }

    public void getFrozenCardsOnTable(ArrayNode output, ObjectMapper objectMapper) {
        ObjectNode frozenNode = objectMapper.createObjectNode();
        frozenNode.put("command", "getFrozenCardsOnTable");
        ArrayNode frozenArray = objectMapper.createArrayNode();
        for (ArrayList<Card> row: gameBoard.getBoard()) {
            for(Card card: row) {
                if (card.isFrozen()) {
                    ObjectNode cardNode = objectMapper.createObjectNode();
                    cardNode.put("mana", card.getMana());
                    cardNode.put("attackDamage", card.getAttackDamage());
                    cardNode.put("description", card.getDescription());
                    cardNode.put("health", card.getHealth());
                    ArrayNode colorsNode = cardNode.putArray("colors");
                    for (String color: card.getColors()) {
                        colorsNode.add(color);
                    }
                    cardNode.put("name", card.getName());
                    frozenArray.add(cardNode);
                }
            }
        }
        frozenNode.set("output", frozenArray);
        output.add(frozenNode);
    }

    public void getTotalGamesPlayed(ArrayNode output, ObjectMapper objectMapper) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getTotalGamesPlayed");
        outputNode.put("output", gameBoard.getPlayer1().getStats().getTotalGamesPlayed());
        output.add(outputNode);
    }

    public void getPlayerOneWins(ArrayNode output, ObjectMapper objectMapper) {
        ObjectNode winsNode = objectMapper.createObjectNode();
        winsNode.put("command", "getPlayerOneWins");
        winsNode.put("output", gameBoard.getPlayer1().getStats().getWins());
        output.add(winsNode);
    }

    public void getPlayerTwoWins(ArrayNode output, ObjectMapper objectMapper) {
        ObjectNode winsNode = objectMapper.createObjectNode();
        winsNode.put("command", "getPlayerTwoWins");
        winsNode.put("output", gameBoard.getPlayer2().getStats().getWins());
        output.add(winsNode);
    }



    public void setStartingPlayer(int startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    public int getStartingPlayer() {
        return startingPlayer;
    }

    public int getRound() {
        return round;
    }
}
