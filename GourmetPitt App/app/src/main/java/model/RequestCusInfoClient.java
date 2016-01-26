package model;


import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class RequestCusInfoClient extends Thread {

    private Socket clientSocket;
    public ArrayList<String> nameAndType;
    public Context context;
    public Customer customerInfo;
    //private HashMap<String, String> loginInfo;

    public RequestCusInfoClient(Context context){
        this.context = context;
    }

    /*public void setLoginInfo(HashMap<String, String> loginInfo){
        this.loginInfo = loginInfo;
    }*/

    public void setNameAndType(ArrayList<String> nameAndType){
        this.nameAndType = nameAndType;
    }

    public void run(){
        if(openConnection()){
            System.out.println("Client Connected!");
            //nameAndType = new ArrayList<String> ();
            handleSession();
        }
    }

    public boolean openConnection(){
        try{
            clientSocket = new Socket("128.237.197.84", 8080);
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
                ArrayList<String> msg = new ArrayList<String>();
                msg.add("getCusInfo");
                oos.writeObject(msg);
                oos.flush();

                oos.writeObject(nameAndType);
                oos.flush();

                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                customerInfo = (Customer)ois.readObject();

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
