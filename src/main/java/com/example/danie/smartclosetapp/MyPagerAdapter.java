package com.example.danie.smartclosetapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {

    int numOfTab;

    public MyPagerAdapter(FragmentManager fm, int NumOfTab) {
        super(fm);
        this.numOfTab = NumOfTab;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
               // NewOutfit newOutfit = new NewOutfit();
                RootFragment rootFragment = new RootFragment();
                return rootFragment;//newOutfit;
            case 1:
                Favorite favotite = new Favorite();
                return favotite;
            case 2:
                AddItem addItem = new AddItem();
                return addItem;
            case 3:
                Weather weather = new Weather();
                return weather;
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return numOfTab;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "new outfit";
            case 1:
                return "favorite";
            case 2:
                return "add item";
            case 3:
                return "weather";
            default:
                return null;
        }
    }
}
