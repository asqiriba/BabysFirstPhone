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
import androidx.core.content.ContextCompat;

import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.SmsManager;
import android.widget.Toast;

import android.os.Bundle;
import android.view.View;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.StartMeetingOptions;
import us.zoom.sdk.ZoomApiError;
import us.zoom.sdk.ZoomAuthenticationError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKAuthenticationListener;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;


public class Router extends AppCompatActivity {

    Caller caller = new Caller();
    private BroadcastReceiver mIntentReceiver;
    boolean videoCallRequest = false;
    private static final int PERMISSION_SEND_SMS = 123;



    private ZoomSDKAuthenticationListener authListener = new ZoomSDKAuthenticationListener() {
        /**
         * This callback is invoked when a result from the SDK's request to the auth server is
         * received.
         */
        @Override
        public void onZoomSDKLoginResult(long result) {
            if (result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS) {
                // Once we verify that the request was successful, we may start the meeting
                startMeeting(Router.this);
            }
        }

        @Override
        public void onZoomSDKLogoutResult(long l) {}@Override
        public void onZoomIdentityExpired() {}@Override
        public void onZoomAuthIdentityExpired() {}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router);
        getData();
        initializeSdk(this);

    }

    /**
     * Initialize the SDK with your credentials. This is required before accessing any of the
     * SDK's meeting-related functionality.
     */
    public void initializeSdk(Context context) {
        ZoomSDK sdk = ZoomSDK.getInstance();
        // TODO: Do not use hard-coded values for your key/secret in your app in production!
        ZoomSDKInitParams params = new ZoomSDKInitParams();
        params.appKey = "ULk65Cb44u1qZwp6GGc42XCYCcRCFjaYrfyM"; // TODO: Retrieve your SDK key and enter it here
        params.appSecret = "dOSV1LbPbti9xxxNMk75TlY3Q91K2Eo0eTuu"; // TODO: Retrieve your SDK secret and enter it here
        params.domain = "zoom.us";
        params.enableLog = true;
        // TODO: Add functionality to this listener (e.g. logs for debugging)
        ZoomSDKInitializeListener listener = new ZoomSDKInitializeListener() {
            /**
             * @param errorCode {@link us.zoom.sdk.ZoomError#ZOOM_ERROR_SUCCESS} if the SDK has been initialized successfully.
             */
            @Override
            public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {}

            @Override
            public void onZoomAuthIdentityExpired() {}
        };
        sdk.initialize(context, listener, params);
    }
    /**
     * Log into a Zoom account through the SDK using your email and password. For more information,
     * see {@link ZoomSDKAuthenticationListener#onZoomSDKLoginResult} in the {@link #authListener}.
     */
    public void login(String username, String password) {
        int result = ZoomSDK.getInstance().loginWithZoom(username, password);
        if (result == ZoomApiError.ZOOM_API_ERROR_SUCCESS) {
            // Request executed, listen for result to start meeting
            ZoomSDK.getInstance().addAuthenticationListener(authListener);
        }
    }

    /**
     * Start an instant meeting as a logged-in user. An instant meeting has a meeting number and
     * password generated when it is created.
     */
    public void startMeeting(Context context) {
        ZoomSDK sdk = ZoomSDK.getInstance();
        if (sdk.isLoggedIn()) {
            MeetingService meetingService = sdk.getMeetingService();
            StartMeetingOptions options = new StartMeetingOptions();
            meetingService.startInstantMeeting(context, options);
        }
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
            sendSMS(phoneNumber, message);

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
                String phoneNumberReceived = msg.substring(0,msg.lastIndexOf(":"));
                String phoneNumber = getIntent().getStringExtra("info").replaceAll("\\D+","");;

                // Only opens video chat if the recipient replies to text
                if (videoCallRequest && body.equals("Y") && phoneNumber.equals(phoneNumberReceived)){
                    // To display a Toast whenever there is an SMS.
                    Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show();

                    String joinMessage = "User is waiting for you in a meeting room. Please click on the link to join room.";
                    String invitationLink = "https://us05web.zoom.us/j/2821683656?pwd=WndHdytYZXNaNkFma2p4R2hTR0ZvUT09";
                    sendSMS(phoneNumber, joinMessage);
                    sendSMS(phoneNumber, invitationLink);

                    if (ZoomSDK.getInstance().isLoggedIn()) {
                        startMeeting(Router.this);
                    } else {
                        String email = "babysfirstphone550@gmail.com";
                        String password = "CompSci550";
                        login(email, password);
                        startMeeting(Router.this);
                    }
                    videoCallRequest = false;
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

    private void sendSMS(String phoneNumber, String message){
        System.out.println("SMS Sent");
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber,null,message, null, null);
        System.out.println("SMS Sent");
    }

}



