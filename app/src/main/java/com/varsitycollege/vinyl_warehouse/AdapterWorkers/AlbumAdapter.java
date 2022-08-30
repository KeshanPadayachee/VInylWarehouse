package com.varsitycollege.vinyl_warehouse.AdapterWorkers;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.varsitycollege.vinyl_warehouse.Music.Album;
import com.varsitycollege.vinyl_warehouse.R;
import com.varsitycollege.vinyl_warehouse.ViewAlbum;

import java.util.List;

    //custom adapter for albums to be used in recycler views
    public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    // String to hold the Album ID
    public static final String ALBUMID = "ALBUMID";
    // Creating a variable for the layout
    private LayoutInflater layoutInflater;
    // List to hold the tracks in the albums
    private List<Album> albumList;

    //Constructor
    public AlbumAdapter(List<Album> albums, Context albumContext) {
        // Inflating the layout to display all tracks in the album
        this.layoutInflater = LayoutInflater.from(albumContext);
        // Populating the list with all tracks in the album
        this.albumList = albums;
    }

    @NonNull
    @Override
    //Connect Layout (Card view) to view
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Connect the custom layout
        View customLayout = layoutInflater.inflate(R.layout.row_recycler, parent, false);
        return new ViewHolder(customLayout);
    }

    @Override
    //Put data in connected layout
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder albumViewHolder, int position) {
        // Creating an album object
        Album album = albumList.get(position);
        // Getting the albums title
        String albumTitle = album.getAlbumTitle();
        // Getting the albums artists
        String albumArtist = album.getArtists().get(0);
        // Getting the albums cover image
        String albumCover = album.getCoverLink();

        //Set data to component
        albumViewHolder.txvRowRecycler_AlbumTitle.setText(albumTitle);
        albumViewHolder.txvRowRecycler_Artist.setText(albumArtist);
        // Loading the albums cover image
        Picasso.get().load(albumCover).into(albumViewHolder.imgRowRecycler_Cover);
    }

    // Method to get the length of the array passed from the activity
    @Override
    public int getItemCount() {
        //Return the length of the list passed from activity
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Variable to store the GUI components
        TextView txvRowRecycler_Artist, txvRowRecycler_AlbumTitle;
        ImageView imgRowRecycler_Cover;
        ProgressBar progressRowRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Assigning the GUI components to the variables
            txvRowRecycler_Artist = itemView.findViewById(R.id.txvRowRecycler_Artist);
            txvRowRecycler_AlbumTitle = itemView.findViewById(R.id.txvRowRecycler_Title);
            imgRowRecycler_Cover = itemView.findViewById(R.id.imgRowRecycler_Cover);
            progressRowRecycler = itemView.findViewById(R.id.progressRowRecycler);

            //OnClick Listener for when an album cover is clicked
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View albumView) {
                    progressRowRecycler.setVisibility(View.VISIBLE);
                    //Go to Album activity
                    Intent i = new Intent(albumView.getContext(), ViewAlbum.class);
                    //Pass Album ID
                    i.putExtra(ALBUMID, albumList.get(getAdapterPosition()).getAlbumID());
                    // starting the activity
                    albumView.getContext().startActivity(i);
                }
            });
        }
    }
}
