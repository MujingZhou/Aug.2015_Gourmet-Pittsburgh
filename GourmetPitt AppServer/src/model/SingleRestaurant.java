package model;

import java.io.Serializable;
import java.util.ArrayList;

public class SingleRestaurant implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public int userID;
       public String restaurantName;
       public String location;
       public int telephone;
       public String openHour;
       public float averageRating;
       public int numberOfRates;
       public ArrayList<String> dishesName;
       public int restaurantID;
       
       public SingleRestaurant(int userID,String restaurantName, String location, int telephone, String openHour, float averageRating,
               int numberOfRates, ArrayList<String> dishesName){
           this.userID=userID;
           this.restaurantName=restaurantName;
           this.location=location;
           this.telephone=telephone;
           this.openHour=openHour;
           this.averageRating=averageRating;
           this.numberOfRates=numberOfRates;
           this.dishesName=dishesName;
       }
}

