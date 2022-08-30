package com.varsitycollege.vinyl_warehouse.FirebaseWorkers;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.formatter.IFillFormatter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.varsitycollege.vinyl_warehouse.Music.CoverLinks;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;
import com.varsitycollege.vinyl_warehouse.Utils.Util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FirebaseStorageWorker {

    // Creating a variable for the google storage url
    public static final String VINYL_WAREHOUSE_APPSPOT_COM = "gs://vinyl-warehouse.appspot.com";
    // images url
    public static final String IMAGES = "images/";
    // Creating a variable for the user id
    private String userID;
    private List<CoverLinks> coverLinks = new ArrayList<>();
    //private List<String>links = new ArrayList<>();
    //Constructor
    public FirebaseStorageWorker(String userID) {

        this.userID = userID;
        FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(userID);

    }

    public void uploadPic(Uri imageUri, String name, OnSuccessListener listener) {
        try {
            //Get Firebase Storage
            StorageReference storageReference = FirebaseStorage.getInstance(VINYL_WAREHOUSE_APPSPOT_COM).getReference();
            //Make Reference for where to store image
            StorageReference imageReference = storageReference.child(IMAGES + CurrentUser.currentUserObj.getPersonId() + "/" + Util.idGenerator() + "/" + name);
            //Push the image to firebase
            imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //get download uri
                    Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                    downloadUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String generatedFilePath = downloadUri.getResult().toString();

                            FirebaseWriteWorker firebaseWriteWorker = new FirebaseWriteWorker(userID);
                            CoverLinks links = new CoverLinks(Util.idGenerator(),generatedFilePath);
                            if (!oldUrlWrite.equals(generatedFilePath)){


                                firebaseWriteWorker.WriteLink(links, new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        //send back uri
                                        oldUrlWrite=generatedFilePath;
                                        listener.onSuccess(generatedFilePath);
                                    }
                                });
                            }

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

        } catch (Exception storageException) {
            // Catching the exception
            Log.d(TAG, "uploadPic: " + storageException.getMessage());
        }
    }
    //declaring variables to delete and replace the image if it is edited
    private static String oldUrl = "";
    private static String oldUrlWrite = "";

    public void deleteImage(String url, OnSuccessListener onSuccessListener) {

        try {
            if (!url.equals(oldUrl)) {
                if (!url.contains("e-cdns-images.dzcdn.net")){
                    //Get Firebase Storage
                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance(VINYL_WAREHOUSE_APPSPOT_COM);
                    //Get reference to where image is stored
                    StorageReference storageReference = firebaseStorage.getReferenceFromUrl(url);
                    //Get listener when the image is delete
                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            onSuccessListener.onSuccess(true);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            onSuccessListener.onSuccess(true);
                        }
                    });
                    oldUrl = url;
                }

            }
        } catch (Exception storageException) {
            // Catching the exception
            Log.d(TAG, "deleteImage: " + storageException.getMessage());
        }

    }

    //Upload bitmap
    public void uploadPic(Bitmap bitmap, OnSuccessListener listener) {
        try {
            //Get Bitmap image ready to upload
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //Lower the quality for upload;
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            //convert Bitmap to bytes to upload
            byte[] data = stream.toByteArray();
            //Get Firebase Storage
            StorageReference storageReference = FirebaseStorage.getInstance(VINYL_WAREHOUSE_APPSPOT_COM).getReference();
            //Make Reference for where to store image
            StorageReference imageReference = storageReference.child(IMAGES + userID + "/" + Util.idGenerator() + "/" + Util.idGenerator());
            //Upload
            imageReference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //get the link back from upload
                    Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                    downloadUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //Get link
                            String generatedFilePath = downloadUri.getResult().toString();
                            listener.onSuccess(generatedFilePath);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } catch (Exception storageException) {
            // Catching the exception
            Log.d(TAG, "uploadPic: " + storageException.getMessage());
        }
    }
}
