package com.example.babysfirstphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
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
    List<String> type;
    List<Integer> images;
    List<String> info;
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
        type = new ArrayList<>();
        images = new ArrayList<>();
        info = new ArrayList<>();

        type.add("phone");
        type.add("video");
        type.add("website");
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
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        // Set up paint buttons to change the background colors
        bgImages = new int[] {R.drawable.a, R.drawable.b,R.drawable.c, R.drawable.d, R.drawable.f,R.drawable.g };
        mainScreen = findViewById(R.id.home_start);

        // Requests permission to read texts
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkCallingOrSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1000);
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

    // Bottom Menu
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.nav_settings:
                        int aryLength = bgImages.length;
                        Random random = new Random();
                        int rNum = random.nextInt(aryLength);
                        mainScreen.setBackgroundResource(bgImages[rNum]);
                        // save that random number to a local storage
                        SharedPreferences sharedPreferences = getSharedPreferences(COLOR, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("color",rNum);
                        editor.commit();
                        break;
                    case R.id.nav_emergency:
                        selectedFragment = new EmergencyFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                        break;
                    case R.id.nav_groups:
                        selectedFragment = new GroupsFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                        break;
                }
                return true;
            };
}
