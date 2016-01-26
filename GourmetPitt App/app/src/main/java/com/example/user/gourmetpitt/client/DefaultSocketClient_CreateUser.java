package com.example.user.gourmetpitt.client;

import android.content.Context;

import com.example.user.gourmetpitt.util.LocalHost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class DefaultSocketClient_CreateUser extends Thread{

    private Socket clientSocket;
    public ArrayList<String> msg = new ArrayList<String> ();
    public ArrayList<String> sendlist = new ArrayList<String>();
    public Context context;

    public DefaultSocketClient_CreateUser(Context context,ArrayList<String> sendlist){
        this.context = context;
        this.sendlist =sendlist;
    }

    public void run(){
        if(openConnection()){
            System.out.println("Client Connected!");
            msg = new ArrayList<String> ();
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

    public void handleSession(){
        try{
            synchronized(context) {
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.writeObject("createUsers");
                oos.flush();
                oos.writeObject(sendlist);
                oos.flush();

                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                msg = (ArrayList<String>) ois.readObject();

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
