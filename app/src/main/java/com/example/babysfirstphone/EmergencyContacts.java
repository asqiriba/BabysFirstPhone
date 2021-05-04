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

    ImageView contactImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);
        editNumber = findViewById(R.id.editNumber);

        Spinner contact_spinner = findViewById(R.id.spinner);
        saveButton = findViewById(R.id.save);
        contactImage = findViewById(R.id.ContactImage);
        editNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

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

        contactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                    After clicking on image the User is to move to controllers/Images Activity
                    page where user select the profile picture of contact.
                 */
                Intent intent = new Intent(EmergencyContacts.this, Images.class);

                /*
                    Here we used startActivityForResult() as we expecting some data back from Images
                     Activity which is contact image selected by user.

                     The contact image we get from Images activity, we extract it using code
  >>>                  Line 97-100.
                 */
                startActivityForResult(intent,1);
            }
        });
    }

    /*
        Here we get the Image which is send from Images class. We also set the contact image.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int image = data.getExtras().getInt("img", 1);
        contactImage.setImageResource(image);
    }
}