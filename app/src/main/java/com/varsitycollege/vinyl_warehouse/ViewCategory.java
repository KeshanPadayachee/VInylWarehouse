package com.varsitycollege.vinyl_warehouse;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;
import com.varsitycollege.vinyl_warehouse.AdapterWorkers.CategoryAdapter;
import com.varsitycollege.vinyl_warehouse.DataWorkers.DataDeleteWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseReadWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseWriteWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.OnGetDataListener;
import com.varsitycollege.vinyl_warehouse.Music.Album;
import com.varsitycollege.vinyl_warehouse.Music.Categories;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;
import com.varsitycollege.vinyl_warehouse.Utils.Validation;

import java.util.ArrayList;
import java.util.List;

public class ViewCategory extends AppCompatActivity {

    // Declaring Constants
    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String MORE_ITEM = " more item";
    public static final String CONFIRMATION_DELETION = "Confirmation deletion";
    public static final String DELETE_THIS_CATEGORY = "Are you sure you want to delete this Category?";
    public static final String SUCCESSFULLY_DELETED = "Category successfully deleted";
    public static final String GOAL_NUMBER = "Set a goal number";
    public static final String SET_GOAL = "Set Goal";
    public static final String GOAL_SET = "Goal Set";
    public static final String PLEASE_TRY_AGAIN = "Incorrect value. Please try again";
    public static final String CANCEL = "Cancel";
    public static final String NUMERIC = "NUMERIC";

    // Creating variables to hold the GUI components
    TextView txvViewCategory_GoalLeft, txvViewCategory_Heading, txvViewCategory_Goal;
    ImageView imgViewCategory_Cover;
    RecyclerView recyclerViewCategory_Albums;
    List<Album> albumList;
    ProgressBar progressBar;
    FloatingActionButton floatingActionButton;
    ImageButton img_btn_Options, imgViewCategory_BackArrow;
    String categoryID;
    Categories category;
    ProgressBar progressViewCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        albumList = new ArrayList<>();

        //Connect Variables to Components
        progressViewCategory = findViewById(R.id.progressViewCategory);
        img_btn_Options = findViewById(R.id.img_btn_Options);
        progressBar = findViewById(R.id.progressBar);
        txvViewCategory_GoalLeft = findViewById(R.id.txvViewCategory_GoalLeft);
        txvViewCategory_Heading = findViewById(R.id.txvViewCategory_Heading);
        imgViewCategory_Cover = findViewById(R.id.imgViewCategory_Cover);
        recyclerViewCategory_Albums = findViewById(R.id.recyclerViewCategory_Albums);
        imgViewCategory_BackArrow = findViewById(R.id.imgViewCategory_BackArrow);
        floatingActionButton = findViewById(R.id.fabViewCategory_AddAlbum);
        txvViewCategory_Goal = findViewById(R.id.txvViewCategory_Goal);
        //Check and get for passed variables
        Bundle extras = getIntent().getExtras();
        if (extras.getString(CATEGORY_ID) == null) {
            // Switching Layouts if ID is not found
            Intent intent = new Intent(ViewCategory.this, IntroWelcome.class);
            startActivity(intent);
            finishAffinity();
        }

