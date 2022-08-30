package com.varsitycollege.vinyl_warehouse;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;
import com.varsitycollege.vinyl_warehouse.DataWorkers.DataDeleteWorker;
import com.varsitycollege.vinyl_warehouse.DataWorkers.DataWriteWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseReadWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseStorageWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseWriteWorker;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EditAlbum extends AppCompatActivity implements View.OnClickListener {
    // Declaring constants
    public static final String ALPHA = "ALPHA";
    private static final String TRUE = "true";
    public static final String ALBUMID = "ALBUMID";
    public static final String ALPHANUMERIC = "ALPHANUMERIC";
    public static final String NEW_LINE = "\n";
    public static final String DATE_FORMAT_1 = "dd MMM yyyy";
    public static final String IMAGE_TYPE = "image/*";
    public static final String DATA = "data";
    public static final String DATE_FORMAT_2 = "dd/MM/yyyy";
    public static final String YOU_HAVE_INCORRECT_DETAILS = "You have incorrect details";
    public static final String CANCELED = "canceled";
    public static final String ADD_COVER = "Add Cover";
    // Creating variables for the GUI components
    Button btnEditAlbum_Edit, btnEditAlbum_DatePicker;
    ImageButton btnEditAlbum_AlbumCover;
    EditText edt_EditAlbum_AlbumTitle, edt_EditAlbum_AlbumGenre, edt_EditAlbum_AlbumArtist;
    ImageView  imgEditAlbum_PreviewAlbumCover, imgBackArrow;
    DatePickerDialog datePicker;
    RatingBar editAlbumRatingBar;
    ProgressBar progressEditAlbum;
    TextInputLayout txtLayoutEditAlbum_AlbumTitle, txtLayoutEditAlbum_AlbumArtist, txtLayoutEditAlbum_AlbumGenre;
    private int rating = 1;
    private boolean[] isSelected = {false, false, false, false, false};
    private boolean[] isValid = {false, false, false};
    private Bitmap bitmapAlbumCover;
    private Uri uriAlbumCover = null;
    private static String albumCover;
    private String albumID;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_album);
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Connect Variables to Components
        btnEditAlbum_Edit = findViewById(R.id.btnEditAlbum);
        btnEditAlbum_AlbumCover = findViewById(R.id.btnEditAlbum_AlbumCover);
        edt_EditAlbum_AlbumTitle = findViewById(R.id.edtEditAlbum_AlbumTitle);
        edt_EditAlbum_AlbumGenre = findViewById(R.id.edtAddAlbum_AlbumGenre);
        edt_EditAlbum_AlbumArtist = findViewById(R.id.edtEditAlbum_AlbumArtist);
        btnEditAlbum_DatePicker = findViewById(R.id.btnEditAlbum_DatePicker);
        editAlbumRatingBar = findViewById(R.id.rtbEditAlbum_Rating);
        txtLayoutEditAlbum_AlbumTitle = findViewById(R.id.txtLayoutEditAlbum_AlbumTitle);
        txtLayoutEditAlbum_AlbumArtist = findViewById(R.id.txtLayoutEditAlbum_AlbumArtist);
        txtLayoutEditAlbum_AlbumGenre = findViewById(R.id.txtLayoutEditAlbum_AlbumGenre);
        imgEditAlbum_PreviewAlbumCover = findViewById(R.id.imgEditAlbum_PreviewAlbumCover);
        imgBackArrow = findViewById(R.id.imgEditAlbum_BackArrow);
        progressEditAlbum = findViewById(R.id.progressEditAlbum);
        albumID = getIntent().getStringExtra(ALBUMID);
        //Get Album data
        btnEditAlbum_Edit.setEnabled(false);
        // Showing the progress dialog
        progressEditAlbum.setVisibility(View.VISIBLE);
        // Creating an instance of the FirebaseReadWorker class
        FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(CurrentUser.currentUserObj.getPersonId());
        firebaseReadWorker.getAlbum(albumID, new OnGetDataListener() {
            Album album;

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //Save data
                Album album2 = dataSnapshot.getValue(Album.class);
                album = album2;
            }

            @Override
            public void onStartWork() {
                setAlbumDetails(album);
            }

            @Override
            public void onFailure() {
                // Empty method. not being used
            }
        });


        //Set OnClick Listeners
        btnEditAlbum_Edit.setOnClickListener(this);
        btnEditAlbum_DatePicker.setOnClickListener(this);
        btnEditAlbum_AlbumCover.setOnClickListener(this);
        imgBackArrow.setOnClickListener(this);

        initDatePicker();
        btnEditAlbum_DatePicker.setText(Util.getTodaysDate());
        //Input text Change if invalid
        edt_EditAlbum_AlbumTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method. not being used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method. not being used
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
        edt_EditAlbum_AlbumGenre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method. not being used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method. not being used
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Validate the input and change
                if (!validate(editable.toString(), ALPHA)) {
                    txtLayoutEditAlbum_AlbumGenre.setError("Incorrect");
                    isValid[1] = false;
                } else {
                    txtLayoutEditAlbum_AlbumGenre.setError("");
                    isValid[1] = true;
                }
            }
        });
        edt_EditAlbum_AlbumArtist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method. not being used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method. not being used
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

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (CurrentUser.currentUserObj.getPersonId() == null) {
            // Switching layouts if user not found
            Intent intent = new Intent(EditAlbum.this, IntroWelcome.class);
            startActivity(intent);
            finish();
        }
    }

    private void initDatePicker() {
        //initialize "default" date for datepicker
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Getting the date
                month = month + 1;
                date = Util.makeDateString(day, month, year);
                btnEditAlbum_DatePicker.setText(date);

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

        // configuring the date picker
        datePicker = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private void setAlbumDetails(Album album) {
        //Set Album details
        btnEditAlbum_Edit.setEnabled(true);
        progressEditAlbum.setVisibility(View.INVISIBLE);
        String artists = "";
        String genres = "";
        // Formatting the display string
        if (album.getArtists().size() > 1) {
            for (String artist : album.getArtists()
            ) {
                artists = artists + artist + "\n";
            }
            artists = artists.substring(0, artists.length() - 1);
        } else {
            artists = album.getArtists().get(0);
        }

        // Formatting the display string

        if (album.getGenre().size() > 1) {
            for (String genre : album.getGenre()
            ) {
                genres = genres + genre + "\n";

            }
            genres = genres.substring(0, genres.length() - 1);
        } else {
            genres = album.getGenre().get(0);
        }

        // Displaying the information
        edt_EditAlbum_AlbumArtist.setText(artists);
        edt_EditAlbum_AlbumTitle.setText(album.getAlbumTitle());
        albumCover = album.getCoverLink();
        Picasso.get().load(album.getCoverLink()).into(imgEditAlbum_PreviewAlbumCover);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_2);

        //Convert String to date
        String strDate = dateFormat.format(album.getDateOfRelease());
        btnEditAlbum_DatePicker.setText(strDate);

        date = strDate;
        edt_EditAlbum_AlbumGenre.setText(genres);
        rating = album.getRating();
        //set stars for rating based on users given rating
        editAlbumRatingBar.setRating(rating);

    }

    //OnClick Listener
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        //Run methods depending of which components requested
        switch (view.getId()) {
            case R.id.btnEditAlbum:
                getAlbumDetails();
                break;
            case R.id.btnEditAlbum_AlbumCover:
                getCover();
                break;
            case R.id.btnEditAlbum_DatePicker:
                openDatePicker(view);
                break;
            case R.id.imgEditAlbum_BackArrow:
                // Switching layouts
                Intent toViewAlbum = new Intent(EditAlbum.this, ViewAlbum.class);
                toViewAlbum.putExtra(ALBUMID, albumID);
                startActivity(toViewAlbum);
                finish();
                break;
        }
    }

    // Method to manage the date picker
    public void openDatePicker(View view) {
        datePicker.show();
    }

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
                    // startActivityForResult(cameraIntent, 100);
                    // Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryActivityResultLauncher.launch(cameraIntent);
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
            if (requestCode == 100) {
                //Check if data from camera is not null
                if (data != null) {
                    //Clears the string for when the image is clicked multiple times, to eliminate the previous URI
                    uriAlbumCover = null;
                    Bitmap captureImage = (Bitmap) data.getExtras().get(DATA);
                    //save the bitmap for later upload
                    bitmapAlbumCover = captureImage;
                    //set the preview
                    imgEditAlbum_PreviewAlbumCover.setImageBitmap(captureImage);
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
                            Picasso.get().load(uriAlbumCover.toString()).into(imgEditAlbum_PreviewAlbumCover);
                        } else {
                            uriAlbumCover = Uri.parse(CANCELED);
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
            progressEditAlbum.setVisibility(View.VISIBLE);
            btnEditAlbum_Edit.setEnabled(false);
            //Get all input
            String albumTitle = edt_EditAlbum_AlbumTitle.getText().toString();
            String genres = edt_EditAlbum_AlbumGenre.getText().toString();
            String artists = edt_EditAlbum_AlbumArtist.getText().toString();
            Date dateConvert = null;
            //String albumID = Util.idGenerator();
            //Get each artist from input
            List<String> artistList = new ArrayList<>();
            String[] artist = artists.split(NEW_LINE);
            Collections.addAll(artistList, artist);

            //Get each genre from input
            List<String> genreList = new ArrayList<>();
            String[] genre = genres.split(NEW_LINE);
            genreList.addAll(Arrays.asList(genre));
            //Convert String to date

            //get rating from rating bar
            rating = Math.round(editAlbumRatingBar.getRating());
            try {
                dateConvert = new SimpleDateFormat(DATE_FORMAT_2).parse(date);
                Album newAlbum = new Album(CurrentUser.currentUserObj.getPersonId(), albumID, albumTitle, genreList, artistList, dateConvert, rating);

                // Creating a data write worker object
                DataWriteWorker dataWriteWorker = new DataWriteWorker(CurrentUser.currentUserObj.getPersonId(), this);

                //Determine if user updated album cover with gallery or camera or did not edit the cover
                //check if gallery was used

                    if (uriAlbumCover == null) {
                        //check if camera was used
                        if (bitmapAlbumCover == null) {
                            //use old cover
                            newAlbum.setCoverLink(albumCover);
                            dataWriteWorker.writeAlbum(newAlbum, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    progressEditAlbum.setVisibility(View.INVISIBLE);
                                    btnEditAlbum_Edit.setEnabled(true);
                                    if (o.toString().equals(TRUE)) {

                                        // Opening the view album activity
                                        Intent i = new Intent(EditAlbum.this, ViewAlbum.class);
                                        //Pass Album ID
                                        i.putExtra(ALBUMID, albumID);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            //delete old image
                            DataDeleteWorker dataDeleteWorker = new DataDeleteWorker(CurrentUser.currentUserObj.getPersonId());
                            dataDeleteWorker.deleteLink(albumCover, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {

                                    CurrentUser.oldCover = albumCover;
                                    // creating a firebase storage worker object
                                    FirebaseStorageWorker firebaseStorageWorker = new FirebaseStorageWorker(CurrentUser.currentUserObj.getPersonId());
                                    firebaseStorageWorker.deleteImage(albumCover, new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            //upload new cover and new album
                                            dataWriteWorker.writeAlbum(newAlbum, bitmapAlbumCover, new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    if (o.toString().equals(TRUE)) {
                                                        // Creating a firebase read worker object
                                                        FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(CurrentUser.currentUserObj.getPersonId());
                                                        // Getting the album object
                                                        firebaseReadWorker.getAlbum(albumID, new OnGetDataListener() {
                                                            Album album = new Album();

                                                            @Override
                                                            public void onSuccess(DataSnapshot dataSnapshot) {
                                                                // Stores album object
                                                                album = dataSnapshot.getValue(Album.class);
                                                            }

                                                            @Override
                                                            public void onStartWork() {
                                                                updateTrackCovers(album.getCoverLink(), new OnSuccessListener() {
                                                                    @Override
                                                                    public void onSuccess(Object o) {
                                                                        progressEditAlbum.setVisibility(View.INVISIBLE);
                                                                        btnEditAlbum_Edit.setEnabled(true);
                                                                        // Opening the view album activity
                                                                        Intent i = new Intent(EditAlbum.this, ViewAlbum.class);
                                                                        //Pass Album ID
                                                                        i.putExtra(ALBUMID, albumID);
                                                                        startActivity(i);
                                                                        finish();
                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onFailure() {
                                                                progressEditAlbum.setVisibility(View.INVISIBLE);
                                                                btnEditAlbum_Edit.setEnabled(true);
                                                            }
                                                        });

                                                    }
                                                }
                                            });
                                        }
                                    });

                                }
                            });

                        }

                    } else {
                        if(!uriAlbumCover.equals(Uri.parse("canceled"))){
                            //delete old image
                            DataDeleteWorker dataDeleteWorker = new DataDeleteWorker(CurrentUser.currentUserObj.getPersonId());
                            dataDeleteWorker.deleteLink(albumCover, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    CurrentUser.oldCover = albumCover;
                                    // Creating firebase storage worker object
                                    FirebaseStorageWorker firebaseStorageWorker = new FirebaseStorageWorker(CurrentUser.currentUserObj.getPersonId());
                                    firebaseStorageWorker.deleteImage(albumCover, new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            //upload new cover and new Album

                                            dataWriteWorker.writeAlbum(newAlbum, uriAlbumCover, new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    if (o.toString().equals(TRUE)) {
                                                        // Creating a firebase read worker object
                                                        FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(CurrentUser.currentUserObj.getPersonId());
                                                        firebaseReadWorker.getAlbum(albumID, new OnGetDataListener() {
                                                            Album album = new Album();

                                                            @Override
                                                            public void onSuccess(DataSnapshot dataSnapshot) {
                                                                album = dataSnapshot.getValue(Album.class);

                                                            }

                                                            @Override
                                                            public void onStartWork() {
                                                                // Updates all track covers with old cover link
                                                                updateTrackCovers(album.getCoverLink(), new OnSuccessListener() {
                                                                    @Override
                                                                    public void onSuccess(Object o) {
                                                                        progressEditAlbum.setVisibility(View.INVISIBLE);
                                                                        btnEditAlbum_Edit.setEnabled(true);
                                                                        // Opening the view album activity
                                                                        Intent i = new Intent(EditAlbum.this, ViewAlbum.class);
                                                                        //Pass Album ID
                                                                        i.putExtra(ALBUMID, albumID);
                                                                        startActivity(i);
                                                                        finish();
                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onFailure() {
                                                                progressEditAlbum.setVisibility(View.INVISIBLE);
                                                                btnEditAlbum_Edit.setEnabled(true);
                                                            }
                                                        });

                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }else{
                            progressEditAlbum.setVisibility(View.INVISIBLE);
                            btnEditAlbum_Edit.setEnabled(true);
                            // Letting the user know the details are invalid
                            Toast.makeText(this, ADD_COVER, Toast.LENGTH_SHORT).show();
                        }



                    }



            } catch (ParseException e) {
                progressEditAlbum.setVisibility(View.INVISIBLE);
                btnEditAlbum_Edit.setEnabled(true);

                e.printStackTrace();
            }

        } else {
            progressEditAlbum.setVisibility(View.INVISIBLE);
            btnEditAlbum_Edit.setEnabled(true);
            // Letting the user know the details are invalid
            Toast.makeText(this, YOU_HAVE_INCORRECT_DETAILS, Toast.LENGTH_SHORT).show();
        }
    }



    // Method to validate the inputs
    private boolean validate(String toString, String validateToo) {

        //boolean variable to set if valid
        boolean isValid = false;

        //if statement to check if null or empty
        if (!Validation.isNullOrEmpty(toString)) {
            switch (validateToo) {
                //case for alphanumeric
                case ALPHANUMERIC: {
                    if (Validation.isAlphanumeric(toString)) {
                        isValid = true;
                    } else {
                        isValid = false;
                    }
                    break;
                }
                //case for date
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

    private void updateTrackCovers(String newCover, OnSuccessListener listener) {
        List<Track> trackList = new ArrayList<>();
        //Write new data
        DataWriteWorker dataWriteWorker = new DataWriteWorker(CurrentUser.currentUserObj.getPersonId(), EditAlbum.this);
        FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(CurrentUser.currentUserObj.getPersonId());
        //Get all track album ids
        firebaseReadWorker.getTracks(albumID, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Track track = new Track();
                //get value
                track = dataSnapshot.getValue(Track.class);
                //check if track cover matches old cover
                if (track.getCoverLink().equals(CurrentUser.oldCover)) {
                    track.setCoverLink(newCover);
                    //write new track
                    dataWriteWorker.writeTrack(track, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                        }
                    });
                }
            }

            @Override
            public void onStartWork() {
                //remove old cover
                CurrentUser.oldCover = "";
                listener.onSuccess(true);
            }

            @Override
            public void onFailure() {

            }
        });
    }
}