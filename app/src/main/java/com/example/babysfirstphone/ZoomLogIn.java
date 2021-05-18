package com.example.babysfirstphone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import us.zoom.sdk.ZoomApiError;
import us.zoom.sdk.ZoomAuthenticationError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKAuthenticationListener;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;

public class ZoomLogIn extends AppCompatActivity {

    private EditText emailText;
    private EditText passwordText;
    private Button loginConfirm;
    private Button logoutConfirm;
    String email;
    String password;

    private ZoomSDKAuthenticationListener authListener = new ZoomSDKAuthenticationListener() {
        /**
         * This callback is invoked when a result from the SDK's request to the auth server is
         * received.
         */
        @Override
        public void onZoomSDKLoginResult(long result) {
            if (result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS) {
                // Once we verify that the request was successful, we may start the meeting
//                startMeeting(ZoomLogIn.this);
                Toast.makeText(ZoomLogIn.this, "Success!", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_zoom_log_in);

        initializeSdk(this);

        FloatingActionButton fab = findViewById(R.id.home_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), MainActivity.class));
            }
        });

//        email = "babysfirstphone550@gmail.com";
//        password = "CompSci550";



        // Settings Pass Code
        emailText = (EditText) findViewById(R.id.email_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        loginConfirm = (Button) findViewById(R.id.button_login);
        logoutConfirm = (Button) findViewById(R.id.button_logout);
        emailText.addTextChangedListener(loginTextWatcher);
        passwordText.addTextChangedListener(loginTextWatcher);

        // Checks if it can log in user after app shutdown
        if(!ZoomSDK.getInstance().isLoggedIn()){
            loadLoginInfo();
            login(email, password);
        }

        // Checks if user is logged in
        logoutConfirm.setEnabled(ZoomSDK.getInstance().isLoggedIn());


        // Lets you into internal settings
        loginConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               email = emailText.getText().toString().trim();
               password = passwordText.getText().toString().trim();
               login(email, password);
               passwordText.getText().clear();
            }
        });

        // Logs out of zoom account
        logoutConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZoomSDK.getInstance().logoutZoom();
                if (!ZoomSDK.getInstance().isLoggedIn()){
                    logoutConfirm.setEnabled(false);
                    Toast.makeText(ZoomLogIn.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

            // Stores username and password
            SharedPreferences sharedPreferences = getSharedPreferences("ZoomLogInInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userName", username);
            editor.putString("password", password);
            editor.apply();
            logoutConfirm.setEnabled(true);
            // Hides Keyboard
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        }else if(ZoomSDK.getInstance().isLoggedIn()){
            logoutConfirm.setEnabled(true);
            // Hides Keyboard
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            Toast.makeText(ZoomLogIn.this, "Already Logged In!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ZoomLogIn.this, "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String userNameInput = passwordText.getText().toString().trim().toLowerCase();
            loginConfirm.setEnabled(!userNameInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void loadLoginInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("ZoomLogInInfo", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("userName", "babysfirstphone550@gmail.com");
        password = sharedPreferences.getString("password", "CompSci550");
//        System.out.println("Stored number " + dadsNumber);
//        System.out.println("Stored number " + momsNumber);
    }
}