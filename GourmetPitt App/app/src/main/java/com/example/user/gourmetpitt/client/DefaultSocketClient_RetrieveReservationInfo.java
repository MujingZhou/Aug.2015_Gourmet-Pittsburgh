package com.example.user.gourmetpitt.client;

import android.content.Context;

import com.example.user.gourmetpitt.util.LocalHost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class DefaultSocketClient_RetrieveReservationInfo extends Thread{

    private Socket clientSocket;
    public ArrayList<String> msg = new ArrayList<String> ();
    public Context context;
    private int restaurantID;
    public ArrayList<String []> reserveList;

    public DefaultSocketClient_RetrieveReservationInfo(Context context){
        this.context = context;
    }

    public void setReservationInfo(int restaurantID){
        this.restaurantID=restaurantID;
    }

    public void run(){
        if(openConnection()){
            System.out.println("Client Connected!");
//            msg = new ArrayList<String> ();
            handleSession();
        }
    }

    public boolean openConnection(){
        try{
            LocalHost localHost = new LocalHost();
            clientSocket = new Socket(localHost.ipAddr, localHost.port);
//            clientSocket = new Socket("128.237.203.53", 4431);
//            clientSocket = new Socket("10.0.0.3", 4432);
            System.out.println("Requesting Connection...");
        }
        catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

//    public void handleSession(){
//
//    }

    public void handleSession(){
        try{
            synchronized(context) {
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.writeObject("retrieveReservationInfo");
                oos.flush();
                oos.writeObject(restaurantID);
                oos.flush();

                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                ArrayList<String []> reserveList=(ArrayList<String []>)ois.readObject();
                setReservationListForActivity(reserveList);
//                System.out.println(allRestaurant.getRestaurantName().get(0));
//                System.out.println(allRestaurant.getAllRestaurant().get(0).dishesName.get(0));



//                System.out.println(staticRestaurants.getRestaurantName().get(0));
//                System.out.println();
//                ArrayList <String> restName=(ArrayList <String>)ois.readObject();
//                System.out.println(restName.get(0));


                clientSocket.close();
                context.notifyAll();
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setReservationListForActivity(ArrayList<String []> reserveList){
        this.reserveList=reserveList;
    }

    public void closeSession() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
