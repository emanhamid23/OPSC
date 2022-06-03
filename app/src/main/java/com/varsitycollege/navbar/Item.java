package com.varsitycollege.navbar;

public class Item {

    String category;
    String item;
    String descriptionItem;
    String dateChoosen;

    public Item(){ }

    public Item(String category, String item, String descriptionItem, String dateChoosen){
        this.category = category;
        this.item = item;
        this.descriptionItem = descriptionItem;
        this.dateChoosen = dateChoosen;
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
