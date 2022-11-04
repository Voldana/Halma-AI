package com.company;

import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class Halma {


    private Icon empty = new ImageIcon("empty");

    // Coordinates and Icons for first and second click
    private Icon firstSelectionIcon, secondSelectionIcon;
    private int firstX, firstY, secondX, secondY, prevFirstX, prevFirstY;

    private int firstSelectionLen;
    private boolean firstClick = true;
    private String firstSelectionStr;

    private final static int maxDepth = 1;
    private Board board;
    private JButton jbEndTurn;
    private int playerTurn = 1;
    private int moveCount = 0;

    // Total moves in current game
    private int grandTotalMoves = 0;

    private Tile[][] tiles;

    GUI gameUI = new GUI();

    public Halma() {
        tiles = new Tile[8][8];
        playerTurn = 1;
        assignCoordinates();

    }

    private void assignCoordinates() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
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

    public void RunGame() {
        board = new Board(tiles);

        GUI jk = new GUI();
        jk.CreateBoard();
        jk.CreateTextBoxArea();
        gameUI = jk;

        setUpGame();
        turnButton();

        // main layout
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
//        givePieceMoves();
        gameUI.AddFrame();
    }

    private void startGame() {
        if(CheckTerminal(tiles))
            return;
        
        if (playerTurn == 1)
            doRandomAction(playerTurn);
        else {
            movePiece(doMinMax());
        }

        //doRandomAction(playerTurn);

        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (Exception e) {

        }
        startGame();
    }

    private Move doMinMax() {
        var possibleMoves = createPossibleMoves(tiles, playerTurn);
        Move bestMove = null;
        int bestMoveValue = Integer.MIN_VALUE;
        for (Move move : possibleMoves) {
            int temp = min(board.doMove(move, tiles), 3 - playerTurn, 1);
            if (temp > bestMoveValue) {
                bestMove = move;
                bestMoveValue = temp;
            }
        }
        if (bestMove == null)
            return possibleMoves.get(new Random().nextInt(possibleMoves.size()));

        gameUI.PrintText("Value: " + bestMoveValue + "\n");
        return bestMove;
    }

    private int min(Tile[][] currentBoard, int currentColor, int depth) {

        if (CheckTerminal(currentBoard))
            return Integer.MAX_VALUE;

        if (depth == maxDepth)
            return evaluate(currentBoard, currentColor);

        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);

        int bestMoveValue = Integer.MAX_VALUE;
        for (Move move : possibleMoves) {
            int temp = max(board.doMove(move, tiles), 3 - currentColor, depth + 1);
            if (temp < bestMoveValue) {
                bestMoveValue = temp;
            }
        }
        return bestMoveValue;
    }

    private int max(Tile[][] currentBoard, int currentColor, int depth) {

        if (CheckTerminal(currentBoard))
            return Integer.MIN_VALUE;

        if (depth == maxDepth)
            return evaluate(currentBoard, currentColor);

        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);

        int bestMoveValue = Integer.MIN_VALUE;
        for (Move move : possibleMoves) {
            int temp = min(board.doMove(move, tiles), 3 - currentColor, depth + 1);
            if (temp > bestMoveValue) {
                bestMoveValue = temp;
            }
        }
        return bestMoveValue;
    }

    private int evaluate(Tile[][] currentBoard, int currentColor) {
        int score = 0;
        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard.length; j++) {
                if (currentBoard[i][j].color == playerTurn) {

                    score += (7 - i);
                    score += (7 - j);


                } else if (currentBoard[i][j].color == (3-playerTurn)) {

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
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
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

    public void turnButton() {

        JPanel buttonsPanel = new JPanel();
        jbEndTurn = new JButton("End Turn");

        jbEndTurn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // method that changes the players turn
                if (prevFirstX <= 4 && prevFirstY <= 4 && grandTotalMoves < 2) {
                    changeTurn(1);
                } else if (prevFirstX <= 4 && prevFirstY <= 7 && grandTotalMoves < 2) {
                    changeTurn(0);
                } else {
                    changeTurn(playerTurn);
                }

            }
        });
        buttonsPanel.add(jbEndTurn);
        gameUI.GetJpanel().add(buttonsPanel);

    }


    public void givePieceMoves() {

        for (int x = 0; x < gameUI.GetSquares().length; x++) {
            for (int y = 0; y < gameUI.GetSquares().length; y++) {

                // Add action listener to every piece in the board
                gameUI.GetSquares()[x][y].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        gameUI.SetCampColors();
                        if (firstClick) {

                            String selectionText = ((JButton) ae.getSource()).getText();
                            firstSelectionIcon = ((JButton) ae.getSource()).getIcon();
                            String[] arr = selectionText.split(",");
                            firstX = Integer.parseInt(arr[0]);
                            firstY = Integer.parseInt(arr[1]);

                            firstSelectionStr = firstSelectionIcon.toString();
                            firstSelectionLen = firstSelectionStr.length();

                            if (tiles[firstX][firstY].color == (playerTurn)) {
                                gameUI.PrintText("You have selected %s at %d, %d\n", firstSelectionStr, firstSelectionLen, firstX, firstY);
                                Tile chosenTile = tiles[firstX][firstY];
                                gameUI.ShowPossibleMoves(board.findPossibleMoves(tiles, chosenTile, null, chosenTile, true));
                                firstClick = false;
                            } else {
                                gameUI.PrintText("You have selected an empty spot.\n");
                            }

                        } else { // save information about second click
                            String selectionText = ((JButton) ae.getSource()).getText();
                            secondSelectionIcon = ((JButton) ae.getSource()).getIcon();
                            String[] arr = selectionText.split(",");
                            secondX = Integer.parseInt(arr[0]);
                            secondY = Integer.parseInt(arr[1]);

                            if (isMoveLegal()) {
                                movePiece(new Move(tiles[firstX][firstY], tiles[secondX][secondY]));
                                prevFirstX = firstX;
                                prevFirstY = firstY;
                                moveCount++;
                                grandTotalMoves++;
                                //todo: checks if there is a winner here
                            }
                            firstClick = true;
                        }
                    }
                });
            }
        }
    }

    private boolean isMoveLegal() {
        List<Tile> legalTiles = new LinkedList<>();
        board.findPossibleMoves(tiles, tiles[firstX][firstY], legalTiles, tiles[firstX][firstY], true);
        Tile targetTile = tiles[secondX][secondY];
/*        for(Move move: legalMoves)
            if(move.finalPos == targetTile)
                return true;*/
        if (legalTiles.contains(targetTile))
            return true;
        return false;
    }

    public void changeTurn(int player) {
        gameUI.PrintText("Player %d has ended their turn.\n", player, player);
        moveCount = 0;
        playerTurn = 3 - player;

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

        int redCounter = 0;
        int blueCounter = 0;

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (currentTiles[x][y].zone == 1) {
                    if (currentTiles[x][y].color == 2) {
                        redCounter++;
                        if (redCounter >= 10){
                            gameUI.PrintText("Player 2 has won");
                            return true;
                        }
                    }
                } else if (currentTiles[x][y].zone == 2) {
                    if (currentTiles[x][y].color == 1) {
                        blueCounter++;
                        if (blueCounter >= 10){
                            gameUI.PrintText("Player 1 has won");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean CheckTerminal(Tile[][] currentTiles, int color) {

        int inOpponentCampCounter = 0;

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (currentTiles[x][y].zone == (3 - color)) {
                    if (currentTiles[x][y].color == color) {
                        inOpponentCampCounter++;
                        if (inOpponentCampCounter >= 10)
                            return true;
                    }
                }
            }
        }
        return false;
    }
}
