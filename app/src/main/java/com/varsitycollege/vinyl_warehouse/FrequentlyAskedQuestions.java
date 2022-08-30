package com.varsitycollege.vinyl_warehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class FrequentlyAskedQuestions extends AppCompatActivity {

    // Declaring variables for the GUI components
    Button btn_tutorial;
    ImageView img_back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequently_asked_questions);
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Assigning GUI components to the variables
        btn_tutorial = findViewById(R.id.btn_view_tutorial);
        img_back_arrow = findViewById(R.id.imgAddTrack_BackArrow);

        // Handling the button click
        btn_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewTutorial = new Intent(FrequentlyAskedQuestions.this, TutorialVideo.class);
                startActivity(viewTutorial);
                finish();

            }
        });

        // Handling the button click
        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToMain = new Intent(FrequentlyAskedQuestions.this, MainActivity.class);
                startActivity(backToMain);
                finish();
            }
        });

    }
}