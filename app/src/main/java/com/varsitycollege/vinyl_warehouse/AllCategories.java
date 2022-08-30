package com.varsitycollege.vinyl_warehouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.varsitycollege.vinyl_warehouse.AdapterWorkers.CategoriesAdapter;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseReadWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.OnGetDataListener;
import com.varsitycollege.vinyl_warehouse.Music.Categories;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;

import java.util.ArrayList;
import java.util.List;

public class AllCategories extends AppCompatActivity {

    // List variable to store the categories
    private List<Categories> categoriesList;

    public static final String CREATE_CATEGORY = "Create Category";
    public static final String CREATE_CATEGORY_IMAGE = "https://firebasestorage.googleapis.com/v0/b/farmcentral-v1.appspot.com/o/createcatgory.png?alt=media&token=e76a9697-8407-4f30-a6d8-7aa969c8e015";

    // Creating variables for the GUI components
    ProgressBar progressAllCategories;
    private RecyclerView recyclerAllCategories_Categories;
    private ImageView imgBackArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_catgories);
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        categoriesList = new ArrayList<>();
        //Connect Variables to Components
        recyclerAllCategories_Categories = findViewById(R.id.recyclerAllCategories_Categories);
        imgBackArrow = findViewById(R.id.imgAllCategories_BackArrow);
        progressAllCategories = findViewById(R.id.progressAllCategories);
        // Showing the progress dialog
        progressAllCategories.setVisibility(View.VISIBLE);
        //Read all categories from db
        FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(CurrentUser.currentUserObj.getPersonId());
        firebaseReadWorker.getAllCategory(new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                // Creates category object and gets object
                Categories category = dataSnapshot.getValue(Categories.class);
                categoriesList.add(category);
            }

            //start the adapter to make display
            @Override
            public void onStartWork() {
                // Hiding the progress dialog
                progressAllCategories.setVisibility(View.INVISIBLE);
                recyclerManager();
            }

            @Override
            public void onFailure() {
                // Empty method, not being used
            }
        });

        imgBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switching layouts
                Intent toMainActivityIntent = new Intent(AllCategories.this, MainActivity.class);
                startActivity(toMainActivityIntent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (CurrentUser.currentUserObj.getPersonId() == null) {
            // Moves to intro if user not found
            Intent introWelcomeIntent = new Intent(AllCategories.this, IntroWelcome.class);
            startActivity(introWelcomeIntent);
            finish();
        }
    }

    public void recyclerManager() {
        //Make the 1st object a CREATE category to make categories and add it to list
        Categories createCategory = new Categories();
        //Add details
        createCategory.setCategoryID(CREATE_CATEGORY);
        createCategory.setCategoryName(CREATE_CATEGORY_IMAGE);
        this.categoriesList.add(0, createCategory);
        //new adapter
        CategoriesAdapter adapter = new CategoriesAdapter(categoriesList, this);
        // the layout of the recycler view
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerAllCategories_Categories.setLayoutManager(layoutManager);
        //Start adapter
        recyclerAllCategories_Categories.setAdapter(adapter);
    }
}