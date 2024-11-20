package org.poo.game;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Game {
    private GameBoard gameBoard;
    private int round;
    private int startingPlayer;
    public static final int ROUND_LIMIT = 10;
    public static final int THIRD = 3;
    public static final int SECOND = 2;
    public static final int FIRST = 1;
    public static final int ROWS = 4;
    public Game() {

    }

    public Game(final long shuffleSeed, final Player player1,
                final Player player2,
                final int playerOneDeckIdx,
                final int playerTwoDeckIdx,
                final Hero playerOneHero,
                final Hero playerTwoHero,
                final ArrayList<Deck> playerOneDecks,
                final ArrayList<Deck> playerTwoDecks) {
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

    /**
     *
     * @param playerIdx indicates the player to whom the turn will be assigned
     */
    public final void setTurnForPlayer(final int playerIdx) {
        if (playerIdx == 1) {
            gameBoard.getPlayer1().setTurn(true);
        } else {
            gameBoard.getPlayer2().setTurn(true);
        }
    }

    /**
     * Method that ends a turn for the current player.
     * If the turn returns to the first player who started the game,
     * the round will be incremented.
     */
    public final void endTurn() {
        int playerIdx = gameBoard.getTurn();
        if (playerIdx != startingPlayer) {
            gameBoard.getPlayer1().getHero().setUsed(false);
            gameBoard.getPlayer2().getHero().setUsed(false);
            for (ArrayList<Card> row: gameBoard.getBoard()) {
                for (Card card: row) {
                    card.setUsed(false);
                }
            }
            if (round >= ROUND_LIMIT) {
                round++;
                gameBoard.getPlayer1().setMana(gameBoard.getPlayer1().getMana() + ROUND_LIMIT);
                gameBoard.getPlayer2().setMana(gameBoard.getPlayer2().getMana() + ROUND_LIMIT);
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
            for (Card card: gameBoard.getBoard().get(THIRD)) {
                card.setFrozen(false);
            }
            for (Card card: gameBoard.getBoard().get(SECOND)) {
                card.setFrozen(false);
            }
        } else {
            gameBoard.getPlayer2().setTurn(false);
            gameBoard.getPlayer1().setTurn(true);
            for (Card card: gameBoard.getBoard().get(0)) {
                card.setFrozen(false);
            }
            for (Card card: gameBoard.getBoard().get(FIRST)) {
                card.setFrozen(false);
            }
        }

    }

    /**
     *
     * @param output the array where the method will add the output
     * @param objectMapper ObjectMapper object, used for methods like:
     *                     createObjectNode(), createArrayNode()
     */
    public final void getPlayerTurn(final ArrayNode output, final ObjectMapper objectMapper) {
        int playerIdx;
        if (gameBoard.getPlayer1().isPlayerTurn()) {
            playerIdx = 1;
        } else {
            playerIdx = 2;
        }
        ObjectNode playerTurnNode = objectMapper.createObjectNode();
        playerTurnNode.put("command", "getPlayerTurn");
        playerTurnNode.put("output", playerIdx);
        output.add(playerTurnNode);

    }

    /**
     * Method that outputs the deck of the player
     * with index "playerIdx"
     * @param output the array where the method will add the output
     * @param objectMapper ObjectMapper object, used for methods like:
     *                      createObjectNode(), createArrayNode()
     * @param playerIdx indicates the player whose deck will be shown by the method
     */
    public final void getPlayerDeck(final ArrayNode output,
                                    final ObjectMapper objectMapper,
                                    final int playerIdx) {
        Player player;
        if (playerIdx == 1) {
            player = gameBoard.getPlayer1();
        } else  {
            player = gameBoard.getPlayer2();
        }
        ObjectNode deckNode = objectMapper.createObjectNode();
        deckNode.put("command", "getPlayerDeck");
        deckNode.put("playerIdx", playerIdx);
        ArrayNode deckArray = objectMapper.createArrayNode();
        for (Card card: player.getDeck().getCards()) {
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

    /**
     * Method that outputs the hero of the player
     * with index "playerIdx"
     * @param output the array where the method will add the output
     * @param objectMapper ObjectMapper object, used for methods like:
     *                      createObjectNode(), createArrayNode()
     * @param playerIdx indicates the player whose hero will be shown by the method
     */
    public final void getPlayerHero(final ArrayNode output,
                                    final ObjectMapper objectMapper,
                                    final int playerIdx) {
        Player player;
        if (playerIdx == 1) {
            player = gameBoard.getPlayer1();
        } else {
            player = gameBoard.getPlayer2();
        }
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

    /**
     * Method that place card on the game board,
     * in case of error, it writes the error in output
     * @param playerIdx the index of the current player
     * @param cardIdx the index of card that will be placed on table
     * @param output the array where the method will add the output
     * @param objectMapper ObjectMapper object, used for methods like:
     *                     createObjectNode(), createArrayNode()
     */

    public final void placeCardOnGameBoard(final int playerIdx,
                                           final int cardIdx,
                                           final ArrayNode output,
                                           final ObjectMapper objectMapper) {

        String error = gameBoard.placeCard(playerIdx, cardIdx);
        if (error != null) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("command", "placeCard");
            errorNode.put("handIdx", cardIdx);
            errorNode.put("error", error);
            output.add(errorNode);
        }
    }

    public final GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Method that outputs the hand of the player
     * with index "playerIdx"
     *
     * @param playerIdx indicates the player
     * @param output the array where the method will add the output
     * @param objectMapper ObjectMapper object, used for methods like:
     *                     createObjectNode(), createArrayNode()
     */
    public final void getPlayerHand(final int playerIdx,
                                    final ArrayNode output,
                                    final ObjectMapper objectMapper) {
        Player player;
        if (playerIdx == 1) {
            player = gameBoard.getPlayer1();
        } else {
            player = gameBoard.getPlayer2();
        }
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

    /**
     * Method that outputs the mana of the player with
     * index "playerIdx"
     * @param playerIdx indicates the player
     * @param output the array where the method will add the output
     * @param objectMapper ObjectMapper object, used for methods like:
     *                     createObjectNode(), createArrayNode()
     */
    public final void getPlayerMana(final int playerIdx,
                                    final ArrayNode output,
                                    final ObjectMapper objectMapper) {
        Player player;
        if (playerIdx == 1) {
            player = gameBoard.getPlayer1();
        } else {
            player = gameBoard.getPlayer2();
        }
        ObjectNode manaNode = objectMapper.createObjectNode();
        manaNode.put("command", "getPlayerMana");
        manaNode.put("playerIdx", playerIdx);
        manaNode.put("output", player.getMana());

        output.add(manaNode);
    }

    /**
     * Method that outputs all the cards placed on the table
     * @param output the array where the method will add the output
     * @param objectMapper ObjectMapper object, used for methods like:
     *                     createObjectNode(), createArrayNode()
     */
    public final void getCardsOnTable(final ArrayNode output,
                                      final ObjectMapper objectMapper) {
        ObjectNode tableNode = objectMapper.createObjectNode();
        tableNode.put("command", "getCardsOnTable");
        ArrayNode tableArray = objectMapper.createArrayNode();
        for (int i = 0; i < ROWS; i++) {
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

    /**
     * Method that is used for the command "cardUsesAttack".
     * In case of error, it will output an error message.
     * @param xAttacker the coordinate "x" of the attacker's card
     * @param yAttacker the coordinate "y" of the attacker's card
     * @param xAttacked the coordinate "x" of the attacked card
     * @param yAttacked the coordinate "y" of the attacked card
     * @param output the array where the method will add the output
     * @param objectMapper ObjectMapper object, used for methods like:
     *                     createObjectNode(), createArrayNode()
     */
    public final void cardUsesAttack(final int xAttacker,
                                     final int yAttacker,
                                     final int xAttacked,
                                     final int yAttacked,
                                     final ArrayNode output,
                                     final ObjectMapper objectMapper) {
        String error = gameBoard.cardAttack(xAttacker, yAttacker,
                                            xAttacked, yAttacked);
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

    /**
     * Method that outputs the card at the position (x,y)
     * @param x the coordinate "x" of the card
     * @param y the coordinate "y" of the card
     * @param output the array where the method will add the output
     * @param objectMapper ObjectMapper object, used for methods like:
     *                     createObjectNode(), createArrayNode()
     */

    public final void getCardAtPosition(final int x,
                                        final int y,
                                        final ArrayNode output,
                                        final ObjectMapper objectMapper) {
        String out = gameBoard.getCardAtPosition(x, y);
        ObjectNode outputNode = objectMapper.createObjectNode();
        if (out != null) {
            outputNode.put("command", "getCardAtPosition");
            outputNode.put("output", out);
            outputNode.put("x", x);
            outputNode.put("y", y);
        } else {
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

    /**
     * Method that is used for the "cardUsesAbility" command
     * @param xAttacker the coordinate "x" of the attacker's card
     * @param yAttacker the coordinate "y" of the attacker's card
     * @param xAttacked the coordinate "x" of the attacked card
     * @param yAttacked the coordinate "y" of the attacked card
     * @param output the array where the method will add the output
     * @param objectMapper ObjectMapper object, used for methods like:
     *                     createObjectNode(), createArrayNode()
     */
    public final void cardUsesAbility(final int xAttacker,
                                      final int yAttacker,
                                      final int xAttacked,
                                      final int yAttacked,
                                      final ArrayNode output,
                                      final ObjectMapper objectMapper) {
        String error = gameBoard.useAbility(xAttacker, yAttacker,
                                            xAttacked, yAttacked);
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

    /**
     * Method that is used for the "useAttackHero" command
     * @param x the coordinate "x" of the card that attacks
     * @param y the coordinate "y" of the card that attacks
     * @param output the array where the method will add the output
     * @param objectMapper ObjectMapper object, used for methods like:
     *                     createObjectNode(), createArrayNode()
     */
    public final void useAttackHero(final int x,
                                    final int y,
                                    final ArrayNode output,
                                    final ObjectMapper objectMapper) {
        String out = gameBoard.cardAttacksHero(x, y);
        if (out != null) {
            if (out.equals("Player one killed the enemy hero.")
                    || out.equals("Player two killed the enemy hero.")) {
                ObjectNode gameEndedNode = objectMapper.createObjectNode();
                gameEndedNode.put("gameEnded", out);
                output.add(gameEndedNode);

            } else {
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

    /**
     * Method that is used for the "useHeroAbility" command
     * @param affectedRow the row that will be affected by the hero's ability
     * @param output the array where the method will add the output
     * @param objectMapper ObjectMapper object, used for methods like:
     *                     createObjectNode(), createArrayNode()
     */
    public final void useHeroAbility(final int affectedRow,
                                     final ArrayNode output,
                                     final ObjectMapper objectMapper) {
        String error = gameBoard.heroAbility(affectedRow);
        if (error != null) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("command", "useHeroAbility");
            errorNode.put("affectedRow", affectedRow);
            errorNode.put("error", error);
            output.add(errorNode);
        }
    }

    /**
     * Method that outputs the frozen cards on the table
     * @param output the array where the method will add the output
     * @param objectMapper ObjectMapper object, used for methods like:
     *                     createObjectNode(), createArrayNode()
     */
    public final void getFrozenCardsOnTable(final ArrayNode output,
                                            final ObjectMapper objectMapper) {
        ObjectNode frozenNode = objectMapper.createObjectNode();
        frozenNode.put("command", "getFrozenCardsOnTable");
        ArrayNode frozenArray = objectMapper.createArrayNode();
        for (ArrayList<Card> row: gameBoard.getBoard()) {
            for (Card card: row) {
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

    /**
     * Method that outputs the total games played in this session
     * @param output the array where the method will add the output
     * @param objectMapper ObjectMapper object, used for methods like:
     *                     createObjectNode(), createArrayNode()
     */
    public final void getTotalGamesPlayed(final ArrayNode output,
                                          final ObjectMapper objectMapper) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "getTotalGamesPlayed");
        outputNode.put("output", gameBoard.getPlayer1().getStats().getTotalGamesPlayed());
        output.add(outputNode);
    }

    /**
     * Method that outputs the playerOne's wins
     * @param output the array where the method will add the output
     * @param objectMapper ObjectMapper object, used for methods like:
     *                     createObjectNode(), createArrayNode()
     */

    public final void getPlayerOneWins(final ArrayNode output,
                                       final ObjectMapper objectMapper) {
        ObjectNode winsNode = objectMapper.createObjectNode();
        winsNode.put("command", "getPlayerOneWins");
        winsNode.put("output", gameBoard.getPlayer1().getStats().getWins());
        output.add(winsNode);
    }
    /**
     * Method that outputs the playerTwo's wins
     * @param output the array where the method will add the output
     * @param objectMapper ObjectMapper object, used for methods like:
     *                     createObjectNode(), createArrayNode()
     */
    public final void getPlayerTwoWins(final ArrayNode output,
                                       final ObjectMapper objectMapper) {
        ObjectNode winsNode = objectMapper.createObjectNode();
        winsNode.put("command", "getPlayerTwoWins");
        winsNode.put("output", gameBoard.getPlayer2().getStats().getWins());
        output.add(winsNode);
    }



    public final void setStartingPlayer(final int startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    public final int getStartingPlayer() {
        return startingPlayer;
    }

    public final int getRound() {
        return round;
    }
}
