package com.varsitycollege.vinyl_warehouse.Deezer;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseWriteWorker;
import com.varsitycollege.vinyl_warehouse.Music.Track;
import com.varsitycollege.vinyl_warehouse.R;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;
import com.varsitycollege.vinyl_warehouse.Utils.Util;
import com.varsitycollege.vinyl_warehouse.ViewAlbum;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

//custom adapter for albums to be used in recycler views
public class DeezerAlbumAdpater extends RecyclerView.Adapter<DeezerAlbumAdpater.ViewHolder> {
    public MediaPlayer mediaPlayer;

    public static final String ALBUMID = "ALBUMID";
    // Creating a variable for the layout
    private LayoutInflater layoutInflater;
    // List to hold the tracks
    private List<Datum> albumList;
    private  String albumID;
    private Date albumDate;
     //Constructor
    public DeezerAlbumAdpater(List<Datum> albums,String albumID,Date albumDate, Context context) {
        // Inflating the layout to display all tracks in the album
        this.layoutInflater = LayoutInflater.from(context);
        this.albumID=albumID;
        this.albumDate=albumDate;
        this.albumList = albums;

    }

    @NonNull
    @Override
    //Connect Layout (Card view) to view
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Connect the custom layout
        View view = layoutInflater.inflate(R.layout.deezer_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    //Put data in connected layout
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Creating an album object
        Datum album = albumList.get(position);
        // Getting the albums title
        String albumTitle = album.getTitle();
        // Getting the albums artists
        String albumArtist = album.getArtist().getName();
        // Getting the albums cover image
        String albumCover = album.getAlbum().getCoverBig();
        LocalTime time = null;
        String duration =album.getDuration() + "s";
        //Set data to component
        holder.txvRowRecycler_AlbumTitle.setText(albumTitle);
        holder.txvRowRecycler_Artist.setText(albumArtist);
        holder.txvDuration.setText(duration);
        // Loading the albums cover image
        Picasso.get().load(albumCover).into(holder.imgRowRecycler_Cover);
    }

    // Method to get the length of the array passed from the activity
    @Override
    public int getItemCount() {
        //Return the length of the list passed from activity
        return albumList.size();
    }
    private void playAudio(String link) {

        String audioUrl = link;

        // initializing media player
        mediaPlayer = new MediaPlayer();

        // below line is use to set the audio
        // stream type for our media player.
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // below line is use to set our
        // url to our media player.
        try {
            try {
                mediaPlayer.setDataSource(audioUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // below line is use to prepare
            // and start our media player.
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // below line is use to display a toast message.
    //    Toast.makeText(this, "Audio started playing..", Toast.LENGTH_SHORT).show();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Variable to store the GUI components
        TextView txvRowRecycler_Artist, txvRowRecycler_AlbumTitle,txvDuration;
        ImageView imgRowRecycler_Cover;
        MaterialButton btnGo , btnOpen;
        ProgressBar progressDeezerCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Assigning the GUI components to the variables

            txvRowRecycler_Artist = itemView.findViewById(R.id.txvArtist);
            txvRowRecycler_AlbumTitle = itemView.findViewById(R.id.txvTitle);
            imgRowRecycler_Cover = itemView.findViewById(R.id.imgCover);
            btnGo = itemView.findViewById(R.id.btnGo);
            progressDeezerCard = itemView.findViewById(R.id.progressDeezerCard);
            txvDuration = itemView.findViewById(R.id.txvDuration);
            btnOpen = itemView.findViewById(R.id.btnOpen);
            btnOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying()) {
                            // pausing the media player if media player
                            // is playing we are calling below line to
                            // stop our media player.
                            mediaPlayer.stop();
                        }}
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(albumList.get(getAdapterPosition()).getLink()));
                    itemView.getContext().startActivity(browserIntent);
                }
            });
            imgRowRecycler_Cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying()) {
                            // pausing the media player if media player
                            // is playing we are calling below line to
                            // stop our media player.
                            mediaPlayer.stop();
                            // mediaPlayer.reset();
                            //mediaPlayer.release();
                        }}
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setMessage("Do You Want To Add This Track");
                    builder.setTitle("Adding Track?");
                    builder.setCancelable(false);
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            progressDeezerCard.setVisibility(View.VISIBLE);
                            Datum datum = albumList.get(getAdapterPosition());
                            FirebaseWriteWorker firebaseWriteWorker = new FirebaseWriteWorker(CurrentUser.currentUserObj.getPersonId());
                            String trackID = Util.idGenerator();
                            List<String> artist= new ArrayList<>();
                            artist.add(datum.getArtist().getName());
                            //String trackTitle, List<String> genre, List<String> artists, Date dateOfRelease, String coverLink
                            Track track = new Track(albumID,trackID,datum.getTitle(),new ArrayList<>(),artist,albumDate,datum.getAlbum().getCoverBig());
                            firebaseWriteWorker.WriteTrack(track, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    progressDeezerCard.setVisibility(View.INVISIBLE);
                                    dialog.dismiss();
                                    Intent goBackToViewAlbum = new Intent(view.getContext(), ViewAlbum.class);
                                    goBackToViewAlbum.putExtra(ALBUMID,albumID);
                                    view.getContext().startActivity(goBackToViewAlbum);

                                }
                            });
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            btnGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Datum album = albumList.get(getAdapterPosition());
                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying()) {
                            // pausing the media player if media player
                            // is playing we are calling below line to
                            // stop our media player.
                            mediaPlayer.stop();
                        }else{
                            playAudio(album.getPreview());
                        }
                    }else{
                        playAudio(album.getPreview());
                    }


                }
            });
        }
    }
}

