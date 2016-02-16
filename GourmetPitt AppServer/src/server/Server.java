package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args){
		ServerSocket listenSocket = null;
		Socket connSocket = null;
		
		try{
			listenSocket = new ServerSocket(4431);
			System.out.println("Server Opened!");
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		while(true){
			try{
				connSocket = listenSocket.accept();
				DefaultSocketServer server = new DefaultSocketServer(connSocket);
				server.start();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}
