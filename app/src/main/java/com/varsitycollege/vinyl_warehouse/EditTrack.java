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
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.varsitycollege.vinyl_warehouse.DataWorkers.DataDeleteWorker;
import com.varsitycollege.vinyl_warehouse.DataWorkers.DataWriteWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseReadWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseStorageWorker;
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

public class EditTrack extends AppCompatActivity implements View.OnClickListener {
    // Declaring constants
    public static final String ALPHA = "ALPHA";
    private static final String TRUE = "true";
    public static final String IMAGE_Type = "image/*";
    public static final String DATE_FORMAT_1 = "dd MMM yyyy";
    public static final String TRACKID = "TRACKID";
    public static final String ALBUMID = "ALBUMID";
    public static final String DATA = "data";
    public static final String DATE_FORMAT_2 = "dd/MM/yyyy";
    public static final String INVALID_INFORMATION = "Invalid Information";
    public static final String CANCELED = "canceled";
    public static final String INCORRECT = "Incorrect";
    public static final String ADD_COVER = "Add Cover";
    // Creating variables for the GUI components
    DatePickerDialog datePicker;
    ProgressBar progressEditTrack;
    ImageButton btnEditTrack_TrackCover;
    Intent intent;
    private EditText edtTrackTitle, edtTrackGenre, edtTrackArtist, edtTrackDateRelease;
    TextInputLayout txtLayoutEditTrack_TrackTitle, txtLayoutEditAlbum_AlbumGenre, txtLayoutEditAlbum_AlbumArtist;
    private ImageView imgTrackCover, imgBackButton;
    public String trackID, albumID;
    private Button btnEditTrackAdd, btnEditTrack_DatePicker;
    public static final String ALPHANUMERIC = "ALPHANUMERIC";
    public static final String NEW_LINE = "\n";
    private boolean[] isValid = {false, false, false};
    private Bitmap bitmapTrackCover;
    private Uri uriTrackCover;
    private String date;
    private String trackCover;
    private boolean isAlbumCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_track);
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        intent = getIntent();
        // Assigning the GUI components to the variables
        edtTrackTitle = findViewById(R.id.edtEditTrack_TrackTitle);
        edtTrackGenre = findViewById(R.id.edtEditTrack_TrackGenre);
        edtTrackArtist = findViewById(R.id.edtEditTrack_TrackArtist);
        btnEditTrack_DatePicker = findViewById(R.id.btnEditTrack_DatePicker);
        imgTrackCover = findViewById(R.id.imgEditTrack_PreviewTrackCover);
        btnEditTrackAdd = findViewById(R.id.btnEditTrack_Add);
        btnEditTrack_TrackCover = findViewById(R.id.btnEditTrack_TrackCover);
        imgBackButton = findViewById(R.id.imgEditTrack_BackArrow);
        progressEditTrack = findViewById(R.id.progressEditTrack);
        txtLayoutEditTrack_TrackTitle = findViewById(R.id.txtLayoutEditTrack_TrackTitle);
        txtLayoutEditAlbum_AlbumGenre = findViewById(R.id.txtLayoutEditAlbum_AlbumGenre);
        txtLayoutEditAlbum_AlbumArtist = findViewById(R.id.txtLayoutEditAlbum_AlbumArtist);
        //get IDs for album and track from intent
        if (intent.getExtras() != null) {
            trackID = intent.getStringExtra(TRACKID);
            albumID = intent.getStringExtra(ALBUMID);
        }

        // Configuring the load dialog
        initDatePicker();
        // Showing the progress dialog
        progressEditTrack.setVisibility(View.VISIBLE);
        // Disabling the add button
        btnEditTrackAdd.setEnabled(false);
        btnEditTrack_DatePicker.setText(Util.getTodaysDate());

        //read data from firebase
        FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(CurrentUser.currentUserObj.getPersonId());
        //pull track data
        firebaseReadWorker.getTrack(trackID, new OnGetDataListener() {
            Track track = new Track();

            // Implementing the interface
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                track = dataSnapshot.getValue(Track.class);
            }

            // Hides loading page
            @Override
            public void onStartWork() {
                // Checks if many artists
                // Hiding the progress dialog
                progressEditTrack.setVisibility(View.INVISIBLE);
                // Enabling the add button
                btnEditTrackAdd.setEnabled(true);
                String artists = "";
                String genres = "";
                if (track.getArtists().size() > 1) {
                    // Lopping through all artists and creating a concated string
                    for (String artist : track.getArtists()
                    ) {
                        artists = artists + artist + "\n";

                    }
                    // Removing trailing spaces
                    artists = artists.substring(0, artists.length() - 1);
                } else {
                    // Gets first artist
                    artists = track.getArtists().get(0);
                }

                if (track.getGenre() != null) {
                    // Checks for many genres
                    if (track.getGenre().size() > 1) {
                        // Loops through all genres, creates a concatted string
                        for (String genre : track.getGenre()
                        ) {
                            genres = genres + genre + "\n";

                        }
                        // Removing trailing spaces
                        genres = genres.substring(0, genres.length() - 1);
                    } else {
                        // Getting first genre
                        genres = track.getGenre().get(0);
                    }
                }

                //   genres = genres.substring(0, genres.length() - 2);
                edtTrackTitle.setText(track.getTrackTitle());
                edtTrackArtist.setText(artists);
                edtTrackGenre.setText(genres);
                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_2);
                //Convert String to date
                String strDate = dateFormat.format(track.getDateOfRelease());
                btnEditTrack_DatePicker.setText(strDate);
                date = strDate;
                trackCover = track.getCoverLink();
                Picasso.get().load(trackCover).into(imgTrackCover);
                // Creating a FirebaseReadWorker object
                FirebaseReadWorker firebaseReadWorker1 = new FirebaseReadWorker(CurrentUser.currentUserObj.getPersonId());
                firebaseReadWorker1.getAlbum(albumID, new OnGetDataListener() {
                    Album album;

                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        // Gets album
                        album = dataSnapshot.getValue(Album.class);
                    }

                    @Override
                    public void onStartWork() {
                        // Checks if album cover link = track cover link
                        isAlbumCover = false;
                        if (album.getCoverLink().equals(track.getCoverLink())) {
                            isAlbumCover = true;
                        }
                        if (track.getCoverLink().contains("e-cdns-images.dzcdn.net") || track.getCoverLink().contains("api.deezer.com")) {
                            isAlbumCover = true;
                        }
                    }

                    @Override
                    public void onFailure() {
                    // EMPTY METHOD NOT BEING USED
                    }
                });
            }

            @Override
            public void onFailure() {
                // Empty method, not being used
            }

        });

        edtTrackTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method, not being used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method, not being used
            }

            @Override
            public void afterTextChanged(Editable editable) {

                //Validate the input and change
                if (!validate(editable.toString(), ALPHANUMERIC)) {
                    txtLayoutEditAlbum_AlbumArtist.setError("Incorrect");
                    isValid[0] = false;
                } else {
                    txtLayoutEditAlbum_AlbumArtist.setError("");
                    isValid[0] = true;
                }
            }
        });

        edtTrackGenre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method, not being used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method, not being used
            }

            @Override
            public void afterTextChanged(Editable editable) {

                //Validate the input and change
                if (!validate(editable.toString(), ALPHA)) {
                    txtLayoutEditAlbum_AlbumGenre.setError(INCORRECT);
                    isValid[1] = false;
                } else {
                    txtLayoutEditAlbum_AlbumGenre.setError("");
                    isValid[1] = true;
                }
            }
        });

        edtTrackArtist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method, not being used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method, not being used
            }

            @Override
            public void afterTextChanged(Editable editable) {

                //Validate the input and change
                if (!validate(editable.toString(), ALPHANUMERIC)) {
                    txtLayoutEditAlbum_AlbumArtist.setError("Incorrect");
                    isValid[2] = false;
                } else {
                    txtLayoutEditAlbum_AlbumArtist.setError("");
                    isValid[2] = true;
                }
            }
        });
        // Setting OnClick Listeners
        btnEditTrackAdd.setOnClickListener(this);
        btnEditTrack_TrackCover.setOnClickListener(this);
        btnEditTrack_DatePicker.setOnClickListener(this);
        imgBackButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Checking if current user is null
        if (CurrentUser.currentUserObj.getPersonId() == null) {
            // Switching layouts
            Intent intent = new Intent(EditTrack.this, IntroWelcome.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        // Running methods depending on which button is clicked
        switch (view.getId()) {
            case R.id.btnEditTrack_TrackCover:
                getTrackImage();
                break;
            case R.id.btnEditTrack_Add:
                getTrackDetails();
                break;
            case R.id.btnEditTrack_DatePicker:
                openDatePicker(view);
                break;
            case R.id.imgEditTrack_BackArrow:
                // Switching layouts
                Intent toViewTrack = new Intent(EditTrack.this, ViewTrack.class);
                toViewTrack.putExtra(TRACKID, trackID);
                startActivity(toViewTrack);
                finish();
                break;
        }
    }

    public void openDatePicker(View view) {
        // Showing date picker
        datePicker.show();
    }

    private void initDatePicker() {
        // Initializing the date picker
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Getting the date from the date picker
                month = month + 1;
                date = Util.makeDateString(day, month, year);
                btnEditTrack_DatePicker.setText(date);
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
                    startActivityForResult(cameraIntent, 100);
                } else {
                    //check for permission
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //Start Gallery
                        Intent takePictureIntent = new Intent(Intent.ACTION_PICK);
                        //set type
                        takePictureIntent.setType(IMAGE_Type);
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
            if (requestCode == 100) {
                //Check if camera data is not null
                if (data != null) {
                    //Clears the string for when the image is clicked multiple times, to eliminate the previous URI
                    uriTrackCover = null;
                    Bitmap captureImage = (Bitmap) data.getExtras().get(DATA);
                    //save the bitmap for later upload
                    bitmapTrackCover = captureImage;
                    //set the preview
                    imgTrackCover.setImageBitmap(captureImage);
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
                            Picasso.get().load(uriTrackCover.toString()).into(imgTrackCover);
                        } else {
                            uriTrackCover = Uri.parse(CANCELED);
                        }
                    }catch (Exception e){
                        Log.d("Gallery", "Gallery: "+e.getMessage());
                    }

                }
            }
    );


    // Method to check if all inputs are valid
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

    // Method to get the track details
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
            progressEditTrack.setVisibility(View.VISIBLE);
            // Disabling the add button
            btnEditTrackAdd.setEnabled(false);
            // Getting the inputs
            String trackId = trackID;
            String trackTitle = edtTrackTitle.getText().toString();
            String trackGenre = edtTrackGenre.getText().toString();
            String trackArtist = edtTrackArtist.getText().toString();
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
                dateConvert = new SimpleDateFormat(DATE_FORMAT_2).parse(date);
                Track newTrack = new Track(albumID, trackID, trackTitle, lstGenres, lstArtists, dateConvert);
                // Creating a DataWriteWorker object
                DataWriteWorker dataWriteWorker = new DataWriteWorker(CurrentUser.currentUserObj.getPersonId(), this);

                    if (uriTrackCover == null) {
                        if (bitmapTrackCover == null) {
                            newTrack.setCoverLink(trackCover);

                            dataWriteWorker.writeTrack(newTrack, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    // Hiding the progress dialog
                                    progressEditTrack.setVisibility(View.INVISIBLE);
                                    // Enabling the edit button
                                    btnEditTrackAdd.setEnabled(true);
                                    if (o.toString().equals(TRUE)) {
                                        // loadDialogBar.HideDialog();
                                        Intent i = new Intent(EditTrack.this, ViewAlbum.class);
                                        //Get title data from the adapter position to display
                                        //Sent the ID so the other class can use the id to do other work
                                        i.putExtra(ALBUMID, newTrack.getAlbumID());
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            //check if is album cover dont delete album cover
                            if (isAlbumCover) {
                                dataWriteWorker.writeTrack(newTrack, bitmapTrackCover, new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        // Hiding the progress dialog
                                        progressEditTrack.setVisibility(View.INVISIBLE);
                                        // Enabling the edit button
                                        btnEditTrackAdd.setEnabled(true);
                                        if (o.toString().equals(TRUE)) {
                                            //   loadDialogBar.HideDialog();
                                            Intent i = new Intent(EditTrack.this, ViewAlbum.class);
                                            //Get title data from the adapter position to display
                                            //Sent the ID so the other class can use the id to do other work
                                            i.putExtra(ALBUMID, newTrack.getAlbumID());
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                });
                            } else {
                                // Creating delete worker object
                                DataDeleteWorker dataDeleteWorker = new DataDeleteWorker(CurrentUser.currentUserObj.getPersonId());
                                dataDeleteWorker.deleteLink(trackCover, new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        // Creating firebase storage worker object
                                        FirebaseStorageWorker firebaseStorage = new FirebaseStorageWorker(CurrentUser.currentUserObj.getPersonId());
                                        // Calls delete image passing track cover
                                        firebaseStorage.deleteImage(trackCover, new OnSuccessListener() {

                                            // After delete updates new track cover
                                            @Override
                                            public void onSuccess(Object o) {

                                                dataWriteWorker.writeTrack(newTrack, bitmapTrackCover, new OnSuccessListener() {
                                                    @Override
                                                    public void onSuccess(Object o) {
                                                        // Hiding the progress dialog
                                                        progressEditTrack.setVisibility(View.INVISIBLE);
                                                        // Enabling the edit button
                                                        btnEditTrackAdd.setEnabled(true);
                                                        if (o.toString().equals(TRUE)) {

                                                            // Switching layout
                                                            Intent i = new Intent(EditTrack.this, ViewAlbum.class);
                                                            //Get title data from the adapter position to display
                                                            //Sent the ID so the other class can use the id to do other work
                                                            i.putExtra(ALBUMID, newTrack.getAlbumID());
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                            }

                        }
                    } else {
                        if (!uriTrackCover.equals(Uri.parse("canceled"))){
                            if (isAlbumCover) {
                                dataWriteWorker.writeTrack(newTrack, uriTrackCover, new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        // Hiding the progress dialog
                                        progressEditTrack.setVisibility(View.INVISIBLE);
                                        // Enabling the edit button
                                        btnEditTrackAdd.setEnabled(true);
                                        if (o.toString().equals(TRUE)) {
                                            Intent i = new Intent(EditTrack.this, ViewAlbum.class);
                                            //Get title data from the adapter postion to display
                                            //Sent the ID so the other class can use the id to do other work
                                            i.putExtra(ALBUMID, newTrack.getAlbumID());
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                });
                            } else {
                                // Creating date delete worker object
                                DataDeleteWorker dataDeleteWorker = new DataDeleteWorker(CurrentUser.currentUserObj.getPersonId());
                                dataDeleteWorker.deleteLink(trackCover, new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        // Creating firebase storage worker object
                                        FirebaseStorageWorker firebaseStorage = new FirebaseStorageWorker(CurrentUser.currentUserObj.getPersonId());
                                        firebaseStorage.deleteImage(trackCover, new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                dataWriteWorker.writeTrack(newTrack, uriTrackCover, new OnSuccessListener() {
                                                    @Override
                                                    public void onSuccess(Object o) {
                                                        // Hiding the progress dialog
                                                        progressEditTrack.setVisibility(View.INVISIBLE);
                                                        // Enabling the edit button
                                                        btnEditTrackAdd.setEnabled(true);
                                                        if (o.toString().equals(TRUE)) {
                                                            // Switching layouts
                                                            Intent i = new Intent(EditTrack.this, ViewAlbum.class);
                                                            //Get title data from the adapter postion to display
                                                            //Sent the ID so the other class can use the id to do other work
                                                            i.putExtra(ALBUMID, newTrack.getAlbumID());
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        }else{
                            // Hiding the progress dialog
                            progressEditTrack.setVisibility(View.INVISIBLE);
                            // Enabling the edit button
                            btnEditTrackAdd.setEnabled(true);
                            Toast.makeText(this, ADD_COVER, Toast.LENGTH_SHORT).show();
                        }
                    }


            } catch (ParseException e) {
                // Hiding the progress dialog
                progressEditTrack.setVisibility(View.INVISIBLE);
                // Enabling the edit button
                btnEditTrackAdd.setEnabled(true);
                // Catching the exception
                e.printStackTrace();
            }
        } else {
            // Hiding the progress dialog
            progressEditTrack.setVisibility(View.INVISIBLE);
            // Enabling the edit button
            btnEditTrackAdd.setEnabled(true);
            // Displaying the error
            Toast.makeText(this, INVALID_INFORMATION, Toast.LENGTH_SHORT).show();
        }
    }
}



