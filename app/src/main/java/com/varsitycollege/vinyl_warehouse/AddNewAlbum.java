package com.varsitycollege.vinyl_warehouse;
//Import statements

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.text.android.TextLayout;
import androidx.core.view.ViewCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.varsitycollege.vinyl_warehouse.DataWorkers.DataWriteWorker;
import com.varsitycollege.vinyl_warehouse.Music.Album;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;
import com.varsitycollege.vinyl_warehouse.Utils.CameraGalleryDialog;
import com.varsitycollege.vinyl_warehouse.Utils.Util;
import com.varsitycollege.vinyl_warehouse.Utils.Validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AddNewAlbum extends AppCompatActivity implements View.OnClickListener {
    //Declaring variables and GUI components
    public static final String ALPHA = "ALPHA";
    private static final String TRUE = "true";
    private static final int ONE_HUNDRED = 100;
    public static final String IMAGE_TYPE = "image/*";
    public static final String DATA = "data";
    public static final String DATE_STRING_FORMAT = "dd/MM/yyyy";
    public static final String YOU_HAVE_INCORRECT_DETAILS = "You have incorrect details";
    public static final String ALPHANUMERIC = "ALPHANUMERIC";
    public static final String NEW_LINE = "\n";
    public static final String PLEASE_ADD_ALBUM_COVER = "Please Add Album Cover";
    public static final String ADD_COVER = "Add Cover";

    // Creating variables for the GUI components
    DatePickerDialog datePicker;
    Button btnAddAlbum_Add, btnAddAlbum_DatePicker;
    ImageButton btnAddAlbum_AlbumCover;
    ProgressBar progressAddAlbum;
    EditText edtAddAlbum_AlbumTitle, edtAddAlbum_AlbumGenre, edtAddAlbum_AlbumArtist;
    TextInputLayout txtLayoutAddAlbum_AlbumTitle, txtLayoutAddAlbum_AlbumArtist, txtLayoutAddAlbum_AlbumGenre;
    ImageView imgAddAlbum_PerviewAlbumCover, imgBackArrow;
    RatingBar addAlbumRatingBar;
    // Variable for the Album rating
    private int rating;
    // Variable to check the number of stars rating
    private final boolean[] isSelected = {false, false, false, false, false};
    // Variable to make sure all input is valid
    private final boolean[] isValid = {false, false, false};
    // Variable for the Album cover
    private Bitmap bitmapAlbumCover;
    private Uri uriAlbumCover;
    // Variable for the date of release
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_album);

        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Assigning the GUI components to the variables
        btnAddAlbum_Add = findViewById(R.id.btnAddAlbum_Add);
        btnAddAlbum_AlbumCover = findViewById(R.id.btnAddAlbum_AlbumCover);
        edtAddAlbum_AlbumTitle = findViewById(R.id.edtAddAlbum_AlbumTitle);
        edtAddAlbum_AlbumGenre = findViewById(R.id.edtAddAlbum_AlbumGenre);
        edtAddAlbum_AlbumArtist = findViewById(R.id.edtAddAlbum_AlbumArtist);
        btnAddAlbum_DatePicker = findViewById(R.id.btnAddAlbum_DatePicker);
        txtLayoutAddAlbum_AlbumTitle = findViewById(R.id.txtLayoutAddAlbum_AlbumTitle);
        txtLayoutAddAlbum_AlbumArtist = findViewById(R.id.txtLayoutAddAlbum_AlbumArtist);
        txtLayoutAddAlbum_AlbumGenre = findViewById(R.id.txtLayoutAddAlbum_AlbumGenre);
        addAlbumRatingBar = findViewById(R.id.rtbAddAlbum_Rating);
        imgAddAlbum_PerviewAlbumCover = findViewById(R.id.imgAddAlbum_PerviewAlbumCover);
        imgBackArrow = findViewById(R.id.imgAddAlbum_BackArrow);
        progressAddAlbum = findViewById(R.id.progressAddAlbum);

        // Set date
        btnAddAlbum_DatePicker.setText(Util.getTodaysDate());
        //Set OnClick Listeners
        btnAddAlbum_Add.setOnClickListener(this);
        btnAddAlbum_DatePicker.setOnClickListener(this);
        btnAddAlbum_AlbumCover.setOnClickListener(this);
        imgBackArrow.setOnClickListener(this);

        // Calling method to sort the album date
        initDatePicker();

        //Input text Change if invalid
        edtAddAlbum_AlbumTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method, not being used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method not being used
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Validate the input and change
                //Code Attribution
                //Link: https://stackoverflow.com/questions/15543186/how-do-i-create-colorstatelist-programmatically
                //Author:tse
                //End
                if (!validate(editable.toString(), ALPHANUMERIC)) {
                    // Validate the input and change the text field color
                    txtLayoutAddAlbum_AlbumTitle.setError("Incorrect");
                    isValid[0] = false;
                } else {
                    txtLayoutAddAlbum_AlbumTitle.setError("");
                    isValid[0] = true;
                }
            }
        });
        edtAddAlbum_AlbumGenre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method not being used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method not being used
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Validate the input and change text field color+
                if (!validate(editable.toString(), ALPHA)) {
                    txtLayoutAddAlbum_AlbumGenre.setError("Incorrect");
                    isValid[1] = false;
                } else {
                    txtLayoutAddAlbum_AlbumGenre.setError("");
                    isValid[1] = true;
                }
            }
        });
        edtAddAlbum_AlbumArtist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method not being used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method not being used
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Validate the input and change the text field color
                if (!validate(editable.toString(), ALPHANUMERIC)) {
                    txtLayoutAddAlbum_AlbumArtist.setError("Incorrect");
                    isValid[2] = false;
                } else {
                    txtLayoutAddAlbum_AlbumArtist.setError("");
                    isValid[2] = true;
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (CurrentUser.currentUserObj.getPersonId() == null) {
            // Switching layouts
            Intent intent = new Intent(AddNewAlbum.this, IntroWelcome.class);
            startActivity(intent);
            finish();
        }
    }

    //OnClick Listener
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        //Run methods depending of which components requested
        switch (view.getId()) {
            case R.id.btnAddAlbum_Add:
                getAlbumDetails();
                break;
            case R.id.btnAddAlbum_AlbumCover:
                getCover();
                break;
            case R.id.btnAddAlbum_DatePicker:
                openDatePicker(view);
                break;
            case R.id.imgAddAlbum_BackArrow:
                // Switching layouts
                Intent toMainActivity = new Intent(AddNewAlbum.this, MainActivity.class);
                startActivity(toMainActivity);
                finish();
                break;
        }
    }

    // Sort album date
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //Adds 1 to the month
                month = month + 1;
                //Calls the method to make the date picker year, month, day makes a string date
                date = Util.makeDateString(day, month, year);
                //sets the date in a string format
                btnAddAlbum_DatePicker.setText(date);

            }
        };
        //creates a calender tp select the year, month and day
        Calendar selectedDate = Calendar.getInstance();
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);
        //set the default date
        date = Util.makeDateString(day, month, year);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        //creates the datepicker with the style, year, month and day
        datePicker = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //sets the max date to the current date
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    public void openDatePicker(View view) {
        // Showing the date picker
        datePicker.show();
    }

    //Code Attribution
    //Link: https://www.youtube.com/watch?v=HtS-qI54GKk
    //Author: waddan soft
    //End
    //Get image camera or gallery
    private void getCover() {
        //call Dialog
        CameraGalleryDialog cameraGalleryDialog = new CameraGalleryDialog((AppCompatActivity) this, this, this, getLayoutInflater());
        //open dialog 
        cameraGalleryDialog.dialog(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                //check if gallery or camera
                if (o.toString().equals(TRUE)) {
                    //Start camera
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, ONE_HUNDRED);
                    //  Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // galleryActivityResultLauncher.launch(cameraIntent);
                } else {
                    //check for permission
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //Start Gallery
                        Intent takePictureIntent = new Intent(Intent.ACTION_PICK);
                        //set type
                        takePictureIntent.setType(IMAGE_TYPE);
                        galleryActivityResultLauncher.launch(takePictureIntent);
                    } else {
                        //Request if permission not given
                        String[] myPermission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(myPermission, 750);
                    }
                }
            }
        });
    }
    //Get result from camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == ONE_HUNDRED) {
                //Clears the string for when the image is clicked multiple times, to eliminate the previous URI
                uriAlbumCover = null;
                //Check if data from camera is null
                if (data != null) {
                    //Clears the string for when the image is clicked multiple times, to eliminate the previous URI
                    uriAlbumCover = null;
                    Bitmap captureImage = (Bitmap) data.getExtras().get(DATA);
                    //save the bitmap for later upload
                    bitmapAlbumCover = captureImage;
                    //set the preview
                    imgAddAlbum_PerviewAlbumCover.setImageBitmap(captureImage);
                }
            }
        }catch (Exception e){
            Log.d("Camera", "Camera: "+e.getMessage());
        }
    }

    //Get gallery result back
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        //here we will handle the result of our intent
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            //Clears the string for when the image is clicked multiple times, to eliminate the previous URI
                            bitmapAlbumCover = null;
                            //image picked
                            //get uri of image
                            Intent data = result.getData();
                            //save uri for later upload
                            uriAlbumCover = data.getData();
                            Picasso.get().load(uriAlbumCover.toString()).into(imgAddAlbum_PerviewAlbumCover);
                        } else {
                            uriAlbumCover = Uri.parse("canceled");
                        }
                    }catch (Exception e){
                        Log.d("Gallery", "Gallery: "+e.getMessage());
                    }

                }
            }
    );

    @SuppressLint("SimpleDateFormat")
    private void getAlbumDetails() {
        boolean isAllValid = true;
        //Check if global validation holder is all valid
        for (boolean b : isValid) {
            if (!b) {
                isAllValid = false;
                break;
            }
        }
        //Check if all is valid
        if (isAllValid) {
            // Showing the progress dialog
            progressAddAlbum.setVisibility(View.VISIBLE);
            // Disabling the add button
            btnAddAlbum_Add.setEnabled(false);
            //Getting all the input
            String albumTitle = edtAddAlbum_AlbumTitle.getText().toString();
            String genres = edtAddAlbum_AlbumGenre.getText().toString();
            String artists = edtAddAlbum_AlbumArtist.getText().toString();
            Date dateConvert = null;
            String albumID = Util.idGenerator();
            //Get each artist from input
            List<String> artistList = new ArrayList<>();
            String[] artist = artists.split(NEW_LINE);
            Collections.addAll(artistList, artist);

            //Get each genre from input
            List<String> genreList = new ArrayList<>();
            String[] genre = genres.split(NEW_LINE);
            genreList.addAll(Arrays.asList(genre));

            //get int number of stars as rating from rating bar
            rating = Math.round(addAlbumRatingBar.getRating());
            //Convert String to date
            try {
                //creates simple date format with a date
                dateConvert = new SimpleDateFormat(DATE_STRING_FORMAT).parse(date);
                //Creates an album object
                Album newAlbum = new Album(CurrentUser.currentUserObj.getPersonId(), albumID, albumTitle, genreList, artistList, dateConvert, rating);
                //creates a datawriteworker object to push the object
                DataWriteWorker dataWriteWorker = new DataWriteWorker(CurrentUser.currentUserObj.getPersonId(), this);
                //checks if there is an album cover
                    if (uriAlbumCover == null) {
                        //pushed to firebase using the created Album object
                        if (bitmapAlbumCover != null) {
                            dataWriteWorker.writeAlbum(newAlbum, bitmapAlbumCover, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    // Hiding the progress dialog
                                    progressAddAlbum.setVisibility(View.INVISIBLE);
                                    // Enabling the add button
                                    btnAddAlbum_Add.setEnabled(true);
                                    //checks if the object is successfully pushed
                                    if (o.toString().equals("true")) {
                                        //hides the loading screen

                                        //redirects to the home screen, MainActivity
                                        Intent i = new Intent(AddNewAlbum.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            // Hiding the progress dialog
                            progressAddAlbum.setVisibility(View.INVISIBLE);
                            // Enabling the add button
                            btnAddAlbum_Add.setEnabled(true);
                            Toast.makeText(this, PLEASE_ADD_ALBUM_COVER, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        if (!uriAlbumCover.equals(Uri.parse("canceled"))){
                            //calls the write method to take in URIAlbumCover
                            dataWriteWorker.writeAlbum(newAlbum, uriAlbumCover, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    // Hiding the progress dialog
                                    progressAddAlbum.setVisibility(View.INVISIBLE);
                                    // Enabling the add button
                                    btnAddAlbum_Add.setEnabled(true);
                                    //checks if the object is pushed
                                    if (o.toString().equals(TRUE)) {

                                        //redirects to the home page, Main Activity
                                        Intent i = new Intent(AddNewAlbum.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });
                        }else {
                            // Hiding the progress dialog
                            progressAddAlbum.setVisibility(View.INVISIBLE);
                            // Enabling the add button
                            btnAddAlbum_Add.setEnabled(true);
                            Toast.makeText(this, ADD_COVER, Toast.LENGTH_SHORT).show();
                        }

                    }



            } catch (ParseException e) {
                // Catching the exception
                e.printStackTrace();
            }
        } else {
            // Hiding the progress dialog
            progressAddAlbum.setVisibility(View.INVISIBLE);
            // Enabling the add button
            btnAddAlbum_Add.setEnabled(true);
            Toast.makeText(this, YOU_HAVE_INCORRECT_DETAILS, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate(String toString, String validateToo) {

        //boolean variable to set if valid
        boolean isValid = false;

        //if statement to check if null or empty
        if (!Validation.isNullOrEmpty(toString)) {
            switch (validateToo) {
                //case for alphanumeric
                case ALPHANUMERIC: {
                    isValid = Validation.isAlphanumeric(toString);
                    break;
                }
                //case for date
                case ALPHA: {
                    isValid = Validation.isAlphabetOnly(toString);
                    break;
                }
            }
        } else {
            isValid = false;
        }

        return isValid;

    }


}