package com.example.user.gourmetpitt.client;

import android.content.Context;

import com.example.user.gourmetpitt.util.LocalHost;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class DefaultSocketClient_DeleteDishes extends Thread{

    private Socket clientSocket;
    public ArrayList<String> msg = new ArrayList<String> ();
    public Context context;
    private String dishName;
    private int restaurantID;


    public DefaultSocketClient_DeleteDishes(Context context){
        this.context = context;
    }

    public void setDeleteDishes(String dishName,int restaurantID){
        this.dishName=dishName;
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
//          clientSocket = new Socket("128.237.203.53", 4431);
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
                oos.writeObject("deleteDishes");
                oos.flush();
                oos.writeObject(restaurantID);
                oos.flush();
                oos.writeObject(dishName);
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
