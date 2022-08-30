package com.varsitycollege.vinyl_warehouse;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;
import com.varsitycollege.vinyl_warehouse.AdapterWorkers.AlbumAdapter;
import com.varsitycollege.vinyl_warehouse.AdapterWorkers.TrackAdapter;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseReadWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.OnGetDataListener;
import com.varsitycollege.vinyl_warehouse.Music.Album;
import com.varsitycollege.vinyl_warehouse.Music.Track;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;
import com.varsitycollege.vinyl_warehouse.UserDetails.UserDetails;
import com.varsitycollege.vinyl_warehouse.Utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;


import jp.wasabeef.transformers.picasso.BlurTransformation;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // Declaring constants
    public static final String NEWEST_FIRST = "Newest First";
    public static final String OLDEST_FIRST = "Oldest First";
    public static final String A_Z = "A-Z";
    public static final String Z_A = "Z-A";
    //declare all relevant UI Components
    RecyclerView recycler_Albums;
    FloatingActionButton fab_MainActivity;
    AlbumAdapter albumAdapter;
    Button btnCategories;
    ImageView btnNav;
    SearchView sv_SearchAlbum;
    ProgressBar progressMain;
    Spinner spinner_Filter;
    //list of albums to store users albums
    List<Album> albumList;
    private FirebaseAuth mAuth;

    //  Button btnNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        recycler_Albums = findViewById(R.id.recyclerMainActivity_Albums);
        fab_MainActivity = findViewById(R.id.fabMainActivity_AddAlbum);
        progressMain = findViewById(R.id.progressMain);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        // Assigning the GUI components to the variables
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View v = navigationView.getHeaderView(0);
        TextView txvProfilename = v.findViewById(R.id.name);

        // Setting data for components
        txvProfilename.setText(CurrentUser.currentUserObj.getPersonName());
        TextView txvProfileEmail = v.findViewById(R.id.email);
        txvProfileEmail.setText(CurrentUser.currentUserObj.getPersonEmail());
        ImageView profile = v.findViewById(R.id.profilepic);
        ImageView backgroundprofile = v.findViewById(R.id.backgroundprofilepic);
        Picasso.get().load(CurrentUser.currentUserObj.getPersonPhoto()).into(profile);
        Picasso.get().load(CurrentUser.currentUserObj.getPersonPhoto()).transform(new BlurTransformation(MainActivity.this, 25, 1))
                .into(backgroundprofile);
        sv_SearchAlbum = (SearchView) findViewById(R.id.searchViewMainActivity_Search); // inititate a search view

        btnCategories = findViewById(R.id.btnMainActivity_Categories);
        btnNav = findViewById(R.id.btnNav);

        recyclerManager();
        //open add new album activity when button clicked
        fab_MainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switching layouts
                Intent i = new Intent(view.getContext(), AddNewAlbum.class);
                startActivity(i);
                finish();
            }
        });
        // Getting authorisation from firebase
        mAuth = FirebaseAuth.getInstance();
        btnNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Opens the side menu
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        //open categories activity when button clicked
        btnCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switching layouts
                Intent goToCategories = new Intent(view.getContext(), AllCategories.class);
                startActivity(goToCategories);
                finish();
            }
        });
        // Checking which item the user selected in menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                // Gets selected item
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id) {

                    // Handling which item is selected
                    case R.id.nav_exit:
                        // Showing the progress dialog
                        progressMain.setVisibility(View.VISIBLE);
                        CurrentUser.currentUserObj = new UserDetails();
                        mAuth.signOut();
                        // Switching layouts
                        Intent signOutAccountIntent = new Intent(MainActivity.this, IntroWelcome.class);
                        startActivity(signOutAccountIntent);
                        // Hiding the progress dialog
                        progressMain.setVisibility(View.INVISIBLE);
                        finish();
                        break;

                    case R.id.nav_account:
                        // Switching layouts
                        Intent accountIntent = new Intent(MainActivity.this, MyAccount.class);
                        startActivity(accountIntent);
                        finish();
                        break;
                    case R.id.nav_badges:
                        // Switching layouts
                        Intent badgesIntent = new Intent(MainActivity.this, Badges.class);
                        startActivity(badgesIntent);
                        finish();
                        break;
                    case R.id.nav_help:
                        //Go to Tutorial activity
                        Intent i = new Intent(MainActivity.this, FrequentlyAskedQuestions.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.nav_graph:
                        //Go to Tutorial activity
                        Intent graphIntent = new Intent(MainActivity.this, Graph.class);
                        startActivity(graphIntent);
                        finish();
                        break;
                    case R.id.nav_twitter:
                        //Go to Tutorial activity
                        Intent browserIntentTwitter = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/MemberVinyl"));
                        startActivity(browserIntentTwitter);
                        finish();
                        break;
                    case R.id.nav_instagram:
                        //Go to Tutorial activity
                        Intent browserIntentInstagram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/vinyl_warehouse_/?hl=en"));
                        startActivity(browserIntentInstagram);
                        finish();
                        break;
                    default:
                        return true;

                }
                return true;
            }
        });

        //get sort spinner to add options to it
        Spinner spinner_sort = (Spinner) findViewById(R.id.spinnerMainActivity_Sort);
        spinner_Filter = findViewById(R.id.spinnerMainActivity_Filter);
        // Create an ArrayAdapter using the sort options array and a default spinner layout
        ArrayAdapter<CharSequence> sort_spinner_adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        sort_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner


        spinner_sort.setAdapter(sort_spinner_adapter);
        //Code Attribution
        //Link: https://developer.android.com/guide/topics/ui/controls/spinner
        //Author: developers.android.com
        //End
        spinner_sort.setOnItemSelectedListener(this);
        spinner_Filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                String genre = spinner_Filter.getSelectedItem() + "";
                filterAlbums(genre, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Empty method, not being used
            }
        });

        // perform set on query text listener event
        sv_SearchAlbum.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // General search
            @Override
            public boolean onQueryTextChange(String query) {
                //update album adapter dynamically as text in search view is changed
                if (query != null && query.length() > 0) //if text is not null
                {
                    // Check if search is greater than 6
                    if (query.length() > 6) {
                        // Checks whether user is searching for a track
                        if (query.substring(0, 6).equals("track:")) {
                            // Reading all tracks
                            List<Track> trackList = new ArrayList<>();
                            FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(CurrentUser.currentUserObj.getPersonId());
                            firebaseReadWorker.getAllTrack(new OnGetDataListener() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    // Storing the tracks
                                    Track track = new Track();
                                    track = dataSnapshot.getValue(Track.class);
                                    trackList.add(track);
                                }

                                // Filter the track being searched
                                @Override
                                public void onStartWork() {
                                    List<Track> filteredTrack = new ArrayList<>();
                                    if (trackList != null) {
                                        String queryMain = query.substring(7);
                                        for (Track track : trackList) {
                                            // Filtering
                                            if (track.getTrackTitle().toLowerCase().contains(queryMain.toLowerCase()) || track.containsArtist(queryMain)) {
                                                filteredTrack.add(track);
                                            }
                                        }
                                        // Setting the adapter to recycler
                                        TrackAdapter trackAdapter = new TrackAdapter(filteredTrack, MainActivity.this);
                                        recycler_Albums.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                        //start adapter
                                        recycler_Albums.setAdapter(trackAdapter);
                                    }
                                }

                                @Override
                                public void onFailure() {
                                // EMPTY METHOD NOT BEING USED
                                }
                            });
                        }
                    }

                    //filter searched albums into new list
                    List<Album> filteredAlbums = new ArrayList<Album>();
                    for (Album album : albumList) {
                        //check if query matches an album title or artist in the list of albums
                        if (album.getAlbumTitle().toLowerCase().contains(query.toLowerCase()) || album.containsArtist(query)) {

                            filteredAlbums.add(album);
                        }
                    }
                    //set filtered searched albums into new adapter
                    albumAdapter = new AlbumAdapter(filteredAlbums, MainActivity.this);
                    recycler_Albums.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    //start adapter
                    recycler_Albums.setAdapter(albumAdapter);

                } else {
                    //show all albums if there is no search
                    albumAdapter = new AlbumAdapter(albumList, MainActivity.this);
                    recycler_Albums.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    //start adapter
                    recycler_Albums.setAdapter(albumAdapter);
                }


                return false;
            }
        });
        //Code Attribution
        //Link: https://codewithap.com/searchview-in-android-studio-latest-version-2020/
        //Author: Akash Kumar
        //End

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (CurrentUser.currentUserObj.getPersonId() == null) {
            // Switching layouts
            Intent intent = new Intent(MainActivity.this, IntroWelcome.class);
            startActivity(intent);
            finish();
        }
    }

    public void filterAlbums(String genre, int position) {
        //filter albums from specified genre into new list and set adapter
        //position less than 0 does not exist so check for error or if all,index 0, is selected, load all albums
        if (position > 0) {
            List<Album> filteredAlbums = new ArrayList<Album>();
            for (Album album : albumList) {
                //check if query matches an album title or artist in the list of albums
                if (album.containsGenre(genre)) {
                    filteredAlbums.add(album);
                }
            }
            albumAdapter = new AlbumAdapter(filteredAlbums, MainActivity.this);

        } else {
            albumAdapter = new AlbumAdapter(albumList, MainActivity.this);
        }

        recycler_Albums.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        //start adapter
        recycler_Albums.setAdapter(albumAdapter);

    }

    public void populateFilter(List<Album> albums) {
        //populate album filter with all genres
        List<String> filterOptions = new ArrayList<>();
        filterOptions.add("All Genres");
        for (Album album : albums) {
            filterOptions.addAll(album.getGenre());
        }
        //filter all genres to eliminate duplicates
        //Code Attribution
        //Link: https://stackoverflow.com/questions/2235471/save-a-list-of-unique-strings-in-the-arraylist
        //Author: Fabian Steeg
        //End

        List<String> uniqueGenres = new ArrayList<String>(new LinkedHashSet<String>(filterOptions));
        //make array adapter with genres
        ArrayAdapter<String> filter_spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, uniqueGenres);
        spinner_Filter.setAdapter(filter_spinner_adapter);
    }

    public void recyclerManager() {

        // Showing the loading dialog
        progressMain.setVisibility(View.VISIBLE);
        albumList = new ArrayList<>();
        FirebaseReadWorker.getAllAlbums(CurrentUser.currentUserObj.getPersonId(), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //add to list
                Album album = dataSnapshot.getValue(Album.class);
                albumList.add(album);
            }

            @Override
            public void onStartWork() {
                if (albumList.size() > 0) {
                    //enable button
                    btnCategories.setVisibility(View.VISIBLE);
                    //Start adapter
                    albumAdapter = new AlbumAdapter(albumList, MainActivity.this);
                    populateFilter(albumList);
                    recycler_Albums.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    //start adapter
                    recycler_Albums.setAdapter(albumAdapter);
                }
                progressMain.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure() {
                // Empty method, not being used
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String filter_choice = adapterView.getItemAtPosition(i).toString();

        //Code Attribution
        //Link: https://www.geeksforgeeks.org/how-to-sort-an-arraylist-of-objects-by-property-in-java/
        //Author: GeeksforGeeks
        //End

        //checks choice and applies the sorting check
        switch (filter_choice) {
            // Sorts newest first
            case NEWEST_FIRST:

                Collections.sort(albumList,
                        Album.albumDateDescendingComparotor);


                break;
            // Sorts oldest first
            case OLDEST_FIRST:
                Collections.sort(albumList,
                        Album.albumDateAscendingComparotor);
                break;
            // Sorts A-Z
            case A_Z:
                Collections.sort(albumList,
                        Album.albumNameAZComparotor);
                break;
            // Sorts Z-A
            case Z_A:
                Collections.sort(albumList,
                        Album.albumNameZAComparotor);
                break;
        }
        //resets the adapters and recycler
        albumAdapter = new AlbumAdapter(albumList, MainActivity.this);
        recycler_Albums.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        //start adapter
        recycler_Albums.setAdapter(albumAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Empty method. not being used
    }
}