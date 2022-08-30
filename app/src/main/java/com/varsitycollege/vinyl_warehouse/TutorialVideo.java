package com.varsitycollege.vinyl_warehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class TutorialVideo extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlaybackEventListener, YouTubePlayer.PlayerStateChangeListener {
    // Declaring variables for the GUI components
    YouTubePlayerView player;
    TextView txvTutorialVideo;

    // API KEY
    String API = "AIzaSyD7X3QYiKCyaisB-r0e55vnDzbG-tqhdZo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Assigning GUI components to the variables
        setContentView(R.layout.activity_tutorial_video);
        player = findViewById(R.id.playerview);
        txvTutorialVideo = findViewById(R.id.txvTutorialVideo_Skip);
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Handling the TextViewOnClickListener
        txvTutorialVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to main activity
                Intent toMainActivity = new Intent(TutorialVideo.this, MainActivity.class);
                startActivity(toMainActivity);
                finish();
            }
        });

        // Initializing the YouTube Player
        player.initialize(API, TutorialVideo.this);
    }

    // Method to handle the YouTube Player initialization
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setPlayerStateChangeListener(TutorialVideo.this);

        youTubePlayer.setPlaybackEventListener(this);
        if (!b) {
            youTubePlayer.cueVideo("Bxj3TA1QwmI");
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        // EMPTY METHOD NOT BEING USED
    }

    @Override
    public void onPlaying() {
        // EMPTY METHOD NOT BEING USED
    }

    @Override
    public void onPaused() {
        // EMPTY METHOD NOT BEING USED
    }

    @Override
    public void onStopped() {
        // EMPTY METHOD NOT BEING USED
    }

    @Override
    public void onBuffering(boolean b) {
        // EMPTY METHOD NOT BEING USED
    }

    @Override
    public void onSeekTo(int i) {
        // EMPTY METHOD NOT BEING USED
    }

    @Override
    public void onLoading() {
        // EMPTY METHOD NOT BEING USED
    }

    @Override
    public void onLoaded(String s) {
        // EMPTY METHOD NOT BEING USED
    }

    @Override
    public void onAdStarted() {
        // EMPTY METHOD NOT BEING USED
    }

    @Override
    public void onVideoStarted() {
        // EMPTY METHOD NOT BEING USED
    }

    @Override
    public void onVideoEnded() {
        // EMPTY METHOD NOT BEING USED
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        // EMPTY METHOD NOT BEING USED
    }
}