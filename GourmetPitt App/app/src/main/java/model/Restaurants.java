package model;
import java.io.Serializable;
import java.util.ArrayList;

public class Restaurants implements Serializable{
    private static final long serialVersionUID = 1L;
    public ArrayList<SingleRestaurant> allRestaurant;

    public Restaurants(){
        allRestaurant=new ArrayList<>();
    }

    public void addRestaurant(SingleRestaurant rest1){
        allRestaurant.add(rest1);
    }

    public ArrayList<SingleRestaurant> getAllRestaurant(){
        return allRestaurant;
    }

    public ArrayList<String> getRestaurantName(){
        ArrayList<String> restNames=new ArrayList<>();
        for (int i=0;i<allRestaurant.size();i++){
            restNames.add(allRestaurant.get(i).restaurantName);
        }
        return restNames;
    }
}
