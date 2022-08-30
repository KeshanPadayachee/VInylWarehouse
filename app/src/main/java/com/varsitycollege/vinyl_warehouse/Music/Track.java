package com.varsitycollege.vinyl_warehouse.Music;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
//class for a Track of an album
public class Track implements Serializable {
    //Declare attributes of a track
    private String albumID;
    private String trackID;
    private String trackTitle;
    private List<String> genre;
    private List<String> artists;
    private Date dateOfRelease;
    private String coverLink;
    //comparers to sort track in mainActivity

    //A-Z comparer
    public static Comparator<Track> albumNameAZComparotor = new Comparator<Track>() {
        @Override
        public int compare(Track track1, Track track2) {
            return track1.getTrackTitle().toUpperCase().compareTo(track2.getTrackTitle().toUpperCase());
        }
    };
    //Z-A comparer
    public static Comparator<Track> albumNameZAComparotor = new Comparator<Track>() {
        @Override
        public int compare(Track track1, Track track2) {
            return track2.getTrackTitle().toUpperCase().compareTo(track1.getTrackTitle().toUpperCase());
        }
    };
    //Oldest to Newest comparer
    public static Comparator<Track> albumDateAscendingComparotor = new Comparator<Track>() {
        @Override
        public int compare(Track track1, Track track2) {
            return  track1.getDateOfRelease().getYear()- track2.getDateOfRelease().getYear();

        }

    };

    //newest to oldest comparer
    public static Comparator<Track> albumDateDescendingComparotor = new Comparator<Track>() {
        @Override
        public int compare(Track track1, Track track2) {
            return  track1.getDateOfRelease().getYear()- track2.getDateOfRelease().getYear();

        }

    };



    //"Default" costructor for track
    public Track() {
     // EMPTY CONSTRUCTOR. DO NOT REMOVE
    }

    //parameterized constructor for all attributes
    public Track(String albumID, String trackID, String trackTitle, List<String> genre, List<String> artists, Date dateOfRelease, String coverLink) {
        this.albumID = albumID;
        this.trackID = trackID;
        this.trackTitle = trackTitle;
        this.genre = genre;
        this.artists = artists;
        this.dateOfRelease = dateOfRelease;
        this.coverLink = coverLink;
    }
    //parameterized constructor for 6 attributes
    public Track(String albumID, String trackID, String trackTitle, List<String> genre, List<String> artists, Date dateOfRelease) {
        this.albumID = albumID;
        this.trackID = trackID;
        this.trackTitle = trackTitle;
        this.genre = genre;
        this.artists = artists;
        this.dateOfRelease = dateOfRelease;
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
    //Track getters and setters
    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getTrackID() {
        return trackID;
    }

    public void setTrackID(String trackID) {
        this.trackID = trackID;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
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

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public Date getDateOfRelease() {
        return dateOfRelease;
    }

    public void setDateOfRelease(Date dateOfRelease) {
        this.dateOfRelease = dateOfRelease;
    }

    public String getCoverLink() {
        return coverLink;
    }

    public void setCoverLink(String coverLink) {
        this.coverLink = coverLink;
    }
}
