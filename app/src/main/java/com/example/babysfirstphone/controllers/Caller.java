package com.example.babysfirstphone.controllers;

import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;

public class Caller {
    /**
     * Class that handles everything related to connecting
     * phone calls.
     */

    public Intent makeCall(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:805-401-7721"));
        return intent;
    }

    public Intent makeCallFromUser(EditText textPhone){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + textPhone.getText().toString()));
        return intent;
    }

    public void printAlert(){
        System.out.println("Works");
    }
}