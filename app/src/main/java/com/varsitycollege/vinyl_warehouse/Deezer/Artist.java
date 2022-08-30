
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
//creating json structure
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "link",
    "picture",
    "picture_small",
    "picture_medium",
    "picture_big",
    "picture_xl",
    "tracklist",
    "type"
})

public class Artist {
    //declaring variables and assigning them to json properties
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("link")
    private String link;
    @JsonProperty("picture")
    private String picture;
    @JsonProperty("picture_small")
    private String pictureSmall;
    @JsonProperty("picture_medium")
    private String pictureMedium;
    @JsonProperty("picture_big")
    private String pictureBig;
    @JsonProperty("picture_xl")
    private String pictureXl;
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
    //getters and setters for name
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }
    //getters and setters for link
    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(String link) {
        this.link = link;
    }
    //getters and setters for picture
    @JsonProperty("picture")
    public String getPicture() {
        return picture;
    }

    @JsonProperty("picture")
    public void setPicture(String picture) {
        this.picture = picture;
    }
    //getters and setters for small picture
    @JsonProperty("picture_small")
    public String getPictureSmall() {
        return pictureSmall;
    }

    @JsonProperty("picture_small")
    public void setPictureSmall(String pictureSmall) {
        this.pictureSmall = pictureSmall;
    }
    //getters and setters for medium picture
    @JsonProperty("picture_medium")
    public String getPictureMedium() {
        return pictureMedium;
    }

    @JsonProperty("picture_medium")
    public void setPictureMedium(String pictureMedium) {
        this.pictureMedium = pictureMedium;
    }
    //getters and setters for big picture
    @JsonProperty("picture_big")
    public String getPictureBig() {
        return pictureBig;
    }

    @JsonProperty("picture_big")
    public void setPictureBig(String pictureBig) {
        this.pictureBig = pictureBig;
    }
    //getters and setters for xl picture
    @JsonProperty("picture_xl")
    public String getPictureXl() {
        return pictureXl;
    }

    @JsonProperty("picture_xl")
    public void setPictureXl(String pictureXl) {
        this.pictureXl = pictureXl;
    }
    //getters and setters for tracks
    @JsonProperty("tracklist")
    public String getTracklist() {
        return tracklist;
    }

    @JsonProperty("tracklist")
    public void setTracklist(String tracklist) {
        this.tracklist = tracklist;
    }
    //getters and setters for types
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
    //adds the variables together in json format
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Artist.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("link");
        sb.append('=');
        sb.append(((this.link == null)?"<null>":this.link));
        sb.append(',');
        sb.append("picture");
        sb.append('=');
        sb.append(((this.picture == null)?"<null>":this.picture));
        sb.append(',');
        sb.append("pictureSmall");
        sb.append('=');
        sb.append(((this.pictureSmall == null)?"<null>":this.pictureSmall));
        sb.append(',');
        sb.append("pictureMedium");
        sb.append('=');
        sb.append(((this.pictureMedium == null)?"<null>":this.pictureMedium));
        sb.append(',');
        sb.append("pictureBig");
        sb.append('=');
        sb.append(((this.pictureBig == null)?"<null>":this.pictureBig));
        sb.append(',');
        sb.append("pictureXl");
        sb.append('=');
        sb.append(((this.pictureXl == null)?"<null>":this.pictureXl));
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
