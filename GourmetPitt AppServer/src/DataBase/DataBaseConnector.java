package DataBase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import model.Customer;
import model.Rate;
import model.Reservation;
import model.SingleRestaurant;
//import util.UserDBHandler;

/* 
 * 
 * ProxyDataBase -- This class is an abstract class which is extended by the 
 * BuildAutoInDataBase. This class has fields of the Connection and Statement of the
 * corresponding SQP database. Four kinds of methods are defined in this class: 1. 
 * CreateDataBase, 2. InSertAutoInDataBase, 3. UpdateAutoInDataBase, 4. DeleteAutoInDataBase.
 * Also, these four kinds of methods are the four interfaces that will be implemented by the
 * BuildAutoInDataBase class.
 * Also, methods in this class will be called by an object of BuildAutoInDataBase in the proxyAutomobile
 * class.
 *  
 */
public class DataBaseConnector {
    private Connection myConnection;
    private Statement myStatement;

    private static final String DB_NAME = "ServerDatabase";
    private static final String TABLE_USER = "User";
    private static final String COLUMN_ID = "UserID";
    private static final String COLUMN_NAME = "UserName";
    private static final String COLUMN_PASS = "Password";
    private static final String COLUMN_IDT = "Identification";
    private static final String COLUMN_FLAVOR = "Flavor";
    
    
    public Connection getMyConnection() {
        return myConnection;
    }

    public Statement getMyStatement() {
        return myStatement;
    }

    /*
     * connectDataBase -- connect to the local mysql database.
     */
    public void connectDataBase() {
        String url = "jdbc:mysql://localhost";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            myConnection = DriverManager.getConnection(url, "root", "");
            myStatement = myConnection.createStatement();
            myStatement.setQueryTimeout(180);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }

