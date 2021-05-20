package com.example.babysfirstphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babysfirstphone.controllers.Contacts;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import us.zoom.sdk.MeetingService;
import us.zoom.sdk.StartMeetingOptions;
import us.zoom.sdk.ZoomApiError;
import us.zoom.sdk.ZoomAuthenticationError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKAuthenticationListener;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;

public class GroupsFragment extends Fragment {
    FloatingActionButton fab;

    int[] images;
    int getNum;
    View groupScreen;
    private BroadcastReceiver mIntentReceiver;
    ArrayList<String> phoneNumbers = new ArrayList<>();
    List<String> type;
    List<String> image;
    List<String> info;
    ArrayList<Contacts> arrayListContact;
    boolean isSelected = true;
    String name;

    private ZoomSDKAuthenticationListener authListener = new ZoomSDKAuthenticationListener() {
        /**
         * This callback is invoked when a result from the SDK's request to the auth server is
         * received.
         */
        @Override
        public void onZoomSDKLoginResult(long result) {
            if (result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS) {
                // Once we verify that the request was successful, we may start the meeting
//                startMeeting(getContext());
            }
        }

        @Override
        public void onZoomSDKLogoutResult(long l) {}@Override
        public void onZoomIdentityExpired() {}@Override
        public void onZoomAuthIdentityExpired() {}
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        // Initialize zoom sdk
        initializeSdk(getContext());

        // Recycler View
        type = new ArrayList<>();
        image = new ArrayList<>();
        info = new ArrayList<>();
        // Obtains name of user
        loadUserName();
        // Obtains tile information
        loadData();
        // Only populates main view if there is any contacts stored
        if (!arrayListContact.isEmpty()){
            for(int i = 0; i < arrayListContact.size() ; i++){
                if(arrayListContact.get(i).getType().equals("video")){
                    type.add(arrayListContact.get(i).getType());
                    image.add(arrayListContact.get(i).getImage());
                    info.add(arrayListContact.get(i).getNumber().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"));
                }
            }
        }

        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.videoList);
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 3. Create an adapter
        GroupAdapter adapter = new GroupAdapter(getActivity(), type, image, info);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        // 4. Set adapter
        recyclerView.setAdapter(adapter);
        // 5. Set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Makes group call
        ImageButton button = (ImageButton) view.findViewById(R.id.groupCallButton);
        button.setEnabled(isSelected);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumbers = adapter.getArrayList();
//                Toast toast = Toast.makeText(view.getContext(), String.valueOf(phoneNumbers.size()), Toast.LENGTH_SHORT);
//                toast.show();

                if (!phoneNumbers.isEmpty() && ZoomSDK.getInstance().isLoggedIn()){
                    button.setEnabled(!isSelected);
                    final Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                    button.startAnimation(animShake);
                    for(int i = 0; i < phoneNumbers.size() ; i++){
                        String number = String.valueOf(phoneNumbers.get(i));
                        String message = name + " would like you to join a video call. Reply with Y to accept.";
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(number,null,message, null, null);
                    }
                    // Wait 60 Seconds before obtaining meeting URL
                    long maxCounter = 60000;
                    long diff = 1000;
                    new CountDownTimer(maxCounter , diff ) {
                        public void onTick(long millisUntilFinished) {
                            long diff = maxCounter - millisUntilFinished;
                            long time = diff  % 1000;
                        }
                        public void onFinish() {
                            button.clearAnimation();
                            button.setImageResource(R.drawable.zm_join_normal);
                            button.setEnabled(isSelected);
                        }
                    }.start();
                }else if(!ZoomSDK.getInstance().isLoggedIn()){
                    Toast login_toast = Toast.makeText(view.getContext(), "Please log into your Zoom Account.", Toast.LENGTH_SHORT);
                    login_toast.show();
//                    // Try to log into zoom
//                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ZoomLogInInfo", Context.MODE_PRIVATE);
//                    String email = sharedPreferences.getString("userName", "defaultValue");
//                    String password = sharedPreferences.getString("password", "defaultValue");
//                    login(email, password);
//
//                    // Try to log in again, if not display message
//                    if(ZoomSDK.getInstance().isLoggedIn()) {
//                        button.setEnabled(!isSelected);
//                        final Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
//                        button.startAnimation(animShake);
//                        for(int i = 0; i < phoneNumbers.size() ; i++){
//                            String number = String.valueOf(phoneNumbers.get(i));
//                            String message = "User would like you to join a video call. Reply with Y to accept.";
//                            SmsManager smsManager = SmsManager.getDefault();
//                            smsManager.sendTextMessage(number,null,message, null, null);
//                        }
//                        // Wait 60 Seconds before obtaining meeting URL
//                        long maxCounter = 10000;
//                        long diff = 1000;
//                        new CountDownTimer(maxCounter , diff ) {
//                            public void onTick(long millisUntilFinished) {
//                                long diff = maxCounter - millisUntilFinished;
//                                long time = diff  % 1000;
//                            }
//                            public void onFinish() {
//                                button.clearAnimation();
//                                button.setImageResource(R.drawable.zm_join_normal);
//                                button.setEnabled(isSelected);
//                            }
//                        }.start();
//                    }
                }
//                else{
//                    Toast login_toast = Toast.makeText(view.getContext(), "Please log into your Zoom Account.", Toast.LENGTH_SHORT);
//                    login_toast.show();
//                }
            }
        });



        // Receives text message
        IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
        mIntentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                phoneNumbers = adapter.getArrayList();
                String msg = intent.getStringExtra("get_msg");
                //Process the sms format and extract body &amp; phoneNumber
                msg = msg.replace("\n", "");
                String body = msg.substring(msg.lastIndexOf(":")+1, msg.length());
                String phoneNumberReceived = msg.substring(0,msg.lastIndexOf(":")).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
