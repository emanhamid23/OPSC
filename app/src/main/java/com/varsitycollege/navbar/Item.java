package com.varsitycollege.navbar;

import android.net.Uri;

public class Item {

    String category;
    String item;
    String descriptionItem;
    String dateChoosen;
    Uri imageURL;

    public Item(){ }

    public Item(String category, String item, String descriptionItem, String dateChoosen, Uri imageURL){
        this.category = category;
        this.item = item;
        this.descriptionItem = descriptionItem;
        this.dateChoosen = dateChoosen;
        this.imageURL = imageURL;
    }

    public String getCategory(){
        return category;
    }

    public String getItem(){
        return item;
    }

    public String getDescriptionItem(){return descriptionItem;}

    public String getDateChoosen(){return dateChoosen;}

}
