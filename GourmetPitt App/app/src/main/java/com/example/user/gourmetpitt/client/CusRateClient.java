package com.example.user.gourmetpitt.client;

import android.app.Fragment;
import android.content.Context;

import com.example.user.gourmetpitt.util.LocalHost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.Rate;
import model.SingleRestaurant;

/**
 * Created by User on 7/26/15.
 */
public class CusRateClient extends Thread {

    Socket clientSocket;
    Context context;
    Rate rate;

    public CusRateClient(Context context, Rate rate){
        this.context = context;
        this.rate = rate;
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
                oos.writeObject("addCusRate");
                oos.flush();

                //msg = new ArrayList<String>();
                //msg.add(restName);
                oos.writeObject(rate);
                oos.flush();

                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                String msg = (String)ois.readObject();

                context.notifyAll();
                clientSocket.close();
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
