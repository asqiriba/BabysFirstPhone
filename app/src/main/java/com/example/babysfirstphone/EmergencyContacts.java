package com.example.babysfirstphone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.babysfirstphone.contacts.Images;
import com.example.babysfirstphone.controllers.Constants;
import com.example.babysfirstphone.controllers.Contacts;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import us.zoom.sdk.ZoomSDK;

public class EmergencyContacts extends AppCompatActivity {

    EditText editNumber;
    String number, contactType;
    Button saveButton;
    FloatingActionButton fab;
    EditText momText;
    EditText dadText;
    Button saveConfirm;
    ArrayList<Contacts> arrayListContact = new ArrayList<>();
    List<String> names = new ArrayList<>();

    ImageView contactImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);

        // Settings Pass Code
        momText = (EditText) findViewById(R.id.mom_text);
        dadText = (EditText) findViewById(R.id.dad_text);
        saveConfirm = (Button) findViewById(R.id.save);
        // Checks that there is input in text box
        momText.addTextChangedListener(loginTextWatcher);
        dadText.addTextChangedListener(loginTextWatcher);

        // Only populates main view if there is any contacts stored
        loadContactData();
        if (!arrayListContact.isEmpty()){
            for(int i = 0; i < arrayListContact.size() ; i++){
                names.add(arrayListContact.get(i).getName().toLowerCase());
            }
        }

        // Lets you into internal settings
        saveConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String momsName = momText.getText().toString().trim().toLowerCase();
                String dadsName = dadText.getText().toString().trim().toLowerCase();

                if(names.contains(momsName) && names.contains(dadsName)){
                    SharedPreferences sharedPreferences = getSharedPreferences("ParentsNames", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Mom", momsName);
                    editor.putString("Dad", dadsName);
                    editor.apply();
                    Toast toast = Toast.makeText(getApplicationContext(), "Parents Saved", Toast.LENGTH_SHORT);
                    toast.show();
                } else if(names.contains(momsName) && !names.contains(dadsName)){
                    SharedPreferences sharedPreferences = getSharedPreferences("ParentsNames", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Mom", momsName);
                    editor.apply();
                    Toast toast = Toast.makeText(getApplicationContext(), "Mom's saved but Dad not found.", Toast.LENGTH_SHORT);
                    toast.show();

                } else if(names.contains(dadsName) && !names.contains(momsName)){
                    SharedPreferences sharedPreferences = getSharedPreferences("ParentsNames", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Dad", dadsName);
                    editor.apply();
                    Toast toast = Toast.makeText(getApplicationContext(), "Dad's saved but Mom not found.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Parents Not Found", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.home_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmergencyContacts.this, MainActivity.class));
            }
        });




//        editNumber = findViewById(R.id.editNumber);
//
////        Spinner contact_spinner = findViewById(R.id.spinner);
//        saveButton = findViewById(R.id.save);
//        contactImage = findViewById(R.id.ContactImage);
//        editNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
//
//        /*
//            Here we set the click listener on SAVE.
//         */
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // Stores contact type and his/her phone number
//                number = editNumber.getText().toString();
////                contactType = contact_spinner.getSelectedItem().toString();
//
//                SharedPreferences sharedPreferences = getSharedPreferences("EmergencyContacts", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString(contactType, number);
//                editor.apply();
//                Toast.makeText(EmergencyContacts.this,
//                        "Update Contact info : " + contactType + " : " + number,
//                        Toast.LENGTH_SHORT).show();
//
////                finish();
//
//            }
//        });
//
//        fab = (FloatingActionButton) findViewById(R.id.home_button);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(EmergencyContacts.this, MainActivity.class));
//            }
//        });
//
//        contactImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                /*
//                    After clicking on image the User is to move to controllers/Images Activity
//                    page where user select the profile picture of contact.
//                 */
//                Intent intent = new Intent(EmergencyContacts.this, Images.class);
//
//                /*
//                    Here we used startActivityForResult() as we expecting some data back from Images
//                     Activity which is contact image selected by user.
//
//                     The contact image we get from Images activity, we extract it using code
//  >>>                  Line 97-100.
//                 */
//                startActivityForResult(intent,1);
//            }
//        });
    }

    /*
//        Here we get the Image which is send from Images class. We also set the contact image.
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        int image = data.getExtras().getInt("img", 1);
//        contactImage.setImageResource(image);
//    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String momInput = momText.getText().toString().trim().toLowerCase();
            String dadInput = dadText.getText().toString().trim().toLowerCase();
            saveConfirm.setEnabled(!momInput.isEmpty() || !dadInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void loadContactData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("contact list", null);
        Type type = new TypeToken<ArrayList<Contacts>>() {}.getType();
        arrayListContact = gson.fromJson(json, type);

        if (arrayListContact == null || arrayListContact.isEmpty()) {
            arrayListContact = new ArrayList<Contacts>();
        }
    }


}