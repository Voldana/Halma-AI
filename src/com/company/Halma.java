package com.company;

import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;

public class Halma {


    private Icon firstSelectionIcon;
    private int firstX, firstY, secondX, secondY;

    private int firstSelectionLen;
    private boolean firstClick = true;
    private String firstSelectionStr;

    private Board board;
    private int playerTurn;

    private final Tile[][] tiles;

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

        jk.setTitle("Halma");
        jk.setVisible(true);
        jk.pack();
        jk.setSize(648, 800);
        jk.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jk.setLocationRelativeTo(null);
        jk.setVisible(true);
    }

    public void setUpGame() {

        gameUI.SetCampColors();
        gameUI.AddMarbles();
        givePieceMoves();
        gameUI.AddFrame();
    }

    public void givePieceMoves() {

        for (int x = 0; x < gameUI.GetSquares().length; x++) {
            for (int y = 0; y < gameUI.GetSquares().length; y++) {


                gameUI.GetSquares()[x][y].addActionListener(ae -> {
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

                    } else {
                        String selectionText = ((JButton) ae.getSource()).getText();
                        String[] arr = selectionText.split(",");
                        secondX = Integer.parseInt(arr[0]);
                        secondY = Integer.parseInt(arr[1]);

                        if (isMoveLegal()) {
                            movePiece(new Move(tiles[firstX][firstY], tiles[secondX][secondY]));
                        }
                        firstClick = true;
                    }
                });
            }
        }
    }

    private boolean isMoveLegal() {
        List<Tile> legalTiles = new LinkedList<>();
        board.findPossibleMoves(tiles, tiles[firstX][firstY], legalTiles, tiles[firstX][firstY], true);
        Tile targetTile = tiles[secondX][secondY];

        return legalTiles.contains(targetTile);
    }

    public void changeTurn(int player) {
        gameUI.PrintText("Player %d has ended their turn.\n", player, player);
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

}
