package models;

public class Category
{
    private final String name;
    private final Integer goal;

    public Category(String name, Integer goal)
    {
        this.name = name;
        this.goal = goal;
    }

    public String getName()
    {
        return name;
    }

    public Integer getGoal()
    {
        return goal;
    }
}
