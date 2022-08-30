package com.varsitycollege.vinyl_warehouse.AdapterWorkers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.varsitycollege.vinyl_warehouse.Music.Track;
import com.varsitycollege.vinyl_warehouse.R;
import java.util.List;
import com.varsitycollege.vinyl_warehouse.ViewTrack;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    // Creating a variable to hold the Track Id
    public static final String TRACKID = "TRACKID";
    // Variable for the layout inflater
    private LayoutInflater layoutInflater;
    // List to hold the tracks
    private List<Track> trackList;

    //Constructor
    public TrackAdapter(List<Track> trackList, Context context) {
        // Assigning the context parameter to the layout inflater variable
        this.layoutInflater = LayoutInflater.from(context);
        // Assigning the track list parameter to the list variable
        this.trackList = trackList;
    }

    @NonNull
    @Override
    //Connect Layout (Card view) to view
    public TrackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Connect the custom layout
        View trackView = layoutInflater.inflate(R.layout.row_recycler, parent, false);
        return new ViewHolder(trackView);
    }

    @Override
    //Put data in connected layout
    public void onBindViewHolder(@NonNull TrackAdapter.ViewHolder holder, int position) {
        // Creating a track object
        Track track = trackList.get(position);
        // Assigning the track title to the object
        String trackTitle = track.getTrackTitle();
        // Assigning the tracks artists to the object
        String trackArtist = track.getArtists().get(0);
        // Assigning the track cover to the object
        String trackCover = track.getCoverLink();
        //Set data to component
        holder.txvRowRecycler_AlbumTitle.setText(trackTitle);
        holder.txvRowRecycler_Artist.setText(trackArtist);
        // Displaying the track cover image
        Picasso.get().load(trackCover).into(holder.imgRowRecycler_Cover);
    }

    @Override
    public int getItemCount() {
        //Return the length of the list passed from activity
        return trackList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Creating variables for the GUI Components
        TextView txvRowRecycler_Artist, txvRowRecycler_AlbumTitle;
        ImageView imgRowRecycler_Cover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Connect the custom layouts components to variables
            txvRowRecycler_Artist = itemView.findViewById(R.id.txvRowRecycler_Artist);
            txvRowRecycler_AlbumTitle = itemView.findViewById(R.id.txvRowRecycler_Title);
            imgRowRecycler_Cover = itemView.findViewById(R.id.imgRowRecycler_Cover);
            //OnClick
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View trackView) {
                    //Go to track
                    Intent i = new Intent(trackView.getContext(), ViewTrack.class);
                    //Pass Track ID
                    i.putExtra(TRACKID, trackList.get(getAdapterPosition()).getTrackID());
                    trackView.getContext().startActivity(i);
                }
            });
        }
    }
}
