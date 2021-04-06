package com.example.babysfirstphone;

import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.babysfirstphone.controllers.Contacts;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView dataList;
    List<String> type;
    List<Integer> images;
    List<String> imageID;
    List<String> info;
    Adapter adapter;
    ArrayList<Contacts> arrayListContact;

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
        type = new ArrayList<>();
        images = new ArrayList<>();
        info = new ArrayList<>();
        imageID = new ArrayList<>();

//        loadData();
//        System.out.println(arrayListContact.get(0).getNumber());
//
//        for(int i = 0; i < arrayListContact.size() ; i++){
//            type.add(arrayListContact.get(i).getType());
//        }
//
//        for(int i = 0; i < arrayListContact.size() ; i++){
//            imageID.add(arrayListContact.get(i).getImageId());
//        }
//
//        for(int i = 0; i < arrayListContact.size() ; i++){
//            info.add(arrayListContact.get(i).getNumber());
//        }



        type.add("phone");
        type.add("video");
        type.add("app");
        type.add("placeholder");
        type.add("placeholder");
        type.add("placeholder");
        type.add("placeholder");
        type.add("placeholder");
        type.add("placeholder");
        type.add("placeholder");
        type.add("placeholder");
        type.add("placeholder");

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

        info.add("1-760-123-4567");
        info.add("1-760-840-1625");
        info.add("3");
        info.add("4");
        info.add("5");
        info.add("6");
        info.add("7");
        info.add("8");
        info.add("9");
        info.add("10");
        info.add("11");
        info.add("12");



        adapter = new Adapter(this, type, images, info);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);

        // Top Menu
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

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


        // Set up paint buttons to change the background colors
        bgImages = new int[] {R.drawable.green_background, R.drawable.red_background,R.drawable.pink_background, R.drawable.purple_background, R.drawable.blue_background, R.drawable.yellow_background  };
        mainScreen = findViewById(R.id.home_start);

        SharedPreferences sharedPreferences = getSharedPreferences("colorTheme", Context.MODE_PRIVATE);
        getNum = sharedPreferences.getInt("color",0);
        mainScreen.setBackgroundResource(bgImages[getNum]);

        // Requests permission to read texts
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkCallingOrSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1000);
        }


    }

    // Obtains stored contact info
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("contact list", null);
        Type type = new TypeToken<ArrayList<Contacts>>() {}.getType();
        arrayListContact = gson.fromJson(json, type);

        if (arrayListContact == null || arrayListContact.isEmpty()) {
            arrayListContact = new ArrayList<Contacts>();
        }
    }

    // Read Texts Permission
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == 1000){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    // Top Menu for settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_navigation, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.top_settings){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
        }
        return super.onOptionsItemSelected(item);
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
