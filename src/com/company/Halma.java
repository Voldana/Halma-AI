package com.company;

import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class Halma {


    private Icon empty = new ImageIcon("empty");
    private int player;

    // Coordinates and Icons for first and second click
    private Icon firstSelectionIcon, secondSelectionIcon;
    private int firstX, firstY, secondX, secondY, prevFirstX, prevFirstY, prevSecondX, prevSecondY;

    private int firstSelectionLen;
    private int clickCount = 1;
    private String firstSelectionStr;

    private Validator validator;
    private JButton jbEndTurn;
    private int playerTurn = 1;
    private int moveCount = 0;

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
        gameboard.AddMarbles();
//        givePieceMoves();
        startGame();
        gameboard.AddFrame();
    }

    private void startGame(){
            doRandomAction(playerTurn);

/*        List<Tile> legalTiles = new LinkedList<>();
        validator.findPossibleMoves(tiles[0][0],legalTiles,tiles[0][0],true);
        for(Tile tile: legalTiles)
            System.out.println("X:" + tile.x + "Y: " + tile.y);*/
    }

    private void doRandomAction(int playerTurn){
        List<Move> possibleMoves = new LinkedList<>();
        for(int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++)
                if(tiles[i][j].color == playerTurn){
                    firstX = i; firstY = j;
                    List<Tile> legalTiles = new LinkedList<>();
                    validator.findPossibleMoves(tiles[firstX][firstY],legalTiles,tiles[firstX][firstY],true);
                    possibleMoves.addAll(createPossibleMoves(legalTiles));
                }
        var random = new Random().nextInt(possibleMoves.size() - 1);
/*        firstX = possibleMoves.get(random).startPos.x;
        firstY = possibleMoves.get(random).startPos.y;
        secondX = possibleMoves.get(random).finalPos.x;
        secondY = possibleMoves.get(random).finalPos.y;*/
        movePiece(possibleMoves.get(random));
    }

    private List<Move> createPossibleMoves(List<Tile> possibleTiles){
        List<Move> possibleMoves = new LinkedList<>();
        for(Tile tile: possibleTiles)
            possibleMoves.add(new Move(tiles[firstX][firstY] , tile));
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
//                                movePiece();
                                prevSecondX = secondX;
                                prevSecondY = secondY;
                                prevFirstX = firstX;
                                prevFirstY = firstY;
                                moveCount++;
                                grandTotalMoves++;
                                //todo: checks if there is a winner here
//                                turnButton();
                            }
                            clickCount--;
                        }
                    }
                });
            }
        }
    }

    private boolean isMoveLegal(){
        List<Tile> legalTiles = new LinkedList<>();
        validator.findPossibleMoves(tiles[firstX][firstY],legalTiles,tiles[firstX][firstY],true);
        Tile targetTile = tiles[secondX][secondY];
/*        for(Move move: legalMoves)
            if(move.finalPos == targetTile)
                return true;*/
        if(legalTiles.contains(targetTile))
            return true;
        return false;
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



    public void movePiece(Move move) {
/*        if(hasJumped())
            validator.jumped = true;*/
        firstX = move.startPos.x;
        firstY = move.startPos.y;
        secondX = move.finalPos.x;
        secondY = move.finalPos.y;
        System.out.println("Moved " + firstX + firstY +" To " + secondX + secondY);
        tiles[secondX][secondY].color = tiles[firstX][firstY].color;
        tiles[firstX][firstY].color = 0;
        gameboard.GetSquares()[secondX][secondY].setIcon(firstSelectionIcon);
        gameboard.GetSquares()[firstX][firstY].setIcon(empty);
        changeTurn(playerTurn);
        startGame();
    }

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
