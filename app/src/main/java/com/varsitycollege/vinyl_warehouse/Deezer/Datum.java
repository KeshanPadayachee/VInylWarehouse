
package com.varsitycollege.vinyl_warehouse.Deezer;
//imports
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;
//declares the json format
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "readable",
    "title",
    "title_short",
    "title_version",
    "link",
    "duration",
    "rank",
    "explicit_lyrics",
    "explicit_content_lyrics",
    "explicit_content_cover",
    "preview",
    "md5_image",
    "artist",
    "album",
    "type"
})

public class Datum {
    //declares variables and assigns it to the json property
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("readable")
    private Boolean readable;
    @JsonProperty("title")
    private String title;
    @JsonProperty("title_short")
    private String titleShort;
    @JsonProperty("title_version")
    private String titleVersion;
    @JsonProperty("link")
    private String link;
    @JsonProperty("duration")
    private Integer duration;
    @JsonProperty("rank")
    private Integer rank;
    @JsonProperty("explicit_lyrics")
    private Boolean explicitLyrics;
    @JsonProperty("explicit_content_lyrics")
    private Integer explicitContentLyrics;
    @JsonProperty("explicit_content_cover")
    private Integer explicitContentCover;
    @JsonProperty("preview")
    private String preview;
    @JsonProperty("md5_image")
    private String md5Image;
    @JsonProperty("artist")
    private Artist artist;
    @JsonProperty("album")
    private Album album;
    @JsonProperty("type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    //getters and setters for ID
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }
    //getters and setters for readable
    @JsonProperty("readable")
    public Boolean getReadable() {
        return readable;
    }

    @JsonProperty("readable")
    public void setReadable(Boolean readable) {
        this.readable = readable;
    }
    //getters and setters for title
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }
    //getters and setters for the short title
    @JsonProperty("title_short")
    public String getTitleShort() {
        return titleShort;
    }

    @JsonProperty("title_short")
    public void setTitleShort(String titleShort) {
        this.titleShort = titleShort;
    }
    //getters and setters for title version
    @JsonProperty("title_version")
    public String getTitleVersion() {
        return titleVersion;
    }

    @JsonProperty("title_version")
    public void setTitleVersion(String titleVersion) {
        this.titleVersion = titleVersion;
    }
    //getters and setters for the link
    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(String link) {
        this.link = link;
    }
    //getters and setters for duration
    @JsonProperty("duration")
    public Integer getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    //getters and setters for rank
    @JsonProperty("rank")
    public Integer getRank() {
        return rank;
    }

    @JsonProperty("rank")
    public void setRank(Integer rank) {
        this.rank = rank;
    }
    //getters and setters for lyrics
    @JsonProperty("explicit_lyrics")
    public Boolean getExplicitLyrics() {
        return explicitLyrics;
    }

    @JsonProperty("explicit_lyrics")
    public void setExplicitLyrics(Boolean explicitLyrics) {
        this.explicitLyrics = explicitLyrics;
    }
    //getters and setters for lyrics explicit
    @JsonProperty("explicit_content_lyrics")
    public Integer getExplicitContentLyrics() {
        return explicitContentLyrics;
    }

    @JsonProperty("explicit_content_lyrics")
    public void setExplicitContentLyrics(Integer explicitContentLyrics) {
        this.explicitContentLyrics = explicitContentLyrics;
    }
    //getters and setters for explicit cover
    @JsonProperty("explicit_content_cover")
    public Integer getExplicitContentCover() {
        return explicitContentCover;
    }

    @JsonProperty("explicit_content_cover")
    public void setExplicitContentCover(Integer explicitContentCover) {
        this.explicitContentCover = explicitContentCover;
    }
    //getters and setters for preview
    @JsonProperty("preview")
    public String getPreview() {
        return preview;
    }

    @JsonProperty("preview")
    public void setPreview(String preview) {
        this.preview = preview;
    }
    //getters and setters for image
    @JsonProperty("md5_image")
    public String getMd5Image() {
        return md5Image;
    }

    @JsonProperty("md5_image")
    public void setMd5Image(String md5Image) {
        this.md5Image = md5Image;
    }
    //getters and setters for artists
    @JsonProperty("artist")
    public Artist getArtist() {
        return artist;
    }

    @JsonProperty("artist")
    public void setArtist(Artist artist) {
        this.artist = artist;
    }
    //getters and setters for album
    @JsonProperty("album")
    public Album getAlbum() {
        return album;
    }

    @JsonProperty("album")
    public void setAlbum(Album album) {
        this.album = album;
    }
    //getters and setters for type
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }
    //getters and setters for any other info
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
    //merges all variables into a string format
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Datum.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("readable");
        sb.append('=');
        sb.append(((this.readable == null)?"<null>":this.readable));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null)?"<null>":this.title));
        sb.append(',');
        sb.append("titleShort");
        sb.append('=');
        sb.append(((this.titleShort == null)?"<null>":this.titleShort));
        sb.append(',');
        sb.append("titleVersion");
        sb.append('=');
        sb.append(((this.titleVersion == null)?"<null>":this.titleVersion));
        sb.append(',');
        sb.append("link");
        sb.append('=');
        sb.append(((this.link == null)?"<null>":this.link));
        sb.append(',');
        sb.append("duration");
        sb.append('=');
        sb.append(((this.duration == null)?"<null>":this.duration));
        sb.append(',');
        sb.append("rank");
        sb.append('=');
        sb.append(((this.rank == null)?"<null>":this.rank));
        sb.append(',');
        sb.append("explicitLyrics");
        sb.append('=');
        sb.append(((this.explicitLyrics == null)?"<null>":this.explicitLyrics));
        sb.append(',');
        sb.append("explicitContentLyrics");
        sb.append('=');
        sb.append(((this.explicitContentLyrics == null)?"<null>":this.explicitContentLyrics));
        sb.append(',');
        sb.append("explicitContentCover");
        sb.append('=');
        sb.append(((this.explicitContentCover == null)?"<null>":this.explicitContentCover));
        sb.append(',');
        sb.append("preview");
        sb.append('=');
        sb.append(((this.preview == null)?"<null>":this.preview));
        sb.append(',');
        sb.append("md5Image");
        sb.append('=');
        sb.append(((this.md5Image == null)?"<null>":this.md5Image));
        sb.append(',');
        sb.append("artist");
        sb.append('=');
        sb.append(((this.artist == null)?"<null>":this.artist));
        sb.append(',');
        sb.append("album");
        sb.append('=');
        sb.append(((this.album == null)?"<null>":this.album));
        sb.append(',');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
