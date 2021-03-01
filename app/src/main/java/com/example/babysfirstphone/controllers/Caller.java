package com.example.babysfirstphone.controllers;

import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;

import static com.example.babysfirstphone.controllers.Constants.EXAMPLE_PHONE_NO;

public class Caller {

    /**
     * Test call function. Takes the default example phone
     * number from controllers/Constants.
     * @return Intent object.
     */
    public Intent makeCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(EXAMPLE_PHONE_NO));
        return intent;
    }

    /**
     * Test call function. Takes the default example phone
     * number from controllers/Constants.
     * @param phoneNo A String with a user phone number.
     * @return Intent object.
     */
    public Intent makeCall(String phoneNo) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(phoneNo));
        return intent;
    }

    /**
     * Function that handles everything related to connecting
     * phone calls.
     * @param textPhone A EditText containing
     * the user's phone number.
     * @return Intent object.
     */
    public Intent makeCallFromUser(EditText textPhone) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + textPhone.getText().toString()));
        return intent;
    }
}