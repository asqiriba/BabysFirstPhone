package com.example.babysfirstphone;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SettingsInternal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_internal);

        FloatingActionButton fab = findViewById(R.id.home_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsInternal();
            }
        });
    }

    public void openSettingsInternal(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}