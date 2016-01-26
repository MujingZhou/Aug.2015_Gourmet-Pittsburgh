package model;

import java.io.Serializable;
import java.util.ArrayList;

public class SingleRestaurant implements Serializable{
    private static final long serialVersionUID = 1L;

    public int userID;
    public int restaurantID;
    public String restaurantName;
    public String location;
    public int telephone;
    public String openHour;
    public String flavor;
    public float averageRating;
    public int numberOfRates;
    public ArrayList<String> dishesName;
    public String url;
//    public ArrayList<String[]> reviews;

    public SingleRestaurant(int userID, String restaurantName, String location, int telephone, String openHour, String flavor, float averageRating,
                            int numberOfRates, ArrayList<String> dishesName,String url){
        this.userID=userID;
        //this.restaurantID=restaurantID;
        this.restaurantName=restaurantName;
        this.location=location;
        this.telephone=telephone;
        this.openHour=openHour;
        this.flavor=flavor;
        this.averageRating=averageRating;
        this.numberOfRates=numberOfRates;
        this.dishesName=dishesName;
        this.url=url;
//        this.reviews=reviews;
    }
}

