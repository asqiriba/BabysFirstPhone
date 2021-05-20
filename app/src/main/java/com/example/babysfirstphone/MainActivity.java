package com.example.babysfirstphone;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babysfirstphone.controllers.Contacts;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import us.zoom.sdk.ZoomApiError;
import us.zoom.sdk.ZoomAuthenticationError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKAuthenticationListener;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;

public class MainActivity extends AppCompatActivity {
    /*
        To retrieve an image from the contact ArrayList as an ImageView:

        ImageView iv_image;
        iv_image.setImageResource(arrayListContact.get(i).getImage());
     */

    RecyclerView dataList;
    List<String> type;
    List<String> images;
    List<String> info;
    List<String> names;
    Adapter adapter;
    ArrayList<Contacts> arrayListContact;
//    ZoomLogIn zoomLogIn = new ZoomLogIn();
    String email;
    String password;
    boolean isLoggedIntoZoom;

    View mainScreen;
    ImageButton paintBtn;
    int[] bgImages;
    String COLOR = "colorTheme";
    int getNum;


    private ZoomSDKAuthenticationListener authListener = new ZoomSDKAuthenticationListener() {
        /**
         * This callback is invoked when a result from the SDK's request to the auth server is
         * received.
         */
        @Override
        public void onZoomSDKLoginResult(long result) {
            if (result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS) {
                // Once we verify that the request was successful, we may start the meeting
//                startMeeting(MainActivity.this);
//                Toast.makeText(MainActivity.this, "Success!!!", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_main);


        //Gets media access.
        askForMediaPermissions();

        // Recycler View
        dataList = findViewById(R.id.dataList);
        type = new ArrayList<>();
        images = new ArrayList<>();
        info = new ArrayList<>();
        names = new ArrayList<>();

        // Logs into zoom if the correct credentials are stored in the app
        initializeSdk(this);
        loadLoginInfo();
        if(!ZoomSDK.getInstance().isLoggedIn()){
            // Wait 1 Seconds before trying to login to zoom
            long maxCounter = 1000;
            long diff = 1000;
            new CountDownTimer(maxCounter , diff ) {
                public void onTick(long millisUntilFinished) {
                    long diff = maxCounter - millisUntilFinished;
                    long time = diff  / 1000;
                    //here you can have your logic to set text to edittext
                }
                public void onFinish() {
                    login(email, password);
                }
            }.start();
        }

        // Obtains data saved in device
        loadData();

        // Only populates main view if there is any contacts stored
        if (!arrayListContact.isEmpty()){
            for(int i = 0; i < arrayListContact.size() ; i++){
                type.add(arrayListContact.get(i).getType());
            }
            for(int i = 0; i < arrayListContact.size() ; i++){
                images.add(arrayListContact.get(i).getImage());
            }
            for(int i = 0; i < arrayListContact.size() ; i++){
                info.add(arrayListContact.get(i).getNumber().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"));
            }
            for(int i = 0; i < arrayListContact.size() ; i++){
                names.add(arrayListContact.get(i).getName());
            }

        }

        adapter = new Adapter(this, type, images, info,names);
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
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 123);
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

    /**
     * Get Media access permission.
     */
    private void askForMediaPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, ask.
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

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

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
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
//            Toast.makeText(MainActivity.this, "Logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Obtains stored login information for zoom
     */
    private void loadLoginInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("ZoomLogInInfo", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("userName", "babysfirstphone550@gmail.com");
        password = sharedPreferences.getString("password", "CompSci550");
//        System.out.println("Stored email: " + email);
//        System.out.println("Stored password: " + password);
    }
}
