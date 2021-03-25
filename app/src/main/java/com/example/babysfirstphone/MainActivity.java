package com.example.babysfirstphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;
import android.widget.ListView;
import android.view.ContextMenu;
import android.widget.Toast;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.babysfirstphone.contacts.ContactDetails;
import com.example.babysfirstphone.contacts.ContactsAdapter;
import com.example.babysfirstphone.controllers.Caller;
import com.example.babysfirstphone.controllers.Contacts;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Random;

import java.util.ArrayList;

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
        bottomNav.setOnNavigationItemSelectedListener(navListener);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomePage()).commit();

        // Set up paint buttons to change the background colors
        bgImages = new int[] {R.drawable.a, R.drawable.b,R.drawable.c, R.drawable.d, R.drawable.f,R.drawable.g };
        mainScreen = findViewById(R.id.home_start);
        paintBtn = (ImageButton) findViewById(R.id.paint);


        paintBtn = (ImageButton) findViewById(R.id.paint);
        paintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int aryLength = bgImages.length;
                Random random = new Random();
                int rNum = random.nextInt(aryLength);
                mainScreen.setBackgroundResource(bgImages[rNum]);

                // save that random number to a local storage
                SharedPreferences sharedPreferences = getSharedPreferences(COLOR, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("color",rNum);
                editor.commit();

            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.nav_settings:
                        selectedFragment = new SettingsFragment();
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