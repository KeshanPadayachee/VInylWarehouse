package com.varsitycollege.vinyl_warehouse.DataWorkers;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseDeleteWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseReadWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseStorageWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseWriteWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.OnGetDataListener;
import com.varsitycollege.vinyl_warehouse.Music.Album;
import com.varsitycollege.vinyl_warehouse.Music.Categories;
import com.varsitycollege.vinyl_warehouse.Music.CoverLinks;
import com.varsitycollege.vinyl_warehouse.Music.Track;

import java.util.ArrayList;
import java.util.List;

public class DataDeleteWorker {
    // Creating a variable to store the users id
    private String userID;

    //Constructor
    public DataDeleteWorker(String userID) {
        this.userID = userID;
    }

    //Method to delete the category
    public void deleteCategory(String categoryID, OnSuccessListener listener) {
        try {
            // Creating an instance of the FirebaseReadWorker class
            FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(userID);
            //Get album data
            firebaseReadWorker.getCategory(categoryID, new OnGetDataListener() {
                Categories categories;

                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    //return of object
                    categories = dataSnapshot.getValue(Categories.class);
                }

                @Override
                public void onStartWork() {
                    //delete category
                    FirebaseDeleteWorker firebaseDeleteWorker = new FirebaseDeleteWorker(userID);
                    firebaseDeleteWorker.deleteCategory(categories, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            listener.onSuccess(true);
                        }
                    });
                }

                @Override
                public void onFailure() {
                    // Empty method, not being used
                }
            });
        } catch (Exception deleteException) {
            Log.d(TAG, "deleteCategory: " + deleteException.getMessage());
        }
    }

    //Delete all tracks related to album 1st, then the albums in the category then delete the album
    public void deleteAlbum(String albumID, boolean isDeleteAccount, OnSuccessListener onSuccessListener) {
        try {
            // Creating an instance of the FirebaseReadWorker class
            FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(userID);
            //Get album data
            firebaseReadWorker.getAlbum(albumID, new OnGetDataListener() {

                // Creating a album variable
                Album album;

                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    //return of object
                    album = dataSnapshot.getValue(Album.class);
                }

                //Once album data is retrieved start to delete related tracks
                @Override
                public void onStartWork() {
                    // Creating a list to hold the tracks
                    List<Track> trackList = new ArrayList<>();

                    //Get all tracks in album
                    firebaseReadWorker.getTracks(albumID, new OnGetDataListener() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            //return of object
                            Track track = dataSnapshot.getValue(Track.class);
                            //store in list
                            trackList.add(track);
                        }

                        //Once all the tracks are retrieved for that album delete each one
                        @Override
                        public void onStartWork() {
                            //Check if there is any tracks
                            if (trackList.size() > 0) {
                                FirebaseDeleteWorker firebaseDeleteWorker = new FirebaseDeleteWorker(userID);
                                for (Track t : trackList) {
                                    //Check if track cover is equal to album cover and if it is remove the
                                    // album cover link so the method does not delete it before the album is deleted
                                    if (t.getCoverLink().equals(album.getCoverLink())) {
                                        t.setCoverLink("");
                                    }
                                    //delete track
                                    firebaseDeleteWorker.deleteTrack(t, new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {

                                        }
                                    });
                                }

                            }
                            //check if delete account is calling the method
                            // if (!isDeleteAccount) {
                            //Delete album from category
                            //get all categories
                            List<Categories> categoriesList = new ArrayList<>();
                            firebaseReadWorker.getAllCategory(new OnGetDataListener() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    //return of object
                                    Categories categories = dataSnapshot.getValue(Categories.class);
                                    //add category to list
                                    categoriesList.add(categories);
                                }

                                @Override
                                public void onStartWork() {
                                    //remove album from category
                                    FirebaseDeleteWorker firebaseDeleteWorker = new FirebaseDeleteWorker(userID);
                                    //check if there is categories
                                    if (categoriesList.size() > 0) {
                                        //loop each category
                                        for (Categories categories : categoriesList) {
                                            if (categories.getAlbumIDs().size() > 0) {
                                                //get all the albums ids in category
                                                List<String> albumList = new ArrayList<>();
                                                albumList = categories.getAlbumIDs();
                                                List<String> newAlbumListExcAlbum = new ArrayList<>();
                                                //check if album to be deleted id's is in category albumId's list
                                                for (String albumID : albumList) {
                                                    //check if id's match
                                                    if (!albumID.equals(album.getAlbumID())) {
                                                        //exclude the id's if found
                                                        newAlbumListExcAlbum.add(albumID);
                                                    }

                                                }
                                                //if any change was made then write the updated category
                                                if (albumList.size() != newAlbumListExcAlbum.size()) {
                                                    // Creating an instance of the FirebaseWriteWorker class
                                                    FirebaseWriteWorker firebaseWriteWorker = new FirebaseWriteWorker(album.getUserID());
                                                    categories.setAlbumIDs(newAlbumListExcAlbum);
                                                    firebaseWriteWorker.WriteCategory(categories, new OnSuccessListener() {
                                                        @Override
                                                        public void onSuccess(Object o) {
                                                            // Empty method, not being used
                                                        }
                                                    });
                                                }

                                            }

                                        }
                                    }

                                    //Delete album
                                    firebaseDeleteWorker.deleteAlbum(album, new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            if (o.toString().equals("true")) {
                                                onSuccessListener.onSuccess(true);
                                            }

                                        }
                                    });
                                }

                                @Override
                                public void onFailure() {
                                    // Empty method, not being used
                                }
                            });

                        }

                        @Override
                        public void onFailure() {
                            // Empty method, not being used
                        }
                    });

                }

                @Override
                public void onFailure() {
                    // Empty method, not being used
                }
            });


        } catch (Exception deleteException) {
            Log.d(TAG, "deleteAlbum: " + deleteException.getMessage());
        }
    }

    public void deleteTrack(Track track, OnSuccessListener onSuccessListener) {
        try {
            //get album
            FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(userID);
            firebaseReadWorker.getAlbum(track.getAlbumID(), new OnGetDataListener() {
                Album album = new Album();

                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    //return of object
                    album = dataSnapshot.getValue(Album.class);
                }

                @Override
                public void onStartWork() {
                    //check if album cover is track cover
                    if (track.getCoverLink().equals(album.getCoverLink())) {
                        //remove album cover
                        track.setCoverLink("");
                    }
                    if (track.getCoverLink().contains("e-cdns-images.dzcdn.net") || track.getCoverLink().contains("api.deezer.com")) {
                        track.setCoverLink("");
                    }
                    if (track.getCoverLink() != "") {
                        deleteLink(track.getCoverLink(), new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                FirebaseDeleteWorker firebaseDeleteWorker = new FirebaseDeleteWorker(userID);
                                firebaseDeleteWorker.deleteTrack(track, new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        onSuccessListener.onSuccess(true);
                                    }
                                });
                            }
                        });
                    } else {
                        //delete album
                        FirebaseDeleteWorker firebaseDeleteWorker = new FirebaseDeleteWorker(userID);
                        firebaseDeleteWorker.deleteTrack(track, new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                onSuccessListener.onSuccess(true);
                            }
                        });
                    }

                }

                @Override
                public void onFailure() {
                    // Empty method, not being used
                }
            });


        } catch (Exception deleteException) {
            // Catching the exception
            Log.d(TAG, "deleteTrack: " + deleteException.getMessage());
        }
    }

    //method to delete everything
    public void deleteAll(OnSuccessListener listener) {
        try {
            //creates a list of all covers
            List<CoverLinks> coverLinks = new ArrayList<>();
            //creates read worker obj
            FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(userID);
            //populates  all cover links from db
            firebaseReadWorker.getAllLinks(new OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    CoverLinks links1 = dataSnapshot.getValue(CoverLinks.class);
                    //adds the coverlinks to the db
                    coverLinks.add(links1);
                }

                @Override
                public void onStartWork() {
                    //checks if null
                    if (coverLinks != null) {
                        //loops through all the cover links to delete all those in the list
                        for (CoverLinks cover : coverLinks) {
                            //creates firebase worker obj and assigns it to the current user
                            FirebaseStorageWorker firebaseStorageWorker = new FirebaseStorageWorker(userID);
                            //calls the delete image method
                            firebaseStorageWorker.deleteImage(cover.getCoverLinks(), new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                }
                            });
                        }listener.onSuccess(true);
                    }

                }

                @Override
                public void onFailure() {

                }
            });
            //catches any errors and logs if breaks
        } catch (Exception deleteException) {
            // Catching the exception
            Log.d(TAG, "deleteAll: " + deleteException.getMessage());
        }
    }
    //method to delete the cover link
    public void deleteLink(String cover, OnSuccessListener listener) {
        //creates a list of type cover link
        List<CoverLinks> links = new ArrayList<>();
        try {
            //creates read worker objects
            FirebaseReadWorker firebaseReadWorker = new FirebaseReadWorker(userID);
            //gets all links from the readworker obj methods of that user id
            firebaseReadWorker.getAllLinks(new OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    //creates and stores cover link object
                    CoverLinks links1 = new CoverLinks();
                    //stores of the object from the db
                    links1 = dataSnapshot.getValue(CoverLinks.class);
                    //checks if the coverlink from the db matches the one being deleted
                    if (links1.getCoverLinks().equals(cover)) {
                        //creates a delete worker object
                        FirebaseDeleteWorker firebaseDeleteWorker = new FirebaseDeleteWorker(userID);
                        //calls the method and deletes the link
                        firebaseDeleteWorker.deleteLink(links1, new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                listener.onSuccess(true);
                            }
                        });
                    }

                }

                @Override
                public void onStartWork() {
                //unused method
                }

                @Override
                public void onFailure() {
                //unused method
                }
            });
        } catch (Exception deleteException) {
            // Catching the exception
            Log.d(TAG, "deleteLink: " + deleteException.getMessage());
        }

    }
}
