package com.example.babysfirstphone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.widget.ImageButton;
import com.example.babysfirstphone.controllers.Caller;
import com.example.babysfirstphone.controllers.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EmergencyFragment extends Fragment {

    FloatingActionButton fab;
    ImageButton dadBtn;
    ImageButton momBtn;

    Caller caller = new Caller();
    Constants constants = new Constants();

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

        fab = (FloatingActionButton) view.findViewById(R.id.home_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        return view;
    }
}
