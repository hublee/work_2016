package com.zeustel.top9;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zeustel.top9.fragments.SupportNoteFragment;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.support, new SupportNoteFragment()).commit();
        }
    }
}
