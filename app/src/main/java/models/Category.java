package models;

public class Category {

    private final String name;
    private final String goal;

    public Category(String name, String goal){
        this.name = name;
        this.goal = goal;
    }

    public String getName(){
        return name;
    }

    public String getGoal(){
        return goal;
    }
}
