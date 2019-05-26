package com.example.danie.smartclosetapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.danie.smartclosetapp.Logic_new_outfit.Frame;

import java.util.ArrayList;

class CustomGridViewAdapter extends BaseAdapter {
    ArrayList<String> imageList;
    private Context mContext;
    private Frame mFrame;

    public CustomGridViewAdapter(ArrayList<String> imagesPath, Context mContext , Frame frame) {
        imageList = imagesPath;
        this.mContext=mContext;
        this.mFrame = frame;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_grid, parent, false);

            // well set up the ViewHolder
            ImageView im = convertView.findViewById(R.id.imageView);
            // store the holder with the view.
            Glide.with(mContext).load(imageList.get(position))
                .into(im);

            if(mFrame.getTile(position).getStatus() == Frame.TileState.CHOSEN ){
                im.setAlpha(150);
         }

        if(mFrame.getTile(position).getStatus() == Frame.TileState.NONE ){
            Glide.with(mContext).load(imageList.get(position))
                    .into(im);
        }



            return convertView;
        }
    }
