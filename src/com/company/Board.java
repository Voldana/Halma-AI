package com.company;

import java.util.LinkedList;
import java.util.List;

public class Board {

    private Tile[][] tiles;
    private List<Move> possibleMoves;
    public Board(Tile[][] tiles) {
        this.tiles = tiles;
    }


    public List<Tile> findPossibleMoves(Tile[][] tiles,Tile firstTile, List<Tile> possibleMoves,Tile startTile, boolean adjacent){
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
                if(firstTile.zone != 0 && firstTile.color != firstTile.zone && newTile.zone == 0)
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
                if(firstTile.zone != 0 && firstTile.color != firstTile.zone && newTile.zone == 0)
                    continue;
                if(newTile == firstTile || possibleMoves.contains(newTile))
                    continue;
                if(newTile.color == 0){
                    possibleMoves.add(newTile);
                    findPossibleMoves(tiles,firstTile,possibleMoves,newTile,false);
                }
            }
        }
        return possibleMoves;
    }

    public Tile[][] doMove(Move move,Tile[][] board){
        var clone = cloneBoard(board);
        clone[move.finalPos.x][move.finalPos.y].color = clone[move.startPos.x][move.startPos.y].color;
        clone[move.startPos.x][move.startPos.y].color = 0;
        return clone;
    }

    private Tile[][] cloneBoard(Tile[][] board){
        Tile[][] clone = new Tile[8][8];
        for(int i = 0; i < 8 ; i++)
            for(int j = 0; j < 8; j++){
                clone[i][j] = new Tile(i, j);
                clone[i][j].x = i;
                clone[i][j].y = j;
                clone[i][j].color = board[i][j].color;
                clone[i][j].zone = board[i][j].zone;
            }
        return clone;
    }

    private boolean isCoordinatesInRange(int x,int y){
        return x >= 0 && x <= 7 && y >= 0 && y <= 7;
    }
}
