import java.io.*;
import java.net.*;
import java.util.*;

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
		boolean first = false;
		while(true){
			socket = serverSocket.accept();
			for(int i = 0; i < 2; i++){
				System.out.println("Connection From: " + socket.getInetAddress());
				out = new DataOutputStream(socket.getOutputStream());
				in = new DataInputStream(socket.getInputStream());
				if(user[i] == null){
					user[i] = new Users(out,in,user, i);
					Thread thread = new Thread(user[i]);
					thread.start();
					if(!first){
						MissileControl missileControl = new MissileControl(user);
						Thread mcThread = new Thread(missileControl);
						mcThread.start();
						first = true;
					}
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
		int i = 0;
		if(myNum == 0){
			i = 1;
		}
		while(true){
			try{
				String message = in.readUTF();
				//System.out.println("Received from " + myNum + ": " + message + "\n");
			//	for(int i = 0; i < 10; i++){
				if(user[i] != null){
					user[i].out.writeUTF("P"+":"+message);
						//System.out.println("Sent to " + i + ": " + message + "\n");
				}
			//	}
			} catch(IOException e){
				user[myNum] = null;
				return;
			}
		}
	}
}

class MissileControl implements Runnable{
	long numMissiles = 0;
	Users[] user;
	ArrayList<Integer> missileX = new ArrayList<Integer>();
	ArrayList<Integer> missileX2 = new ArrayList<Integer>();
	
	public MissileControl(Users[] user){
		this.user = user;
	}

	public void run(){
		int count = 0;
		Random rand = new Random();
		
		while(true){
			count ++;
       	 //add missiles on timer
		 //long missileElapsed = (System.nanoTime() - missileStartTime);
       	 //System.out.println(missileElapsed + "\n");
       	 //if(missileElapsed > (2000 - count/12)){
			if(count % 500000000 == 0){
       	 	//first missile always goes down the middle
				if(numMissiles == 0){
				//	boolean added = false;
					for(int i = 0; i < 2; i++){
						System.out.println("For First\n");
                	//speed is controlled for now
						if(user[i] != null){
							try{
								if(i==0){
									missileX.add(856);
									//added = true;
								}
								else if(i==1){
									missileX2.add(856);
								}
									user[i].out.writeUTF("First Missile:"+0);
									System.out.println("First Missle FIRED!\n");
							} catch(IOException e){
								System.out.println("IOException\n");
							}
						}
					}
					numMissiles++;
				}
				else{
					double rando = rand.nextDouble();
					//boolean added = false;
					for(int i = 0; i < 2; i++){
						if(user[i] != null){
							try{
								if(i==0){
									missileX.add(856);
							//		added = true;
								}
								else if(i==1){
									missileX2.add(856);
								}
									user[i].out.writeUTF("New Missile:"+rando);
									System.out.println("Missle FIRED! to user: " + i + "\n");
							}catch(IOException e){
								System.out.println("Exception\n");
							}
						}
					}
				}
			}
			if(count % 4000000 == 0){
				for(int i = 0; i < missileX.size(); i++){
					int updateX = missileX.get(i)-6;
					missileX.set(i, missileX.get(i)-(6));
					if(missileX.get(i) < -50){
						missileX.remove(i);
					}
					//for(int j = 0; j < 2; j++){
						if(user[0] != null){
							try{
								user[0].out.writeUTF("Mis Upd:"+i+":"+updateX);
							} catch(IOException e){
								System.out.println(i+": "+updateX);
							}
							if(updateX < -100){
								try{
									user[0].out.writeUTF("Remove:"+i);
								} catch(IOException e){
									System.out.println(i+" Removed");
								}
							}
						}
					//}
				}
				for(int i = 0; i < missileX2.size(); i++){
					int updateX = missileX2.get(i)-6;
					missileX2.set(i, missileX2.get(i)-(6));
					if(missileX2.get(i) < -100){
						missileX2.remove(i);
					}
			//		for(int j = 0; j < 2; j++){
						if(user[1] != null){
							try{
								user[1].out.writeUTF("Mis Upd:"+i+":"+updateX);
							} catch(IOException e){
								System.out.println(i+": "+updateX);
							}
							if(updateX < -100){
								try{
									user[1].out.writeUTF("Remove:"+i);
								} catch(IOException e){
									System.out.println(i+" Removed");
								}
							}
						}
				//	}
				}
			}

		}
	}

}