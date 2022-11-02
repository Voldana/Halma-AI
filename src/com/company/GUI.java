package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GUI extends JFrame {

    private JPanel board;
    private JButton[][] squares = new JButton[8][8];

    private JTextArea jPrintArea;
    JPanel jpNavigationRight = new JPanel(new GridLayout(0, 1));

    public JButton[][] GetSquares() {
        return squares;
    }

    public JPanel GetJpanel() {
        return jpNavigationRight;
    }

    private Icon homePiece = new ImageIcon();
    private Icon awayPiece = new ImageIcon();
    private Icon empty = new ImageIcon("empty");

    public void CreateBoard() {

        board = new JPanel(new GridLayout(8, 8));
        board.setSize(400, 400);
        for (int x = 0; x < squares.length; x++) {
            for (int y = 0; y < squares[x].length; y++) {
                // creates the button
                squares[x][y] = new JButton(x + "," + y);
                squares[x][y].setPreferredSize(new Dimension(50, 50));
                // makes the color by default black
                squares[x][y].setForeground(Color.BLACK);
                squares[x][y].setOpaque(true);
                // adds the buttons
                board.add(squares[x][y]);
            }
        }


    }

    public void CreateTextBoxArea() {
        JPanel jpText = new JPanel(new GridLayout(0, 1));
        jPrintArea = new JTextArea(5, 10);
        jPrintArea.requestFocusInWindow();
        jpText.add(jPrintArea);
        JScrollPane scroll = new JScrollPane(jPrintArea);
        jpText.add(scroll);
        jpNavigationRight.add(jpText);
        add(jpNavigationRight, BorderLayout.SOUTH);
    }

    public void SetCampColors() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                squares[x][y].setBackground(new Color(255, 255, 255));
                if ((x + y) <= 3) {
                    squares[x][y].setBackground(new Color(199, 230, 255));
                }
                if ((x + y) >= 11) {
                    squares[x][y].setBackground(new Color(255, 199, 199));
                }
            }
        }
    }

    public void AddMarbles() {
        homePiece = new ImageIcon("red.png"); // these are the default icons
        awayPiece = new ImageIcon("blue.png"); // these are the default icons
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {

                if ((x + y) <= 3) {
                    squares[x][y].setIcon(awayPiece);
                } else if ((x + y) >= 11) {
                    squares[x][y].setIcon(homePiece);
                } else {
                    squares[x][y].setIcon(empty);
                }
            }
        }
    }

    public void UpdateGUI(Tile[][] tiles) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if(tiles[x][y].color == 1){
                    squares[x][y].setIcon(awayPiece);
                }
                else if(tiles[x][y].color == 2){
                    squares[x][y].setIcon(homePiece);
                }
                else {
                    squares[x][y].setIcon(empty);
                }
            }
        }
    }

    public void ShowPossibleMoves(List<Tile> possibleTiles){
        for(Tile tile : possibleTiles){
            squares[tile.x][tile.y].setBackground(Color.green);
        }
    }

    public void AddFrame() {
        add(board, BorderLayout.CENTER);
    }

    public void PrintText(String text) {
        jPrintArea.append(text);
    }

    public void PrintText(String text, int x, int y) {
        jPrintArea.append(String.format(text, x, y));
    }

    public void PrintText(String text, String select, int length, int x, int y) {
        jPrintArea.append(String.format(text, select.substring(0, length - 4), x, y));
    }

    public void ClearTextBox() {
        jPrintArea.setText(null);
    }

}
