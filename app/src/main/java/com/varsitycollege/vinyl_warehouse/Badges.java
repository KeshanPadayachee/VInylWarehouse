package com.varsitycollege.vinyl_warehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.google.firebase.database.DataSnapshot;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseReadWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.OnGetDataListener;
import com.varsitycollege.vinyl_warehouse.Music.Album;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;

import java.util.ArrayList;
import java.util.List;

//Code Attribution
//Link: https://www.youtube.com/watch?v=fqU4zc_XeX0&t=232s
//Author:Inside Android
//Code: Animation animationSlide= AnimationUtils.loadAnimation();
//Animations in anim folder
//End
public class Badges extends AppCompatActivity implements View.OnClickListener {

    // Creating variables for the GUI components
    ProgressBar progressBadges;
    ImageView imgBadges_badges10, imgBadges_badges20, imgBadges_badges50, imgBadges_badges100, imgBackArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //get all badges images
        imgBadges_badges10 = findViewById(R.id.imgBadges_badges10);
        imgBadges_badges20 = findViewById(R.id.imgBadges_badges20);
        imgBadges_badges50 = findViewById(R.id.imgBadges_badges50);
        imgBadges_badges100 = findViewById(R.id.imgBadges_badges100);
        imgBackArrow = findViewById(R.id.imgBadges_BackArrow);
        progressBadges = findViewById(R.id.progressBadges);

        //set viability for all badges
        imgBadges_badges10.setVisibility(View.INVISIBLE);
        imgBadges_badges20.setVisibility(View.INVISIBLE);
        imgBadges_badges50.setVisibility(View.INVISIBLE);
        imgBadges_badges100.setVisibility(View.INVISIBLE);

        //set on click listeners for badges and backarrow
        imgBadges_badges10.setOnClickListener(this);
        imgBadges_badges20.setOnClickListener(this);
        imgBadges_badges50.setOnClickListener(this);
        imgBadges_badges100.setOnClickListener(this);
        imgBackArrow.setOnClickListener(this);


        //implement firebase worker to pull user data
        FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(CurrentUser.currentUserObj.getPersonId());
        List<Album> albumList = new ArrayList<>();
        // Showing the progress dialog
        progressBadges.setVisibility(View.VISIBLE);

        Animation animationSlide = AnimationUtils.loadAnimation(Badges.this, R.anim.lefttoright);
        //pull all users albums from firebase into list
        FirebaseReadWorker.getAllAlbums(CurrentUser.currentUserObj.getPersonId(), new OnGetDataListener() {

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                // Getting all the albums
                Album album = dataSnapshot.getValue(Album.class);
                albumList.add(album);
            }

            @Override
            public void onStartWork() {
                // Hiding the progress dialog
                progressBadges.setVisibility(View.INVISIBLE);
                //set badges for user based on albums they have
                switch (albumList.size() / 10) {
                    case 1:
                        imgBadges_badges10.setVisibility(View.VISIBLE);
                        imgBadges_badges20.setVisibility(View.INVISIBLE);
                        imgBadges_badges50.setVisibility(View.INVISIBLE);
                        imgBadges_badges100.setVisibility(View.INVISIBLE);

                        imgBadges_badges10.startAnimation(animationSlide);
                        break;
                    case 2:
                        imgBadges_badges10.setVisibility(View.VISIBLE);
                        imgBadges_badges20.setVisibility(View.VISIBLE);
                        imgBadges_badges50.setVisibility(View.INVISIBLE);
                        imgBadges_badges100.setVisibility(View.INVISIBLE);

                        imgBadges_badges10.startAnimation(animationSlide);
                        imgBadges_badges20.startAnimation(animationSlide);
                        break;
                    case 5:
                        imgBadges_badges10.setVisibility(View.VISIBLE);
                        imgBadges_badges20.setVisibility(View.VISIBLE);
                        imgBadges_badges50.setVisibility(View.VISIBLE);
                        imgBadges_badges100.setVisibility(View.INVISIBLE);

                        imgBadges_badges10.startAnimation(animationSlide);
                        imgBadges_badges20.startAnimation(animationSlide);
                        imgBadges_badges50.startAnimation(animationSlide);
                        break;
                    case 10:
                        imgBadges_badges10.setVisibility(View.VISIBLE);
                        imgBadges_badges20.setVisibility(View.VISIBLE);
                        imgBadges_badges50.setVisibility(View.VISIBLE);
                        imgBadges_badges100.setVisibility(View.VISIBLE);

                        imgBadges_badges10.startAnimation(animationSlide);
                        imgBadges_badges20.startAnimation(animationSlide);
                        imgBadges_badges50.startAnimation(animationSlide);
                        imgBadges_badges100.startAnimation(animationSlide);
                        break;
                }
            }

            @Override
            public void onFailure() {
                // Empty method. not being used
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (CurrentUser.currentUserObj.getPersonId() == null) {
            // Swtiching layouts
            Intent introWelcomeIntent = new Intent(Badges.this, IntroWelcome.class);
            startActivity(introWelcomeIntent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        Animation animationRotate = AnimationUtils.loadAnimation(Badges.this, R.anim.rotate);
        //set code for clicks of different badge components
        switch (view.getId()) {
            case R.id.imgBadges_badges10:
                imgBadges_badges10.startAnimation(animationRotate);
                break;
            case R.id.imgBadges_badges20:
                imgBadges_badges20.startAnimation(animationRotate);
                break;
            case R.id.imgBadges_badges50:
                imgBadges_badges50.startAnimation(animationRotate);
                break;
            case R.id.imgBadges_badges100:
                imgBadges_badges100.startAnimation(animationRotate);
                break;
            case R.id.imgBadges_BackArrow:
                // Switching layouts
                Intent toMainActivity = new Intent(Badges.this, MainActivity.class);
                startActivity(toMainActivity);
                finish();
                break;
        }
    }
}