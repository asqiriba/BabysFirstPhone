package com.example.babysfirstphone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.widget.ImageButton;
import com.example.babysfirstphone.controllers.Caller;
import com.example.babysfirstphone.controllers.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EmergencyFragment extends Fragment {

    FloatingActionButton fab;
    ImageButton dadBtn;
    ImageButton momBtn;
    ImageButton mapBtn;

    Caller caller = new Caller();
    Constants constants = new Constants();
    int[] images;
    int getNum;
    View emergencyScreen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency, container, false);

        dadBtn = (ImageButton) view.findViewById(R.id.dadBtn);
        dadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(caller.makeCall(constants.getEMERGENCY_NO1()));
            }
        });

        momBtn = (ImageButton) view.findViewById(R.id.momBtn);
        momBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(caller.makeCall(constants.getEMERGENCY_NO2()));
            }
        });

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
        images = new int[] {R.drawable.a, R.drawable.b,R.drawable.c, R.drawable.d, R.drawable.f,R.drawable.g };
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("colorTheme", Context.MODE_PRIVATE);
        getNum = sharedPreferences.getInt("color",0);
        emergencyScreen.setBackgroundResource(images[getNum]);


    }
}
