package com.example.babysfirstphone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.babysfirstphone.controllers.Caller;
import com.example.babysfirstphone.controllers.Contacts;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EmergencyFragment extends Fragment {

    FloatingActionButton fab;
    ImageButton dadBtn;
    ImageButton momBtn;
    ImageButton mapBtn;

    Caller caller = new Caller();
    int[] images;
    int getNum;
    View emergencyScreen;
    ArrayList<Contacts> arrayListContact;
    List<String> name = new ArrayList<>();
    List<String> type = new ArrayList<>();
    List<String> image = new ArrayList<>();
    List<String> info = new ArrayList<>();
    String dadPhoneNumber;
    String momPhoneNumber;
    String dadsName;
    String momsName;
    boolean dadAllowed = false;
    boolean momAllowed = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency, container, false);

//        dadBtn = (ImageButton) view.findViewById(R.id.dadBtn);
//        dadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                emergencyCall("Dad");
//            }
//        });
//
//        momBtn = (ImageButton) view.findViewById(R.id.momBtn);
//        momBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                emergencyCall("Mom");
//            }
//        });

        /////////////////// My Code //////////////////////////////
        // Obtains contact information from saved contacts
        loadContactData();
        if (!arrayListContact.isEmpty()){
            for(int i = 0; i < arrayListContact.size() ; i++){
                type.add(arrayListContact.get(i).getType());
                image.add(arrayListContact.get(i).getImage());
                info.add(arrayListContact.get(i).getNumber().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"));
                name.add(arrayListContact.get(i).getName().toLowerCase().toLowerCase());
            }
        }
        // Obtains parents names from inside settings
        loadParentsNames();

        dadBtn = (ImageButton) view.findViewById(R.id.dadBtn);
        momBtn = (ImageButton) view.findViewById(R.id.momBtn);

        // Index of parents information in other arrays
        int indexOfDad;
        int indexOfMom;


        if (!info.isEmpty() && name.contains(dadsName)){
            indexOfDad = name.indexOf(dadsName);
            if(type.get(indexOfDad).equals("phone")){
                // Gets Dad info
                dadPhoneNumber = info.get(indexOfDad);
                dadBtn.setImageBitmap(BitmapFactory.decodeFile(image.get(indexOfDad)));
                dadAllowed = true;
                // Saves Mom info for MapActivity
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ParentsNumbers", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Dad", dadPhoneNumber);
                editor.apply();
            }else{
                dadBtn.setImageResource(R.drawable.children_icon_1);
            }
        }

        if (!info.isEmpty() && name.contains(momsName)){
            indexOfMom = name.indexOf(momsName);
            if(type.get(indexOfMom).equals("phone")){
                // Obtains Mom's info for MapActivity
                momPhoneNumber = info.get(indexOfMom);
                momBtn.setImageBitmap(BitmapFactory.decodeFile(image.get(indexOfMom)));
                momAllowed = true;
                // Saves Mom's info
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ParentsNumbers", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Mom", momPhoneNumber);
                editor.apply();

            }else{
                momBtn.setImageResource(R.drawable.girl_icon);
            }
        }

        dadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dadAllowed){
                    callPhoneNumber(dadPhoneNumber);
                }else{
                    Toast toast = Toast.makeText(getContext(), "Dad could not be found.", Toast.LENGTH_SHORT);
                    toast.show();
                }
//                emergencyCall("Dad");
            }
        });


        momBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(momAllowed){
                    callPhoneNumber(momPhoneNumber);
                }else{
                    Toast toast = Toast.makeText(getContext(), "Mom could not be found.", Toast.LENGTH_SHORT);
                    toast.show();
                }
//                emergencyCall("Mom");
            }
        });


        /////////////////// My Code //////////////////////////////

        mapBtn = (ImageButton) view.findViewById(R.id.mapBtn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), MapsActivity.class);
                startActivity(in);
            }
        });

        fab = (FloatingActionButton) view.findViewById(R.id.home_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        emergencyScreen = view.findViewById(R.id.emergency);
        changeColor();

        return view;
    }

    public void changeColor() {
        images = new int[] {R.drawable.green_background, R.drawable.red_background,R.drawable.pink_background, R.drawable.purple_background, R.drawable.blue_background, R.drawable.yellow_background };
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("colorTheme", Context.MODE_PRIVATE);
        getNum = sharedPreferences.getInt("color",0);
        emergencyScreen.setBackgroundResource(images[getNum]);


    }

    public void emergencyCall(String contactType) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("EmergencyContacts", Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString(contactType,"");
        startActivity(caller.makeCall(phoneNumber));

    }

    private void loadContactData() {
//        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("contact list", null);
        Type type = new TypeToken<ArrayList<Contacts>>() {}.getType();
        arrayListContact = gson.fromJson(json, type);

        if (arrayListContact == null || arrayListContact.isEmpty()) {
            arrayListContact = new ArrayList<Contacts>();
        }
    }
    private void loadParentsNames() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ParentsNames", Context.MODE_PRIVATE);
        momsName = sharedPreferences.getString("Mom", "Default").toLowerCase();
        dadsName = sharedPreferences.getString("Dad", "Default").toLowerCase();
    }

    public void callPhoneNumber(String phoneNumber) {

        // If permission is already given, connect a call.
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.CALL_PHONE}, 101);
                    return;
                }
            }
            startActivity(caller.makeCall(phoneNumber));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
