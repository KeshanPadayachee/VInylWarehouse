package com.varsitycollege.vinyl_warehouse;
//Import statements

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;
import com.varsitycollege.vinyl_warehouse.AdapterWorkers.TrackAdapter;
import com.varsitycollege.vinyl_warehouse.DataWorkers.DataDeleteWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseReadWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.OnGetDataListener;
import com.varsitycollege.vinyl_warehouse.Music.Album;
import com.varsitycollege.vinyl_warehouse.Music.Track;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ViewAlbum extends AppCompatActivity {
    //Declaring Variables and components
    public static final String ALBUMID = "ALBUMID";
    public static final String DATE_FORMAT = "dd MMM yyyy";
    public static final String DATE_FORMAT_2 = "dd/MM/yyyy";
    public static final String NO = "No";
    public static final String YES = "Yes";
    public static final String CONFIRMATION_DELETION = "Confirmation deletion";
    public static final String DELETE_THIS_ALBUM = "Are you sure you want to delete this Album?";
    public static final String ALBUM_NAME = "AlbumName";
    public static final String ALBUM_ARTIST = "AlbumArtist";
    public static final String ALBUM_DATE = "AlbumDate";
    public static final String ALBUM_ID = "AlbumID";
    // Declaring variable to communicate with components
    private ImageView imgViewAlbum_AlbumCover, imgBackArrow, img_btn_Options;
    private TextView txvViewAlbum_ReleaseDateData, txvViewAlbum_GenreData, txvViewAlbum_AlbumName,
            txvViewAlbum_Artist;

    TrackAdapter trackAdapter;
    RatingBar viewAlbumRatingBar;
    FloatingActionButton fab;
    ProgressBar progressViewAlbum;
    RecyclerView recyclerView;
    String albumid, cover;
    Album deezerTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_album);
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Connecting variable to components
        imgViewAlbum_AlbumCover = findViewById(R.id.imgViewAlbum_AlbumCover);
        txvViewAlbum_ReleaseDateData = findViewById(R.id.txvViewAlbum_ReleaseDateData);
        txvViewAlbum_GenreData = findViewById(R.id.txvViewAlbum_GenreData);
        txvViewAlbum_AlbumName = findViewById(R.id.txvViewAlbum_AlbumName);
        txvViewAlbum_Artist = findViewById(R.id.txvViewAlbum_Artist);
        imgBackArrow = findViewById(R.id.imgViewAlbum_BackArrow);
        img_btn_Options = findViewById(R.id.img_btn_Options);
        progressViewAlbum = findViewById(R.id.progressViewAlbum);
        viewAlbumRatingBar = findViewById(R.id.rtbViewAlbum_Rating);

        fab = findViewById(R.id.fabViewAlbum_AddTrack);
        Bundle extras = getIntent().getExtras();
        if (extras.getString(ALBUMID) != null) {
            // Showing the progress dialog
            progressViewAlbum.setVisibility(View.VISIBLE);
            albumid = extras.getString(ALBUMID);
            if (CurrentUser.currentUserObj != null) {
                recyclerManager();
            }


            extras.remove(ALBUMID);
        } else {
            // Switching layouts
            Intent intent = new Intent(ViewAlbum.this, IntroWelcome.class);
            startActivity(intent);
            finish();
        }

        // OnClickListener for the floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switching layouts
                Intent i = new Intent(view.getContext(), DeezerAddTrack.class);
                i.putExtra(ALBUM_NAME, deezerTest.getAlbumTitle());
                i.putExtra(ALBUM_ARTIST, deezerTest.getArtists().get(0));
                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_2);

                //Convert String to date
                String strDate = dateFormat.format(deezerTest.getDateOfRelease());
                i.putExtra(ALBUM_DATE, strDate);
                i.putExtra(ALBUM_ID, deezerTest.getAlbumID());

                startActivity(i);
                finish();
            }
        });


        // OnClickListener for the back arrow button
        imgBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // goes to the previous page.
                Intent toMainActivity = new Intent(ViewAlbum.this, MainActivity.class);
                startActivity(toMainActivity);
                finish();
            }
        });

        // OnClickListener for the options button
        img_btn_Options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creates a popup menu with edit and delete items
                PopupMenu popupMenu = new PopupMenu(ViewAlbum.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_edit_delete, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    //checks which item has been clicked and calls the relative method
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.itemMenu_Edit:
                                goToEditAlbum();
                                return true;
                            case R.id.itemMenu_Delete:
                                deleteAlbum();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //shows the menu
                popupMenu.show();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //goes to the intro page, if the user is not found
        if (CurrentUser.currentUserObj.getPersonId() == null) {
            Intent intent = new Intent(ViewAlbum.this, IntroWelcome.class);
            startActivity(intent);
            finish();
        }
    }

    // Method to manage the recycler view
    public void recyclerManager() {
        List<Track> trackList = new ArrayList<>();
        //Get Album data
        FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(CurrentUser.currentUserObj.getPersonId());
        firebaseReadWorker.getAlbum(albumid, new OnGetDataListener() {
            Album album;

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //Save data
                Album album2 = dataSnapshot.getValue(Album.class);
                album = album2;
                deezerTest = album2;
            }

            @Override
            public void onStartWork() {
                //calls the method to populate the UI fields and hides the loading page
                try {
                    setAlbumDetails(album);
                } catch (Exception exception) {
                    Intent intent = new Intent(ViewAlbum.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure() {
                // Method not being used
            }
        });

        //Get Track data
        firebaseReadWorker.getTracks(albumid, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //Add track data
                Track track = dataSnapshot.getValue(Track.class);
                trackList.add(track);
                // Showing the progress dialog
                progressViewAlbum.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStartWork() {
                // Hiding the progress dialog
                progressViewAlbum.setVisibility(View.INVISIBLE);
                //Check if Album has tracks
                if (trackList != null) {
                    //Start adapter
                    recyclerView = findViewById(R.id.recyclerViewAlbum_ViewTracks);
                    trackAdapter = new TrackAdapter(trackList, ViewAlbum.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ViewAlbum.this));
                    //start adapter
                    recyclerView.setAdapter(trackAdapter);
                }
            }

            @Override
            public void onFailure() {
                progressViewAlbum.setVisibility(View.INVISIBLE);
            }
        });
    }

    // Method to set the albums details
    private void setAlbumDetails(Album album) {
        // Hiding the progress dialog
        progressViewAlbum.setVisibility(View.INVISIBLE);
        //Set Album details
        String artists = "";
        String genres = "";
        //checks if there are many artists
        if (album.getArtists() != null) {
            //loops through al the artists and creates a string with all artists
            for (String artist : album.getArtists()
            ) {
                artists = artists + artist + ",";

            }
            //gets rid of the space at the end
            artists = artists.substring(0, artists.length() - 1);
        } else {
            //sets the artist to the first artist
            artists = album.getArtists().get(0);
        }
        //checks if there are many genres
        if (album.getGenre() != null) {
            //loops through the genres and creates a string with all genres
            for (String genre : album.getGenre()
            ) {
                genres = genres + genre + ",";

            }
            //gets rid of the space at the end
            genres = genres.substring(0, genres.length() - 1);
        } else {
            //sets the string to the first genre
            genres = album.getGenre().get(0);
        }

        // Displaying the details to the user
        txvViewAlbum_Artist.setText(artists);
        txvViewAlbum_AlbumName.setText(album.getAlbumTitle());
        //Code Attribution
        //Link: https://www.geeksforgeeks.org/how-to-use-picasso-image-loader-library-in-android/
        //Author: GeeksforGeeks
        //End
        //Sets the UI components to values
        Picasso.get().load(album.getCoverLink()).into(imgViewAlbum_AlbumCover);
        cover = album.getCoverLink();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        //Convert String to date
        String strDate = dateFormat.format(album.getDateOfRelease());
        txvViewAlbum_ReleaseDateData.setText(strDate);
        txvViewAlbum_GenreData.setText(genres);
        viewAlbumRatingBar.setRating(album.getRating());
    }

    // Method to delete an album
    private void deleteAlbum() {
        //creates an alert popup to confirm delete account with the relative info about the pop up
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(CONFIRMATION_DELETION);
        builder.setMessage(DELETE_THIS_ALBUM);
        builder.setPositiveButton(YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //shows the loading page
                //deletes the album
                // Showing the progress dialog
                progressViewAlbum.setVisibility(View.VISIBLE);
                DataDeleteWorker dataDeleteWorker = new DataDeleteWorker(CurrentUser.currentUserObj.getPersonId());
                dataDeleteWorker.deleteLink(cover, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        dataDeleteWorker.deleteAlbum(albumid, false, new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                // Hiding the progress dialog
                                progressViewAlbum.setVisibility(View.INVISIBLE);
                                //goes to the main menu
                                Intent intent = new Intent(ViewAlbum.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });

            }
        });
        builder.setNegativeButton(NO, new DialogInterface.OnClickListener() {
            @Override
            //if no is selected
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        //shows the alert popup
        builder.show();
    }

    // Method to go to edit album screen
    private void goToEditAlbum() {
        Intent i = new Intent(ViewAlbum.this, EditAlbum.class);
        //Get title data from the adapter postion to display
        //Sent the ID so the other class can use the id to do other work
        i.putExtra(ALBUMID, albumid);
        startActivity(i);
        finish();
    }


}