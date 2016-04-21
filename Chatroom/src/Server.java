import java.io.*;
import java.net.*;

public class Server{

	static ServerSocket serverSocket;
	static Socket socket;
	static DataOutputStream out;
	static DataInputStream in;
	static Users[] user = new Users[10];
	
	public static void main(String[] args) throws Exception{
		System.out.println("Starting Server...");
		serverSocket = new ServerSocket(7777);
		System.out.println("Server Started...");
		while(true){
			socket = serverSocket.accept();
			for(int i = 0; i < 10; i++){
				System.out.println("Connection From: " + socket.getInetAddress());
				out = new DataOutputStream(socket.getOutputStream());
				in = new DataInputStream(socket.getInputStream());
				if(user[i] == null){
					user[i] = new Users(out,in,user, i);
					Thread thread = new Thread(user[i]);
					thread.start();
					break;
				}
			}
		}
	}
}

class Users implements Runnable{

	DataOutputStream out;
	DataInputStream in;
	Users[] user = new Users[10];
	String name;
	int myNum;

	public Users(DataOutputStream out, DataInputStream in, Users[] user, int myNum){
		this.out = out;
		this.in = in;
		this.user = user;
		this.myNum = myNum;
	}

	public void run(){
		try{
			name = in.readUTF();
		} catch(IOException e){
			user[myNum] = null;
			return;
		}
		while(true){
			try{
				String message = in.readUTF();
				for(int i = 0; i < 10; i++){
					if(user[i] != null){
						user[i].out.writeUTF(name + ": " + message);
					}
				}
			} catch(IOException e){
				user[myNum] = null;
				return;
			}
		}
	}
}