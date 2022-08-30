package com.varsitycollege.vinyl_warehouse.FirebaseWorkers;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.varsitycollege.vinyl_warehouse.Music.Album;
import com.varsitycollege.vinyl_warehouse.Music.Categories;
import com.varsitycollege.vinyl_warehouse.Music.CoverLinks;
import com.varsitycollege.vinyl_warehouse.Music.Track;

import java.util.List;

public class FirebaseWriteWorker {

    // Creating a variable for Track
    public static final String TRACK = "TRACK";
    // Creating a variable for Album
    public static final String ALBUM = "ALBUM";
    // Creating a variable for User
    public static final String USER = "users";
    // Creating a variable for Category
    public static final String CATEGORY = "CATEGORY";
    // Creating a variable for the user id
    private String userID;
    //Code Attribution
    //Link: https://www.youtube.com/watch?v=1qoR9XnWRBc
    //Author:PRABEESH R K
    //End
    // Constructor
    public FirebaseWriteWorker(String userID) {
        // Assigning the userid parameter to the created variable
        this.userID = userID;
    }

    //Add a new Album to db
    public void WriteAlbum(Album newAlbum, OnSuccessListener listener) {
        try {
            // Adding the album to the database
            FirebaseDatabase.getInstance().getReference().child(USER).child(userID).child(ALBUM).child(newAlbum.getAlbumID()).setValue(newAlbum).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    listener.onSuccess(true);
                }
            });
        } catch (Exception writeException) {
            // Catching the exception
            Log.d(TAG, "writeAlbum: " + writeException.getMessage());
        }
    }

    //Add a new Track to db
    public void WriteTrack(Track newTrack, OnSuccessListener listener) {
        try {
            // Adding the track to the database
            FirebaseDatabase.getInstance().getReference().child(USER).child(userID).child(TRACK).child(newTrack.getTrackID()).setValue(newTrack).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    listener.onSuccess(true);
                }
            });
        } catch (Exception writeException) {
            // Catching the exception
            Log.d(TAG, "WriteTrack: " + writeException.getMessage());
        }
    }

    //Create a new Category to db
    public void WriteCategory(Categories newCategory, OnSuccessListener listener) {
        try {
            // Adding the category to the database
            FirebaseDatabase.getInstance().getReference().child(USER).child(userID).child(CATEGORY).child(newCategory.getCategoryID()).setValue(newCategory).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    listener.onSuccess(true);
                }
            });
        } catch (Exception writeException) {
            // Catching the exception
            Log.d(TAG, "WriteCategory: " + writeException.getMessage());
        }
    }
    //Create a new Link to db
    public void WriteLink(CoverLinks links, OnSuccessListener listener) {
        try {
            // Adding the category to the database
            FirebaseDatabase.getInstance().getReference().child(USER).child(userID).child("LINKS").child(links.getCoverId()).setValue(links).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    listener.onSuccess(true);
                }
            });
        } catch (Exception writeException) {
            // Catching the exception
            Log.d(TAG, "WriteLink: " + writeException.getMessage());
        }
    }

}
