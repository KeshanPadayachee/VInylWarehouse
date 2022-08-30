package com.varsitycollege.vinyl_warehouse;
//imports
import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.varsitycollege.vinyl_warehouse.Deezer.Datum;
import com.varsitycollege.vinyl_warehouse.Deezer.DeezerAlbumAdpater;
import com.varsitycollege.vinyl_warehouse.Deezer.Root;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeezerAddTrack extends AppCompatActivity {
    //creates variables
    public static final String ALBUMID = "ALBUMID";
    RecyclerView recycler;
    //creates a list of type datum
    List<Datum> main = new ArrayList<>();
    String title = "";
    String name = "";
    String date = "";
    String albumId = "";
    //specifies the date format
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    //declare UI components
    FloatingActionButton imgDeezerTrack_BackArrow, fabDeezerAddTrack_Next, fabDeezerAddTrack_Prev;
    ProgressBar progressDeezerAddTrack;
    TextView txtDeezerPerv, txvDeezerNext;
    Button txvManually;
    int index = 0;
    DeezerAlbumAdpater albumAdpater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deezer_add_track);
        //assigns the UI components to the assigned variables
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        recycler = findViewById(R.id.recycler);
        Bundle extras = getIntent().getExtras();
        // Assigning UI components to variables
        txvManually = findViewById(R.id.txvDeezerAddTrack_ManuallyAddTrack);
        imgDeezerTrack_BackArrow = findViewById(R.id.imgDeezerTrack_BackArrow);
        progressDeezerAddTrack = findViewById(R.id.progressDeezerAddTrack);
        fabDeezerAddTrack_Next = findViewById(R.id.fabDeezerAddTrack_Next);
        fabDeezerAddTrack_Prev = findViewById(R.id.fabDeezerAddTrack_Prev);
        txtDeezerPerv = findViewById(R.id.txtDeezerPerv);
        txvDeezerNext = findViewById(R.id.txvDeezerNext);
        //gets the track details from the bundle
        title = extras.getString("AlbumName");
        albumId = extras.getString("AlbumID");
        date = extras.getString("AlbumDate");
        name = extras.getString("AlbumArtist");
        // Showing the progress dialog
        progressDeezerAddTrack.setVisibility(View.VISIBLE);
        // Disabling components
        fabDeezerAddTrack_Prev.setEnabled(false);
        fabDeezerAddTrack_Next.setEnabled(false);
        imgDeezerTrack_BackArrow.setEnabled(false);
        new getData().execute();
        fabDeezerAddTrack_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (albumAdpater.mediaPlayer!=null){
                    // stopping playback
                    albumAdpater.mediaPlayer.stop();
                }
                //checks if there is less than 75 tracks
                if (index < 75) {
                    //shows the progress and enables the next and previous buttons
                    progressDeezerAddTrack.setVisibility(View.VISIBLE);
                    fabDeezerAddTrack_Prev.setEnabled(false);
                    fabDeezerAddTrack_Next.setEnabled(false);
                    //adds an additional 25
                    index = index + 25;
                    //checks if the tracks are 75
                    if (index == 75) {
                        //enable the UI components
                        fabDeezerAddTrack_Next.setVisibility(View.INVISIBLE);
                        txvDeezerNext.setVisibility(View.INVISIBLE);
                    }
                    //checks if there is more than 25 tracks
                    if (index >= 25) {
                        //enables the UI componnets
                        fabDeezerAddTrack_Prev.setVisibility(View.VISIBLE);
                        txtDeezerPerv.setVisibility(View.VISIBLE);
                    }
                    new getData().execute();
                }

            }
        });
        //checks if the previous button is clicked
        fabDeezerAddTrack_Prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (albumAdpater.mediaPlayer!=null){
                    // stopping playback
                    albumAdpater.mediaPlayer.stop();
                }
                //checks if there is more than 0 tracks
                if (index > 0) {
                    //enables UI components
                    progressDeezerAddTrack.setVisibility(View.VISIBLE);
                    fabDeezerAddTrack_Prev.setEnabled(false);
                    fabDeezerAddTrack_Next.setEnabled(false);

                    index = index - 25;
                    //checks if there is 0 tracks
                    if (index == 0) {
                        //enables UI components
                        fabDeezerAddTrack_Prev.setVisibility(View.INVISIBLE);
                        txtDeezerPerv.setVisibility(View.INVISIBLE);
                    }
                    //checks if there is less than 75 tracks
                    if (index < 75) {
                        //enables UI components
                        fabDeezerAddTrack_Next.setVisibility(View.VISIBLE);
                        txvDeezerNext.setVisibility(View.VISIBLE);
                    }
                    new getData().execute();
                }

            }
        });
        txvManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (albumAdpater.mediaPlayer!=null){
                    // stopping playback
                    albumAdpater.mediaPlayer.stop();
                }
                //goes to the add new track page passing the ID
                Intent i = new Intent(view.getContext(), AddNewTrack.class);
                i.putExtra(ALBUMID, albumId);
                startActivity(i);
                finish();
            }
        });
        imgDeezerTrack_BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (albumAdpater.mediaPlayer!=null){
                    // stopping playback
                    albumAdpater.mediaPlayer.stop();
                }
                //goes back to the view album page passing in the ID
                Intent goBackToViewAlbum = new Intent(view.getContext(), ViewAlbum.class);
                goBackToViewAlbum.putExtra(ALBUMID, albumId);
                view.getContext().startActivity(goBackToViewAlbum);
                finish();
            }
        });


    }

    public void addCards() {
        // Hiding the progress dialog
        progressDeezerAddTrack.setVisibility(View.INVISIBLE);
        // Enabling controls
        imgDeezerTrack_BackArrow.setEnabled(true);
        fabDeezerAddTrack_Prev.setEnabled(true);
        fabDeezerAddTrack_Next.setEnabled(true);
        //start adapter

        Date dateConvert = null;
        try {
            dateConvert = new SimpleDateFormat(DATE_FORMAT).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
         albumAdpater = new DeezerAlbumAdpater(main, albumId, dateConvert, DeezerAddTrack.this);
        recycler.setLayoutManager(new LinearLayoutManager(DeezerAddTrack.this));
        //start adapter
        recycler.setAdapter(albumAdpater);
        }

    class getData extends AsyncTask<Void, Void, Void> {
        String text = "";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //deezer api key
                URL url = new URL("https://api.deezer.com/search?q=album:\"" + title + "\" artisit=\"" + name + "\"" + "&index=" + index);
                //makes connection
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    text = text + line;
                }

                ObjectMapper mapper = new ObjectMapper();

                ObjectMapper om = new ObjectMapper();
                Root root = mapper.readValue(text, Root.class);
                main = root.getData();

                Log.d(TAG, root.getData().get(0).getTitle());
                root.toString();
            } catch (Exception e) {
                Log.d(TAG, "doInBackground: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Initializing
            super.onPostExecute(result);
            addCards();
        }
    }


}