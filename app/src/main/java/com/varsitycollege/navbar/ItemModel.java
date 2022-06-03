package com.varsitycollege.navbar;

public class ItemModel {

    String Category;
    String Item;
    String Description;
    String Date;
    String Image;
    public ItemModel(){

    }

    public ItemModel(String category, String item, String description, String date, String image){
        Category = category;
        Item = item;
        Description = description;
        Date = date;
        Image = image;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
