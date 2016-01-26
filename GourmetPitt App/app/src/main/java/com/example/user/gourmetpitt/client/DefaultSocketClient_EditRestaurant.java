package com.example.user.gourmetpitt.client;

import android.content.Context;

import com.example.user.gourmetpitt.util.LocalHost;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class DefaultSocketClient_EditRestaurant extends Thread{

    private Socket clientSocket;
    public ArrayList<String> msg = new ArrayList<String> ();
    public Context context;

    private int restaurantID;
    private String newRestName;
    private String newLocation;
    private String newTelephone;
    private String newOpenHour;
    private String newFlavor;
    private ArrayList<String> editRestList;

    public DefaultSocketClient_EditRestaurant(Context context){
        this.context = context;
    }

    public void setEditRestaurant(int restaurantID,String newRestName,String newLocation,int newTelephone,String newOpenHour,String newFlavor){
        editRestList=new ArrayList<>();
        this.restaurantID=restaurantID;
        this.newRestName=newRestName;
        this.newLocation=newLocation;
        this.newTelephone=String.valueOf(newTelephone);
        this.newOpenHour=newOpenHour;
        this.newFlavor=newFlavor;

        editRestList.add(String.valueOf(this.restaurantID));
        editRestList.add(this.newRestName);
        editRestList.add(this.newLocation);
        editRestList.add(this.newTelephone);
        editRestList.add(this.newOpenHour);
        editRestList.add(this.newFlavor);

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
                oos.writeObject("editRestInfo");
                oos.flush();
                oos.writeObject(editRestList);
                oos.flush();

                clientSocket.close();
                context.notifyAll();
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void closeSession() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
