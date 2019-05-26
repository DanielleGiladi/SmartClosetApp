package com.example.danie.smartclosetapp;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements NewOutfit.OnFragmentInteractionListener ,
        AddItem.OnFragmentInteractionListener, Weather.OnFragmentInteractionListener,
        Favorite.OnFragmentInteractionListener {

    private Toolbar mainToolBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mainToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mainToolBar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("NEW OUTFIT"));
        tabLayout.addTab(tabLayout.newTab().setText("FAVORITE"));
        tabLayout.addTab(tabLayout.newTab().setText("ADD ITEM"));
        tabLayout.addTab(tabLayout.newTab().setText("WEATHER"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager() , tabLayout.getTabCount());
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

           @Override
        protected void onStart() {
            super.onStart();
            sendToLogin();


    }

    private void sendToLogin() {
          FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if(currentUser == null){
                Intent LoginIntent = new Intent(MainActivity.this , LoginActivity.class);
                startActivity(LoginIntent);
                finish();
            }


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu){
      getMenuInflater().inflate(R.menu.main_menu, menu);

       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutBtn:
                logout();
                return true;

            case R.id.aboutAsBtn:
                aboutAs();
                return true;

                default:
                    return  false;
        }

    }

    private void logout() {
        mAuth.signOut();
        sendToLogin();
    }

    private void aboutAs() {
        Intent AboutASIntent = new Intent(MainActivity.this , AboutAs.class);
        startActivity(AboutASIntent);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
// do noting
    }



}
