package com.example.babysfirstphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.view.View;

import com.example.babysfirstphone.controllers.Caller;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    // We make a Caller object, from Controllers/Caller.java
    Caller caller = new Caller();

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

    /** Called when the user taps the helper button */
    public void testContactActivity(View view) {
        Intent intent = new Intent(getBaseContext(), ContactDataActivity.class);
        startActivity(intent);
    }
}