import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

	static Socket socket;
	static DataOutputStream out;
	static DataInputStream in;
	
	public static void main(String[] args) throws Exception{
		System.out.println("Connecting...");
		socket = new Socket("10.145.177.246", 7777);
		System.out.println("Connection successful");
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		Input input = new Input(in);
		Thread thread = new Thread(input);
		thread.start();
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your name and then press enter.");
		String name = sc.nextLine();
		out.writeUTF(name);
		while(true){
			String sendMessage = sc.nextLine();
			out.writeUTF(sendMessage);
		}
	}
}

class Input implements Runnable{

	DataInputStream in;

	public Input(DataInputStream in){
		this.in = in;
	}

	public void run(){
		while(true){
			try{
				String message = in.readUTF();
				System.out.println(message);
			} catch(IOException e){
			}
		}
	}
}