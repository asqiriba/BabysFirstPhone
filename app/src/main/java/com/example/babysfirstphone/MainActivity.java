package com.example.babysfirstphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
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

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView dataList;
    List<String> titles;
    List<Integer> images;
    Adapter adapter;


    // We used ArrayList because its size can be increased or reduced dynamically.
//    ArrayList<Contacts> arrayListContact;
//    ContactsAdapter contactAdapter;
//    Contacts contacts;
//
//    Button contactAddButton;
//    ListView listContacts;
//
//    final int CONTACT_VIEW = 1, CONTACT_DELETE = 2;

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
        titles.add("AR");
        titles.add("4");
        titles.add("5");
        titles.add("6");
        titles.add("7");
        titles.add("8");
        titles.add("9");
        titles.add("10");
        titles.add("11");
        titles.add("12");
        titles.add("13");
        titles.add("14");
        titles.add("15");

        images.add(R.drawable.img_avatar);
        images.add(R.drawable.img_avatar2);
        images.add(R.drawable.image_avatar3);
        images.add(R.drawable.image_avatar4);
        images.add(R.drawable.img_avatar);
        images.add(R.drawable.img_avatar2);
        images.add(R.drawable.image_avatar3);
        images.add(R.drawable.image_avatar4);
        images.add(R.drawable.img_avatar);
        images.add(R.drawable.img_avatar2);
        images.add(R.drawable.image_avatar3);
        images.add(R.drawable.image_avatar4);
        images.add(R.drawable.img_avatar);
        images.add(R.drawable.img_avatar2);
        images.add(R.drawable.image_avatar3);
        images.add(R.drawable.image_avatar4);


        adapter = new Adapter(this, titles, images);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);


        // Bottom Menu
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomePage()).commit();

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