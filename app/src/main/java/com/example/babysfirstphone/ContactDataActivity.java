package com.example.babysfirstphone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.example.babysfirstphone.contacts.ContactEditActivity;
import com.example.babysfirstphone.controllers.Contacts;
import com.zipow.videobox.confapp.meeting.immersive.ZmImmersiveTouchEventHandler;

/*
    In ContactDataActivity class, we take the inputs from user like Name, Number,
    and Profile Photo.
 */
public class ContactDataActivity extends Activity {

    String[] types = new String[]{"phone", "video", "app"};
    EditText editName, editNumber;
    ImageView contactImage;
    Button saveButton;
    private String picturePath;
    private static final int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_data);

        editName = findViewById(R.id.editName);
        editNumber = findViewById(R.id.editNumber);
        contactImage = findViewById(R.id.ContactImage);
        Spinner contactType = findViewById(R.id.dropdown);
        saveButton = findViewById(R.id.save);

        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        contactType.setAdapter(dropdownAdapter);


        /////////////////////////////////////////  HERE JIE ////////////////////////////////////////////////////////////
        contactType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String check = contactType.getSelectedItem().toString();
                System.out.println(check);

                if(check.equals("app")){
                    editName.setHint("Enter Website address");
                    editNumber.setEnabled(false);
                    editNumber.setHint("Nothing needed here.");
                }else{
                    editName.setHint("Contact Name");
                    editNumber.setEnabled(true);
                    editNumber.setHint("1 (000) 000-0000");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /////////////////////////////////////////  HERE JIE ////////////////////////////////////////////////////////////



        /*
            Here we set the Click listener on Image view. So that it select the profile pictures.
         */
        contactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                    We get the images from the user's phone.
                 */
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

                /*
                    Use for listing app images.
                    After clicking on image the User is to move to controllers/Images Activity
                    page where user select the profile picture of contact.
                 */
//                Intent intent = new Intent(ContactDataActivity.this, Images.class);

                /*
                    Here we used startActivityForResult() as we expecting some data back from Images
                     Activity which is contact image selected by user.

                     The contact image we get from Images activity, we extract it using code
  >>>                  Line 97-100.
                 */
//                startActivityForResult(intent, 1);
            }
        });

        /*
            Format the input number as a phone number.
         */
        editNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        /*
            Here we set the click listener on SAVE.
         */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /////////////////////////////////////////  ALSO HERE JIE ////////////////////////////////////////////////////////////
                /*
                    Here we created the object of Contact class and called the Parameterised
                    Constructor.
                 */
                Contacts contacts = new Contacts(editName.getText().toString(),
                        editNumber.getText().toString(),
                        picturePath,
                        String.valueOf(contactType.getSelectedItem())
                );
                /////////////////////////////////////////  ALSO HERE JIE ////////////////////////////////////////////////////////////

                /*
                    When user click the SAVE button, the activity goes from here to
                    SettingsInternal.java file. We might want to change this.
                 */
                Intent intent = new Intent(ContactDataActivity.this, ContactEditActivity.class);

                /*
                    We can’t send the Object of a class using Intent without implementing
                    Serializable Interface.
                 */
                intent.putExtra("data", contacts);

                /*
                    Here we set the result code as 2, and second argument as intent Object.
                 */
                setResult(2, intent);
                finish();
            }
        });

    }

    /*
        Here we get the Image which is send from Images class. We also set the contact image.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.ContactImage);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
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