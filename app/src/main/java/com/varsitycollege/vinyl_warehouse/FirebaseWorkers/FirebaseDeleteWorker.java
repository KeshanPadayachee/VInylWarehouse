package com.varsitycollege.vinyl_warehouse.FirebaseWorkers;

import static android.content.ContentValues.TAG;

import android.util.Log;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.varsitycollege.vinyl_warehouse.Music.Album;
import com.varsitycollege.vinyl_warehouse.Music.Categories;
import com.varsitycollege.vinyl_warehouse.Music.CoverLinks;
import com.varsitycollege.vinyl_warehouse.Music.Track;

public class FirebaseDeleteWorker {

    // Variable to store the Track
    public static final String TRACK = "TRACK";
    // Variable to store the Album
    public static final String ALBUM = "ALBUM";
    // Variable to store the User
    public static final String USER = "users";
    // Variable to store the Category
    public static final String CATEGORY = "CATEGORY";
    // Variable to store the users id
    private String userID;

    //Constructor
    public FirebaseDeleteWorker(String userID) {
        this.userID = userID;
    }
    //Code Attribution
    //Link: https://www.youtube.com/watch?v=2yepe4GYa90
    //Author: Simplified Coding
    //End
    //Delete a single album with cover
    public void deleteAlbum(Album album, OnSuccessListener onSuccessListener) {
        try {

            //Get Reference
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(USER).child(userID).child(ALBUM).child(album.getAlbumID());
            //Delete the value from DB Reference
            reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //instantiate  storage reference
                    FirebaseStorageWorker firebaseStorageWorker = new FirebaseStorageWorker(userID);
                    //Delete image
                    firebaseStorageWorker.deleteImage(album.getCoverLink(), new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            onSuccessListener.onSuccess(true);
                        }
                    });
                }
            });
        } catch (Exception deleteException) {
            // Catching the exception
            Log.d(TAG, "deleteAlbum: " + deleteException.getMessage());
        }
    }

    //Delete a single track with cover
    public void deleteTrack(Track track, OnSuccessListener onSuccessListener) {
        try {
            //Get Reference
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(USER).child(userID).child(TRACK).child(track.getTrackID());
            //Delete the value from DB Reference
            reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //instantiate storage reference
                    FirebaseStorageWorker firebaseStorageWorker = new FirebaseStorageWorker(userID);
                    //Check if link is null
                    if (track.getCoverLink()!="") {
                        //if not null then delete the image
                        firebaseStorageWorker.deleteImage(track.getCoverLink(), new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                onSuccessListener.onSuccess(true);
                            }
                        });
                    }else{
                        onSuccessListener.onSuccess(true);
                    }
                }
            });
        } catch (Exception deleteException) {
            // Catching the exception
            Log.d(TAG, "deleteTrack: " + deleteException.getMessage());
        }
    }

    //Delete single category
    public void deleteCategory(Categories category, OnSuccessListener onSuccessListener) {
        try {
            //Get Reference
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(USER).child(userID).child(CATEGORY).child(category.getCategoryID());
            //Delete the value from DB Reference
            reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    onSuccessListener.onSuccess(true);
                }
            });
        } catch (Exception deleteException) {
            // Catching the exception
            Log.d(TAG, "deleteCategory: " + deleteException.getMessage());
        }
    }

    //Delete single link
    public void deleteLink(CoverLinks link, OnSuccessListener onSuccessListener) {
        try {
            //Get Reference
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(USER).child(userID).child("LINKS").child(link.getCoverId());
            //Delete the value from DB Reference
            reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    onSuccessListener.onSuccess(true);
                }
            });
        } catch (Exception deleteException) {
            // Catching the exception
            Log.d(TAG, "deleteLink: " + deleteException.getMessage());
        }
    }

    //delete user
    public void deleteUser(OnSuccessListener onSuccessListener) {
        try {
            //Get Reference
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(USER).child(userID);
            //Delete the value from DB Reference
            reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    onSuccessListener.onSuccess(true);
                }
            });
        } catch (Exception deleteException) {
            // Catching the exception
            Log.d(TAG, "deleteUser: " + deleteException.getMessage());
        }
    }
}
