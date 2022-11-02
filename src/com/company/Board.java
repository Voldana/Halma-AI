package com.company;

import javax.swing.*;
import java.awt.*;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JFrame{

    private JPanel board;
    private JButton[][] squares = new JButton[8][8];

    private JTextArea jPrintArea;
    JPanel jpNavigationRight = new JPanel(new GridLayout(0, 1));

    public JPanel GetBoard(){
        return board;
    }

    public JButton[][] GetSquares(){
        return squares;
    }

    public JPanel GetJpanel(){
        return jpNavigationRight;
    }

    public void CreateBoard(){

        // Halma Board Layout - this creates the whole board
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



    public void AddFrame(){
        add(board, BorderLayout.CENTER);
    }

    public void PrintText(String text){
        jPrintArea.append(text);
    }
    public void PrintText(String text, int x, int y){
        jPrintArea.append(String.format(text, x, y));
    }
    public void PrintText(String text,String select, int length, int x, int y){
        jPrintArea.append(String.format(text,select.substring(0, length - 4), x, y));
    }

    public void ClearTextBox(){
        jPrintArea.setText(null);
    }

}