    /*
     * createDataBase -- create the databse from the given text file..
     */
    public void createDataBase(Statement myStatement) {
        String filename = "create_database.txt";
        FileReader file;
        try {
            file = new FileReader(filename);

            BufferedReader buff = new BufferedReader(file);
            String line;
            while ((line = buff.readLine()) != null) {
                myStatement.executeUpdate(line);
            }
            buff.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    // public void insertRestNameToDataBase(Statement myStatement,
    // Connection myConnection, String restName) throws Exception {
    //
    // String subCommand ="insert into Restaurant(RestName) values(?);";
    // PreparedStatement ps1 = myConnection.prepareStatement(subCommand);
    // Object ob[]={restName};
    // ps1 = setPreparedStatement(1, ob, ps1);
    // ps1.executeUpdate();
    // }

    public boolean checkDuplicate(String username) {

        String user = null;

        try {
            ResultSet result1 = myStatement
                    .executeQuery("select username from ServerDatabase.User where username = '"
                            + username + "'");

            while (result1.next()) {
                user = result1.getString("username");

            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (user != null)
            return false;
        else
            return true;

    }

    public void addElement(String email, String password,
            String identification, String flavor) {

        try {
            myStatement.executeUpdate(" insert into ServerDatabase.User values(" + null + ",'"
                    + email + "','" + password + "','" + identification + "','"
                    + flavor + "'); ");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public ResultSet retrieveRestaurantInfo(Statement myStatement,
            Connection myConnection) throws Exception {
        String returnInfo = "";
        String subCommand = "select * from ServerDatabase.Restaurants;";
        PreparedStatement ps1 = myConnection.prepareStatement(subCommand);

        ResultSet rs = ps1.executeQuery();
        return rs;
    }
    
    public ResultSet retrieveCustomerName(Statement myStatement,
            Connection myConnection, int userID) throws Exception {
        String returnInfo = "";
        String subCommand = "select * from ServerDatabase.User where UserID='"+userID+"';";
        PreparedStatement ps1 = myConnection.prepareStatement(subCommand);
        ResultSet rs = ps1.executeQuery();
        return rs;
    }

    public ResultSet retrieveDishesInfo(Statement myStatement,
            Connection myConnection, int RestaurantID) throws Exception {

        String subCommand = "select * from ServerDatabase.Dishes where RestaurantID=?;";

        PreparedStatement ps1 = myConnection.prepareStatement(subCommand);
        ps1.setInt(1, RestaurantID);

        ResultSet rs = ps1.executeQuery();
        return rs;
    }
    
    public ResultSet retrieveReservationInfo(Statement myStatement,
            Connection myConnection, int RestaurantID) throws Exception {

        String subCommand = "select * from ServerDatabase.MyReservation where RestaurantID=?;";

        PreparedStatement ps1 = myConnection.prepareStatement(subCommand);
        ps1.setInt(1, RestaurantID);
        ResultSet rs = ps1.executeQuery();
        return rs;
    }
    
    public ResultSet retrieveReviewInfo(Statement myStatement,
            Connection myConnection, int RestaurantID) throws Exception {

        String subCommand = "select * from ServerDatabase.MyRate where RestaurantID=?;";

        PreparedStatement ps1 = myConnection.prepareStatement(subCommand);
        ps1.setInt(1, RestaurantID);
        ResultSet rs = ps1.executeQuery();
        return rs;
    }

    public void createMyRestaurant(ArrayList<String> editInfo) {
        String userName=editInfo.get(0);
        String restaurantName = editInfo.get(1);
        int telephone = Integer.valueOf(editInfo.get(2));
        String location = editInfo.get(3);
        String openHour = editInfo.get(4);
        String flavor = editInfo.get(5);
        System.out.println("userName"+userName);
        String query = "SELECT * FROM ServerDatabase.User WHERE UserName='"+userName+"';";
        PreparedStatement ps1;
        int userID=0;
        try {
            ps1 = myConnection.prepareStatement(query);
//            ps1.setString(1,userName);
            ResultSet rs=ps1.executeQuery();
        
        while(rs.next()){
            userID=rs.getInt("UserID");
        }
        
        System.out.println("UserID is "+userID);
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        String subCommand = "insert into ServerDatabase.Restaurants(UserID,RestaurantName,Telephone,Location,OpenHour,Flavor,AverageRating,NumberOfRates) "
                + "values(?,?,?,?,?,?,?,?);";
        try {
            ps1 = myConnection.prepareStatement(subCommand);
            System.out.println("Create UserID is "+userID);
            ps1.setInt(1, userID);
            ps1.setString(2, restaurantName);
            ps1.setInt(3, telephone);
            ps1.setString(4, location);
            ps1.setString(5, openHour);
            ps1.setString(6, flavor);
            ps1.setFloat(7, 0.0f);
            ps1.setInt(8, 0);
            ps1.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    public void deleteRestaurant(String restName, String userName){
        String query = "SELECT * FROM ServerDatabase.User WHERE UserName='"+userName+"';";
        PreparedStatement ps1;
        int userID=0;
        try {
            ps1 = myConnection.prepareStatement(query);
//            ps1.setString(1,userName);
            ResultSet rs=ps1.executeQuery();
        
        while(rs.next()){
            userID=rs.getInt("UserID");
        }
           System.out.println("delete: "+restName);
           String subCommand="delete from ServerDatabase.Restaurants where UserID='"+userID+"' and RestaurantName='"+restName+"';";
           ps1 = myConnection.prepareStatement(subCommand);
           ps1.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void editRestaurant(ArrayList<String> editRestList){
        int restaurantID=Integer.valueOf(editRestList.get(0));
        String newRestName=editRestList.get(1);
        String newLocation=editRestList.get(2);
        int newTelephone=Integer.valueOf(editRestList.get(3));
        String newOpenHour=editRestList.get(4);
        String newFlavor=editRestList.get(5);
        
        String subCommand="update ServerDatabase.Restaurants set RestaurantName='"+newRestName+"',Telephone='"+newTelephone+"',"
                + "Location='"+newLocation+"',OpenHour='"+newOpenHour+"',Flavor='"+newFlavor+"' where RestaurantID='"+restaurantID+"';";
        PreparedStatement ps1;
        try {
            ps1 = myConnection.prepareStatement(subCommand);
        
        ps1.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean duplicateLocation(Statement myStatement,
            Connection myConnection, String location) {
        String subCommand = "select * from ServerDatabase.Restaurants;";
        PreparedStatement ps1;
        try {
            ps1 = myConnection.prepareStatement(subCommand);
            ResultSet rs = ps1.executeQuery();

            while (rs.next()) {
                String locationAlreadyInDB = rs.getString("Location");
                if (locationAlreadyInDB.equals(location)) {
                    return true;
                }
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public Customer getCustomer(Statement stmt, ArrayList<String> nameAndType){
        String userName = nameAndType.get(0);
        String identification = nameAndType.get(1);
        ArrayList<String> favs = new ArrayList<String>();
        ArrayList<Reservation> reserves = new ArrayList<Reservation>();
        ArrayList<Rate> rates = new ArrayList<Rate>();
        
        try{
            String query = "SELECT * FROM %s.%s WHERE %s='%s' AND %s='%s';";
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);
            formatter.format(query, DB_NAME, TABLE_USER, COLUMN_NAME, userName, COLUMN_IDT, identification);
            ResultSet rs = stmt.executeQuery(sb.toString());
            if(rs.next()){
                int user_id = rs.getInt(1);
                String password = rs.getString(3);
                String flavor = rs.getString(5);
                
                query = "SELECT * FROM %s.MyReservation WHERE %s='%d';";
                sb = new StringBuilder();
                formatter = new Formatter(sb, Locale.US);
                formatter.format(query, DB_NAME, "UserID", user_id);
                rs = stmt.executeQuery(sb.toString());
                ArrayList<Integer> restIDs = new ArrayList<Integer>();
                ArrayList<String> reserveTimes = new ArrayList<String>();
                while(rs.next()){
                    restIDs.add(rs.getInt(3));
                    reserveTimes.add(rs.getString(4));
                }
                for(int i = 0; i < restIDs.size(); i++){
                    int rest_id = restIDs.get(i);
                    
                    query = "SELECT * FROM %s.Restaurants WHERE %s='%d';";
                    sb = new StringBuilder();
                    formatter = new Formatter(sb, Locale.US);
                    formatter.format(query, DB_NAME, "RestaurantID", rest_id);
                    rs = stmt.executeQuery(sb.toString());
                    formatter.close();
                    String restName = "";
                    while(rs.next()){
                        restName = rs.getString(3);
                    }
                    
                    String reserveTime = reserveTimes.get(i); 
                    Reservation reserve = new Reservation(restName, reserveTime);
                    reserves.add(reserve);
                }
                
                query = "SELECT * FROM %s.MyFavorite WHERE %s='%d';";
                sb = new StringBuilder();
                formatter = new Formatter(sb, Locale.US);
                formatter.format(query, DB_NAME, "CustomerID", user_id);
                rs = stmt.executeQuery(sb.toString());
                restIDs = new ArrayList<Integer>();
                while(rs.next()){
                    restIDs.add(rs.getInt(4));
                }
                for(int i = 0; i < restIDs.size(); i++){
                    int rest_id = restIDs.get(i);

                    query = "SELECT * FROM %s.Restaurants WHERE %s='%d';";
                    sb = new StringBuilder();
                    formatter = new Formatter(sb, Locale.US);
                    formatter.format(query, DB_NAME, "RestaurantID", rest_id);
                    rs = stmt.executeQuery(sb.toString());
                    formatter.close();
                    String restName = "";
                    while(rs.next()){
                        restName = rs.getString(3);
                    }

                    favs.add(restName);
                }
                
                query = "SELECT * FROM %s.MyRate WHERE %s='%d';";
                sb = new StringBuilder();
                formatter = new Formatter(sb, Locale.US);
                formatter.format(query, DB_NAME, "UserID", user_id);
                rs = stmt.executeQuery(sb.toString());
                restIDs = new ArrayList<Integer>();
                ArrayList<Float> ratings = new ArrayList<Float>();
                ArrayList<String> reviews = new ArrayList<String>();
                while(rs.next()){
                    restIDs.add(rs.getInt(3));
                    ratings.add(rs.getFloat(4));
                    reviews.add(rs.getString(5));
                }
                for(int i = 0; i < restIDs.size(); i++){
                    int rest_id = restIDs.get(i);

                    query = "SELECT * FROM %s.Restaurants WHERE %s='%d';";
                    sb = new StringBuilder();
                    formatter = new Formatter(sb, Locale.US);
                    formatter.format(query, DB_NAME, "RestaurantID", rest_id);
                    rs = stmt.executeQuery(sb.toString());
                    formatter.close();
                    String restName = "";
                    while(rs.next()){
                        restName = rs.getString(3);
                    }
                    
                    float rating = ratings.get(i);
                    String review = reviews.get(i);
                    Rate rate = new Rate(userName, restName, rating, review);
                    rates.add(rate);
                }
                
                Customer customerInfo = new Customer(userName, password, identification, flavor,
                        favs, reserves, rates);
                return customerInfo;
            }
            return null;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean addCustomer(Statement stmt, Customer customer) throws SQLException{
        String query = "SELECT %s FROM %s.%s WHERE %s='%s'";
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format(query, COLUMN_ID, DB_NAME, TABLE_USER, COLUMN_NAME, customer.userName);
        ResultSet rs = stmt.executeQuery(sb.toString());
        formatter.close();
        
        int user_id = 0;
        while(rs.next())    user_id = rs.getInt(1);
        if(user_id != 0){   
            System.out.println("User already exists");
            return false;
        }

        else{
            String command = "REPLACE INTO %s.%s(%s,%s,%s,%s) VALUES('%s','%s','%s','%s');";
            sb = new StringBuilder();
            formatter = new Formatter(sb, Locale.US);
            formatter.format(command, DB_NAME, TABLE_USER, COLUMN_NAME, COLUMN_PASS, COLUMN_IDT, COLUMN_FLAVOR,
                    customer.userName, customer.password, customer.identification, customer.flavor);
            System.out.println(sb.toString());
            stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            formatter.close();

            return true;
        }
    }
    
    public void addMyFav(Statement stmt, Customer customer, String fav) throws SQLException{
		String query = "SELECT UserID FROM %s.%s WHERE %s='%s';";
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		formatter.format(query, DB_NAME, TABLE_USER, COLUMN_NAME, customer.userName);
		ResultSet rs = stmt.executeQuery(sb.toString());
		formatter.close();
		int user_id = 0;
		while(rs.next())	user_id = rs.getInt(1);
		
		
		query = "SELECT * FROM %s.%s WHERE %s='%s'";
		sb = new StringBuilder();
		formatter = new Formatter(sb, Locale.US);
		formatter.format(query, DB_NAME, "Restaurants", "RestaurantName", fav);
		rs = stmt.executeQuery(sb.toString());
		formatter.close();
		
		int rest_id = 0;
		int business_id = 0;
		while(rs.next()){
			rest_id = rs.getInt(1);
			business_id = rs.getInt(2);
		}
			
		query = "SELECT FavoriteID FROM %s.MyFavorite WHERE %s='%d' AND %s='%d';";
		sb = new StringBuilder();
		formatter = new Formatter(sb, Locale.US);
		formatter.format(query, DB_NAME, "RestaurantID", rest_id, "CustomerID", user_id);
		rs = stmt.executeQuery(sb.toString());
		formatter.close();
		int test_id = 0;
		while(rs.next()){
			test_id = rs.getInt(1);
		}
		if(test_id != 0)	System.out.println("Favorite already exists");
		else{
			sb = new StringBuilder();
			String command = "REPLACE INTO %s.MyFavorite(%s,%s,%s) VALUES('%d','%d','%d');";
			formatter = new Formatter(sb, Locale.US);
			formatter.format(command, DB_NAME, "BusinessID", "CustomerID", "RestaurantID", business_id, user_id, rest_id);
			stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			formatter.close();
		}
		
	}
    
    public void addMyReserve(Statement stmt, Customer customer, Reservation reserve) throws SQLException{
		String query = "SELECT UserID FROM %s.%s WHERE %s='%s';";
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		formatter.format(query, DB_NAME, TABLE_USER, COLUMN_NAME, customer.userName);
		ResultSet rs = stmt.executeQuery(sb.toString());
		formatter.close();
		int user_id = 0;
		while(rs.next())	user_id = rs.getInt(1);
		/*if(user_id == 0){
			System.out.println("Err to get user_id");
			return false;
		}*/
		
		//int reservesNum = customer.reservations.size();
		//for(int i = 0; i < reservesNum; i++){
			//Reservation reserve = customer.reservations.get(i);
		
		query = "SELECT RestaurantID FROM %s.%s WHERE %s='%s'";
		sb = new StringBuilder();
		formatter = new Formatter(sb, Locale.US);
		formatter.format(query, DB_NAME, "Restaurants", "RestaurantName", reserve.restName);
		rs = stmt.executeQuery(sb.toString());
		formatter.close();
		
		int rest_id = 0;
		while(rs.next())	rest_id = rs.getInt(1);
			
		query = "SELECT ReservationID FROM %s.MyReservation WHERE %s='%d' AND %s='%s' AND %s='%d';";
		sb = new StringBuilder();
		formatter = new Formatter(sb, Locale.US);
		formatter.format(query, DB_NAME, "RestaurantID", rest_id, "ReserveTime", reserve.reserveTime, "UserID", user_id);
		rs = stmt.executeQuery(sb.toString());
		formatter.close();
		int reserve_id = 0;
		while(rs.next()){
			reserve_id = rs.getInt(1);
		}
		if(reserve_id != 0)	System.out.println("Reservation already exists");
		else{

			String command = "REPLACE INTO %s.MyReservation(%s,%s,%s) VALUES('%d','%d','%s');";
			sb = new StringBuilder();
			formatter = new Formatter(sb, Locale.US);
			formatter.format(command, DB_NAME, "UserID", "RestaurantID", "ReserveTime", user_id, rest_id, reserve.reserveTime);
			stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			formatter.close();
		}
		//}
	}
    
    public void addMyRate(Statement stmt, Rate rate) throws SQLException{
		String query = "SELECT UserID FROM %s.%s WHERE %s='%s';";
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		formatter.format(query, DB_NAME, TABLE_USER, COLUMN_NAME, rate.customerName);
		ResultSet rs = stmt.executeQuery(sb.toString());
		formatter.close();
		int user_id = 0;
		while(rs.next())	user_id = rs.getInt(1);
		
		query = "SELECT * FROM %s.%s WHERE %s='%s'";
		sb = new StringBuilder();
		formatter = new Formatter(sb, Locale.US);
		formatter.format(query, DB_NAME, "Restaurants", "RestaurantName", rate.restaurantName);
		rs = stmt.executeQuery(sb.toString());
		formatter.close();
		
		int rest_id = 0;
		float oldAverageRating = 0;
		int oldNumOfRates = 0;
		if(rs.next()){
			rest_id = rs.getInt(1);
			oldAverageRating = rs.getFloat(8);
			oldNumOfRates = rs.getInt(9);
		}
		
		//int ratesNum = customer.rates.size();
		//for(int i = 0; i < ratesNum; i++){
			//Rate rate = customer.rates.get(i);
			
		query = "SELECT * FROM %s.MyRate WHERE %s='%d' AND %s='%d';";
		sb = new StringBuilder();
		formatter = new Formatter(sb, Locale.US);
		formatter.format(query, DB_NAME, "UserID", user_id, "RestaurantID", rest_id);
		rs = stmt.executeQuery(sb.toString());
		formatter.close();
		int rate_id = 0;
		float oldRating = 0;
		if(rs.next()){
			rate_id = rs.getInt(1);
			oldRating = rs.getFloat(4);
		}
		if(rate_id != 0){	
			System.out.println("Rate already exists, now updating it ...");
			
			String command = "UPDATE %s.MyRate SET %s='%f',%s='%s' WHERE %s='%d' AND %s='%d'";
			sb = new StringBuilder();
			formatter = new Formatter(sb, Locale.US);
			formatter.format(command, DB_NAME, "Rating", rate.rating, "Review", rate.review, "UserID", user_id, "RestaurantID", rest_id);
			stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			formatter.close();
			System.out.println("MyRate updated! now update average rating ...");
			
			float newAverageRating = (oldAverageRating * (float)oldNumOfRates - oldRating + rate.rating) / (float)oldNumOfRates;
			command = "UPDATE %s.Restaurants SET %s='%f' WHERE %s='%d'";
			sb = new StringBuilder();
			formatter = new Formatter(sb, Locale.US);
			formatter.format(command, DB_NAME, "AverageRating", newAverageRating, "RestaurantID", rest_id);
			stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			formatter.close();
			System.out.println("Average rating updated!");
		}
		else{

			System.out.println("Adding new rate in MyRate ...");
			sb = new StringBuilder();
			String command = "REPLACE INTO %s.MyRate(%s,%s,%s,%s) VALUES('%d','%d','%f','%s');";
			formatter = new Formatter(sb, Locale.US);
			formatter.format(command, DB_NAME, "UserID", "RestaurantID", "Rating", "Review"
					, user_id, rest_id, rate.rating, rate.review);
			System.out.println(sb.toString());
			stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			formatter.close();
			System.out.println("Rate added! Now update average rating ...");
			
			float newAverageRating = (oldAverageRating * (float)oldNumOfRates + rate.rating) / (float)(oldNumOfRates + 1.0);
			int newNumOfRates = oldNumOfRates + 1;
			command = "UPDATE %s.Restaurants SET %s='%f',%s='%d' WHERE %s='%d'";
			sb = new StringBuilder();
			formatter = new Formatter(sb, Locale.US);
			formatter.format(command, DB_NAME, "AverageRating", newAverageRating, "NumberOfRates", newNumOfRates, "RestaurantID", rest_id);
			stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			formatter.close();
			System.out.println("Average rating updated!");
		}
		//}
	}
    
    public boolean loginMatch(Statement stmt, HashMap<String, String> loginInfo){
        try{
            String query = "SELECT * FROM %s.%s WHERE %s='%s'";
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);
            formatter.format(query, DB_NAME, TABLE_USER, COLUMN_NAME, loginInfo.get("UserName"));
            ResultSet rs = stmt.executeQuery(sb.toString());
            formatter.close();

            int user_id = 0;
            String password = "";
            String identification = "";
            if(rs.next()){
                user_id = rs.getInt(1);
                System.out.println("user_id is: " + user_id);
                /*password = rs.getString(3);
                identification = rs.getString(4);
                String inputPassword = loginInfo.get("Password");
                String inputIdentification = loginInfo.get("Identification");
                if(password.equals(inputPassword) && identification.equals(inputIdentification))
                    return true;
                else    return false;*/
            }
            if(user_id == 0){
                System.out.println("User does not exist!");
                return false;
            }
            else{
                password = rs.getString(3);
                System.out.println("password is: "+password);
                identification = rs.getString(4);
                System.out.println("identification is: "+identification);
                String inputPassword = loginInfo.get("Password");
                String inputIdentification = loginInfo.get("Identification");
                if(password.equals(inputPassword) && identification.equals(inputIdentification)){
                    System.out.println("Matched!");
                    return true;
                }
                else{
                    System.out.println("Info does not match!");
                    return false;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        
    }
    
    public SingleRestaurant getRestDetail(Statement stmt, String restName){

    	ArrayList<String> dishes = new ArrayList<String>();

    	try{
    		String query = "SELECT * FROM %s.%s WHERE %s='%s';";
    		StringBuilder sb = new StringBuilder();
    		Formatter formatter = new Formatter(sb, Locale.US);
    		formatter.format(query, DB_NAME, "Restaurants", "RestaurantName", restName);
    		ResultSet rs = stmt.executeQuery(sb.toString());

    		if(rs.next()){
    			System.out.println("restaurant found!");
    			int rest_id = rs.getInt(1);
    			int user_id = rs.getInt(2);
    			int telephone = rs.getInt(4);
    			String location = rs.getString(5);
    			String openHour = rs.getString(6);
    			float averageRating = rs.getFloat(8);
    			int numberOfRates = rs.getInt(9);

    			query = "SELECT * FROM %s.Dishes WHERE %s='%d';";
    			sb = new StringBuilder();
    			formatter = new Formatter(sb, Locale.US);
    			formatter.format(query, DB_NAME, "RestaurantID", rest_id);
    			rs = stmt.executeQuery(sb.toString());
    			while(rs.next()){
    				String dishName = rs.getString(3); 
    				dishes.add(dishName);
    			}

    			SingleRestaurant rest = new SingleRestaurant(user_id, restName, location, telephone, openHour, averageRating,
    					numberOfRates, dishes);
    			return rest;
    		}
    		System.out.println("Nothing");
    		return null;
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }


    public void deleteFav(Statement stmt, String userName, String restName){

    	try{
    		String query = "SELECT * FROM %s.%s WHERE %s='%s';";
    		StringBuilder sb = new StringBuilder();
    		Formatter formatter = new Formatter(sb, Locale.US);
    		formatter.format(query, DB_NAME, "Restaurants", "RestaurantName", restName);
    		ResultSet rs = stmt.executeQuery(sb.toString());

    		if(rs.next()){
    			System.out.println("restaurant found!");
    			int rest_id = rs.getInt(1);
    			
    			query = "SELECT * FROM %s.%s WHERE %s='%s';";
        		sb = new StringBuilder();
        		formatter = new Formatter(sb, Locale.US);
        		formatter.format(query, DB_NAME, "User", "UserName", userName);
        		rs = stmt.executeQuery(sb.toString());
        		
        		if(rs.next()){
        			System.out.println("Customer found!");
        			int user_id = rs.getInt(1);

        			String command = "DELETE FROM %s.MyFavorite WHERE %s='%d' AND %s='%s';";
        			sb = new StringBuilder();
        			formatter = new Formatter(sb, Locale.US);
        			formatter.format(command, DB_NAME, "RestaurantID", rest_id, "CustomerID", user_id);
        			stmt.executeUpdate(sb.toString());
        		}
    		}
  
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }

    public void delMyReserve(Statement stmt, Customer customer, Reservation reserve){

    	try{
    		String query = "SELECT * FROM %s.%s WHERE %s='%s';";
    		StringBuilder sb = new StringBuilder();
    		Formatter formatter = new Formatter(sb, Locale.US);
    		formatter.format(query, DB_NAME, "Restaurants", "RestaurantName", reserve.restName);
    		ResultSet rs = stmt.executeQuery(sb.toString());

    		if(rs.next()){
    			System.out.println("restaurant found!");
    			int rest_id = rs.getInt(1);
    			
    			query = "SELECT * FROM %s.%s WHERE %s='%s';";
        		sb = new StringBuilder();
        		formatter = new Formatter(sb, Locale.US);
        		formatter.format(query, DB_NAME, "User", "UserName", customer.userName);
        		rs = stmt.executeQuery(sb.toString());
        		
        		if(rs.next()){
        			System.out.println("Customer found!");
        			int user_id = rs.getInt(1);

        			String command = "DELETE FROM %s.MyReservation WHERE %s='%d' AND %s='%s' AND %s='%s';";
        			sb = new StringBuilder();
        			formatter = new Formatter(sb, Locale.US);
        			formatter.format(command, DB_NAME, "RestaurantID", rest_id, "UserID", user_id, "ReserveTime", reserve.reserveTime);
        			stmt.executeUpdate(sb.toString());
        		}
    		}
  
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public ArrayList<String> getAllRests(Statement stmt){
    	ArrayList<String> allRests = new ArrayList<String>();
    	try{
    		String query = "SELECT * FROM %s.%s;";
    		StringBuilder sb = new StringBuilder();
    		Formatter formatter = new Formatter(sb, Locale.US);
    		formatter.format(query, DB_NAME, "Restaurants");
    		ResultSet rs = stmt.executeQuery(sb.toString());

    		while(rs.next()){
    			System.out.println("Adding one restaurant ...");
    			String restName = rs.getString(3);
    			
    			allRests.add(restName);
    		}
    		return allRests;
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		return allRests;
    	}
    }
    
    public void delMyRate(Statement stmt, Rate rate){
    	try{
    		String query = "SELECT * FROM %s.%s WHERE %s='%s';";
    		StringBuilder sb = new StringBuilder();
    		Formatter formatter = new Formatter(sb, Locale.US);
    		formatter.format(query, DB_NAME, "Restaurants", "RestaurantName", rate.restaurantName);
    		ResultSet rs = stmt.executeQuery(sb.toString());

    		if(rs.next()){
    			System.out.println("restaurant found!");
    			int rest_id = rs.getInt(1);
    			float oldAverageRating = rs.getFloat(8);
    			int oldNumOfRates = rs.getInt(9);
    			
    			query = "SELECT * FROM %s.%s WHERE %s='%s';";
        		sb = new StringBuilder();
        		formatter = new Formatter(sb, Locale.US);
        		formatter.format(query, DB_NAME, "User", "UserName", rate.customerName);
        		rs = stmt.executeQuery(sb.toString());
        		formatter.close();
        		
        		if(rs.next()){
        			System.out.println("Customer found!");
        			int user_id = rs.getInt(1);
        			
        			query = "SELECT * FROM %s.MyRate WHERE %s='%d' AND %s='%d';";
    				sb = new StringBuilder();
    				formatter = new Formatter(sb, Locale.US);
    				formatter.format(query, DB_NAME, "UserID", user_id, "RestaurantID", rest_id);
    				rs = stmt.executeQuery(sb.toString());
    				formatter.close();
    				float oldRating = 0;
    				if(rs.next()){
    					oldRating = rs.getFloat(4);
    				}

        			String command = "DELETE FROM %s.MyRate WHERE %s='%d' AND %s='%d';";
        			sb = new StringBuilder();
        			formatter = new Formatter(sb, Locale.US);
        			formatter.format(command, DB_NAME, "UserID", user_id, "RestaurantID", rest_id);
        			stmt.executeUpdate(sb.toString());
        			System.out.println("Personal rating deleted! Now update average rating ...");
        			
        			if(oldNumOfRates == 1){
        				System.out.println("Now no one rates the restaurant!");
        				command = "UPDATE %s.Restaurants SET %s='%f',%s='%d' WHERE %s='%d'";
        				sb = new StringBuilder();
        				formatter = new Formatter(sb, Locale.US);
        				float newRating = 0;
        				int newNumber = 0;
        				formatter.format(command, DB_NAME, "AverageRating", newRating, "NumberOfRates", newNumber, "RestaurantID", rest_id);
        				stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
        				formatter.close();
        				System.out.println("Average rating updated!");
        			}
        			else{
        				
        				float newAverageRating = (oldAverageRating * (float)oldNumOfRates - oldRating) / ((float)(oldNumOfRates - 1));
        				int newNumOfRates = oldNumOfRates - 1;
        				command = "UPDATE %s.Restaurants SET %s='%f',%s='%d' WHERE %s='%d'";
        				sb = new StringBuilder();
        				formatter = new Formatter(sb, Locale.US);
        				formatter.format(command, DB_NAME, "AverageRating", newAverageRating, "NumberOfRates", newNumOfRates, "RestaurantID", rest_id);
        				stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
        				formatter.close();
        				System.out.println("Average rating updated!");
        			}
        		}
    		}
  
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
     
    public ArrayList<Rate> getRestRatings(Statement stmt, String restName){
    	ArrayList<Rate> rates = new ArrayList<Rate>();

    	try{
    		String query = "SELECT * FROM %s.Restaurants WHERE %s='%s';";
			StringBuilder sb = new StringBuilder();
			Formatter formatter = new Formatter(sb, Locale.US);
			formatter.format(query, DB_NAME, "RestaurantName", restName);
			ResultSet rs = stmt.executeQuery(sb.toString());
			int rest_id = 0;
			if(rs.next()){
				rest_id = rs.getInt(1); 
			}
    		
    		query = "SELECT * FROM %s.%s WHERE %s='%s';";
    		sb = new StringBuilder();
    		formatter = new Formatter(sb, Locale.US);
    		formatter.format(query, DB_NAME, "MyRate", "RestaurantID", rest_id);
    		rs = stmt.executeQuery(sb.toString());
    		ArrayList<Integer> userIDs = new ArrayList<Integer>();
    		ArrayList<Float> ratings = new ArrayList<Float>();
    		ArrayList<String> reviews = new ArrayList<String>();

    		while(rs.next()){
    			System.out.println("review found!");
    			int user_id = rs.getInt(2);
    			float rating = rs.getFloat(4);
    			String review = rs.getString(5);
    			
    			userIDs.add(user_id);
    			ratings.add(rating);
    			reviews.add(review);
    		}

    		for(int i = 0; i < userIDs.size(); i++){
    			query = "SELECT * FROM %s.User WHERE %s='%d';";
        		sb = new StringBuilder();
        		formatter = new Formatter(sb, Locale.US);
        		formatter.format(query, DB_NAME, "UserID", userIDs.get(i));
        		rs = stmt.executeQuery(sb.toString());
        		String userName = "";
        		if(rs.next()){
        			userName = rs.getString(2); 
        		}
        		Rate rate = new Rate(userName, restName, ratings.get(i), reviews.get(i));
        		rates.add(rate);
    		}
    		
    		return rates;
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		return rates;
    	}
    }
    
    public ArrayList<String> getRecommends(Statement stmt, String flavor){
    	ArrayList<String> recommends = new ArrayList<String>();
    	try{
    		String query = "SELECT * FROM %s.Restaurants WHERE %s='%s';";
    		StringBuilder sb = new StringBuilder();
    		Formatter formatter = new Formatter(sb, Locale.US);
    		formatter.format(query, DB_NAME, "Flavor", flavor);
    		ResultSet rs = stmt.executeQuery(sb.toString());
    		
    		while(rs.next()){
    			String recommend = rs.getString(3);
    			recommends.add(recommend);
    		}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return recommends;
    }
    
    public void addDishes(int restID,String dishName){
        String subCommand = "insert into ServerDatabase.Dishes(RestaurantID,DishName) "
                + "values(?,?);";
        try {
        PreparedStatement ps1 = myConnection.prepareStatement(subCommand);
        
        
            ps1.setInt(1, restID);
        
        ps1.setString(2, dishName);
        ps1.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
    }
    
    public void deleteDishes(int restID,String dishName){
        String subCommand = "delete from ServerDatabase.Dishes where RestaurantID='"+restID+"' and DishName='"+dishName+"';";
        try {
        PreparedStatement ps1 = myConnection.prepareStatement(subCommand);      
//        ps1.setInt(1, restID);       
//        ps1.setString(2, dishName);
        
        ps1.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
    }
}











