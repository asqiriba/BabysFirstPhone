package com.example.babysfirstphone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.babysfirstphone.controllers.Caller;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomePage extends Fragment {

    Caller caller = new Caller();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        caller.printAlert();
        View view = inflater.inflate(R.layout.home_page, container, false);
        return view;
    }
}