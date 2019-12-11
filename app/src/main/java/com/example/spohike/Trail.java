package com.example.spohike;


public class Trail {
    String id;
    String name;
    String summary;
    String longitude;
    String latitude;
    String stars;
    String length;
    String photoURL;

    public Trail(String id, String name, String summary, String longitude, String latitude, String stars, String length, String photoURL) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.longitude = longitude;
        this.latitude = latitude;
        this.stars = stars;
        this.length = length;
        this.photoURL = photoURL;

    }

    @Override
    public String toString() {
        return "Trail{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", stars='" + stars + '\'' +
                ", longitude='" + length + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
