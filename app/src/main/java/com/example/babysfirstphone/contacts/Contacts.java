package com.example.babysfirstphone.contacts;


import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class Contacts extends AppCompatActivity {
    public static ArrayList<com.example.babysfirstphone.controllers.Contacts> arrayListContact;

    /*
       Writes the created user into a json file for persistence.
    */
    public void saveData(ArrayList<com.example.babysfirstphone.controllers.Contacts> arrayListContact) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(arrayListContact);
        editor.putString("contact list", json);
        editor.apply();
    }

    /*
        Reads the created user into a json file for persistence.
     */
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("contact list", null);
        Type type = new TypeToken<ArrayList<com.example.babysfirstphone.controllers.Contacts>>() {}.getType();
        arrayListContact = gson.fromJson(json, type);

        if (arrayListContact == null || arrayListContact.isEmpty()) {
            arrayListContact = new ArrayList<>();
        }
    }

    /*
        To delete from file, go to View/Tool Windows/Device File Explorer; the file in which it's all
        written is in /data/data/com.example.babysfirstphone/shared_prefs/shared preferences.xml
     */
    public void deleteData(ArrayList<com.example.babysfirstphone.controllers.Contacts> arrayListContact) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();

        Gson gson = new Gson();
        String json = gson.toJson(arrayListContact);
        editor.putString("contact list", json);
        editor.apply();
    }

    public void deleteData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }

}
