package com.example.babysfirstphone.contacts;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.babysfirstphone.MainActivity;
import com.example.babysfirstphone.R;
import com.example.babysfirstphone.controllers.Contacts;

public class ContactData extends Activity {

    EditText editName, editNumber;
    Spinner contactType;
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
        contactType = findViewById(R.id.dropdown);
        saveButton = findViewById(R.id.save);

        contactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contacts contacts = new Contacts(editName.getText().toString(),
                        editNumber.getText().toString(),
                        picturePath,
                        String.valueOf(contactType.getSelectedItem())
                );

                Intent intent = new Intent(ContactData.this, MainActivity.class);

                intent.putExtra("data", contacts);
                setResult(2, intent);

                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
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
}