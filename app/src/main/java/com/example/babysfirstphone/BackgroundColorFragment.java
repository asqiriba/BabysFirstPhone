package com.example.babysfirstphone;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class BackgroundColorFragment extends Fragment {

    View screen;
    ImageButton greenBtn, redBtn, pinkBtn, purpleBtn, blueBtn, yellowBtn;
    int[] bgImages;
    String COLOR = "colorTheme";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_background_color, container, false);

        // Set up paint buttons to change the background colors
        bgImages = new int[] {R.drawable.green_background, R.drawable.red_background,R.drawable.pink_background, R.drawable.purple_background, R.drawable.blue_background, R.drawable.yellow_background };
        screen = view.findViewById(R.id.background);
        greenBtn = (ImageButton) view.findViewById(R.id.green);
        redBtn = (ImageButton) view.findViewById(R.id.red);
        pinkBtn = (ImageButton) view.findViewById(R.id.pink);
        purpleBtn = (ImageButton) view.findViewById(R.id.purple);
        blueBtn = (ImageButton) view.findViewById(R.id.blue);
        yellowBtn = (ImageButton) view.findViewById(R.id.yellow);


        greenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screen.setBackgroundResource(bgImages[0]);

                // save that random number to a local storage
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(COLOR, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("color",0);
                editor.commit();

            }
        });

        redBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screen.setBackgroundResource(bgImages[1]);

                // save that random number to a local storage
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(COLOR, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("color",1);
                editor.commit();

            }
        });
//
        pinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screen.setBackgroundResource(bgImages[2]);

                // save that random number to a local storage
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(COLOR, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("color",2);
                editor.commit();

            }
        });
//
        purpleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screen.setBackgroundResource(bgImages[3]);

                // save that random number to a local storage
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(COLOR, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("color",3);
                editor.commit();

            }
        });
//
        blueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screen.setBackgroundResource(bgImages[4]);

                // save that random number to a local storage
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(COLOR, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("color",4);
                editor.commit();

            }
        });
//
        yellowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screen.setBackgroundResource(bgImages[5]);

                // save that random number to a local storage
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(COLOR, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("color",5);
                editor.commit();

            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}