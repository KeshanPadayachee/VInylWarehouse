package com.varsitycollege.vinyl_warehouse.FirebaseWorkers;

import static android.content.ContentValues.TAG;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.varsitycollege.vinyl_warehouse.Music.Track;

public class FirebaseReadWorker {

    // Variable to store the User
    public static final String USER = "users";
    // Variable to store the Album
    public static final String ALBUM = "ALBUM";
    // Variable to store the Track
    public static final String TRACK = "TRACK";
    // Variable to store the Category
    public static final String CATEGORY = "CATEGORY";
    // Variable to store the users id
    private String userID;

    //Constructor
    public FirebaseReadWorker(String userID) {
        this.userID = userID;
    }
    //Code Attribution
    //Link: https://www.youtube.com/watch?v=1qoR9XnWRBc
    //Author:PRABEESH R K
    //End
    //Return all the albums
    public static void getAllAlbums(String userID, OnGetDataListener listener) {
        try {
            //Get Reference
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(USER).child(userID).child(ALBUM);
            //Find all the values under reference
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Get all album data
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        if (snapshot1.exists()) {
                            //return object
                            listener.onSuccess(snapshot1);
                        }
                    }
                    //notify done getting data
                    listener.onStartWork();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onFailure();
                }
            });
        } catch (Exception readException) {
            // Catching the exception
            Log.d(TAG, "getAllAlbum: " + readException.getMessage());
        }
    }
    public  void getAllLinks(OnGetDataListener listener) {
        try {
            //Get Reference
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(USER).child(userID).child("LINKS");
            //Find all the values under reference
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Get all album data
                    for (DataSnapshot albumSnapshot : snapshot.getChildren()) {
                        if (albumSnapshot.exists()) {
                            //return object
                            listener.onSuccess(albumSnapshot);
                        }
                    }
                    //notify done getting data
                    listener.onStartWork();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onFailure();
                }
            });
        } catch (Exception readException) {
            // Catching the exception
            Log.d(TAG, "getAllLinks: " + readException.getMessage());
        }
    }
    //Return all categories
    public void getAllCategory(OnGetDataListener listener) {
        try {
            //Get Reference
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(USER).child(userID).child(CATEGORY);
            //Find all the values under reference
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Get all category data
                    for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                        if (categorySnapshot.exists()) {
                            //return object
                            listener.onSuccess(categorySnapshot);
                        }
                    }
                    //notify done getting data
                    listener.onStartWork();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onFailure();
                }
            });
        } catch (Exception readException) {
            // Catching the exception
            Log.d(TAG, "getAllCategory: " + readException.getMessage());
        }
    }
    //Return all Track
    public void getAllTrack(OnGetDataListener listener) {
        try {
            //Get Reference
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(USER).child(userID).child(TRACK);
            //Find all the values under reference
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Get all category data
                    for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                        if (categorySnapshot.exists()) {
                            //return object
                            listener.onSuccess(categorySnapshot);
                        }
                    }
                    //notify done getting data
                    listener.onStartWork();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onFailure();
                }
            });
        } catch (Exception readException) {
            // Catching the exception
            Log.d(TAG, "getAllTrack: " + readException.getMessage());
        }
    }

    //Return album
    public void getAlbum(String albumID, OnGetDataListener listener) {
        try {
            //Get Reference
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(USER).child(userID).child(ALBUM).child(albumID);
            //Find all the values under reference
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Get album data
                    if (snapshot.exists()) {
                        //return object
                        listener.onSuccess(snapshot);
                    }else{
                        listener.onFailure();
                    }
                    //notify done getting data
                    listener.onStartWork();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onFailure();
                }
            });

        } catch (Exception readException) {
            // Catching the exception
            Log.d(TAG, "getAlbum: " + readException.getMessage());
        }
    }

    public void getTrack(String trackID, OnGetDataListener listener) {
        try {
            //Get Reference
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(USER).child(userID).child(TRACK).child(trackID);
            //Find all the values under reference
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Get album data
                    if (snapshot.exists()) {
                        //return object
                        listener.onSuccess(snapshot);
                        //notify done getting data
                        listener.onStartWork();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onFailure();
                }
            });
        } catch (Exception readException) {
            // Catching the exception
            Log.d(TAG, "getTrack: " + readException.getMessage());
        }
    }

    //Return all tracks
    public void getTracks(String albumID, OnGetDataListener listener) {

        try {   //Get Reference
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(USER).child(userID).child(TRACK);
            //Find all the values under reference
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Get track data
                    for (DataSnapshot trackSnapshot : snapshot.getChildren()) {
                        if (trackSnapshot.exists()) {
                            Track track = trackSnapshot.getValue(Track.class);
                            //check if track is in the required album
                            if (track.getAlbumID().equals(albumID)) {
                                //return object
                                listener.onSuccess(trackSnapshot);
                            }
                        }
                    }
                    //notify done getting data
                    listener.onStartWork();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onFailure();
                }
            });
        } catch (Exception readException) {
            // Catching the exception
            Log.d(TAG, "getTracks: " + readException.getMessage());
        }
    }

    //Return category
    public void getCategory(String categoryID, OnGetDataListener listener) {
        try {
            //Get Reference
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(USER).child(userID).child(CATEGORY).child(categoryID);
            //Find all the values under reference
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Get album data
                    if (snapshot.exists()) {
                        //return object
                        listener.onSuccess(snapshot);
                    }
                    //notify done getting data
                    listener.onStartWork();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onFailure();
                }
            });
        } catch (Exception readException) {
            // Catching the exception
            Log.d(TAG, "getCategory: " + readException.getMessage());
        }
    }

    //Return category
    public void getUser(OnGetDataListener listener) {
        try {
            //Get Reference
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(USER).child(userID);
            //Find all the values under reference
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Get album data
                    if (snapshot.exists()) {
                        //return object
                        listener.onSuccess(snapshot);
                    }
                    //notify done getting data
                    listener.onStartWork();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onFailure();
                }
            });
        } catch (Exception readException) {
            // Catching the exception
            Log.d(TAG, "getUser: " + readException.getMessage());
        }
    }
}