        // OnClickListener for the Back Arrow
        imgViewCategory_BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View backView) {
                // Switching screens
                Intent toAllCategories = new Intent(ViewCategory.this, AllCategories.class);
                startActivity(toAllCategories);
                finishAffinity();
            }
        });

        //Get category id
        categoryID = extras.getString(CATEGORY_ID);
        extras.remove(CATEGORY_ID);
        FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(CurrentUser.currentUserObj.getPersonId());
        img_btn_Options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Setting up the pop up menu
                PopupMenu popupMenu = new PopupMenu(ViewCategory.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_category, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    // OnClick
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            // Setting the goal
                            case R.id.itemMenuCategory_AddGoal:
                                firebaseReadWorker.getCategory(categoryID, new OnGetDataListener() {
                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        // Check if category has albums
                                        Categories categories = dataSnapshot.getValue(Categories.class);
                                        if (categories.getAlbumIDs() != null) {
                                            // Pop up box
                                            popUp();
                                        } else {
                                            Toast.makeText(ViewCategory.this, "Add an Album Before Setting Goal", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onStartWork() {
                                        // EMPTY METHOD NOT BEING USED
                                    }

                                    @Override
                                    public void onFailure() {
                                        // EMPTY METHOD NOT BEING USED
                                    }
                                });

                                return true;
                            case R.id.itemMenuCategory_Delete:
                                // Deleting the category
                                deleteCategory();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                // Showing the pop up menu
                popupMenu.show();
            }
        });

        // OnClick Listener for the floating action button
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // switching screens
                Intent allAlbumIntent = new Intent(view.getContext(), AllAlbums.class);
                allAlbumIntent.putExtra(CATEGORY_ID, categoryID);
                startActivity(allAlbumIntent);
            }
        });

        // Showing the progress dialog
        progressViewCategory.setVisibility(View.VISIBLE);
        floatingActionButton.setEnabled(false);
        //Read category from db

        firebaseReadWorker.getCategory(categoryID, new OnGetDataListener() {


            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //Save data
                Categories category2 = dataSnapshot.getValue(Categories.class);
                category = category2;

            }

            @Override
            public void onStartWork() {
                //set the heading
                //loadDialogBar.HideDialog();
                txvViewCategory_Heading.setText(category.getCategoryName());
                txvViewCategory_Goal.setText("Goal: " + category.getCategoryGoal());
                //calculate progress and setting progress bar values
                if (category.getCategoryGoal() != 0) {
                    if (category.getAlbumIDs() != null) {
                        txvViewCategory_GoalLeft.setVisibility(View.VISIBLE);
                        int numOfAlbumsLeft = category.getAlbumIDs().size() - category.getCategoryGoal();
                        progressBar.setMax(category.getCategoryGoal());
                        progressBar.setProgress(category.getCategoryGoal() - Math.abs(numOfAlbumsLeft));
                        txvViewCategory_GoalLeft.setText(Math.abs(numOfAlbumsLeft) + MORE_ITEM);
                    }

                    // Hiding the progress dialog
                }
                progressViewCategory.setVisibility(View.INVISIBLE);
                floatingActionButton.setEnabled(true);
                if (category.getAlbumIDs() != null) {
                    //prepare list for adapter
                    List<Album> albumList = new ArrayList<>();
                    if (CurrentUser.currentUserObj.getPersonId() == null) {
                        // switching layouts
                        Intent intent = new Intent(ViewCategory.this, IntroWelcome.class);
                        startActivity(intent);
                        finish();
                    }
                    final int[] count = {0};
                    // Creating firebase read worker object
                    FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(CurrentUser.currentUserObj.getPersonId());
                    for (String albumID : category.getAlbumIDs()) {
                        firebaseReadWorker.getAlbum(albumID, new OnGetDataListener() {
                            Album album;

                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                //get data
                                Album album1 = dataSnapshot.getValue(Album.class);
                                album = album1;
                                albumList.add(album);
                            }

                            @Override
                            public void onStartWork() {
                                //Add to list
                                // Hiding the progress dialog
                                progressViewCategory.setVisibility(View.INVISIBLE);
                                floatingActionButton.setEnabled(true);
                                if (CurrentUser.currentUserObj.getPersonId() == null) {
                                    // switching layouts
                                    Intent intent = new Intent(ViewCategory.this, IntroWelcome.class);
                                    startActivity(intent);
                                    finish();
                                }
                                count[0]++;
                                if (count[0] == albumList.size()) {
                                    recyclerManager(albumList);
                                }


                            }

                            @Override
                            public void onFailure() {
                                // Hiding the progress dialog
                                progressViewCategory.setVisibility(View.INVISIBLE);
                                // enabling the floating button
                                floatingActionButton.setEnabled(true);
                            }
                        });

                    }

                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (CurrentUser.currentUserObj.getPersonId() == null) {
            // Switching layouts - to intro
            Intent intent = new Intent(ViewCategory.this, IntroWelcome.class);
            startActivity(intent);
            finish();
        }
    }

    //Method to delete the selected category
    private void deleteCategory() {
        // Initializing the Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewCategory.this);
        builder.setTitle(CONFIRMATION_DELETION);
        builder.setMessage(DELETE_THIS_CATEGORY);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Showing the progress dialog
                progressViewCategory.setVisibility(View.VISIBLE);

                //constructor
                DataDeleteWorker dataDeleteWorker = new DataDeleteWorker(CurrentUser.currentUserObj.getPersonId());
                //calls the delete method
                dataDeleteWorker.deleteCategory(categoryID, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        //starts intent to move to next activity
                        // Hiding the progress dialog
                        progressViewCategory.setVisibility(View.INVISIBLE);

                        // Switching layouts
                        Intent intent = new Intent(ViewCategory.this, MainActivity.class);
                        Toast.makeText(ViewCategory.this, SUCCESSFULLY_DELETED, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Setting the alert dialog to no
                dialogInterface.dismiss();
            }
        });
        // Showing the dialog box
        builder.show();

    }

    // Method to handle the pop up menu
    private void popUp() {
        EditText category_Goal;
        //Code Attribution
        //Link: https://www.youtube.com/watch?v=bSWmajyG4zU
        //Author: ProgrammingWizards TV
        //End

        // Initializing the Alert Dialog
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(ViewCategory.this);
        builder.setMessage(GOAL_NUMBER);
        //builder.setIcon(R.id.icl)
        builder.setTitle(SET_GOAL);
        builder.setCancelable(true);

        // Dynamically creating an edit box
        category_Goal = new EditText(this);
        // Assigning edit box to the pop up menu
        builder.setView(category_Goal);
        builder.setPositiveButton(SET_GOAL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String inputGoal = category_Goal.getText().toString();
                // Validating if input is correct
                if (validate(inputGoal, NUMERIC)) {
                    // Showing the progress dialog
                    progressViewCategory.setVisibility(View.VISIBLE);
                    category.setCategoryGoal(Integer.parseInt(inputGoal));
                    // Creating an instance of the FirebaseWriteWorker class
                    FirebaseWriteWorker firebaseWriteWorker = new FirebaseWriteWorker(CurrentUser.currentUserObj.getPersonId());
                    firebaseWriteWorker.WriteCategory(category, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            // Hiding the progress dialog
                            progressViewCategory.setVisibility(View.INVISIBLE);
                            // Switching layouts
                            Intent refreshAct = new Intent(ViewCategory.this, ViewCategory.class);
                            refreshAct.putExtra(CATEGORY_ID, categoryID);
                            startActivity(refreshAct);
                            finishAffinity();
                            Toast.makeText(ViewCategory.this, GOAL_SET, Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    // Closes the dialog
                    dialogInterface.dismiss();
                    Toast.makeText(ViewCategory.this, PLEASE_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Closes the dialog
                dialogInterface.dismiss();
            }
        });

        // Displaying the alert dialog
        AlertDialog ad = builder.create();
        ad.show();

    }

    // Method to validate new details
    private boolean validate(String toString, String validateToo) {
        // Declaring boolean variable to check for validity
        boolean isValid = false;

        // Checking for null values
        if (!Validation.isNullOrEmpty(toString)) {
            switch (validateToo) {
                // Case for alphanumeric
                case NUMERIC: {
                    if (Validation.isNumOnly(toString)) {
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

    // Method to manage the recycler view
    public void recyclerManager(List<Album> albumList) {
        // Hiding the progress dialog
        progressViewCategory.setVisibility(View.INVISIBLE);
        // Enabling the floating button
        floatingActionButton.setEnabled(true);
        if (albumList != null) {
            if (albumList.get(0).getCoverLink() != null) {
                String categoryImage = albumList.get(0).getCoverLink();
                // Displaying the image
                Picasso.get().load(categoryImage).into(imgViewCategory_Cover);
            }
        }


        //new adapter
        CategoryAdapter adapter = new CategoryAdapter(albumList, category, this);
        recyclerViewCategory_Albums.setLayoutManager(new LinearLayoutManager(this));
        //start adapter
        recyclerViewCategory_Albums.setAdapter(adapter);
    }
}