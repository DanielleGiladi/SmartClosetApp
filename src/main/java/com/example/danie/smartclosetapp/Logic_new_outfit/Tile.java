package com.example.danie.smartclosetapp.Logic_new_outfit;

public class Tile  {


    private Frame.TileState mStatus;

    public Tile() {

        mStatus = Frame.TileState.NONE;

    }

    public Frame.TileState getStatus() {
        return mStatus;
    }

    public void setStatus(Frame.TileState status) {
        this.mStatus = status;
    }
}
