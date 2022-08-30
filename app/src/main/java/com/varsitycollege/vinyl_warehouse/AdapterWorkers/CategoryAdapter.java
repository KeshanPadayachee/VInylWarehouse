package com.varsitycollege.vinyl_warehouse.AdapterWorkers;

import static com.varsitycollege.vinyl_warehouse.AdapterWorkers.CategoriesAdapter.CATEGORY_ID;

import android.app.Activity;
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
import com.varsitycollege.vinyl_warehouse.Music.Album;
import com.varsitycollege.vinyl_warehouse.Music.Categories;
import com.varsitycollege.vinyl_warehouse.R;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;
import com.varsitycollege.vinyl_warehouse.ViewAlbum;
import com.varsitycollege.vinyl_warehouse.ViewCategory;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    // Creating a variable to hold the Album Id
    public static final String ALBUMID = "ALBUMID";
    // Variable for the layout
    private LayoutInflater layoutInflater;
    // Creating a list to store the albums
    private List<Album> albumList;
    // Creating a categories object
    private Categories categories;

    //Constructor
    public CategoryAdapter(List<Album> albums, Categories categories, Context context) {
        // Passing the context to the layout inflater variable
        this.layoutInflater = LayoutInflater.from(context);
        // Assigning the albums parameter to the variable
        this.albumList = albums;
        // Assigning the category parameter to the variable
        this.categories = categories;
    }

    @NonNull
    @Override
    //Connect Layout (Card view) to view
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Connect the custom layout
        View categoryView = layoutInflater.inflate(R.layout.row_recycler, parent, false);
        return new ViewHolder(categoryView);
    }

    @Override
    //Put data in connected layout
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        // Creating an album object
        Album album = albumList.get(position);
        // Assigning the album title to the album object
        String albumTitle = album.getAlbumTitle();
        // Assigning the album artist to the album object
        String albumArtist = album.getArtists().get(0);
        // Assigning the album cover to the album object
        String albumCover = album.getCoverLink();

        //Set data to component
        holder.txvRowRecycler_AlbumTitle.setText(albumTitle);
        holder.txvRowRecycler_Artist.setText(albumArtist);
        // Showing the album cover
        Picasso.get().load(albumCover).into(holder.imgRowRecycler_Cover);
    }

    @Override
    public int getItemCount() {
        //Return the length of the list passed from activity
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txvRowRecycler_Artist, txvRowRecycler_AlbumTitle;
        ImageView imgRowRecycler_Cover;
        ProgressBar progressRowRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Connect the custom layouts components to variables
            txvRowRecycler_Artist = itemView.findViewById(R.id.txvRowRecycler_Artist);
            txvRowRecycler_AlbumTitle = itemView.findViewById(R.id.txvRowRecycler_Title);
            imgRowRecycler_Cover = itemView.findViewById(R.id.imgRowRecycler_Cover);
            progressRowRecycler = itemView.findViewById(R.id.progressRowRecycler);

            //OnClick event
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View categoryView) {
                    //Go to Album activity
                    Intent i = new Intent(categoryView.getContext(), ViewAlbum.class);
                    //Pass Album ID
                    i.putExtra(ALBUMID, albumList.get(getAdapterPosition()).getAlbumID());
                    categoryView.getContext().startActivity(i);
                }
            });
            //If long click then remove the album from the category
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View categoryView) {

                    //Get all the albums
                    List<String> albumIDList = new ArrayList<>();
                    albumIDList = categories.getAlbumIDs();
                    List<String> newAlbumListExcAlbum = new ArrayList<>();
                    for (String albumID : albumIDList) {
                        //Exclude the album that the user wants to removed
                        if (!albumID.equals(albumList.get(getAdapterPosition()).getAlbumID())) {
                            newAlbumListExcAlbum.add(albumID);
                        }

                    }
                    //Write the new category
                    progressRowRecycler.setVisibility(View.VISIBLE);
                    FirebaseWriteWorker firebaseWriteWorker = new FirebaseWriteWorker(CurrentUser.currentUserObj.getPersonId());
                    categories.setAlbumIDs(newAlbumListExcAlbum);
                    firebaseWriteWorker.WriteCategory(categories, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            progressRowRecycler.setVisibility(View.INVISIBLE);
                            //Go back to category with updated category
                            Intent refreshAct = new Intent(categoryView.getContext(), ViewCategory.class);
                            refreshAct.putExtra(CATEGORY_ID, categories.getCategoryID());
                            categoryView.getContext().startActivity(refreshAct);
                            Activity activity = (Activity) categoryView.getContext();
                            activity.finish();
                        }
                    });
                    return false;
                }
            });
        }
    }
}
