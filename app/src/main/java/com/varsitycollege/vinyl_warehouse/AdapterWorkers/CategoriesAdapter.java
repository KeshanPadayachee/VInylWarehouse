package com.varsitycollege.vinyl_warehouse.AdapterWorkers;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;
import com.varsitycollege.vinyl_warehouse.AllCategories;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseReadWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseWriteWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.OnGetDataListener;
import com.varsitycollege.vinyl_warehouse.LoadDialogBar;
import com.varsitycollege.vinyl_warehouse.MainActivity;
import com.varsitycollege.vinyl_warehouse.Music.Album;
import com.varsitycollege.vinyl_warehouse.Music.Categories;
import com.varsitycollege.vinyl_warehouse.R;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;
import com.varsitycollege.vinyl_warehouse.Utils.Util;
import com.varsitycollege.vinyl_warehouse.Utils.Validation;
import com.varsitycollege.vinyl_warehouse.ViewCategory;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    // Creating a variable to hold the Category ID
    public static final String CATEGORY_ID = "CATEGORY_ID";
    // Creating a list to hold the users categories
    private List<Categories> categoriesList;
    // Creating a variable for the layout
    private LayoutInflater layoutInflater;

    //Constructor
    public CategoriesAdapter(List<Categories> categoriesList, Context context) {
        // Assigning the categories list parameters to the created list
        this.categoriesList = categoriesList;
        // Assigning the context parameter to the created layout inflater variable
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    //Connect Layout (Card view) to view
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Connect custom layout
        View cardView = layoutInflater.inflate(R.layout.custom_categories, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    //Put data in connected layout
    public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder holder, int position) {
        Categories category = categoriesList.get(position);
        //Check if category is not the one for making another one
        if (position > 0) {
            //Get the Album cover to set as the category image if there is an Album
            if (category.getAlbumIDs() != null) {

                //Get the Album from the database
                FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(CurrentUser.currentUserObj.getPersonId());
                firebaseReadWorker.getAlbum(category.getAlbumIDs().get(0), new OnGetDataListener() {

                    // Creating a variable to store the album
                    Album album;

                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        // Assigning the album from the database to the local variable
                        album = dataSnapshot.getValue(Album.class);
                    }

                    @Override
                    public void onStartWork() {
                        // Set the image
                        String categoryImage = album.getCoverLink();
                        // Showing the cover images
                        Picasso.get().load(categoryImage).into(holder.imgCustomCategories_CategoryImage);
                    }

                    @Override
                    public void onFailure() {
                        // Empty method, not being used
                    }
                });
            }

            // Set the name of the category
            String categoryName = category.getCategoryName();
            holder.txvCustomCategory_CategoryName.setText(categoryName);
        } else {
            // if it was the 1st object then set a different parameter for the creation of a category
            String categoryName = category.getCategoryID();
            String categoryImage = category.getCategoryName();
            Picasso.get().load(categoryImage).into(holder.imgCustomCategories_CategoryImage);
            holder.txvCustomCategory_CategoryName.setText(categoryName);
        }
    }

    // Method to get the item count of the list
    @Override
    public int getItemCount() {
        //return size of the list
        return categoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Creating variables for the GUI components
        private TextView txvCustomCategory_CategoryName;
        private ImageView imgCustomCategories_CategoryImage;
        ProgressBar progressCustomCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Assigning the GUI components to the created variables
            txvCustomCategory_CategoryName = itemView.findViewById(R.id.txvCustomCategory_CategoryName);
            imgCustomCategories_CategoryImage = itemView.findViewById(R.id.imgCustomCategories_CategoryImage);
            progressCustomCategory = itemView.findViewById(R.id.progressCustomCategory);

            //OnClick
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View categoriesView) {
                    // if the create category was clicked
                    if (getAdapterPosition() == 0) {
                        // Building the alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                        builder.setTitle("Create Category");
                        // Set up the input
                        final EditText input = new EditText(builder.getContext());
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);
                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //if use wants to create a category
                                String name = "";
                                //get input
                                name = input.getText().toString();
                                //validate , store and refresh
                                createCategory(name, builder.getContext());
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else {
                        // else to and view the category
                        Intent refreshAct = new Intent(categoriesView.getContext(), ViewCategory.class);
                        refreshAct.putExtra(CATEGORY_ID, categoriesList.get(getAdapterPosition()).getCategoryID());
                        categoriesView.getContext().startActivity(refreshAct);
                        // Creating the activity
                        Activity activity = (Activity)categoriesView.getContext();
                        activity.finish();
                    }
                }
            });
        }

        //write a category to db
        private void createCategory(String name, Context context) {
            //check if valid

            boolean isValid = Validation.isAlphanumeric(name);
            if (isValid) {
                // make a category
                progressCustomCategory.setVisibility(View.VISIBLE);
                // Creating a categories object
                Categories categories = new Categories();
                // Assigning values to the object
                categories.setCategoryID(Util.idGenerator());
                categories.setCategoryName(name);
                // Creating an instance of the FirebaseWriteWorker
                FirebaseWriteWorker firebaseWriteWorker = new FirebaseWriteWorker(CurrentUser.currentUserObj.getPersonId());
                //write category to db
                firebaseWriteWorker.WriteCategory(categories, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        //refresh
                        progressCustomCategory.setVisibility(View.INVISIBLE);
                        // Switching to the layout that displays all the categories
                        Intent refreshAct = new Intent(context, MainActivity.class);
                        context.startActivity(refreshAct);
                        Activity activity = (Activity) itemView.getContext();
                        activity.finish();
                    }
                });

            } else {
                // Notifying the user that details are valid
                Toast.makeText(context, "Name was invalid", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
