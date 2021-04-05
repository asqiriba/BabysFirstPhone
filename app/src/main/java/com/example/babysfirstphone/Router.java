package com.example.babysfirstphone;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.example.babysfirstphone.controllers.Caller;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.SmsManager;
import android.widget.Toast;


public class Router extends AppCompatActivity {

    Caller caller = new Caller();
    private BroadcastReceiver mIntentReceiver;
    boolean videoCallRequest = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router);
        getData();



    }


    public void getData(){
        if(getIntent().getStringExtra("type").equals("phone")){
            callPhoneNumber(getIntent().getStringExtra("info"));
            finish();
        }
        else if(getIntent().getStringExtra("type").equals("video")){
            videoCallRequest = true;
            String phoneNumber = getIntent().getStringExtra("info");
            String message = "User would like you to join a video call. Reply with Y to accept.";
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber,null, message, null, null);

            // Wait 30 Seconds before going back
            long maxCounter = 30000;
            long diff = 1000;
            new CountDownTimer(maxCounter , diff ) {
                public void onTick(long millisUntilFinished) {
                    long diff = maxCounter - millisUntilFinished;
                    long time = diff  / 1000;
                    //here you can have your logic to set text to edittext
                }
                public void onFinish() {
                    if (videoCallRequest){
                        videoCallRequest = false;
                        finish();
                    }
                }
            }.start();
        }
        else{
            Toast.makeText(this, getIntent().getStringExtra("type"), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
        mIntentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("get_msg");

                //Process the sms format and extract body &amp; phoneNumber
                msg = msg.replace("\n", "");
                String body = msg.substring(msg.lastIndexOf(":")+1, msg.length());
                String pNumber = msg.substring(0,msg.lastIndexOf(":"));

                if (videoCallRequest && body.equals("Y")){
                    // To display a Toast whenever there is an SMS.
                    Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show();
                    videoCallRequest = false;
                    String email = "babysfirstphone550@gmail.com";
                    String password = "CompSci550";
                    finish();
                }
            }
        };

        this.registerReceiver(mIntentReceiver, intentFilter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mIntentReceiver);
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        /*
          This function checks on system permissions to make phone calls,
          then connects the phone call.
         */
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhoneNumber("1-805-123-4567");
            }
        }
    }

    public void callPhoneNumber(String phoneNumber) {

        // If permission is already given, connect a call.
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Router.this, new String[]{
                            Manifest.permission.CALL_PHONE}, 101);
                    return;
                }
            }
            startActivity(caller.makeCall(phoneNumber));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}



