package com.varsitycollege.vinyl_warehouse.Music;

import java.util.List;
//class to store a users categories
public class Categories {

    //declare attributes of a category
    private String categoryID;
    private String categoryName;
    private int categoryGoal;
    private List<String> albumIDs;

    //"default" constructor for a category
    public Categories() {
    }
    //parameterized constructor for all attributes
    public Categories(String categoryID, String categoryName, int categoryGoal, List<String> albumIDs) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categoryGoal = categoryGoal;
        this.albumIDs = albumIDs;
    }

    //getters and setters for category
    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryGoal() {
        return categoryGoal;
    }

    public void setCategoryGoal(int categoryGoal) {
        this.categoryGoal = categoryGoal;
    }

    public List<String> getAlbumIDs() {
        return albumIDs;
    }

    public void setAlbumIDs(List<String> albumIDs) {
        this.albumIDs = albumIDs;
    }
}
