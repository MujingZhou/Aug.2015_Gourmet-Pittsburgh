package com.example.user.gourmetpitt.client;

import android.app.Fragment;
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
 * Created by User on 7/28/15.
 */
public class AllRestClient extends Thread {

    Socket clientSocket;
    Fragment fragment;
    public ArrayList<String> allRests;

    public AllRestClient(Fragment fragment){
        this.fragment = fragment;
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
                oos.writeObject("getAllRests");
                oos.flush();

                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                allRests = (ArrayList<String>)ois.readObject();

                fragment.notifyAll();
                clientSocket.close();
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
