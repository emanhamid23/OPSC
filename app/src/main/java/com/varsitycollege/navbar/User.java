package com.varsitycollege.navbar;

public class User {

    private String categoryName;
    private String categoryGoal;

    public User(){

    }

    public User(String categoryName, String categoryGoal){
        this.categoryName = categoryName;
        this.categoryGoal = categoryGoal;
    }

    public String getCategoryName(){
        return categoryName;
    }

    public String getCategoryGoal(){
        return categoryGoal;
    }
}