//                Toast toast = Toast.makeText(view.getContext(), phoneNumberReceived, Toast.LENGTH_SHORT);
//                toast.show();
                if(!phoneNumbers.isEmpty() && phoneNumbers.contains(phoneNumberReceived) && body.equals("Y")){
                    Toast toast = Toast.makeText(view.getContext(), "Success!", Toast.LENGTH_SHORT);
                    toast.show();
                    ArrayList<String> otherPhoneNumbers = adapter.getArrayList();
                    otherPhoneNumbers.remove(phoneNumberReceived);
                    String prompt = name + " is waiting for you in this room: ";
                    String message = prompt + "https://us05web.zoom.us/j/2821683656?pwd=WndHdytYZXNaNkFma2p4R2hTR0ZvUT09";
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumberReceived,null,message, null, null);

//                    for(int i = 0; i < phoneNumbers.size() ; i++){
//                        String number = String.valueOf(phoneNumbers.get(i));
//                        String message = "https://us05web.zoom.us/j/2821683656?pwd=WndHdytYZXNaNkFma2p4R2hTR0ZvUT09";
//                        SmsManager smsManager = SmsManager.getDefault();
//                        smsManager.sendTextMessage(number,null,message, null, null);
//                    }
                    if(!otherPhoneNumbers.isEmpty()){
                        for(int i = 0; i < otherPhoneNumbers.size() ; i++){
                            String number = String.valueOf(otherPhoneNumbers.get(i));
                            prompt = name + " is on a group call. Join by following this link: ";
                            message = prompt + "https://us05web.zoom.us/j/2821683656?pwd=WndHdytYZXNaNkFma2p4R2hTR0ZvUT09";
                            smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(number,null,message, null, null);
                        }
                    }
                    // Make zoom call here
                    if(ZoomSDK.getInstance().isLoggedIn()){
                        startMeeting(getActivity());
                    }
                    // End zoom call here
                }
            }
        };
        getActivity().registerReceiver(this.mIntentReceiver, intentFilter);


        // Floating button to return to home
        fab = (FloatingActionButton) view.findViewById(R.id.home_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        // Change Background color
        groupScreen = view.findViewById(R.id.groups);
        changeColor();

        return view;
    }

    public void changeColor() {
        images = new int[] {R.drawable.green_background, R.drawable.red_background,R.drawable.pink_background, R.drawable.purple_background, R.drawable.blue_background, R.drawable.yellow_background  };
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("colorTheme", Context.MODE_PRIVATE);
        getNum = sharedPreferences.getInt("color",0);
        groupScreen.setBackgroundResource(images[getNum]);
    }

    private void loadData() {
//        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences");
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("contact list", null);
        Type type = new TypeToken<ArrayList<Contacts>>() {}.getType();
        arrayListContact = gson.fromJson(json, type);

        if (arrayListContact == null || arrayListContact.isEmpty()) {
            arrayListContact = new ArrayList<Contacts>();
        }
    }

    private void loadUserName() {
//        SharedPreferences sharedPreferences = getSharedPreferences("userName", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("UserName", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("name", "User");
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
}