package com.example.user.gourmetpitt.client;

import android.app.Fragment;

import com.example.user.gourmetpitt.util.LocalHost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.Customer;
import model.Reservation;

/**
 * Created by User on 7/27/15.
 */
public class DeleteMyReserveClient extends Thread {
    Socket clientSocket;
    Fragment fragment;
    Reservation reserve;
    Customer customer;

    public DeleteMyReserveClient(Fragment fragment, Customer customer, Reservation reserve){
        this.fragment = fragment;
        this.reserve = reserve;
        this.customer = customer;
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
                oos.writeObject("delMyReserve");
                oos.flush();

                //msg = new ArrayList<String>();
                //msg.add(restName);
                oos.writeObject(customer);
                oos.flush();
                oos.writeObject(reserve);
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
