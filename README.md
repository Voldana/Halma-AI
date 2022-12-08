# Halma Java



## Description
This is the project I designed for the students of Artificial Intelligence course held in fall of 2022 as a teaching assistance.

The students must use the knowledge that they acquired during the course to add a min-max algorithm and an evaluation function for the game to work.

The game is consisted of a 8x8 board with 10 colored pieces on two corners in front of each other.The player who manages to move all his pieces to the opponent's corner wins the game.

*This project contains GUI in order to make it easier to play and see the results.
___
## Rules
Each player can move their pieces in 8 different directions (diagonal movement is allowed).
The movements should follow the rules below:

1- Moving to an empty cell:
    i. Player moves a piece to an empty cell adjacent to the piece.
   ii. This concludes the player's turn.

2- One or several jumps over adjacent pieces:
    i. A piece can jump over one and exactly one adjacent piece if there's an empty cell on the other side of that cell.
   ii. After jumping player can jump over adjacent pieces any number of times as long as it's with the exact same piece they first jumped with.

3- If a piece reaches the opponent starting zone (AKA their goal zone) that piece cannot exit the opponent zone.
