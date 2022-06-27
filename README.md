# Labyrinth Game

## Gameplay

The game can be played with one or two players. The players start by choosing their "secret room" inside of the labyrinth (if only one player is playing, then press "next" when Player 2 should be choosing their secret room. Once the game has started, players take turns moving through the labyrinth. The player can only move to a non-diagonal adjacent square not blocked by a wall. All the walls start off invisible, and then once they are hit by the player the wall becomes visible. If a player hits an invisible wall, runs out of moves, presses the "next button", gets attacked by the foe, attacks another player, or picks up the treasure, then their turns end. The foe moves if any player is within 3 squares of the "treasure room".

## Objective

The object of the game is to collect the treasure from the treasure room and bring it back to the secret room before the character dies. The treasure is hidden until a player successfully enters the treasure room, then it is visible for the rest of the game (represented by a gold border). If a player successfully takes the treasure back to their secret room, then that player wins.

## The Foe

The foe, represented by the snake, is visible until a player gets attacked, then it is visible for the rest of the game. The foe resides in the secret room, and moves closer to the player at the end of each round if the player is within three spaces of the treasure room. If a player has the treasure, then the foe moves towards that player, regardless of how far the treasure-holding player is or how close the other player is. The foe can move diagonally and through walls.

## The Player

The player, represented by the pig (Player 1) or the rabbit (Player 2), is controlled by the user. The player starts off with 8 "fatigue", which determines the amount of moves the player can travel per turn. When the player is attacked by the foe, they teleport back to their secret room and lose 2 fatigue. If the fatigue is ever less than 4, then the player is eliminated and therefore out of the game. If the player lands in the same square as the other player with the treasure, the two players fight and whoever has the highest fatigue value wins and gets the treasure, and the losing player is teleported 3 squares away in any direction, and the current player's turn ends. If the player picks up the treasure, their fatigue is immediately set to 4. If the player loses the treasure, the fatig is set back to its original value (if the player is attacked by the foe and loses the treasure, then the previous value is subtracted by 2, as normal.

## End Game Scenarios

### Winning Scenario

If a player gets back to their secret room with their treasure, then that player wins. The other player does not win if the other player is eliminated.

### Losing Scenario

If all players become eliminated, then the CPU wins and the players lose.

## UI

<img width="1123" alt="Labyrinth Game Screenshot" src="https://user-images.githubusercontent.com/63774420/176037459-7ae98ca1-6f87-4d83-bcfe-6641fe966f32.png">
