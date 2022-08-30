package com.varsitycollege.vinyl_warehouse.Music;

import java.util.List;

public class CoverLinks {
    private String coverId;
    private String coverLinks;

    public CoverLinks() {
    }

    public CoverLinks(String coverId, String coverLinks) {
        this.coverId = coverId;
        this.coverLinks = coverLinks;
    }

    public String getCoverId() {
        return coverId;
    }

    public void setCoverId(String coverId) {
        this.coverId = coverId;
    }

    public String getCoverLinks() {
        return coverLinks;
    }

    public void setCoverLinks(String coverLinks) {
        this.coverLinks = coverLinks;
    }
}
