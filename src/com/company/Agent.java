package com.company;

import java.util.LinkedList;
import java.util.List;

public class Agent {
    private Board board;

    public Agent(Board board){
        this.board = board;
    }

    public Move doMinMax(Tile[][] tiles, byte playerTurn) {
        Pair temp = max(tiles, playerTurn, (byte) (0));

        return temp.move;
    }

    private Pair max(Tile[][] currentBoard, byte currentColor, byte depth) {

        if (checkTerminal(currentBoard))
            return new Pair(null, Integer.MIN_VALUE);

        // check depth here

        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);

        // write your codes here

        // return pair(move, value)
        return new Pair(null, 0);
    }

    private Pair min(Tile[][] currentBoard, byte currentColor, byte depth) {

        // write your codes here

        // return pair(move, value)
        return new Pair(null, 0);
    }

    private int evaluate(Tile[][] currentBoard, byte currentColor) {
        short score = 0;

        // write your codes here
        // use the given parameter "currentColor" or "playerTurn" if needed

        return score;

    }

    public List<Move> createPossibleMoves(Tile[][] newBoard, int currentColor) {
        List<Move> possibleMoves = new LinkedList<>();
        for (byte i = 0; i < 8; i++)
            for (byte j = 0; j < 8; j++)
                if (newBoard[i][j].color == currentColor) {
                    List<Tile> legalTiles = new LinkedList<>();
                    board.findPossibleMoves(newBoard, newBoard[i][j], legalTiles, newBoard[i][j], true);
                    for (Tile tile : legalTiles)
                        possibleMoves.add(new Move(newBoard[i][j], tile));
                }
        return possibleMoves;
    }


    public boolean checkTerminal(Tile[][] currentTiles) {

        byte redCounter = 0;
        byte blueCounter = 0;

        for (byte x = 0; x < 8; x++) {
            for (byte y = 0; y < 8; y++) {
                if (currentTiles[x][y].zone == 1) {
                    if (currentTiles[x][y].color == 2) {
                        redCounter++;
                        if (redCounter >= 10) {
                            return true;
                        }
                    }
                } else if (currentTiles[x][y].zone == 2) {
                    if (currentTiles[x][y].color == 1) {
                        blueCounter++;
                        if (blueCounter >= 10) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}