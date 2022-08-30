package com.varsitycollege.vinyl_warehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;

public class PrivacyPolicy extends AppCompatActivity {

    //Variable declaration
    private ImageView imgBackArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_privacy_policy);
        // Assigning component to the variable
        imgBackArrow = findViewById(R.id.imgPrivacyPolicy_BackArrow);

        //OnclickLister for privacy policy button
        imgBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switching layouts
                Intent toMainActivity = new Intent(PrivacyPolicy.this, MainActivity.class);
                startActivity(toMainActivity);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (CurrentUser.currentUserObj.getPersonId() == null) {
            // Switching layouts
            Intent intent = new Intent(PrivacyPolicy.this, IntroWelcome.class);
            startActivity(intent);
            finish();
        }
    }
}