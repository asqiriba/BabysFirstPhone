package com.example.babysfirstphone;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import us.zoom.sdk.ZoomSDK;


public class UserNameActivity extends AppCompatActivity {

    private EditText userNameText;
    private Button save;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);

        // Sets default or stored name
        loadUserName();
        // Updates current name for user
        updateTextView(name);

        userNameText = (EditText) findViewById(R.id.user_text);
        save = (Button) findViewById(R.id.save_button);
        userNameText.addTextChangedListener(loginTextWatcher);



        // Stores user name
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = userNameText.getText().toString().trim();
                SharedPreferences sharedPreferences = getSharedPreferences("UserName", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", name);
                editor.apply();
                Toast.makeText(UserNameActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                updateTextView(name);

                // Hides Keyboard
                Utils.hideKeyboard(UserNameActivity.this);

            }
        });



    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String userNameInput = userNameText.getText().toString().trim().toLowerCase();
            save.setEnabled(!userNameInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void loadUserName() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserName", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("name", "User");
    }

    public void updateTextView(String toThis) {
        TextView nameOfUser = (TextView) findViewById(R.id.CurrentName);
        nameOfUser.setText(toThis);
    }

    public static class Utils{
        public static void hideKeyboard(@NonNull Activity activity) {
            // Check if no view has focus:
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}