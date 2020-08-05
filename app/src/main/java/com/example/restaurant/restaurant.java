package com.example.restaurant;

public class restaurant {
    private String image, description, location, name, pno;
    private float rating;

    public restaurant() {

    }

    public restaurant(String image, String description, String location, String name, String pno, float rating) {
        this.image = image;
        this.description = description;
        this.location = location;
        this.name = name;
        this.pno = pno;
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return pno;
    }

    public void setPhone(String pno) {
        this.pno = pno;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}