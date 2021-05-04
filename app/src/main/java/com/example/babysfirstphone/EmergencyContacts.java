package com.example.babysfirstphone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.babysfirstphone.contacts.Images;
import com.example.babysfirstphone.controllers.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class EmergencyContacts extends AppCompatActivity {

    EditText editNumber;
    String number, contactType;
    Button saveButton;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);
        editNumber = findViewById(R.id.editNumber);


        Spinner contact_spinner = findViewById(R.id.spinner);
        saveButton = findViewById(R.id.save);

        /*
            Here we set the click listener on SAVE.
         */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Stores contact type and his/her phone number
                number = editNumber.getText().toString();
                contactType = contact_spinner.getSelectedItem().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("EmergencyContacts", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(contactType, number);
                editor.apply();
                Toast.makeText(EmergencyContacts.this,
                        "Update Contact info : " + contactType + " : " + number,
                        Toast.LENGTH_SHORT).show();

//                finish();

            }
        });

        fab = (FloatingActionButton) findViewById(R.id.home_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmergencyContacts.this, MainActivity.class));
            }
        });




    }
}