package com.example.user.gourmetpitt.client;

import android.content.Context;

import com.example.user.gourmetpitt.util.LocalHost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import model.Customer;
import model.SingleRestaurant;

/**
 * Created by User on 7/26/15.
 */
public class AddFavClient extends Thread {

    private Socket clientSocket;
    public Context context;
    public Customer customer;
    public SingleRestaurant restaurant;

    public AddFavClient(Context context, Customer customer, SingleRestaurant restaurant){
        this.context = context;
        this.customer = customer;
        this.restaurant = restaurant;
    }

    public void run(){
        if(openConnection()){
            System.out.println("Client Connected!");
            handleSession();
        }
    }

    public boolean openConnection(){
        try{
            LocalHost localHost = new LocalHost();
            clientSocket = new Socket(localHost.ipAddr, localHost.port);
            // System.out.println("Requesting Connection...");
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
//                msg.add("createUsers");
                oos.writeObject("addFav");
                oos.flush();

                oos.writeObject(customer);
                oos.flush();
                oos.writeObject(restaurant);
                oos.flush();

                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                String msg = (String) ois.readObject();

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
