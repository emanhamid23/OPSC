package models;

public class Data {

    private final String itemname;
    private final String categoryname;
    private final String date;

    private final String itemdescription;

    public Data(String name, String categoryname, String date, String itemdescription){
        this.itemname = name;
        this.categoryname = categoryname;
        this.date = date;
        this.itemdescription = itemdescription;

    }

    public String getName(){
        return itemname;
    }

    public String getCategoryname(){
        return categoryname;
    }

    public String getDate(){
        return date;
    }

    public String getItemdescription(){
        return itemdescription;
    }

}