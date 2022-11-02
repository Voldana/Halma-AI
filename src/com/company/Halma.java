package com.company;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
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

    private JButton jbEndTurn;
    private int playerTurn;
    private int moveCount = 0;

    private ArrayList<JButton> checkQueenWinner = new ArrayList<JButton>();
    private ArrayList<JButton> checkKingWinner = new ArrayList<JButton>();
    // Total moves in current game
    private int grandTotalMoves = 0;


    private boolean movedAdjacent = false;


    Board gameboard = new Board();

    public Halma() {

    }

    public void RunGame() {
        // displays everything in HalmaBoard()
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

        addFort();
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
                            if (validateMove()) {
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
                }); // end of actionListener
            }
        }
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
    public void addFort() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                // designing the top left corner!
                if ((x + y) <= 3) {
                    gameboard.GetSquares()[x][y].setBackground(new Color(199, 236, 255));
                }
                // designing the bottom right corner!
                if ((x + y) >= 11) {
                    gameboard.GetSquares()[x][y].setBackground(new Color(255, 199, 199));
                }
            }
        }
    }

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
        gameboard.GetSquares()[secondX][secondY].setIcon(firstSelectionIcon);
        gameboard.GetSquares()[firstX][firstY].setIcon(empty);
    } // end method movePiece

}
