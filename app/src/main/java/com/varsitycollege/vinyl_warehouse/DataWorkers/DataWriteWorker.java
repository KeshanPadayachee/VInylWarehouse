package com.varsitycollege.vinyl_warehouse.DataWorkers;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseReadWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseStorageWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseWriteWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.OnGetDataListener;
import com.varsitycollege.vinyl_warehouse.Music.Album;
import com.varsitycollege.vinyl_warehouse.Music.Track;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;
import com.varsitycollege.vinyl_warehouse.Utils.Util;

public class DataWriteWorker {
    // Creating variables
    private String userID;
    private Context context;

    //Constructor
    public DataWriteWorker(String userID, Context context) {
        // Assigning parameters to the variables
        this.userID = userID;
        this.context = context;
    }

    //Write album data and upload cover image
    public void writeAlbum(Album newAlbum, Uri imageUri, OnSuccessListener listener) {
        try {
            //Get file name
            String fileName = Util.getFileName(imageUri, context);
            //Upload image
            FirebaseStorageWorker firebaseStorageWorker = new FirebaseStorageWorker(userID);
            firebaseStorageWorker.uploadPic(imageUri, fileName, new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    newAlbum.setCoverLink(o.toString());
                    // Creating an instance of the FirebaseWriteWorker class
                    FirebaseWriteWorker firebaseWriteWorker = new FirebaseWriteWorker(userID);
                    //Write album to db
                    firebaseWriteWorker.WriteAlbum(newAlbum, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            listener.onSuccess(true);
                        }
                    });
                }
            });

        } catch (Exception writeException) {
            // Catching the exception
            Log.d(TAG, "writeAlbum: " + writeException.getMessage());
        }
    }

    //Write album data and upload cover image
    public void writeAlbum(Album newAlbum, Bitmap bitmapCover, OnSuccessListener listener) {
        try {
            // Creating an instance of the FirebaseStorageWorker class
            FirebaseStorageWorker firebaseStorageWorker = new FirebaseStorageWorker(userID);
            //Upload image
            firebaseStorageWorker.uploadPic(bitmapCover, new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    //set the cover from the uploaded image
                    newAlbum.setCoverLink(o.toString());

                    // Creating an instance of the FirebaseWriteWorker class
                    FirebaseWriteWorker firebaseWriteWorker = new FirebaseWriteWorker(userID);
                    //Write album to db
                    firebaseWriteWorker.WriteAlbum(newAlbum, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            listener.onSuccess(true);
                        }
                    });
                }
            });
        } catch (Exception writeException) {
            // Catching the exception
            Log.d(TAG, "writeAlbum: " + writeException.getMessage());
        }
    }

    //Write album data
    public void writeAlbum(Album newAlbum, OnSuccessListener listener) {
        try {
            // Creating an instance of the FirebaseWriteWorker class
            FirebaseWriteWorker firebaseWriteWorker = new FirebaseWriteWorker(userID);
            //Write album to db
            firebaseWriteWorker.WriteAlbum(newAlbum, new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    listener.onSuccess(true);
                }
            });
        } catch (Exception writeException) {
            // Catching the exception
            Log.d(TAG, "writeAlbum: " + writeException.getMessage());
        }
    }


    //Write track to db
    public void writeTrack(Track newTrack, Uri imageUri, OnSuccessListener listener) {
        try {
            //Check if image was sent
            // if image provided then upload the image
            FirebaseStorageWorker firebaseStorageWorker = new FirebaseStorageWorker(userID);
            //get file name
            String fileName = Util.getFileName(imageUri, context);
            //upload image
            firebaseStorageWorker.uploadPic(imageUri, fileName, new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    //Write track to db
                    FirebaseWriteWorker firebaseWriteWorker = new FirebaseWriteWorker(userID);
                    //set the track cover to one that was uploaded
                    newTrack.setCoverLink(o.toString());
                    //write track
                    firebaseWriteWorker.WriteTrack(newTrack, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            listener.onSuccess(true);
                        }
                    });
                }
            });

        } catch (Exception writeException) {
            // Catching the exception
            Log.d(TAG, "writeTrack: " + writeException.getMessage());
        }
    }

    //Write track to db
    public void writeTrack(Track newTrack, Bitmap bitmapCover, OnSuccessListener listener) {
        try {
            String[] coverLink = {""};
            // if image provided then upload the image
            FirebaseStorageWorker firebaseStorageWorker = new FirebaseStorageWorker(userID);
            //upload image
            firebaseStorageWorker.uploadPic(bitmapCover, new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    //Write track to db
                    FirebaseWriteWorker firebaseWriteWorker = new FirebaseWriteWorker(userID);
                    //set the track cover to one that was uploaded
                    newTrack.setCoverLink(o.toString());
                    //write track
                    firebaseWriteWorker.WriteTrack(newTrack, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            listener.onSuccess(true);
                        }
                    });
                }
            });
        } catch (Exception writeException) {
            // Catching the exception
            Log.d(TAG, "writeTrack: " + writeException.getMessage());
        }
    }

    //Write track to db
    public void writeTrack(Track newTrack, OnSuccessListener listener) {
        try {
            String[] coverLink = {""};
            //get album id
            String albumID = newTrack.getAlbumID();
            FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(userID);
            //get album data
            firebaseReadWorker.getAlbum(albumID, new OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    //Get album cover
                    Album album = dataSnapshot.getValue(Album.class);

                        coverLink[0] = album.getCoverLink();

                }

                @Override
                public void onStartWork() {
                    //Write track with album cover link db
                    FirebaseWriteWorker firebaseWriteWorker = new FirebaseWriteWorker(userID);
                    if (newTrack.getCoverLink()==null){
                    newTrack.setCoverLink(coverLink[0]);}
                    //write track
                    firebaseWriteWorker.WriteTrack(newTrack, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            listener.onSuccess(true);
                        }
                    });
                }

                @Override
                public void onFailure() {
                    // Empty Method, not being used
                }
            });
        } catch (Exception writeException) {
            // Catching the exception
            Log.d(TAG, "writeTrack: " + writeException.getMessage());
        }
    }
}
