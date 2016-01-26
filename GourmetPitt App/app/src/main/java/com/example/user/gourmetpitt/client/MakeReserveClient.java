package com.example.user.gourmetpitt.client;

import android.content.Context;

import com.example.user.gourmetpitt.util.LocalHost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.Customer;
import model.Reservation;
import model.SingleRestaurant;

/**
 * Created by User on 7/27/15.
 */
public class MakeReserveClient extends Thread {

    private Socket clientSocket;
    public Context context;
    public Customer customer;
    public Reservation reserve;

    public MakeReserveClient(Context context, Customer customer, Reservation reserve){
        this.context = context;
        this.customer = customer;
        this.reserve = reserve;
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
                oos.writeObject("makeReserve");
                oos.flush();

                oos.writeObject(customer);
                oos.flush();
                oos.writeObject(reserve);
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
