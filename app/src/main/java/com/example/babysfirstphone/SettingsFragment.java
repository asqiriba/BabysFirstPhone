package com.example.babysfirstphone;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

public class SettingsFragment extends Fragment {
    FloatingActionButton fab;
    private EditText editTextSettings;
    private Button buttonConfirm;
    private TextInputLayout textInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        fab = (FloatingActionButton) view.findViewById(R.id.home_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        // Settings Pass Code
        editTextSettings = (EditText) view.findViewById(R.id.text_settings);
        buttonConfirm = (Button) view.findViewById(R.id.button_confirm);
        editTextSettings.addTextChangedListener(loginTextWatcher);

        // Lets you into internal settings
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingsInternal.class));
            }
        });

        return view;
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String userNameInput = editTextSettings.getText().toString().trim().toLowerCase();
            buttonConfirm.setEnabled(userNameInput.equals("settings"));
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}