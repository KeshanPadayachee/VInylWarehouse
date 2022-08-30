package com.varsitycollege.vinyl_warehouse;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.CursorIndexOutOfBoundsException;
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
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;
import com.varsitycollege.vinyl_warehouse.DataWorkers.DataWriteWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseReadWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.OnGetDataListener;
import com.varsitycollege.vinyl_warehouse.Music.Album;
import com.varsitycollege.vinyl_warehouse.Music.Track;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;
import com.varsitycollege.vinyl_warehouse.Utils.CameraGalleryDialog;
import com.varsitycollege.vinyl_warehouse.Utils.Util;
import com.varsitycollege.vinyl_warehouse.Utils.Validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AddNewTrack extends AppCompatActivity implements View.OnClickListener {

    // Declaring constants
    public static final String ALPHA = "ALPHA";
    private static final String TRUE = "true";
    private static final int ONE_HUNDRED = 100;
    public static final String IMAGE_TYPE = "image/*";
    public static final String DATA = "data";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String INVALID_INFORMATION = "Invalid Information";
    public static final String ADD_COVER = "Add Cover";

    // Creating variables for the GUI components
    Button btnAddTrack_Add, btnAddTrack_DatePicker;
    ImageButton btnAddTrack_TrackCover;
    EditText edtAddTrack_TrackTitle, edtAddTrack_TrackGenre, edtAddTrack_TrackArtist;
    ImageView imgAddTrack_PreviewTrackCover, imgBackArrow;
    DatePickerDialog datePicker;
    ProgressBar progressAddTrack;
    TextInputLayout txtLayoutAddTrack_TrackTitle, txtLayoutAddTrack_TrackArtist, txtLayoutAddTrack_TrackGenre;
    public static final String ALBUMID = "ALBUMID";
    public static final String ALPHANUMERIC = "ALPHANUMERIC";

    public static final String NEW_LINE = "\n";

    // Variables needed for adding a new track
    private final boolean[] isValid = {false, false, false};
    private Bitmap bitmapTrackCover;
    private Uri uriTrackCover;
    private String albumID;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_track);
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Check and get for passed variables
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        //Get category id
        albumID = extras.getString(ALBUMID);
        getAlbumArtisit(albumID);
        //Connect Variables to Components
        btnAddTrack_Add = findViewById(R.id.btnAddTrack_Add);
        btnAddTrack_DatePicker = findViewById(R.id.btnAddTrack_DatePicker);
        btnAddTrack_TrackCover = findViewById(R.id.btnAddTrack_TrackCover);
        edtAddTrack_TrackTitle = findViewById(R.id.edtAddTrack_TrackTitle);
        edtAddTrack_TrackGenre = findViewById(R.id.edtAddTrack_TrackGenre);
        edtAddTrack_TrackArtist = findViewById(R.id.edtAddTrack_TrackArtist);
        txtLayoutAddTrack_TrackTitle = findViewById(R.id.txtLayoutAddTrack_TrackTitle);
        txtLayoutAddTrack_TrackArtist = findViewById(R.id.txtLayoutAddTrack_TrackArtist);
        txtLayoutAddTrack_TrackGenre = findViewById(R.id.txtLayoutAddTrack_TrackGenre);

        imgAddTrack_PreviewTrackCover = findViewById(R.id.imgAddTrack_PreviewTrackCover);
        imgBackArrow = findViewById(R.id.imgAddTrack_BackArrow);
        initDatePicker();
        // Setting the on click listeners
        btnAddTrack_Add.setOnClickListener(this);
        btnAddTrack_TrackCover.setOnClickListener(this);
        btnAddTrack_DatePicker.setOnClickListener(this);
        btnAddTrack_DatePicker.setText(Util.getTodaysDate());
        imgBackArrow.setOnClickListener(this);
        progressAddTrack = findViewById(R.id.progressAddTrack);
        // Checking if the TRACK TITLE valid/invalid
        edtAddTrack_TrackTitle.addTextChangedListener(new TextWatcher() {
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
                //Validate the input and change
                if (!validate(editable.toString(), ALPHANUMERIC)) {
                    txtLayoutAddTrack_TrackTitle.setError("Incorrect");
                    isValid[0] = false;
                } else {
                    txtLayoutAddTrack_TrackTitle.setError("");
                    isValid[0] = true;
                }
            }
        });

        // Checking if the GENRE is valid/invalid
        edtAddTrack_TrackGenre.addTextChangedListener(new TextWatcher() {
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
                //Validate the input and change
                if (!validate(editable.toString(), ALPHA)) {
                    txtLayoutAddTrack_TrackGenre.setError("Incorrect");
                    isValid[1] = false;
                } else {
                    txtLayoutAddTrack_TrackGenre.setError("");
                    isValid[1] = true;
                }
            }
        });

        // Checking if the ARTIST is valid.invalid
        edtAddTrack_TrackArtist.addTextChangedListener(new TextWatcher() {
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
                //Validate the input and change
                if (!validate(editable.toString(), ALPHANUMERIC)) {
                    txtLayoutAddTrack_TrackArtist.setError("Incorrect");
                    isValid[2] = false;
                } else {
                    txtLayoutAddTrack_TrackArtist.setError("");
                    isValid[2] = true;
                }
            }
        });


    }

    private void getAlbumArtisit(String albumID) {
        // Creating firebase read worker object
        FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(CurrentUser.currentUserObj.getPersonId());
        // Getting artist from album
        firebaseReadWorker.getAlbum(albumID, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Album album = dataSnapshot.getValue(Album.class);
                String artists = "";
                String genre = "";
                // Formatting of string
                if (album.getArtists().size() > 1) {
                    for (String artist : album.getArtists()
                    ) {
                        artists = artists + artist + "\n";

                    }
                    artists = artists.substring(0, artists.length() - 1);
                } else {
                    artists = album.getArtists().get(0);
                }
                if (album.getGenre() != null) {


                if (album.getGenre().size() > 1) {
                    for (String artist : album.getGenre()
                    ) {
                        genre = genre + artist + "\n";
                    }
                    genre = genre.substring(0, genre.length() - 1);
                } else {
                    genre = album.getGenre().get(0);
                }
                }
                // Appending the artists and genre
                edtAddTrack_TrackArtist.append(artists);
                edtAddTrack_TrackGenre.append(genre);
            }

            @Override
            public void onStartWork() {
            // EMPTY METHOD NOT BEING USED
            }

            @Override
            public void onFailure() {
            // EMPTY METHOD NOT BEING USED
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (CurrentUser.currentUserObj.getPersonId() == null) {
            // Switching layouts
            Intent intent = new Intent(AddNewTrack.this, IntroWelcome.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean validate(String toString, String validateToo) {
        // Declaring boolean variable to check for validity
        boolean isValid = false;

        // Checking for null values
        if (!Validation.isNullOrEmpty(toString)) {
            switch (validateToo) {
                // Case for alphanumeric
                case ALPHANUMERIC: {
                    if (Validation.isAlphanumeric(toString)) {
                        isValid = true;
                    } else {
                        isValid = false;
                    }
                    break;
                }
                case ALPHA: {
                    if (Validation.isAlphabetOnly(toString)) {
                        isValid = true;
                    } else {
                        isValid = false;
                    }
                    break;
                }
            }
        } else {
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void onClick(View view) {
        // Running methods depending on which button is clicked
        switch (view.getId()) {
            case R.id.btnAddTrack_TrackCover:
                getTrackImage();
                break;
            case R.id.btnAddTrack_Add:
                getTrackDetails();
                break;
            case R.id.btnAddTrack_DatePicker:
                openDatePicker(view);

                break;
            case R.id.imgAddTrack_BackArrow:
                // Switching layouts
                Intent toViewAlbum = new Intent(AddNewTrack.this, ViewAlbum.class);
                toViewAlbum.putExtra(ALBUMID, albumID);
                startActivity(toViewAlbum);
                finish();
                break;
        }
    }

    private void initDatePicker() {
        // Initializing a DatePicker Dialog
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // gets user date
                month = month + 1;
                date = Util.makeDateString(day, month, year);
                btnAddTrack_DatePicker.setText(date);

            }
        };

        // Creates a calender to select the year, month and day
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        //set the default date
        date = Util.makeDateString(day, month, year);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        // Configuring the date picker
        datePicker = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    // Method to show the date picker
    public void openDatePicker(View view) {
        datePicker.show();
    }

    // Getting the track image
    private void getTrackImage() {
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
                //Check if data from camera is not null
                if (data != null) {
                    //Clears the string for when the image is clicked multiple times, to eliminate the previous URI
                    uriTrackCover = null;
                    Bitmap captureImage = (Bitmap) data.getExtras().get(DATA);
                    //save the bitmap for later upload
                    bitmapTrackCover = captureImage;
                    //set the preview
                    imgAddTrack_PreviewTrackCover.setImageBitmap(captureImage);
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
                            bitmapTrackCover = null;
                            //image picked
                            //get uri of image
                            Intent data = result.getData();
                            //save uri for later upload
                            uriTrackCover = data.getData();
                            Picasso.get().load(uriTrackCover.toString()).into(imgAddTrack_PreviewTrackCover);
                        } else {
                            uriTrackCover = Uri.parse("canceled");
                        }
                    }catch (Exception e){
                        Log.d("Gallery", "Gallery: "+e.getMessage());
                    }
                }
            }
    );

    private void getTrackDetails() {
        boolean allValidDetails = true;

        // Checking if global validity holder is all true
        for (boolean check : isValid) {
            if (!check) {
                allValidDetails = false;
                break;
            }
        }

        // All details are valid and getting the track details
        if (allValidDetails) {
            // Showing the progress dialog
            progressAddTrack.setVisibility(View.VISIBLE);
            // Disabling the add button
            btnAddTrack_Add.setEnabled(false);
            // Getting the inputs
            String trackID = Util.idGenerator();
            String trackTitle = edtAddTrack_TrackTitle.getText().toString();
            String trackGenre = edtAddTrack_TrackGenre.getText().toString();
            String trackArtist = edtAddTrack_TrackArtist.getText().toString();
            Date dateConvert;

            // Isolating each artist in case of more than one
            List<String> lstArtists = new ArrayList<>();
            String[] artists = trackArtist.split(NEW_LINE);
            Collections.addAll(lstArtists, artists);

            // Isolating each genre in case of more than one
            List<String> lstGenres = new ArrayList<>();
            String[] genres = trackGenre.split(NEW_LINE);
            Collections.addAll(lstGenres, genres);


            // Converting the date of release to DATE data type
            try {

                dateConvert = new SimpleDateFormat(DATE_FORMAT).parse(date);
                Track newTrack = new Track(albumID, trackID, trackTitle, lstGenres, lstArtists, dateConvert);
                DataWriteWorker dataWriteWorker = new DataWriteWorker(CurrentUser.currentUserObj.getPersonId(), this);
                //Determine if the user wants to use album cover , gallery or camera.
                    //Check if gallery is null
                    if (uriTrackCover == null) {
                        //Check if camera is null then use album cover
                        if (bitmapTrackCover == null) {
                            //use album cover
                            dataWriteWorker.writeTrack(newTrack, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    // Hiding the progress dialog
                                    progressAddTrack.setVisibility(View.INVISIBLE);
                                    // Enabling the add button
                                    btnAddTrack_Add.setEnabled(true);
                                    if (o.toString().equals(TRUE)) {
                                        Intent viewAlbumIntent = new Intent(AddNewTrack.this, ViewAlbum.class);
                                        //Get title data from the adapter position to display
                                        //Sent the ID so the other class can use the id to do other work
                                        viewAlbumIntent.putExtra(ALBUMID, newTrack.getAlbumID());
                                        startActivity(viewAlbumIntent);
                                        finish();
                                    }
                                }
                            });
                        } else {

                            //use cover from camera
                            dataWriteWorker.writeTrack(newTrack, bitmapTrackCover, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    // Hiding the progress dialog
                                    progressAddTrack.setVisibility(View.INVISIBLE);
                                    // Enabling the add button
                                    btnAddTrack_Add.setEnabled(true);
                                    if (o.toString().equals(TRUE)) {
                                        Intent viewAlbumIntent = new Intent(AddNewTrack.this, ViewAlbum.class);
                                        //Get title data from the adapter position to display
                                        //Sent the ID so the other class can use the id to do other work
                                        viewAlbumIntent.putExtra(ALBUMID, newTrack.getAlbumID());
                                        startActivity(viewAlbumIntent);
                                        finish();
                                    }
                                }
                            });
                        }
                    } else {
                        if(!uriTrackCover.equals(Uri.parse("canceled"))){
                            //use gallery for cover
                            dataWriteWorker.writeTrack(newTrack, uriTrackCover, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    // Hiding the progress dialog
                                    progressAddTrack.setVisibility(View.INVISIBLE);
                                    // Enabling the add button
                                    btnAddTrack_Add.setEnabled(true);
                                    if (o.toString().equals(TRUE)) {
                                        Intent viewAlbumIntent = new Intent(AddNewTrack.this, ViewAlbum.class);
                                        //Get title data from the adapter position to display
                                        //Sent the ID so the other class can use the id to do other work
                                        viewAlbumIntent.putExtra(ALBUMID, newTrack.getAlbumID());
                                        startActivity(viewAlbumIntent);
                                        finish();
                                    }
                                }
                            });
                        }else{
                            // Hiding the progress dialog
                            progressAddTrack.setVisibility(View.INVISIBLE);
                            // Enabling the add button
                            btnAddTrack_Add.setEnabled(true);
                            Toast.makeText(this, ADD_COVER, Toast.LENGTH_SHORT).show();
                        }

                    }



            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            // Hiding the progress dialog
            progressAddTrack.setVisibility(View.INVISIBLE);
            // Enabling the add button
            btnAddTrack_Add.setEnabled(true);
            Toast.makeText(this, INVALID_INFORMATION, Toast.LENGTH_SHORT).show();
        }
    }


}