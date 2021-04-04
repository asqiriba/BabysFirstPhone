package com.example.babysfirstphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView dataList;
    List<String> titles;
    List<Integer> images;
    Adapter adapter;


    View mainScreen;
    ImageButton paintBtn;
    int[] bgImages;
    String COLOR = "colorTheme";
    int getNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recycler View
        dataList = findViewById(R.id.dataList);
        titles = new ArrayList<>();
        images = new ArrayList<>();

        titles.add("phone");
        titles.add("video call");
        titles.add("website");
        titles.add("placeholder");
        titles.add("placeholder");
        titles.add("placeholder");
        titles.add("placeholder");
        titles.add("placeholder");
        titles.add("placeholder");
        titles.add("placeholder");
        titles.add("placeholder");
        titles.add("placeholder");


        images.add(R.drawable.img_avatar);
        images.add(R.drawable.img_avatar2);
        images.add(R.drawable.image_avatar3);
        images.add(R.drawable.img_avatar);
        images.add(R.drawable.img_avatar2);
        images.add(R.drawable.image_avatar3);
        images.add(R.drawable.img_avatar);
        images.add(R.drawable.img_avatar2);
        images.add(R.drawable.image_avatar3);
        images.add(R.drawable.img_avatar);
        images.add(R.drawable.img_avatar2);
        images.add(R.drawable.image_avatar3);




        adapter = new Adapter(this, titles, images);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);


        // Bottom Menu
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        // Change the grey scale to colors
        bottomNav.setItemIconTintList(null);
        // Change the size of the icons in the bottom Navigation Bar
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNav.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(com.google.android.material.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            // set your height here
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, displayMetrics);
            // set your width here
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

        bottomNav.setOnNavigationItemSelectedListener(navListener);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomePage()).commit();

        // Set up paint buttons to change the background colors
        bgImages = new int[] {R.drawable.green_background, R.drawable.red_background,R.drawable.pink_background, R.drawable.purple_background, R.drawable.blue_background, R.drawable.yellow_background  };
        mainScreen = findViewById(R.id.home_start);

        SharedPreferences sharedPreferences = getSharedPreferences("colorTheme", Context.MODE_PRIVATE);
        getNum = sharedPreferences.getInt("color",0);
        mainScreen.setBackgroundResource(bgImages[getNum]);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.nav_paint:
                        selectedFragment = new BackgroundColorFragment();
                        break;
                    case R.id.nav_emergency:
                        selectedFragment = new EmergencyFragment();
                        break;
                    case R.id.nav_groups:
                        selectedFragment = new GroupsFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            };
}