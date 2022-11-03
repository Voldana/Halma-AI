package com.company;

import java.util.LinkedList;
import java.util.List;

public class Validator {

    private Tile[][] tiles;
    public boolean movedOnce = false;
    public boolean jumped = false;

    private List<Move> possibleMoves;
    public Validator(Tile[][] tiles) {
        this.tiles = tiles;
    }


    public List<Tile> findPossibleMoves(Tile firstTile, List<Tile> possibleMoves,Tile startTile, boolean adjacent){
        if(possibleMoves == null)
            possibleMoves = new LinkedList<>();
        int x; int y;
        Tile newTile;
        for(int i = -1; i <=1; i++) {
            for (int j = -1; j <= 1; j++) {
                x = startTile.x + i;
                y = startTile.y + j;
                if(!isCoordinatesInRange(x,y))
                    continue;
                newTile = tiles[x][y];
                if(startTile.zone != 0 && startTile.color != startTile.zone && newTile.zone == 0)
                    continue;
                if(newTile.color == 0){
                    if(adjacent)
                        possibleMoves.add(newTile);
                    continue;
                }
                x += i;
                y += j;
                if(!isCoordinatesInRange(x,y))
                    continue;
                newTile = tiles[x][y];
                if(newTile == firstTile || possibleMoves.contains(newTile))
                    continue;
                if(newTile.color == 0){
                    possibleMoves.add(newTile);
                    findPossibleMoves(firstTile,possibleMoves,newTile,false);
                }
            }
        }
        return possibleMoves;
    }

    private boolean isCoordinatesInRange(int x,int y){
        return x >= 0 && x <= 7 && y >= 0 && y <= 7;
    }
}
