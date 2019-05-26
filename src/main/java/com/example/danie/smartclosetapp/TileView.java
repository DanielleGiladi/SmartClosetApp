package com.example.danie.smartclosetapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class TileView extends LinearLayout {

    ImageView image;


    public TileView(Context context) {
        super(context);
        this.setOrientation(VERTICAL);
        image = new ImageView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        image.setLayoutParams(layoutParams);
        image.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setBackgroundColor(0xFF4682B4);
        image.setVisibility(VISIBLE);




        addView(image);
    }
}
