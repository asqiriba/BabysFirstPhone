package com.example.babysfirstphone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.babysfirstphone.contacts.Images;
import com.example.babysfirstphone.controllers.Contacts;

/*
    In ContactDataActivity class, we take the inputs from user like Name, Number,
    and Profile Photo.
 */
public class ContactDataActivity extends Activity {

    EditText editName, editNumber;
    ImageView contactImage;
    Button saveButton;
    private int image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_data);

        editName = (EditText) findViewById(R.id.editName);
        editNumber = (EditText) findViewById(R.id.editNumber);
        contactImage = (ImageView) findViewById(R.id.ContactImage);
        saveButton = (Button) findViewById(R.id.save);

        /*
            Here we set the Click listener on Image view. So that it select the profile pictures.
         */
        contactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                    After clicking on image the User is to move to controllers/Images Activity
                    page where user select the profile picture of contact.
                 */
                Intent intent= new Intent(ContactDataActivity.this, Images.class);

                /*
                    Here we used startActivityForResult() as we expecting some data back from Images
                     Activity which is contact image selected by user.

                     The contact image we get from Images activity, we extract it using code
  >>>                  Line 97-100.
                 */
                startActivityForResult(intent,1);
            }
        });

        /*
            Here we set the click listener on SAVE button.
         */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                    Here we created the object of Contact class and called the Parameterised
                    Constructor.
                 */
                Contacts contacts = new Contacts(editName.getText().toString(),
                        editNumber.getText().toString(),
                        image);

                /*
                    When user click the SAVE button, the activity goes from here to
                    MainActivity.java file. We might want to change this.
                 */
                Intent intent = new Intent(ContactDataActivity.this, MainActivity.class);

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

        image = data.getExtras().getInt("img",1);
        contactImage.setImageResource(image);
    }
}