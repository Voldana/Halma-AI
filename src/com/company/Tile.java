package com.company;

public class Tile {
    int x;
    int y;
    // 0 is empty 1 is red and 2 is blue
    int color = 0;
    // 0 is none, 1 is red zone and 2 is blue zone

    int zone = 0;
    public Tile(int x,int y){
        this.x = x;
        this.y = y;
    }
}
