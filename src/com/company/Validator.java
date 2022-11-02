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
                if(newTile.color == 0 && adjacent){
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

    /*public List<Move> findPossibleMoves(int startX,int startY){
        possibleMoves = new LinkedList<>();
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
                        possibleMoves.add(new Move(startPos,tiles[x][y]));
                    else if((!movedOnce || jumped) && (i != 0 || j!=0)){
                        x += i;
                        y += j;
                        if(!isCoordinatesInRange(x,y))
                            continue;
                        if(tiles[x][y].color == 0){
                            possibleMoves.add(new Move(startPos,tiles[x][y]));
                            searchForJumps(startPos,tiles[x][y]);
                        }
                    }
                }
            }
        }
        return possibleMoves;
    }

    private List<Move> searchForJumps(Tile startPos, Tile newPos){
        int x;int y;
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                x = newPos.x + i;
                y = newPos.y + j;
                if(isCoordinatesInRange(x,y) && tiles[x][y] != startPos)
            }
        }
    }*/

    public void endTurn(){
        movedOnce = false;
        jumped = false;
    }
    private boolean isCoordinatesInRange(int x,int y){
        return x >= 0 && x <= 7 && y >= 0 && y <= 7;
    }
}
