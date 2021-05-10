package com.example.babysfirstphone.contacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.babysfirstphone.R;
import com.example.babysfirstphone.controllers.Contacts;

/*
    Used to display the selected contact.
    In ContactDetails we used TextView to display name and number.
 */
public class ContactDetails extends Activity {

    Contacts ContactDetails;
    private String contactName, contactNumber, contactImage;
    TextView tv_name, tv_number;
    ImageView iv_image;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_data);

        iv_image = (ImageView) findViewById(R.id.ContactImage);
        backButton = (Button) findViewById(R.id.save);
        tv_name = (TextView) findViewById(R.id.editName);
        tv_number = (TextView) findViewById(R.id.editNumber);

        Intent intent = getIntent();
        ContactDetails = (Contacts) intent.getSerializableExtra("details");

        contactImage = ContactDetails.getImage();
        contactNumber = ContactDetails.getNumber();
        contactName = ContactDetails.getName();

        tv_name.setText(contactName);
        tv_number.setText(contactNumber);
        iv_image.setImageBitmap(BitmapFactory.decodeFile(contactImage));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}