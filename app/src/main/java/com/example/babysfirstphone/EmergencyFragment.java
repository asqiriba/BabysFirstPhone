package com.example.babysfirstphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        images = new int[] {R.drawable.green_background, R.drawable.red_background,R.drawable.pink_background, R.drawable.purple_background, R.drawable.blue_background, R.drawable.yellow_background };
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("colorTheme", Context.MODE_PRIVATE);
        getNum = sharedPreferences.getInt("color",0);
        emergencyScreen.setBackgroundResource(images[getNum]);


    }


}
