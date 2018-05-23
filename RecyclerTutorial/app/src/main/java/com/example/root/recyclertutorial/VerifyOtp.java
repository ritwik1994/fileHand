package com.example.root.recyclertutorial;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Luther on 5/23/18.
 */

public class VerifyOtp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.otp_layout);

    }
}
