# GwentStone Light - Game

## Description
GwentStone Light is a game that combines elements of Hearthstone and Gwent.  
It is designed for two players only.  
At the start of the game, each player selects a deck and is assigned a hero to represent them.  
The turn order is random. The game is turn-based, meaning each player takes their turn to ends it manually.  
Cards in a player's hand can be placed on the board to be used, but this  
requires mana, as each card has a designated mana cost to pay. Cards can interact only   
when they are placed on the board. Each player has two rows on the board to place their  
cards. Each type of card has its specific row on the board. The heroes have their own  
space on the table, separate from the rest of the cards, and are placed on the board at  
the start of the game. Each type of hero has a unique ability that affects a specific row.  
The game ends when a hero dies. The player whose hero is still alive wins the game.  

## Game Commands

* getPlayerDeck  - outputs in JSON format the deck of the current player.  
* getPlayerHero - outputs in JSON format the hero of the current player.  
* getPlayerTurn - outputs in JSON format the index of the current player.  
* placeCard - places a card on the board. It also has a parameter called  
handIdx, which represents the index of the selected card in the hand.  
In case of an error, it will output the erorr message.    
* endPlayerTurn - ends the current player's turn.  
* getCardsInHand - outputs in JSON format the hand of the current player.  
* getCardsOnTable - outputs in JSON format each card on the board, row by 
row, from the left to right.  
* getPlayerMana - outputs in JSON format the current player's mana.
* cardUsesAttack - takes 4 parameters: the x and y coordinates of the attacker,  
and the x and y coordinates of the attacked card. The command is used for the attacking  
one card with another.  
In case of an error, it outputs the error message.  
* getCardAtPosition - takes two parameters: the x and y coordinates of the card  
to be searched. As the name suggests, the command outputs the card found at the   
given position.    
* cardUsesAbility - takes 4 parameters: the x and y coordinates of the attacker,  
and the x and y coordinates of the attacked card. The command is used to activate  
the ability of one card on another. In case of an error, it outputs the error message.  
* useAttackHero - takes two parameters: the x and y coordinates of the card that will attack  
the enemy hero. As the name suggests, the command is used to attack a hero with a card.  
In case of an error, it outputs the error message.  
* useHeroAbility - takes one parameter: the index of the affected row. The command is used to  
activate the ability of the current player's hero on the selected card. In case of an error,  
it outputs the error message.  
* getFrozenCardsOnTable - outputs in JSON format each frozen card on the board, row by  
row, from the left to right.  
* getTotalGamesPlayed - outputs in JSON format the total number of games played in a gaming session.  
* getPlayerOneWins - outputs in JSON format the total number of games won by player one.  
* getPlayerTwoWins - outputs in JSON format the total number of games won by player two.  
  

## Class Details

### Card

* Has 8 fields: mana, health, attackDamage  
description, colors, name, frozen, used.  
* The 'frozen' and 'used' fields were created to track the status of the card.  
* Has two constructors: one of them takes a CardInput object as a parameter,  
initializes all common fields, and sets 'frozen' and 'used' to false.  
* Has getters and setters for each field of the class.  
* isFrontRowCard() method: indicates that the card is a front row card.  
* isTank() method: indicates that the card is a tank.  

### Hero

* A subclass of the Card class.  
* Has two constructors: one of them takes a CardInput object as a parameter,   
uses the parent's constructor, sets the health to 30, and sets the frozen  
to false, which is the final version.  
* isAlive() method: checks if the health is greater than 0.  


### Deck

* Has the 'cards' field, which is an ArrayList of Card objects.  
* Has a constructor that takes an ArrayList of CardInput objects as parameter,  
and initializes the 'cards' field to an ArrayList of Card objects.  
* shuffle() method: shuffles the deck using the seed provided at the start of the game  
* drawCard() method: draws the card placed on top of the deck.  


### Stats

* A class that creates an object that tracks the stats of a player.  
* Contains getters and setters of each field.  
* Has two methods that increments each field.  

### Player

* Has the following fields: stats, mana, hero, hand, deck, turn.  
* Has a constructor that takes no parameter, initializes a Stats object, mana to 0, hand to a new ArrayList of  
Card objects, and sets the turn field to false too.  
* Contains getters and setters.  
* takeCardInHand() method: takes a drawn card from the player's deck in hand.  
* resetPlayer() method: resets the player for a every new game.  


### GameBoard

* Contains two players: playerOne and playerTwo.  
* The board is represented by an ArrayList of ArrayLists of Card objects.  
* placeCard() method: implements the logic behind 'placeCard' command.  
* cardAttack() method: implements the logic behind 'cardUsesAttack' command.  
* getCardAtPosition() method: implements the logic behind 'getCardAtPosition' command.  
* useAbility() method: implements the logic behind 'cardUsesAbility' command.  
* cardAttacksHero() method: implements the logic behind 'useAttackHero' command.  
* subZero(), lowBlow(), earthBorn(), bloodThirst() methods: implements the logic  
behind each hero's ability.  
* heroAbility() method: implements the logic behind 'useHeroAbility' command.  
* getTurn() method: returns the current player's index.  


### Game 

* Has the following fields: gameBoard, round and the starting player's index.  
* Has a constructor that initializes a game (creates a new board,  
sets the round field to 1, sets the mana for each player to 1, sets the selected  
deck for each player, sets the hero for each player, shuffle both decks and places   
the first card into each player's hand)  
* Contains all the methods that write in JSON format.



### GameSession 

* contains a single static method called startSession().
* at the beginning of the method, two players will be created, which will then be  
reset after each game.  
* for each game, the method will retrieve the data from the inputData object, which will  
then create a new game.  
* then, the method will iterate through each action and execute the received command.  


