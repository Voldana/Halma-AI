package com.company;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class Halma {

    // Menu Items
    private JMenuItem mItemExit;
    // Board Icons
    private Icon homePiece = new ImageIcon();
    private Icon awayPiece = new ImageIcon();
    private Icon empty = new ImageIcon("empty");

    // Coordinates and Icons for first and second click
    private Icon firstSelectionIcon, secondSelectionIcon;
    private int firstX, firstY, secondX, secondY, prevFirstX, prevFirstY, prevSecondX, prevSecondY;
    // Useful adaptations of first click
    private int firstSelectionLen;
    private int clickCount = 1;
    private String firstSelectionStr;

    private Validator validator;
    private JButton jbEndTurn;
    private int playerTurn;
    private int moveCount = 0;

    private ArrayList<JButton> checkQueenWinner = new ArrayList<JButton>();
    private ArrayList<JButton> checkKingWinner = new ArrayList<JButton>();
    // Total moves in current game
    private int grandTotalMoves = 0;

    private Tile[][] tiles;

    private boolean movedAdjacent = false;


    Board gameboard = new Board();

    public Halma() {
        tiles = new Tile[8][8];
        assignCoordinates();
    }

    private void assignCoordinates() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j] = new Tile(i,j);

                if((i+j) <= 3){
                    tiles[i][j].color = 1;
                    tiles[i][j].zone = 1;
                }
                else if((i+j) >= 11){
                    tiles[i][j].color = 2;
                    tiles[i][j].zone = 2;
                }
            }
        }
    }

    public void RunGame() {
        validator = new Validator(tiles);

        Board jk = new Board();
        jk.CreateBoard();
        jk.CreateTextBoxArea();
        gameboard = jk;

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
    }

    public void setUpGame() {

        gameboard.SetCampColors();
        addPieces();
        givePieceMoves();
        gameboard.AddFrame();
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
        gameboard.GetJpanel().add(buttonsPanel);

    }


    public void givePieceMoves() {
        // Iterate through every piece in board
        for (int x = 0; x < gameboard.GetSquares().length; x++) {
            for (int y = 0; y < gameboard.GetSquares().length; y++) {

                // Add action listener to every piece in the board
                gameboard.GetSquares()[x][y].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        if (clickCount == 1) { // save information about first click

                            String selectionText = ((JButton) ae.getSource()).getText();
                            firstSelectionIcon = ((JButton) ae.getSource()).getIcon();
                            String[] arr = selectionText.split(",");
                            firstX = Integer.parseInt(arr[0]);
                            firstY = Integer.parseInt(arr[1]);

                            firstSelectionStr = firstSelectionIcon.toString();
                            firstSelectionLen = firstSelectionStr.length();

                            if (!firstSelectionIcon.equals(empty)) {
                                gameboard.PrintText("You have selected %s at %d, %d\n", firstSelectionStr, firstSelectionLen, firstX, firstY);

                            } else {
                                gameboard.PrintText("You have selected an empty spot.\n");
                            }

                            clickCount++;
                        } else { // save information about second click
                            String selectionText = ((JButton) ae.getSource()).getText();
                            secondSelectionIcon = ((JButton) ae.getSource()).getIcon();
                            String[] arr = selectionText.split(",");
                            secondX = Integer.parseInt(arr[0]);
                            secondY = Integer.parseInt(arr[1]);

                            // validation - if validates do movePiece
                            if (isMoveLegal()) {
                                validator.movedOnce = true;
                                movePiece();
                                prevSecondX = secondX;
                                prevSecondY = secondY;
                                prevFirstX = firstX;
                                prevFirstY = firstY;
                                moveCount++;
                                grandTotalMoves++;
                                // isWinner(); // checks if there is a winner
                            }
                            clickCount--;
                        }
                    }
                });
            }
        }
    }

    private boolean isMoveLegal(){
        List<Tile> legalMoves = validator.findPossibleMoves(firstX,firstY);
        Tile targetTile = tiles[secondX][secondY];
        if(legalMoves.contains(targetTile))
            return true;
        return false;
    }

    public boolean validateMove() {
        // is the first move the same as the previous first piece move?
        if (moveCount > 0) {
            if (firstX != prevSecondX && firstY != prevSecondY) {
                gameboard.PrintText("You have already moved a piece to %d, %d\n", prevSecondX, prevSecondY);
                return false;
            }
            if (secondY == prevFirstY && secondX == prevFirstX) {
                gameboard.PrintText("You you cannot move back to %d, %d\n", prevSecondX, prevSecondY);
                return false;
            }

        }

        // Print appropriate message for valid moves
        if (!firstSelectionIcon.equals(empty) && secondSelectionIcon.equals(empty)) { // move to adjacent spot
            if (Math.abs(secondX - firstX) <= 1 && Math.abs(secondY - firstY) <= 1) {
                if (moveCount == 0) {
                    gameboard.PrintText("You have moved to %d, %d\n", secondX, secondY);
                    movedAdjacent = true;
                    return true;
                } else {
                    gameboard.PrintText("You cannot move here%n");
                    return false;
                }
            } else if ((Math.abs(secondX - firstX) == 0 || Math.abs(secondX - firstX) == 2) &&
                    (Math.abs(secondY - firstY) == 0 || Math.abs(secondY - firstY) == 2)) { // jumping pieces
                if (checkJumpedPiece()) { // if there is a piece being jumped over
                    if (movedAdjacent == true) {
                        gameboard.PrintText("You cannot move here\n");
                        return false;
                    } else {
                        gameboard.PrintText("You have moved to %d, %d\n", secondX, secondY);
                        movedAdjacent = false;
                        return true;
                    }
                }
            }
        }
        // Print appropriate message for invalid moves
        if (firstSelectionIcon.equals(empty)) {
            gameboard.PrintText("Empty spot deselected.\n");
            return false;
        } else if (secondSelectionIcon.equals(firstSelectionIcon)) {
            gameboard.PrintText(firstSelectionStr.substring(0, firstSelectionLen - 4) + " has been deselected\n");
            return false;
        } else {
            gameboard.PrintText("You cannot move here\n");
            return false;
        }
    }

    public int changeTurn(int player) {
        validator.endTurn();
        if (player == 0) {
            gameboard.PrintText("Player 1 has ended their turn.\n");
            player++;
            movedAdjacent = false;
            moveCount = 0;
            return playerTurn = player;
        } else {
            gameboard.PrintText("Player 2 has ended their turn.\n");
            movedAdjacent = false;
            moveCount = 0;
            return playerTurn = player;
        }
    }


    public boolean checkJumpedPiece() {
        // Calculate position of spot being jumped over
        int averageX = Math.abs((secondX + firstX) / 2);
        int averageY = Math.abs((secondY + firstY) / 2);
        // If the spot contains a piece, it is a valid move
        if (!gameboard.GetSquares()[averageX][averageY].getIcon().equals(empty)) {
            return true;
        }
        return false;
    }

    // This is a different color - for where the pieces will belong
    // The orignal color of black will be changed to green


    public void addPieces() {
        homePiece = new ImageIcon("red.png"); // these are the default icons
        awayPiece = new ImageIcon("blue.png"); // these are the default icons
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                // designing the top left corner!
                if ((x + y) <= 3) {
                    gameboard.GetSquares()[x][y].setIcon(awayPiece);
                }
                // designing the bottom right corner!
                else if ((x + y) >= 11) {
                    gameboard.GetSquares()[x][y].setIcon(homePiece);
                } else {
                    gameboard.GetSquares()[x][y].setIcon(empty);
                }
            }
        }
    } // end method addPieces

    // basically it moves the pieces and refreshes layout
    public void movePiece() {
        if(hasJumped())
            validator.jumped = true;
        tiles[secondX][secondY].color = tiles[firstX][firstY].color;
        tiles[firstX][firstY].color = 0;
        gameboard.GetSquares()[secondX][secondY].setIcon(firstSelectionIcon);
        gameboard.GetSquares()[firstX][firstY].setIcon(empty);
    } // end method movePiece

    private boolean hasJumped(){
        return Math.abs(firstX - secondX) > 1 || Math.abs(firstY - secondY) > 1;
    }


    private boolean CheckTerminal()
    {

        int redCounter = 0;
        int blueCounter = 0;

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (tiles[x][y].zone == 1) {
                    if (tiles[x][y].color == 2) {
                        redCounter++;
                        if(redCounter >= 10)
                            return true;
                    }
                }
                else if (tiles[x][y].zone == 2) {
                    if (tiles[x][y].color == 1) {
                        blueCounter++;
                        if(blueCounter >= 10)
                            return true;
                    }
                }
            }
        }
        return false;
    }
    private boolean CheckTerminal(int color) {

        int inOpponentCampCounter = 0;

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (tiles[x][y].zone == (3-color)) {
                    if (tiles[x][y].color == color) {
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
