package com.varsitycollege.vinyl_warehouse.Music;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
//class for Album data
public class Album {
    //declare attributes of an album
    private String userID;
    private String albumID;
    private String albumTitle;
    private List<String> genre;
    private List<String> artists;
    private Date dateOfRelease;
    private int rating;
    private String coverLink;

    //comparers to sort albums in mainActivity

    //A-Z comparer
    public static Comparator<Album> albumNameAZComparotor = new Comparator<Album>() {
        @Override
        public int compare(Album album1, Album album2) {
            return album1.getAlbumTitle().toUpperCase().compareTo(album2.getAlbumTitle().toUpperCase());
        }
    };
    //Z-A comparer
    public static Comparator<Album> albumNameZAComparotor = new Comparator<Album>() {
        @Override
        public int compare(Album album1, Album album2) {
            return album2.getAlbumTitle().toUpperCase().compareTo(album1.getAlbumTitle().toUpperCase());
        }
    };
   //Oldest to Newest comparer
    public static Comparator<Album> albumDateAscendingComparotor = new Comparator<Album>() {
        @Override
        public int compare(Album album1, Album album2) {
            return  album1.getDateOfRelease().getYear()- album2.getDateOfRelease().getYear();

        }

    };

    //newest to oldest comparer
    public static Comparator<Album> albumDateDescendingComparotor = new Comparator<Album>() {
        @Override
        public int compare(Album album1, Album album2) {
            return  album2.getDateOfRelease().getYear()- album1.getDateOfRelease().getYear();

        }

    };



    //"default" constructor
    public Album() {

    }
    //constructor with 1 attribute
    public Album(String userID) {
        this.userID = userID;
    }

   //Album parameterized constructor for all attributes
    public Album(String userID, String albumID, String albumTitle, List<String> genre, List<String> artists, Date dateOfRelease, int rating, String coverLink) {
        this.userID = userID;
        this.albumID = albumID;
        this.albumTitle = albumTitle;
        this.genre = genre;
        this.artists = artists;
        this.dateOfRelease = dateOfRelease;
        this.rating = rating;
        this.coverLink = coverLink;

    }
    //Album parameterized constructor for 7 attributes
    public Album(String userID, String albumID, String albumTitle, List<String> genre, List<String> artists, Date dateOfRelease, int rating) {
        this.userID = userID;
        this.albumID = albumID;
        this.albumTitle = albumTitle;
        this.genre = genre;
        this.artists = artists;
        this.dateOfRelease = dateOfRelease;
        this.rating = rating;

    }

    //method to check if Album contains a specific artist who made it. Used for filter in main activity
    public boolean containsArtist(String artistName)
    {
        boolean result = false;
        for (String artist : artists) {
            if (artist.toUpperCase().contains(artistName.toLowerCase())) {
                result = true;
                break;
            }
        }
        return result;
    }

    //method to check if Album contains a specific genre category. Used for filter in main activity
    public boolean containsGenre(String genreName)
    {
        boolean result = false;
        for (String gen : genre) {
            if (genreName.equalsIgnoreCase(gen))
            {
                result = true;
                break;
            }
        }
        return result;
    }

    //album getters and setters


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> musician) {
        this.artists = musician;
    }

    public Date getDateOfRelease() {
        return dateOfRelease;
    }

    public void setDateOfRelease(Date dateOfRelease) {
        this.dateOfRelease = dateOfRelease;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getCoverLink() {
        return coverLink;
    }

    public void setCoverLink(String coverLink) {
        this.coverLink = coverLink;
    }

}
