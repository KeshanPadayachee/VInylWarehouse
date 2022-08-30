package com.varsitycollege.vinyl_warehouse.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.varsitycollege.vinyl_warehouse.R;

import java.io.DataInput;

public class CameraGalleryDialog {

    // Creating variables for the CameraGalleryDialog object
    private AppCompatActivity appCompatActivity;
    private Activity activity;
    private Context context;
    private LayoutInflater layoutInflater;
    private Uri uri;
    private boolean isBitmap;
    DialogInterface dialogInterface;
    // Constructor
    public CameraGalleryDialog(AppCompatActivity appCompatActivity, Activity activity, Context context, LayoutInflater layoutInflater) {
        this.appCompatActivity = appCompatActivity;
        this.activity = activity;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    // Getters and setter
    public boolean isBitmap() {
        return isBitmap;
    }

    public void setBitmap(boolean bitmap) {
        isBitmap = bitmap;
    }

    public void dialog(OnSuccessListener listener) {
        //Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //Dialog title
        builder.setTitle("Choose Gallery or Camera for Cover");
        // Connect the custom layout
        View viewMain = layoutInflater.inflate(R.layout.custom_camera_gallery_dialog, null);
        //Connect componets to variables
        ImageView imageButtonCamera = viewMain.findViewById(R.id.imgCustomCameraGalleryDialog_Camera);
        ImageView imageButtonGallery = viewMain.findViewById(R.id.imgCustomCameraGalleryDialog_Gallery);
        TextView txvCamera = viewMain.findViewById(R.id.txvCustomCameraGalleryDialog_Camera);
        TextView txvGallery = viewMain.findViewById(R.id.txvCustomCameraGalleryDialog_Gallery);
        //set the custom layout to dialog
        builder.setView(viewMain);

        //Listen for onclick
        //send back to activity if user clicked gallery or camera
        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View cameraButton) {
                isBitmap=true;
                viewMain.performLongClick();
                dialogInterface.dismiss();
                listener.onSuccess(isBitmap);
            }
        });
        imageButtonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View galleryButton) {
                isBitmap = false;
                dialogInterface.dismiss();
                listener.onSuccess(isBitmap);
            }
        });

        txvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View cameraTextView) {
                isBitmap=true;
                viewMain.performLongClick();
                dialogInterface.dismiss();
                listener.onSuccess(isBitmap);
            }
        });
        txvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View galleryTextView) {
                isBitmap = false;
                viewMain.performLongClick();
                dialogInterface.dismiss();
                listener.onSuccess(isBitmap);
            }
        });
        //Show dialog
        dialogInterface = builder.show();



    }

}
