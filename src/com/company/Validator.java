package com.company;

import java.util.LinkedList;
import java.util.List;

public class Validator {

    private Tile[][] tiles;
    public boolean movedOnce = false;
    public boolean jumped = false;
    public Validator(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public List<Tile> findPossibleMoves(int startX,int startY){
        List<Tile> possibleMoves = new LinkedList<>();
        int x;
        int y;
        Tile startPos = tiles[startX][startY];
        for(int i = -1; i <=1; i++){
            for(int j = -1; j <=1 ; j++){
                x = startX + i;
                y = startY + j;
                //Moving out of goal is illegal
                if(startPos.zone != 0 && startPos.color != startPos.zone && tiles[x][y].zone == 0)
                    continue;

                if(isCoordinatesInRange(x,y)){
                    if(tiles[x][y].color == 0 && !movedOnce)
                        possibleMoves.add(tiles[x][y]);
                    else if((!movedOnce || jumped) && (i != 0 || j!=0)){
                        x += i;
                        y += j;
                        if(!isCoordinatesInRange(x,y))
                            continue;
                        if(tiles[x][y].color == 0)
                            possibleMoves.add(tiles[x][y]);
                    }
                }
            }
        }
        return possibleMoves;
    }

    public void endTurn(){
        movedOnce = false;
        jumped = false;
    }
    private boolean isCoordinatesInRange(int x,int y){
        return x >= 0 && x <= 7 && y >= 0 && y <= 7;
    }
}