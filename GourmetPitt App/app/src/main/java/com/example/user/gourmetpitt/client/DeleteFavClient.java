package com.example.user.gourmetpitt.client;

import android.app.Fragment;
import android.content.Context;

import com.example.user.gourmetpitt.util.LocalHost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.Customer;
import model.SingleRestaurant;

/**
 * Created by User on 7/26/15.
 */
public class DeleteFavClient extends Thread {

    Socket clientSocket;
    Fragment fragment;
    String restName;
    String customerName;

    public DeleteFavClient(Fragment fragment, String customerName, String restName){
        this.fragment = fragment;
        this.restName = restName;
        this.customerName = customerName;
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
            synchronized(fragment) {
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                /*oos.writeObject("Hello there");
                oos.flush();*/
                /*ArrayList<String> msg = new ArrayList<String>();
                msg.add("getRestDetail");*/
                oos.writeObject("deleteFav");
                oos.flush();

                //msg = new ArrayList<String>();
                //msg.add(restName);
                oos.writeObject(restName);
                oos.flush();
                oos.writeObject(customerName);
                oos.flush();

                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                String msg = (String)ois.readObject();

                fragment.notifyAll();
                clientSocket.close();
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

