package com.varsitycollege.vinyl_warehouse;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;
import com.varsitycollege.vinyl_warehouse.DataWorkers.DataDeleteWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseDeleteWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseReadWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.OnGetDataListener;
import com.varsitycollege.vinyl_warehouse.Music.Track;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

public class ViewTrack extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlaybackEventListener, YouTubePlayer.PlayerStateChangeListener {

    //static final variables
    private static final String CONFIRM_DELETION = "Confirmation deletion";
    private static final String CONFIRM_ACCOUNT_DELETION = "Are you sure you want to delete your account?";
    private static final String GOOGLE_API = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=";
    public static final String TRACK_ID = "TRACKID";
    public static final String ALBUM_ID = "ALBUMID";
    public static final String ITEMS = "items";
    public static final String ID = "id";
    public static final String NO = "NO";
    public static final String YES = "Yes";
    public static final String VIDEO_ID = "videoId";
    public static final String DATE_FORMAT_1 = "dd MMM yyyy";
    // Creating variables for the GUI components
    private String albumID, trackID;
    YouTubePlayerView player;
    String API = "AIzaSyD7X3QYiKCyaisB-r0e55vnDzbG-tqhdZo";
    String VideoID = "";
    TextView txvTrackTitle, txvTrackArtist, txvTrackGenre, txvTrackDateOfRelease;
    ImageView imgCover;
    ImageButton imgBackArrow;
    ImageButton imgViewTrack_Options;
    ProgressBar progressViewTracks;
    Track track = new Track();
    //LoadDialogBar loadDialogBar;
    String cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_track);
        //setting UI Components to the variables

        player = findViewById(R.id.playerview);
        imgBackArrow = findViewById(R.id.imgViewTrack_BackArrow);
        imgCover = findViewById(R.id.imgViewTrack_TackCover);
        txvTrackTitle = findViewById(R.id.txvViewTrack_TrackTitle);
        txvTrackArtist = findViewById(R.id.txvViewTrack_Artists);
        txvTrackGenre = findViewById(R.id.txvViewTrack_Genre);
        txvTrackDateOfRelease = findViewById(R.id.txvViewTrack_DateOfRelease);
        imgViewTrack_Options = findViewById(R.id.imgViewTrack_Options);
        progressViewTracks = findViewById(R.id.progressViewTracks);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        //gets selected track ID
        trackID = extras.getString(TRACK_ID);
        progressViewTracks.setVisibility(View.VISIBLE);
        //Reads the current users track from firebase
        FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(CurrentUser.currentUserObj.getPersonId());
        firebaseReadWorker.getTrack(trackID, new OnGetDataListener() {

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //gets the track
                track = dataSnapshot.getValue(Track.class);
            }

            @Override
            public void onStartWork() {
                //hides loading page
                progressViewTracks.setVisibility(View.INVISIBLE);
                //declare display strings
                String artists = "";
                String genres = "";
                //checks if there is multiple artists
                if (track.getArtists().size() > 1) {
                    //loops through all artists and formats it into a string
                    for (String artist : track.getArtists()
                    ) {
                        //creates a display string
                        artists = artists + artist + ",";

                    }
                    //gets rid of the space of the end using string manipulation
                    artists = artists.substring(0, artists.length() - 1);
                } else {
                    //gets the current artist
                    artists = track.getArtists().get(0);
                }
                //checks if there is multiple genre in the object
                if (track.getGenre()!=null){
                    if (track.getGenre().size() > 1) {
                        //loops through all the tracks
                        for (String genre : track.getGenre()
                        ) {
                            //creates a string for all genres combined
                            genres = genres + genre + ",";

                        }
                        //gets rid of the space at the end
                        genres = genres.substring(0, genres.length() - 1);
                    } else {
                        //sets the genre to first genre
                        genres = track.getGenre().get(0);
                    }
                }else{
                    genres = "N/A";
                }

                //sets text for the UI display
                albumID = track.getAlbumID();
                txvTrackTitle.setText(track.getTrackTitle());
                txvTrackArtist.setText(artists);
                txvTrackGenre.setText(genres);
                String strDate="";
                if (track.getDateOfRelease()!=null){
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_1);
                    //Convert String to date
                    strDate = dateFormat.format(track.getDateOfRelease());

                }

                txvTrackDateOfRelease.setText(strDate);

                //Gets the Youtube video for the title and artist
                new getJson().execute(GOOGLE_API + track.getTrackTitle() + "+" + track.getArtists().get(0) + "&maxResults=1&key=AIzaSyD7X3QYiKCyaisB-r0e55vnDzbG-tqhdZo");
                Picasso.get().load(track.getCoverLink()).into(imgCover);
                cover = track.getCoverLink();
            }

            @Override
            public void onFailure() {
                //Method not used
            }
        });

        //On click listener to go to the previous screen
        imgBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creates intent to go to View album page
                Intent toViewAlbum = new Intent(ViewTrack.this, ViewAlbum.class);
                toViewAlbum.putExtra(ALBUM_ID, albumID);
                //goes to the previous page
                startActivity(toViewAlbum);
                finish();
            }
        });
        //checks if the options button is clicked
        imgViewTrack_Options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creates a popup dilogue for the view track page
                PopupMenu popupMenu = new PopupMenu(ViewTrack.this, view);
                //sets the menu items
                popupMenu.getMenuInflater().inflate(R.menu.menu_edit_delete, popupMenu.getMenu());
                //checks which menu item is clicked
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        //Switch to check which item is clicked
                        switch (menuItem.getItemId()) {
                            case R.id.itemMenu_Edit:
                                //if the Edit track is clicked
                                editTrack();
                                return true;
                            case R.id.itemMenu_Delete:
                                //if the delete item is clicked
                                deleteTrack();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //shows the menu dialogue
                popupMenu.show();

            }
        });

    }


    //method to go to edit track page
    private void editTrack() {
        //creates intent to the edit track page, passing in the Album ID and Track ID
        Intent toEditTrack = new Intent(ViewTrack.this, EditTrack.class);
        toEditTrack.putExtra(TRACK_ID, trackID);
        toEditTrack.putExtra(ALBUM_ID, albumID);
        startActivity(toEditTrack);
        finish();
    }

    // Method to delete a track
    private void deleteTrack() {
        //popups an alert dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewTrack.this);
        //sets the relative information for the popup alert box
        builder.setTitle(CONFIRM_DELETION);
        builder.setMessage(CONFIRM_ACCOUNT_DELETION);
        builder.setPositiveButton(YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // showing the progress dialog
                progressViewTracks.setVisibility(View.VISIBLE);
                DataDeleteWorker dataDeleteWorker = new DataDeleteWorker(CurrentUser.currentUserObj.getPersonId());
                dataDeleteWorker.deleteTrack(track, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        // Switching Layouts
                        // Hiding the progress dialog
                        progressViewTracks.setVisibility(View.INVISIBLE);
                        Intent toEditTrack = new Intent(ViewTrack.this, ViewAlbum.class);
                        toEditTrack.putExtra(TRACK_ID, trackID);
                        toEditTrack.putExtra(ALBUM_ID, albumID);
                        startActivity(toEditTrack);
                    }
                });


            }
        });
        builder.setNegativeButton(NO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Cancel
                dialogInterface.dismiss();
            }
        });
        //shows the Alert popup
        builder.show();

    }

    //shows and plays the youtube video
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        // Loading the YouTube video
        youTubePlayer.setPlayerStateChangeListener(ViewTrack.this);
        youTubePlayer.setPlaybackEventListener(this);
        if (!b) {
            youTubePlayer.cueVideo(VideoID);
        }
    }

    //Hides the dialogue when the Youtube video doesnt work
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        //Method not being used
    }

    @Override
    public void onPlaying() {
//Method not being used
    }

    @Override
    public void onPaused() {
//Method not being used
    }

    @Override
    public void onStopped() {
//Method not being used
    }

    @Override
    public void onBuffering(boolean b) {
//Method not being used
    }

    @Override
    public void onSeekTo(int i) {
//Method not being used
    }

    @Override
    public void onLoading() {
//Method not being used
    }

    @Override
    public void onLoaded(String s) {
        //Method not being used
    }

    @Override
    public void onAdStarted() {
        //Method not being used
    }

    @Override
    public void onVideoStarted() {
        //Method not being used
    }

    @Override
    public void onVideoEnded() {
        //Method not being used
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        // Showing the error
        Toast.makeText(this, errorReason.toString(), Toast.LENGTH_LONG).show();
    }

    // INNER CLASS TO SEARCH FOR THE YOUTUBE VIDEO
    class getJson extends AsyncTask<String, Void, String> {
        String text = "";

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL(arg0[0]);
                // Sending a request to the YouTube API
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                // Concat JSON
                while (line != null) {
                    line = bufferedReader.readLine();
                    text = text + line;
                }
            } catch (Exception e) {
                Log.d(TAG, "doInBackground: "+e);
            }

            try {
                JSONObject jsonObj = new JSONObject(text);
                JSONArray jArr = jsonObj.getJSONArray(ITEMS);
                for (int i = 0; i < jArr.length(); i++) {
                    // getting object from items array
                    JSONObject itemObj = jArr.getJSONObject(i);

                    // getting id object from item object
                    JSONObject idObj = itemObj.getJSONObject(ID);

                    // getting videoId from idObject
                    String videoId = idObj.getString(VIDEO_ID);
                    VideoID = videoId;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return text;
        }

        @Override
        protected void onPostExecute(String result) {
            // Initializing
            super.onPostExecute(result);
            player.initialize(API, ViewTrack.this);
        }
    }

}

