package com.example.babysfirstphone;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.example.babysfirstphone.controllers.Caller;
import com.example.babysfirstphone.controllers.Contacts;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import android.os.Bundle;
import android.view.View;

import java.lang.reflect.Type;
import java.util.ArrayList;

import us.zoom.sdk.JoinMeetingOptions;
import us.zoom.sdk.JoinMeetingParams;
import us.zoom.sdk.MeetingItem;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingStatus;
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
    ArrayList<Contacts> arrayListContact;



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
        initializeSdk(this);
        loadData();
        getData();


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

        /**
     * Join a meeting without any login/authentication with the meeting's number & password
     */
    public void joinMeeting(Context context, String meetingNumber, String password) {
        MeetingService meetingService = ZoomSDK.getInstance().getMeetingService();
        JoinMeetingOptions options = new JoinMeetingOptions();
        JoinMeetingParams params = new JoinMeetingParams();
        params.displayName = ""; // TODO: Enter your name
        params.meetingNo = meetingNumber;
        params.password = password;
        meetingService.joinMeetingWithParams(context, params, options);
    }


    public void getData(){
        if(getIntent().getStringExtra("type").equals("phone")){
            callPhoneNumber(getIntent().getStringExtra("info"));
            finish();
        }
        else if(getIntent().getStringExtra("type").equals("video")){

            if(ZoomSDK.getInstance().isLoggedIn()){
                videoCallRequest = true;
                String phoneNumber = getIntent().getStringExtra("info");
                String message = "User would like you to join a video call. Reply with Y to accept.";
                sendSMS(phoneNumber, message);

                // Wait 30 Seconds before going back
                long maxCounter = 60000;
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
            }else{

                Toast.makeText(this, "Please sign in to your Zoom account.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
            }

        }
        else{
            try {
                String url = getIntent().getStringExtra("names"); // get the website url from the name edit text
                // add http or https if the download link does not contain it
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(myIntent);
                finish();
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No application can handle this request."
                        + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
//            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
//            if (launchIntent != null) {
//                startActivity(launchIntent);
//                finish();
//            } else {
//                Toast.makeText(Router.this, "There is no package available in android", Toast.LENGTH_LONG).show();
//            }
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
                if (videoCallRequest && body.equals("Y") ){
                    // To display a Toast whenever there is an SMS.
                    Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show();

                    if (ZoomSDK.getInstance().isLoggedIn()) {
                        startMeeting(Router.this);

                        // Wait 3 Seconds before obtaining meeting URL
                        long maxCounter = 3000;
                        long diff = 1000;
                        new CountDownTimer(maxCounter , diff ) {
                            public void onTick(long millisUntilFinished) {
                                long diff = maxCounter - millisUntilFinished;
                                long time = diff  / 1000;
                                //here you can have your logic to set text to edittext
                            }
                            public void onFinish() {
                                String meetingURL = ZoomSDK.getInstance().getInMeetingService().getCurrentMeetingUrl();
                                if (meetingURL == null){
                                    // Try to get meeting URL again
                                    long maxCounter = 3000;
                                    long diff = 1000;
                                    new CountDownTimer(maxCounter , diff ) {
                                        public void onTick(long millisUntilFinished) { }
                                        public void onFinish() {
                                            String meetingURL = ZoomSDK.getInstance().getInMeetingService().getCurrentMeetingUrl();
                                            String joinMessage = "User is waiting for you in a meeting room. Please click on the link to join room: ";
                                            sendSMS(phoneNumber, joinMessage + meetingURL);
                                            videoCallRequest = false;
                                        }
                                    }.start();
                                }else{
                                    String joinMessage = "User is waiting for you in a meeting room. Please click on the link to join room: ";
                                    sendSMS(phoneNumber, joinMessage + meetingURL);
                                    videoCallRequest = false;
                                }

                            }
                        }.start();
                    } else {
//                        String email = "babysfirstphone550@gmail.com";
//                        String password = "CompSci550";
                        SharedPreferences sharedPreferences = getSharedPreferences("ZoomLogInInfo", Context.MODE_PRIVATE);
                        String email = sharedPreferences.getString("userName", "defaultValue");
                        String password = sharedPreferences.getString("password", "defaultValue");
                        login(email, password);
                        startMeeting(Router.this);

                        // Wait 3 Seconds before obtaining meeting URL
                        long maxCounter = 3000;
                        long diff = 1000;
                        new CountDownTimer(maxCounter , diff ) {
                            public void onTick(long millisUntilFinished) {
                                long diff = maxCounter - millisUntilFinished;
                                long time = diff  / 1000;
                                //here you can have your logic to set text to edittext
                            }
                            public void onFinish() {
                                String meetingURL = ZoomSDK.getInstance().getInMeetingService().getCurrentMeetingUrl();
                                if (meetingURL == null){
                                    // Try to get meeting URL again
                                    long maxCounter = 3000;
                                    long diff = 1000;
                                    new CountDownTimer(maxCounter , diff ) {
                                        public void onTick(long millisUntilFinished) { }
                                        public void onFinish() {
                                            String meetingURL = ZoomSDK.getInstance().getInMeetingService().getCurrentMeetingUrl();
                                            String joinMessage = "User is waiting for you in a meeting room. Please click on the link to join room: ";
                                            sendSMS(phoneNumber, joinMessage + meetingURL);
                                            videoCallRequest = false;
                                        }
                                    }.start();
                                }else{
                                    String joinMessage = "User is waiting for you in a meeting room. Please click on the link to join room: ";
                                    sendSMS(phoneNumber, joinMessage + meetingURL);
                                    videoCallRequest = false;
                                }
                            }
                        }.start();
                    }
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

    private void sendSms(String phoneNumber, String message){
        System.out.println("SMS Sent");
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber,null,message, null, null);
        System.out.println("SMS Sent");
    }

    public void sendSMS(String phoneNumber, String message) {

        // If permission is already given, connect a call.
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) !=PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Router.this, new String[]{
                            Manifest.permission.SEND_SMS}, 123);
                    return;
                }
            }
            sendSms(phoneNumber, message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("contact list", null);
        Type type = new TypeToken<ArrayList<Contacts>>() {}.getType();
        arrayListContact = gson.fromJson(json, type);

        if (arrayListContact == null || arrayListContact.isEmpty()) {
            arrayListContact = new ArrayList<>();
        }
    }

}



