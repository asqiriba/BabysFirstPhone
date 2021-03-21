package com.example.babysfirstphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.babysfirstphone.controllers.Caller;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // We make a Caller object, from Controllers/Caller.java
    Caller caller = new Caller();

    View mainScreen;
    ImageButton paintBtn;
    int[] images;
    String COLOR = "colorTheme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Call interface
        ImageButton button = (ImageButton) findViewById(R.id.image_button_android);
        button.setOnClickListener(view -> callPhoneNumber());

        // Bottom Menu
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomePage()).commit();

        // Set up paint buttons to change the background colors
        images = new int[] {R.drawable.a, R.drawable.b,R.drawable.c, R.drawable.d, R.drawable.f,R.drawable.g };
        mainScreen = findViewById(R.id.home_start);
        paintBtn = (ImageButton) findViewById(R.id.paint);

        paintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int aryLength = images.length;
                Random random = new Random();
                int rNum = random.nextInt(aryLength);
                mainScreen.setBackgroundResource(images[rNum]);

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

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        /*
          This function checks on system permissions to make phone calls,
          then connects the phone call.
         */
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhoneNumber();
            }
        }
    }

    public void callPhoneNumber() {

        // If permission is already given, connect a call.
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.CALL_PHONE}, 101);
                    return;
                }
            }
            startActivity(caller.makeCall());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

