package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import model.Customer;
import model.Restaurants;
import model.SingleRestaurant;
import model.Reservation;
import model.Rate;
import DataBase.DataBaseConnector;

public class DefaultSocketServer extends Thread {

    private Socket connSocket;
    // private Server serverClass;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    // private String opFlag = "";

    static int i = 0;

    private ArrayList<String> msg = new ArrayList<String>();

    public DefaultSocketServer(Socket connSocket) {
        this.connSocket = connSocket;
    }

    // public void setOpFlag(String opFlag){
    // this.opFlag=opFlag;
    // }

    public void run() {

        synchronized (this) {
            try {
                oos = new ObjectOutputStream(connSocket.getOutputStream());
                ois = new ObjectInputStream(connSocket.getInputStream());
                String opFlag = (String) ois.readObject();

                //System.out.println("after init");

                if (opFlag.equals("createRestaurant")) {
                    System.out.println("create rest");
                    int returnValue = createRestaurant();
                    // if (returnValue==0) oos.writeObject("insertOK");
                    // else oos.writeObject("duplicate");
                }

                else if (opFlag.equals("retrieveRestInfo")) {
                    retrieveRestaurantInfo();
                }

                else if (opFlag.equals("createUsers")) {
                    createUser2();
                }
                
                else if (opFlag.endsWith("getCusInfo")){
                    getCustomerInfo();
                }
                
                else if (opFlag.endsWith("login")){
                    login();
                }
                
                else if (opFlag.equals("deleteRestaurant")){
                    deleteRestaurant();
                }
                
                else if (opFlag.equals("editRestInfo")){
                    editRestaurant();
                }
                
                else if (opFlag.equals("add_dishes")){
                    addDishes();
                }
                
                else if (opFlag.equals("deleteDishes")){
                    deleteDishes();
                }
                
                else if (opFlag.equals("retrieveReservationInfo")){
                    retrieveReservation();
                }
                
                else if (opFlag.equals("retrieveReviewInfo")){
                    retrieveReviews();
                }
                
                else if (opFlag.equals("getRestDetail")){
					String restName = (String)ois.readObject();
					System.out.println("getting restaurant " + restName + " ...");
					DataBaseConnector userDBHandler = new DataBaseConnector();
					userDBHandler.connectDataBase();
					Statement stmt = userDBHandler.getMyStatement();
					SingleRestaurant restDetail = userDBHandler.getRestDetail(stmt, restName);
					ArrayList<Rate> ratings = userDBHandler.getRestRatings(stmt, restName);
					
					if(restDetail != null)	System.out.println("restaurant found!");
					
					oos.writeObject(restDetail);
					oos.flush();
					oos.writeObject(ratings);
					oos.flush();
                }
                
                else if(opFlag.equals("addFav")){
                	Customer customer = (Customer)ois.readObject();
                	SingleRestaurant restaurant = (SingleRestaurant)ois.readObject();
                	System.out.println("Adding " + restaurant.restaurantName + " to favorite for " + customer.userName + " ...");
                	DataBaseConnector userDBHandler = new DataBaseConnector();
					userDBHandler.connectDataBase();
					Statement stmt = userDBHandler.getMyStatement();
					userDBHandler.addMyFav(stmt, customer, restaurant.restaurantName);
					
					oos.writeObject("done");
					oos.flush();
                }
                
                else if(opFlag.equals("deleteFav")){
                	String restName = (String)ois.readObject();
                	String cusName = (String)ois.readObject();
					System.out.println("deleting restaurant " + restName + " ...");
					DataBaseConnector userDBHandler = new DataBaseConnector();
					userDBHandler.connectDataBase();
					Statement stmt = userDBHandler.getMyStatement();
					userDBHandler.deleteFav(stmt, cusName, restName);
					
					oos.writeObject("deleted");
					oos.flush();
					
					/*if(restDetail != null)	System.out.println("restaurant found!");
					
					oos.writeObject(restDetail);
					oos.flush();*/
                }
                else if(opFlag.equals("addCusRate")){
                	Rate rate = (Rate)ois.readObject();
                	System.out.println("adding rate from " + rate.customerName + "for " + rate.restaurantName + " ...");
					DataBaseConnector userDBHandler = new DataBaseConnector();
					userDBHandler.connectDataBase();
					Statement stmt = userDBHandler.getMyStatement();
					userDBHandler.addMyRate(stmt, rate);
					
					oos.writeObject("added");
					oos.flush();
                }
                else if(opFlag.equals("makeReserve")){
                	Customer customer = (Customer)ois.readObject();
                	Reservation reserve = (Reservation)ois.readObject();
                	DataBaseConnector userDBHandler = new DataBaseConnector();
					userDBHandler.connectDataBase();
					Statement stmt = userDBHandler.getMyStatement();
					userDBHandler.addMyReserve(stmt, customer, reserve);
					
					oos.writeObject("done");
					oos.flush();
                }
                else if(opFlag.equals("delMyReserve")){
                	Customer customer = (Customer)ois.readObject();
                	Reservation reserve = (Reservation)ois.readObject();
                	DataBaseConnector userDBHandler = new DataBaseConnector();
					userDBHandler.connectDataBase();
					Statement stmt = userDBHandler.getMyStatement();
					userDBHandler.delMyReserve(stmt, customer, reserve);
					
					oos.writeObject("reservation seleted");
					oos.flush();
                }
                else if(opFlag.equals("getAllRests")){
                	DataBaseConnector userDBHandler = new DataBaseConnector();
					userDBHandler.connectDataBase();
					Statement stmt = userDBHandler.getMyStatement();
					ArrayList<String> allRests = userDBHandler.getAllRests(stmt);
					
					oos.writeObject(allRests);
					oos.flush();
                }
                else if(opFlag.equals("delMyRate")){
                	Rate rate = (Rate)ois.readObject();
                	DataBaseConnector userDBHandler = new DataBaseConnector();
					userDBHandler.connectDataBase();
					Statement stmt = userDBHandler.getMyStatement();
					userDBHandler.delMyRate(stmt, rate);
					
					oos.writeObject("deleted");
					oos.flush();
                }
                else if(opFlag.equals("getRecommends")){
                	Customer customer = (Customer)ois.readObject();
                	String flavor= customer.flavor;
                	
                	DataBaseConnector userDBHandler = new DataBaseConnector();
					userDBHandler.connectDataBase();
					Statement stmt = userDBHandler.getMyStatement();
					ArrayList<String> recommends = userDBHandler.getRecommends(stmt, flavor);
					oos.writeObject(recommends);
					oos.flush();
                }
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void login(){
        try {
        HashMap<String, String> loginInfo = (HashMap<String, String>)ois.readObject();
        DataBaseConnector db1 = new DataBaseConnector();
        db1.connectDataBase();
       
        Statement myStatement = db1.getMyStatement();
        db1.createDataBase(myStatement);
        boolean pass = db1.loginMatch(myStatement, loginInfo);

        if(pass){
            msg.add("pass");
            
                oos.writeObject(msg);
            
            oos.flush();
        }
        else{
            msg.add("fail");
            oos.writeObject(msg);
            oos.flush();
        }
    }catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
    
    public void createUser2(){
        ArrayList<String> cusInfoArr;
        try {
            cusInfoArr = (ArrayList<String>)ois.readObject();
       
        String userName = cusInfoArr.get(0);
        String password = cusInfoArr.get(1);
        String userType = cusInfoArr.get(2);
        String flavor = cusInfoArr.get(3);
        
        Customer newCustomer = new Customer(userName, password, userType, flavor
                , new ArrayList<String>(), new ArrayList<Reservation>(), new ArrayList<Rate>());
        DataBaseConnector db1 = new DataBaseConnector();
        db1.connectDataBase();
       
        Statement myStatement = db1.getMyStatement();
        db1.createDataBase(myStatement);
        boolean added = db1.addCustomer(myStatement, newCustomer);
        
//        oos = new ObjectOutputStream(connSocket.getOutputStream());
        ArrayList<String> msg = new ArrayList<String>();
        if(added == true)   {
            msg.add("added");
            SendGmail sendmail=new SendGmail(userName);
            
            System.out.println("sending email");
        }
        else    msg.add("notAdded");
        oos.writeObject(msg);
        oos.flush();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void getCustomerInfo(){
        DataBaseConnector db1 = new DataBaseConnector();
        db1.connectDataBase();
        try {
        Statement myStatement = db1.getMyStatement();
        db1.createDataBase(myStatement);
        ArrayList<String> nameAndType = (ArrayList<String>)ois.readObject();
        Customer customerInfo=db1.getCustomer(myStatement, nameAndType);
//        UserDBHandler userDBHandler = new UserDBHandler();
//        Customer customerInfo = userDBHandler.getCustomer(stmt, nameAndType);
        //if(customerInfo != null){
//            ObjectOutputStream oos = new ObjectOutputStream(connSocket.getOutputStream());
           
                oos.writeObject(customerInfo);
           
            oos.flush();
             } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
    
    
    public void createUser() {
        try {
            DataBaseConnector db1 = new DataBaseConnector();
            db1.connectDataBase();

            Statement myStatement = db1.getMyStatement();
            db1.createDataBase(myStatement);
            msg = new ArrayList<String>();

            // String inFromClient = (String)ois.readObject();
            ArrayList<String> inlist = (ArrayList<String>) ois.readObject();

            boolean checkresult = checkDup(inlist);

            if (checkresult) {
                msg.add("true");

            } else {
                msg.add("false");
            }

            oos.writeObject(msg);
            oos.flush();
            // serverClass.notify();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean checkDup(ArrayList<String> getlist) {

        DataBaseConnector db1 = new DataBaseConnector();
        db1.connectDataBase();

        Statement myStatement = db1.getMyStatement();
//        db1.createDataBase(myStatement);
        boolean check = db1.checkDuplicate(getlist.get(0));

        if (check) {

            db1.addElement(getlist.get(0), getlist.get(1), getlist.get(2),
                    getlist.get(3));

            return true;

        } else {
            return false;
        }
    }

    public int createRestaurant() {
        try {
            // oos = new ObjectOutputStream(connSocket.getOutputStream());
            // ois = new ObjectInputStream(connSocket.getInputStream());

            ArrayList<String> editInfo = (ArrayList<String>) ois.readObject();

            DataBaseConnector db1 = new DataBaseConnector();
            db1.connectDataBase();

            Statement myStatement = db1.getMyStatement();
            db1.createDataBase(myStatement);
            String location = editInfo.get(2);
            System.out.println("location is " + location);

            if (db1.duplicateLocation(myStatement, db1.getMyConnection(),
                    location) == false) {
                db1.createMyRestaurant(editInfo);
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void retrieveRestaurantInfo() {
        try {
            // oos = new ObjectOutputStream(connSocket.getOutputStream());
            // ois = new ObjectInputStream(connSocket.getInputStream());
            // String inFromClient = (String) ois.readObject();
            // System.out.println("Client: " + inFromClient);

            DataBaseConnector db1 = new DataBaseConnector();
            db1.connectDataBase();

            Statement myStatement = db1.getMyStatement();
            db1.createDataBase(myStatement);

            ResultSet rs = db1.retrieveRestaurantInfo(myStatement,
                    db1.getMyConnection());

            Restaurants allRestaurants = new Restaurants();

            ArrayList<SingleRestaurant> restList = new ArrayList<>();

            while (rs.next()) {
                int RestaurantID = rs.getInt("RestaurantID");
                int userID = rs.getInt("UserID");
                String restaurantName = rs.getString("RestaurantName");
                int telephone = rs.getInt("Telephone");
                String location = rs.getString("Location");
                String openHour = rs.getString("OpenHour");
                float averageRating = rs.getFloat("AverageRating");
                int numberOfRates = rs.getInt("NumberOfRates");

                ResultSet rs2 = db1.retrieveDishesInfo(myStatement,
                        db1.getMyConnection(), RestaurantID);
                ArrayList<String> dishesName = new ArrayList<>();
                while (rs2.next()) {
                    dishesName.add(rs2.getString("DishName"));
                }

                SingleRestaurant rest1 = new SingleRestaurant(userID,
                        restaurantName, location, telephone, openHour,
                        averageRating, numberOfRates, dishesName);
                rest1.restaurantID = RestaurantID;
                restList.add(rest1);
                allRestaurants.addRestaurant(rest1);
            }

            oos.writeObject(allRestaurants);
//            System.out.println("here is "
//                    + allRestaurants.getRestaurantName().get(0));

            oos.flush();
        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    
    public void deleteRestaurant(){
        try {
        DataBaseConnector db1 = new DataBaseConnector();
        db1.connectDataBase();

        Statement myStatement = db1.getMyStatement();
        db1.createDataBase(myStatement);      
        String restName=(String)ois.readObject();
        String userName=(String)ois.readObject();
        
        db1.deleteRestaurant(restName,userName);
//        db1.deleteRestaurant()    
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
    
    public void editRestaurant(){
        try {
            DataBaseConnector db1 = new DataBaseConnector();
            db1.connectDataBase();

            Statement myStatement = db1.getMyStatement();
            db1.createDataBase(myStatement);      
            
            ArrayList<String> editRestList=(ArrayList<String>)ois.readObject();
            db1.editRestaurant(editRestList);          
//            db1.deleteRestaurant()    
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
   
    public void addDishes(){
        try {
            DataBaseConnector db1 = new DataBaseConnector();
            db1.connectDataBase();

            Statement myStatement = db1.getMyStatement();
            db1.createDataBase(myStatement);      
            
            int restID=(int)ois.readObject();
            String dishName=(String)ois.readObject();
            db1.addDishes(restID,dishName);        
//            db1.deleteRestaurant()    
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
    }
    
    public void deleteDishes(){
        try {
            DataBaseConnector db1 = new DataBaseConnector();
            db1.connectDataBase();

            Statement myStatement = db1.getMyStatement();
            db1.createDataBase(myStatement);      
            
            int restID=(int)ois.readObject();
            String dishName=(String)ois.readObject();
            db1.deleteDishes(restID,dishName);        
//            db1.deleteRestaurant()    
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
    }
    
    public void retrieveReservation(){
        try {
            DataBaseConnector db1 = new DataBaseConnector();
            db1.connectDataBase();

            Statement myStatement = db1.getMyStatement();
            db1.createDataBase(myStatement);      
            
            int restID=(int)ois.readObject();
//            String dishName=(String)ois.readObject();
//            db1.deleteDishes(restID,dishName);        
//            db1.deleteRestaurant()    
            
            ResultSet rs= db1.retrieveReservationInfo(myStatement, db1.getMyConnection(), restID);
            
            ArrayList <String[]> reservationList=new ArrayList<>();
            
            while(rs.next()){
                String [] singleReservation=new String[2];
                
                
                ResultSet rs2=db1.retrieveCustomerName(myStatement, db1.getMyConnection(), rs.getInt("UserID"));
                String userName="";
                while(rs2.next()){
                    userName=rs2.getString("UserName");
                }
                singleReservation[0]=userName;
                singleReservation[1]=rs.getString("ReserveTime");
                reservationList.add(singleReservation);  
                System.out.println("retr reservation"+userName);
            }
                
            oos.writeObject(reservationList);
            oos.flush();
            
            
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
    }
    
    
    public void retrieveReviews(){
        try {
            DataBaseConnector db1 = new DataBaseConnector();
            db1.connectDataBase();

            Statement myStatement = db1.getMyStatement();
            db1.createDataBase(myStatement);      
            
            int restID=(int)ois.readObject();
//            String dishName=(String)ois.readObject();
//            db1.deleteDishes(restID,dishName);        
//            db1.deleteRestaurant()    
            
            ResultSet rs= db1.retrieveReviewInfo(myStatement, db1.getMyConnection(), restID);
            
            ArrayList <String[]> reviewList=new ArrayList<>();
            
            while(rs.next()){
                String [] singleReview=new String[3];
                
                
                ResultSet rs2=db1.retrieveCustomerName(myStatement, db1.getMyConnection(), rs.getInt("UserID"));
                String userName="";
                while(rs2.next()){
                    userName=rs2.getString("UserName");
                }
                singleReview[0]=userName;
                singleReview[1]=rs.getString("Review");
                singleReview[2]=String.valueOf(rs.getFloat("Rating"));
                reviewList.add(singleReview); 
                 System.out.println("retr review"+userName);
            }
            
           
                
            oos.writeObject(reviewList);
            oos.flush();
            
            
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
    }

}
