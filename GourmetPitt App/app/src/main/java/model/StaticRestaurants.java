package model;

import java.util.ArrayList;

/**
 * Created by zhoumujing on 7/23/15.
 */
public class StaticRestaurants {

    public static ArrayList<SingleRestaurant> staticRestaurants;

    public StaticRestaurants(){

    }

    public StaticRestaurants(Restaurants restaurants){
        staticRestaurants=restaurants.getAllRestaurant();
    }

    public ArrayList<SingleRestaurant> getStaticRestaurants(){
        return staticRestaurants;
    }


    public ArrayList<String> getRestaurantName(){
        ArrayList<String> restNames=new ArrayList<>();
        for (int i=0;i<staticRestaurants.size();i++){
            restNames.add(staticRestaurants.get(i).restaurantName);
        }
        return restNames;
    }

}
