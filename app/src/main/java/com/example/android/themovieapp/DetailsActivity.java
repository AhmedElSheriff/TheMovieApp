package com.example.android.themovieapp;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DetailsActivity extends AppCompatActivity{
    private ArrayAdapter<String> listAdapter;
    private String[] fragmentArray = {"FRAGMENT1","FRAGMENT2"};
    private ListView listView;
    DrawerLayout drawerLayout;
    String movieId;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new DetailsActivityFragment()).commit();
        }


    }


}
