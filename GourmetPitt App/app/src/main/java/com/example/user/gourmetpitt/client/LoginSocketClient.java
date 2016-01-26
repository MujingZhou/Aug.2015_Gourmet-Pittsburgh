package com.example.user.gourmetpitt.client;

import android.content.Context;

import com.example.user.gourmetpitt.util.LocalHost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class LoginSocketClient extends Thread{

    private Socket clientSocket;
    public ArrayList<String> msg = new ArrayList<String> ();
    public Context context;
    private HashMap<String, String> loginInfo;

    public LoginSocketClient(Context context){
        this.context = context;
    }

    public void setLoginInfo(HashMap<String, String> loginInfo){
        this.loginInfo = loginInfo;
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
//            clientSocket = new Socket("128.237.197.84", 8080);
            clientSocket = new Socket(localHost.ipAddr, localHost.port);
//
// clientSocket = new Socket("128.237.203.53", 4431);
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
                /*oos.writeObject("Hello there");
                oos.flush();*/
//                msg = new ArrayList<String>();
//                msg.add("login");
                oos.writeObject("login");
                oos.flush();
                oos.writeObject(loginInfo);
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
