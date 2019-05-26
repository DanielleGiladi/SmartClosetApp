package com.example.danie.smartclosetapp.Logic_new_outfit;

public class Frame {

    private int size;

    public enum TileState {
        CHOSEN , NONE;

        @Override
        public String toString() {
            switch(this) {
                case NONE:
                default:
                    return "";
                case CHOSEN:
                    return "CHOSEN";
            }
        }
    }

    private Tile mTiles[] ;


    public Frame(int size) {
        this.size = size;
        this.mTiles = new Tile[size];

        for(int i = 0 ; i< mTiles.length ; i++) {
            mTiles[i] = new Tile();
        }
    }

    public int getFrameSize() {
        return mTiles.length;
    }

    public Tile getTile(int position) {
        return mTiles[position];
    }

    public Boolean setTile(int position){
        if(mTiles[position].getStatus() == TileState.NONE){
            mTiles[position].setStatus(TileState.CHOSEN);
            return true;
        }
        else  if(mTiles[position].getStatus() == TileState.CHOSEN){
            mTiles[position].setStatus(TileState.NONE);
            return true;
        }
        return false;
    }
}
