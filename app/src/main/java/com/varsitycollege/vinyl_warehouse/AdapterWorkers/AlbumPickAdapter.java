package com.varsitycollege.vinyl_warehouse.AdapterWorkers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseWriteWorker;
import com.varsitycollege.vinyl_warehouse.LoadDialogBar;
import com.varsitycollege.vinyl_warehouse.Music.Album;
import com.varsitycollege.vinyl_warehouse.Music.Categories;
import com.varsitycollege.vinyl_warehouse.R;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;
import java.util.ArrayList;
import java.util.List;
import com.varsitycollege.vinyl_warehouse.ViewCategory;

public class AlbumPickAdapter extends RecyclerView.Adapter<AlbumPickAdapter.ViewHolder> {
    // Variable to store the layout
    private LayoutInflater layoutInflater;
    // List to store all the users albums
    private List<Album> albumList;
    // Creating an instance of the categories class
    private Categories categories;
    // Variable for the category id
    public static final String CATEGORY_ID = "CATEGORY_ID";

    //Constructor
    public AlbumPickAdapter(Categories categories, List<Album> albums, Context context) {
        // Assigning the context parameter to the layout inflater variable
        this.layoutInflater = LayoutInflater.from(context);
        // Assigning the users albums parameter to the list variable
        this.albumList = albums;
        // Assigning the categories object parameter to the categories variable
        this.categories = categories;
    }

    @NonNull
    @Override
    //Connect Layout (Card view) to View
    public AlbumPickAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Connect the custom layout
        View cardView = layoutInflater.inflate(R.layout.row_recycler, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    //Put data in connected layout
    public void onBindViewHolder(@NonNull AlbumPickAdapter.ViewHolder holder, int position) {
        // Creating an album object
        Album album = albumList.get(position);
        // Getting the albums title
        String albumTitle = album.getAlbumTitle();
        // Getting the albums artists
        String albumArtist = album.getArtists().get(0);
        // Getting the albums cover image
        String albumCover = album.getCoverLink();

        //Set data to component
        holder.txvRowRecycler_AlbumTitle.setText(albumTitle);
        holder.txvRowRecycler_Artist.setText(albumArtist);
        // Setting the image to be the albums cover image
        Picasso.get().load(albumCover).into(holder.imgRowRecycler_Cover);
    }

    // Method to get the length of the list passed from the activity
    @Override
    public int getItemCount() {
        //Return the length of the list passed from activity
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Creating variables for the GUI components
        TextView txvRowRecycler_Artist, txvRowRecycler_AlbumTitle;
        ImageView imgRowRecycler_Cover;
        ProgressBar progressRowRecycler;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Assigning the GUI components to the created variables
            txvRowRecycler_Artist = itemView.findViewById(R.id.txvRowRecycler_Artist);
            txvRowRecycler_AlbumTitle = itemView.findViewById(R.id.txvRowRecycler_Title);
            imgRowRecycler_Cover = itemView.findViewById(R.id.imgRowRecycler_Cover);
            progressRowRecycler = itemView.findViewById(R.id.progressRowRecycler);

            //OnClick
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View albumView) {
                    //Get Chosen Album
                    String albumID = albumList.get(getAdapterPosition()).getAlbumID();
                    // Showing the loading bar
                    progressRowRecycler.setVisibility(View.VISIBLE);

                    //Write picked Album to category
                    FirebaseWriteWorker firebaseWriteWorker = new FirebaseWriteWorker(CurrentUser.currentUserObj.getPersonId());
                    List<String> albumIDs = new ArrayList<>();
                    //Check if category has albums in it
                    if (categories.getAlbumIDs() != null) {
                        // Getting the albums ID
                        albumIDs = categories.getAlbumIDs();
                    }
                    //Add Album
                    albumIDs.add(albumID);
                    //Write Category with new album
                    Categories newCategories = new Categories(categories.getCategoryID(), categories.getCategoryName(), categories.getCategoryGoal(), albumIDs);
                    firebaseWriteWorker.WriteCategory(newCategories, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            //Go back to category view
                            progressRowRecycler.setVisibility(View.INVISIBLE);
                            Intent i = new Intent(albumView.getContext(), ViewCategory.class);
                            //Pass Category ID
                            i.putExtra(CATEGORY_ID, categories.getCategoryID());
                            albumView.getContext().startActivity(i);
                        }
                    });

                }
            });
        }
    }
}
