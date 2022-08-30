package com.varsitycollege.vinyl_warehouse;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.varsitycollege.vinyl_warehouse.AdapterWorkers.AlbumPickAdapter;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseReadWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.OnGetDataListener;
import com.varsitycollege.vinyl_warehouse.Music.Album;
import com.varsitycollege.vinyl_warehouse.Music.Categories;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;

import java.util.ArrayList;
import java.util.List;

public class AllAlbums extends AppCompatActivity {

    // Creating variables for the GUI components
    RecyclerView recyclerAllAlbums_AllAlbums;
    AlbumPickAdapter albumAdapter;
    ProgressBar progressAllAlbum;


    // Declaring constants
    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String ON_FAILURE_DATA_REQUEST = "onFailure: Data request";
    public static final String ON_FAILURE_RECYCLER_FAILED = "onFailure: Recycler Failed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set screen orientation for activity to portrait only

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_albums);
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        progressAllAlbum = findViewById(R.id.progressAllAlbum);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        //Get category id
        String categoryID = extras.getString(CATEGORY_ID);
        // Creating an instance of the FirebaseReadWorker class
        FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(CurrentUser.currentUserObj.getPersonId());
        // Showing the progress dialog
        progressAllAlbum.setVisibility(View.VISIBLE);
        // Implementing the interface methods
        firebaseReadWorker.getCategory(categoryID, new OnGetDataListener() {
            Categories category;

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                // Gets the category object
                category = dataSnapshot.getValue(Categories.class);
            }

            @Override
            public void onStartWork() {
                //Start adapter

                recyclerManager(category);
            }

            @Override
            public void onFailure() {
                Log.d(TAG, ON_FAILURE_DATA_REQUEST);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Moves to intro if user not found
        if (CurrentUser.currentUserObj.getPersonId() == null) {
            // Switching layouts
            Intent intent = new Intent(AllAlbums.this, IntroWelcome.class);
            startActivity(intent);
            finish();
        }
    }

    // Method to manage the recycler view
    public void recyclerManager(Categories categories) {

        List<Album> albumList = new ArrayList<>();
        //Get all album data
        FirebaseReadWorker.getAllAlbums(CurrentUser.currentUserObj.getPersonId(), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                // Getting all the albums
                Album album = dataSnapshot.getValue(Album.class);
                albumList.add(album);
            }

            //start adapter
            @Override
            public void onStartWork() {
                // Hiding the progress dialog
                progressAllAlbum.setVisibility(View.INVISIBLE);
                recyclerAllAlbums_AllAlbums = findViewById(R.id.recyclerAllAlbums_AllAlbums);
                albumAdapter = new AlbumPickAdapter(categories, albumList, AllAlbums.this);
                recyclerAllAlbums_AllAlbums.setLayoutManager(new LinearLayoutManager(AllAlbums.this));
                //start adapter
                recyclerAllAlbums_AllAlbums.setAdapter(albumAdapter);
            }

            @Override
            public void onFailure() {
                // Hiding the progress dialog
                progressAllAlbum.setVisibility(View.INVISIBLE);
                Log.d(TAG, ON_FAILURE_RECYCLER_FAILED);
            }
        });


    }
}