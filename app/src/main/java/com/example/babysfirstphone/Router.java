package com.example.babysfirstphone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import com.example.babysfirstphone.controllers.Caller;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.widget.Toast;

public class Router extends AppCompatActivity {

    Caller caller = new Caller();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router);

        getData();
        finish();
        
    }


    public void getData(){
        if(getIntent().getStringExtra("type").equals("phone")){
            callPhoneNumber();
        }
        else{
            Toast.makeText(this, getIntent().getStringExtra("type"), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        }
    }


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
                    ActivityCompat.requestPermissions(Router.this, new String[]{
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