package com.company;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class Halma {

    private Board board;
    private final Tile[][] tiles;

    private final static byte maxDepth = 4;
    private byte playerTurn;
    private short totalMoves = 0;
    private byte firstX, firstY, secondX, secondY;

    GUI gameUI = new GUI();

    public Halma() {
        tiles = new Tile[8][8];
        playerTurn = 1;
        assignCoordinates();
    }

    private void assignCoordinates() {
        for (byte i = 0; i < 8; i++) {
            for (byte j = 0; j < 8; j++) {
                tiles[i][j] = new Tile(i, j);

                if ((i + j) <= 3) {
                    tiles[i][j].color = 1;
                    tiles[i][j].zone = 1;
                } else if ((i + j) >= 11) {
                    tiles[i][j].color = 2;
                    tiles[i][j].zone = 2;
                }
            }
        }
    }

    private void startGame() {

        CheckWinner();

        if (playerTurn == 1)
            doRandomAction(playerTurn);
        else {
            movePiece(doMinMax());
        }

        startGame();
    }

    private void CheckWinner() {
        if (CheckTerminal(tiles)) {
            gameUI.PrintText("\n Game has ended! \n");
            try {
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
            } catch (Exception ignored) {}
        }
    }


    private Move doMinMax() {
        Pair temp = max(tiles, playerTurn, (byte) (0), Integer.MIN_VALUE, Integer.MAX_VALUE);
        totalMoves++;
        gameUI.PrintText("\n+ Value: " + temp.value + "    totalMoves: " + totalMoves + "\n");
        return temp.move;
    }

    private Pair max(Tile[][] currentBoard, byte currentColor, byte depth, int alpha, int beta) {

        if (CheckTerminal(currentBoard))
            return new Pair(null, Integer.MIN_VALUE);
        if (depth == maxDepth)
            return new Pair(null, evaluate(currentBoard, currentColor));

        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);
        Move bestMove = null;
        int bestMoveValue = Integer.MIN_VALUE;
        for (Move move : possibleMoves) {
            Pair temp = min(board.doMove(move, currentBoard), (byte) (3 - currentColor), (byte) (depth + 1), alpha, beta);
            if (temp.value > bestMoveValue) {
                bestMoveValue = temp.value;
                bestMove = move;
                if(alpha < bestMoveValue)
                    alpha = bestMoveValue;
                if(beta <= alpha)
                    break;
            }
        }

        return new Pair(bestMove, bestMoveValue);
    }

    private Pair min(Tile[][] currentBoard, byte currentColor, byte depth, int alpha, int beta) {

        if (CheckTerminal(currentBoard))
            return new Pair(null, Integer.MAX_VALUE);

        if (depth == maxDepth)
            return new Pair(null, evaluate(currentBoard, currentColor));

        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);
        Move bestMove = null;

        int bestMoveValue = Integer.MAX_VALUE;
        for (Move move : possibleMoves) {
            Pair temp = max(board.doMove(move, currentBoard), (byte) (3 - currentColor), (byte) (depth + 1), alpha, beta);
            if (temp.value < bestMoveValue) {
                bestMoveValue = temp.value;
                bestMove = move;
                if(beta > bestMoveValue)
                    beta = bestMoveValue;
                if(beta <= alpha)
                    break;
            }
        }
        return new Pair(bestMove, bestMoveValue);
    }

    private int evaluate(Tile[][] currentBoard, byte currentColor) {
        short score = 0;
        for (byte i = 0; i < currentBoard.length; i++) {
            for (byte j = 0; j < currentBoard.length; j++) {
                if (currentBoard[i][j].color == playerTurn) {
                    score += (7 - i);
                    score += (7 - j);
                } else if (currentBoard[i][j].color == (3 - playerTurn)) {

                    score -= i;
                    score -= j;

                }
            }
        }
        return score;
    }

    private void doRandomAction(int playerTurn) {

        var possibleMoves = createPossibleMoves(tiles, playerTurn);
        var random = new Random().nextInt(possibleMoves.size() - 1);
        firstX = possibleMoves.get(random).startPos.x;
        firstY = possibleMoves.get(random).startPos.y;
        secondX = possibleMoves.get(random).finalPos.x;
        secondY = possibleMoves.get(random).finalPos.y;
        movePiece(possibleMoves.get(random));
    }

    private List<Move> createPossibleMoves(Tile[][] newBoard, int currentColor) {
        List<Move> possibleMoves = new LinkedList<>();
        for (byte i = 0; i < 8; i++)
            for (byte j = 0; j < 8; j++)
                if (tiles[i][j].color == currentColor) {
                    firstX = i;
                    firstY = j;
                    List<Tile> legalTiles = new LinkedList<>();
                    board.findPossibleMoves(newBoard, newBoard[firstX][firstY], legalTiles, newBoard[firstX][firstY], true);
                    for (Tile tile : legalTiles)
                        possibleMoves.add(new Move(newBoard[i][j], tile));
                }
        return possibleMoves;
    }

    public void changeTurn(short player) {
        gameUI.PrintText("Player %d has ended their turn.\n", player, player);
        playerTurn = (byte) (3 - player);
    }

    public void movePiece(Move move) {
        firstX = move.startPos.x;
        firstY = move.startPos.y;
        secondX = move.finalPos.x;
        secondY = move.finalPos.y;
        tiles[secondX][secondY].color = tiles[firstX][firstY].color;
        tiles[firstX][firstY].color = 0;
        changeTurn(playerTurn);
        gameUI.UpdateGUI(tiles);
    }

    private boolean CheckTerminal(Tile[][] currentTiles) {

        byte redCounter = 0;
        byte blueCounter = 0;

        for (byte x = 0; x < 8; x++) {
            for (byte y = 0; y < 8; y++) {
                if (currentTiles[x][y].zone == 1) {
                    if (currentTiles[x][y].color == 2) {
                        redCounter++;
                        if (redCounter >= 10) {
                            gameUI.PrintText("Player 2 has won");
                            return true;
                        }
                    }
                } else if (currentTiles[x][y].zone == 2) {
                    if (currentTiles[x][y].color == 1) {
                        blueCounter++;
                        if (blueCounter >= 10) {
                            gameUI.PrintText("Player 1 has won");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public void RunGame() {
        board = new Board();
        GUI jk = new GUI();
        jk.CreateBoard();
        jk.CreateTextBoxArea();
        gameUI = jk;
        setUpGame();
        CreateLayout(jk);
    }

    private void CreateLayout(GUI jk) {
        jk.setTitle("Halma");
        jk.setVisible(true);
        jk.pack();
        jk.setSize(648, 800);
        jk.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closes frame
        jk.setLocationRelativeTo(null);
        jk.setVisible(true); // makes HalmaBoard visible
        startGame();
    }

    public void setUpGame() {

        gameUI.SetCampColors();
        gameUI.AddMarbles();
        gameUI.AddFrame();
    }
}
