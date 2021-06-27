package com.example.musiccafe;

public class model {
    String Image,name,price;

    public model() {
    }

    public model(String image, String name, String price) {
        this.Image = image;
        this.name = name;
        this.price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
