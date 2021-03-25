package com.example.babysfirstphone.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.babysfirstphone.MainActivity;
import com.example.babysfirstphone.R;
import com.example.babysfirstphone.controllers.Contacts;

public class ContactData extends Activity {

    EditText editName, editNumber;
    ImageView contactImage;
    Button saveButton;
    private int image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_data);

        editName= (EditText) findViewById(R.id.editName);
        editNumber= (EditText) findViewById(R.id.editNumber);

        contactImage= (ImageView) findViewById(R.id.ContactImage);
        saveButton = (Button) findViewById(R.id.save);

        contactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(ContactData.this, MediaStore.Images.class);

                startActivityForResult(intent2,1);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Contacts contacts = new Contacts(editName.getText().toString(),
                        editNumber.getText().toString(),
                        image);

                Intent intent5 = new Intent(ContactData.this, MainActivity.class);

                intent5.putExtra("data",contacts);
                setResult(2, intent5);

                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        image = data.getExtras().getInt("img",1);
        contactImage.setImageResource(image);

    }

}