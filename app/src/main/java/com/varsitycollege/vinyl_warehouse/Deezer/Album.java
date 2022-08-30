
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
//orders the data of the json data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "title",
    "cover",
    "cover_small",
    "cover_medium",
    "cover_big",
    "cover_xl",
    "md5_image",
    "tracklist",
    "type"
})

public class Album {
    //declaring variables and assigning it to the json placeholder variables
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("cover")
    private String cover;
    @JsonProperty("cover_small")
    private String coverSmall;
    @JsonProperty("cover_medium")
    private String coverMedium;
    @JsonProperty("cover_big")
    private String coverBig;
    @JsonProperty("cover_xl")
    private String coverXl;
    @JsonProperty("md5_image")
    private String md5Image;
    @JsonProperty("tracklist")
    private String tracklist;
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
    //getters and setters for Title
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }
    //getters and setters for Cover
    @JsonProperty("cover")
    public String getCover() {
        return cover;
    }

    @JsonProperty("cover")
    public void setCover(String cover) {
        this.cover = cover;
    }
    //getters and setters for Small cover
    @JsonProperty("cover_small")
    public String getCoverSmall() {
        return coverSmall;
    }

    @JsonProperty("cover_small")
    public void setCoverSmall(String coverSmall) {
        this.coverSmall = coverSmall;
    }
    //getters and setters for medium cover
    @JsonProperty("cover_medium")
    public String getCoverMedium() {
        return coverMedium;
    }

    @JsonProperty("cover_medium")
    public void setCoverMedium(String coverMedium) {
        this.coverMedium = coverMedium;
    }
    //getters and setters for big cover
    @JsonProperty("cover_big")
    public String getCoverBig() {
        return coverBig;
    }

    @JsonProperty("cover_big")
    public void setCoverBig(String coverBig) {
        this.coverBig = coverBig;
    }
    //getters and setters for xl cover
    @JsonProperty("cover_xl")
    public String getCoverXl() {
        return coverXl;
    }

    @JsonProperty("cover_xl")
    public void setCoverXl(String coverXl) {
        this.coverXl = coverXl;
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
    //getters and setters for track list
    @JsonProperty("tracklist")
    public String getTracklist() {
        return tracklist;
    }

    @JsonProperty("tracklist")
    public void setTracklist(String tracklist) {
        this.tracklist = tracklist;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
    //method to add all the variables into json format
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Album.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null)?"<null>":this.title));
        sb.append(',');
        sb.append("cover");
        sb.append('=');
        sb.append(((this.cover == null)?"<null>":this.cover));
        sb.append(',');
        sb.append("coverSmall");
        sb.append('=');
        sb.append(((this.coverSmall == null)?"<null>":this.coverSmall));
        sb.append(',');
        sb.append("coverMedium");
        sb.append('=');
        sb.append(((this.coverMedium == null)?"<null>":this.coverMedium));
        sb.append(',');
        sb.append("coverBig");
        sb.append('=');
        sb.append(((this.coverBig == null)?"<null>":this.coverBig));
        sb.append(',');
        sb.append("coverXl");
        sb.append('=');
        sb.append(((this.coverXl == null)?"<null>":this.coverXl));
        sb.append(',');
        sb.append("md5Image");
        sb.append('=');
        sb.append(((this.md5Image == null)?"<null>":this.md5Image));
        sb.append(',');
        sb.append("tracklist");
        sb.append('=');
        sb.append(((this.tracklist == null)?"<null>":this.tracklist));
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
