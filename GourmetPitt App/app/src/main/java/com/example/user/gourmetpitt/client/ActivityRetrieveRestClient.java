package com.example.user.gourmetpitt.client;

import android.app.Fragment;
import android.content.Context;

import com.example.user.gourmetpitt.util.LocalHost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import model.Rate;
import model.SingleRestaurant;

/**
 * Created by User on 7/28/15.
 */
public class ActivityRetrieveRestClient extends Thread {

    Socket clientSocket;
    Context context;
    String restName;
    public SingleRestaurant restDetail;
    public ArrayList<Rate> ratings;

    public ActivityRetrieveRestClient(Context context, String restName){
        this.context = context;
        this.restName = restName;
    }

    public void run(){
        if(openConnection()){
            System.out.println("Client connected!");
            handleSession();
        }
    }

    public boolean openConnection(){
        try{
            LocalHost localHost = new LocalHost();
            clientSocket = new Socket(localHost.ipAddr, localHost.port);
            System.out.println("Requesting Connection...");
        }
        catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void handleSession(){
        try{
            synchronized(context) {
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                /*oos.writeObject("Hello there");
                oos.flush();*/
                /*ArrayList<String> msg = new ArrayList<String>();
                msg.add("getRestDetail");*/
                oos.writeObject("getRestDetail");
                oos.flush();

                //msg = new ArrayList<String>();
                //msg.add(restName);
                oos.writeObject(restName);
                oos.flush();

                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                restDetail = (SingleRestaurant)ois.readObject();
                ratings = (ArrayList<Rate>)ois.readObject();

                context.notifyAll();
                clientSocket.close();
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
