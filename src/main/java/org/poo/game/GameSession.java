package org.poo.game;
import org.poo.fileio.Input;
import org.poo.fileio.ActionsInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;

public final class GameSession {
        private GameSession() {
            System.out.println("Private constructor");
        }

    /**
     *
     * @param inputData Input object,that contains the input of the game
     * @param output ArrayNode object, the "main" array for the output
     * @param objectMapper ObjectMapper object, used for methods like:
     *                     createObjectNode(), createArrayNode()
     */

    public static void startSession(final Input inputData,
                                        final ArrayNode output, final ObjectMapper objectMapper) {
        /* initialized the game session's players */
        Player player1 = new Player();
        Player player2 = new Player();

        for (int i = 0; i < inputData.getGames().size(); i++) {
            ArrayList<Deck> playerOneDecks = new ArrayList<>();
            ArrayList<Deck> playerTwoDecks = new ArrayList<>();

            for (int j = 0; j < inputData.getPlayerOneDecks().getNrDecks(); j++) {
                playerOneDecks.add(new Deck(inputData.getPlayerOneDecks().getDecks().get(j)));
                         }
            for (int j = 0; j < inputData.getPlayerTwoDecks().getNrDecks(); j++) {
                playerTwoDecks.add(new Deck(inputData.getPlayerTwoDecks().getDecks().get(j)));
            }
            player1.resetPlayer();
            player2.resetPlayer();
            long seed = inputData.getGames().get(i).getStartGame().getShuffleSeed();
            int playerOneDeckIdx =
                    inputData.getGames().get(i).getStartGame().getPlayerOneDeckIdx();
            int playerTwoDeckIdx =
                    inputData.getGames().get(i).getStartGame().getPlayerTwoDeckIdx();
            Hero playerOneHero =
                    new Hero(inputData.getGames().get(i).getStartGame().getPlayerOneHero());
            Hero playerTwoHero =
                    new Hero(inputData.getGames().get(i).getStartGame().getPlayerTwoHero());
            int startingPlayer =
                    inputData.getGames().get(i).getStartGame().getStartingPlayer();
            Game game =
                    new Game(seed, player1, player2, playerOneDeckIdx, playerTwoDeckIdx,
                            playerOneHero, playerTwoHero, playerOneDecks, playerTwoDecks);
            game.setStartingPlayer(startingPlayer);
            game.setTurnForPlayer(startingPlayer);
            for (ActionsInput action: inputData.getGames().get(i).getActions()) {
                String command = action.getCommand();
                switch (command) {
                    case "getPlayerDeck":
                        game.getPlayerDeck(output, objectMapper, action.getPlayerIdx());
                        break;
                    case "getPlayerHero":
                        game.getPlayerHero(output, objectMapper, action.getPlayerIdx());
                        break;
                    case "getPlayerTurn":
                        game.getPlayerTurn(output, objectMapper);
                        break;
                    case "placeCard":
                        game.placeCardOnGameBoard(game.getGameBoard().getTurn(),
                                                action.getHandIdx(), output, objectMapper);
                        break;
                    case "endPlayerTurn":
                        game.endTurn();
                        break;
                    case "getCardsInHand":
                        game.getPlayerHand(action.getPlayerIdx(), output, objectMapper);
                        break;
                    case "getCardsOnTable":
                        game.getCardsOnTable(output, objectMapper);
                        break;
                    case "getPlayerMana":
                        game.getPlayerMana(action.getPlayerIdx(), output, objectMapper);
                        break;
                    case "cardUsesAttack":
                        game.cardUsesAttack(action.getCardAttacker().getX(),
                                            action.getCardAttacker().getY(),
                                            action.getCardAttacked().getX(),
                                            action.getCardAttacked().getY(),
                                            output, objectMapper);
                        break;
                    case "getCardAtPosition":
                        game.getCardAtPosition(action.getX(), action.getY(),
                                                output, objectMapper);
                        break;
                    case "cardUsesAbility":
                        game.cardUsesAbility(action.getCardAttacker().getX(),
                                            action.getCardAttacker().getY(),
                                            action.getCardAttacked().getX(),
                                            action.getCardAttacked().getY(),
                                            output, objectMapper);
                        break;
                    case "useAttackHero":
                        game.useAttackHero(action.getCardAttacker().getX(),
                                            action.getCardAttacker().getY(),
                                            output, objectMapper);
                        break;
                    case "useHeroAbility":
                        game.useHeroAbility(action.getAffectedRow(), output, objectMapper);
                        break;
                    case "getFrozenCardsOnTable" :
                        game.getFrozenCardsOnTable(output, objectMapper);
                        break;
                    case "getTotalGamesPlayed":
                        game.getTotalGamesPlayed(output, objectMapper);
                        break;
                    case "getPlayerOneWins":
                        game.getPlayerOneWins(output, objectMapper);
                        break;
                    case "getPlayerTwoWins":
                        game.getPlayerTwoWins(output, objectMapper);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
