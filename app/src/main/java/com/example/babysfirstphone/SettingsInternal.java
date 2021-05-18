package com.example.babysfirstphone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.babysfirstphone.contacts.ContactEditActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsInternal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_internal);

        FloatingActionButton fab = findViewById(R.id.home_button);
        fab.setOnClickListener(view -> startActivity(new Intent(view.getContext(),
                MainActivity.class)));

        Button contactButton = findViewById(R.id.contactButton);
        contactButton.setOnClickListener(view -> startActivity(new Intent(view.getContext(),
                ContactEditActivity.class)));

        Button zoomAccountButton = findViewById(R.id.zoomButton);
        zoomAccountButton.setOnClickListener(view -> startActivity(new Intent(view.getContext(),
                ZoomLogIn.class)));

        Button userNameButton = findViewById(R.id.userName);
        userNameButton.setOnClickListener(view -> startActivity(new Intent(view.getContext(),
                UserNameActivity.class)));

        Button emergencyContacts = findViewById(R.id.emergencyButton);
        emergencyContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), EmergencyContacts.class));
            }
        });
    }
}