package com.pongkings.pongtest;

import android.graphics.BitmapFactory;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static Socket getSocket() {
        return socket;
    }

    static Socket socket;
    static DataOutputStream out;
    static DataInputStream in;
    GamePanel myGamePanel;


    public Client(GamePanel gp) throws Exception{
        myGamePanel = gp;
        myGamePanel.setClientRunning(true);
        System.out.println("Connecting...");
        socket = new Socket("10.146.32.21", 7777);// 10.146.240.71
        System.out.println("Connection successful");
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        Input input = new Input(in, myGamePanel);
        Thread thread = new Thread(input);
        thread.start();
        Output output = new Output(out, myGamePanel);
        Thread thread2 = new Thread(output);
        thread2.start();
    }


}

class Input implements Runnable{

    DataInputStream in;
    GamePanel myGamePanel;

    public Input(DataInputStream in, GamePanel gp){
        this.in = in;
        myGamePanel = gp;
    }

    public void run(){
        while(myGamePanel.getClientRunning()){
            try{
                    String message = in.readUTF();
                    String split[] = message.split(":");
                    if(split[0].equals("P")){
                        if (myGamePanel.getEnemyPlayer() != null) {
                            myGamePanel.setEnemyPlayerY(Integer.parseInt(split[1]));
                        }
                    }
                    else if(split[0].equals("First Missile")) {
                        if (myGamePanel.getMissileList() != null ) {
                            myGamePanel.addMissile(new Missile(BitmapFactory.decodeResource(myGamePanel.getResources(), R.drawable.
                                    missile), myGamePanel.WIDTH, myGamePanel.HEIGHT / 2, 45, 15, 200, 13));
                        }
                    }
                    else if(split[0].equals("New Missile")) {
                        if (myGamePanel.getMissileList() != null ) {
                                myGamePanel.addMissile(new Missile(BitmapFactory.decodeResource(myGamePanel.getResources(), R.drawable.missile),
                                        myGamePanel.WIDTH, (int) (Double.parseDouble(split[1]) * (myGamePanel.HEIGHT - (myGamePanel.getMaxBorderHeight() * 2)) + myGamePanel.getMaxBorderHeight()),
                                        45, 15, 200, 13));

                        }
                    }
                    else if(split[0].equals("Mis Upd")){
                        if(myGamePanel.getMissileList() != null){
                            if(myGamePanel.getMissileList().size()>0) {
                                myGamePanel.getMissileList().get(Integer.parseInt(split[1])).update(Integer.parseInt(split[2]));
                            }
                        }
                    }
                    else if(split[0].equals("Remove")){
                        if(myGamePanel.getMissileList() != null){
                            myGamePanel.getMissileList().remove(Integer.parseInt(split[1]));
                        }
                    }



            } catch(IOException e){ }
        }
    }
}

class Output implements Runnable{

    DataOutputStream out;
    GamePanel myGamePanel;
    int myPlayerY;


    public Output(DataOutputStream out, GamePanel gp){
        this.out = out;
        myGamePanel = gp;
        myPlayerY = GamePanel.HEIGHT/2; //starting y position for all players
    }

    public void run(){
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Enter your name and then press enter.");
//        String name = sc.nextLine();
          while(myGamePanel.getWidth()==0 || myGamePanel.getPlayer()==null || myGamePanel.getMissileList()==null){}
//          String width = ""+ myGamePanel.WIDTH;
//          if(myGamePanel.getPlayer()!=null && myGamePanel.getMissileList()!=null){
//              try {
//                  out.writeUTF(width);
//              } catch (IOException e) {
//              }
//          }
//        out.writeUTF(name);
        while(myGamePanel.getClientRunning()){
//            System.out.println("Client Out Running");
            if(myGamePanel.getPlayer()!=null) {
//                System.out.println("Player exists!");
                int grabY = myGamePanel.getPlayerY();
                if(myPlayerY != grabY) {
                    myPlayerY = grabY;
                    String sendMessage = "" + myPlayerY;
                    try {
                        out.writeUTF(sendMessage);
                        System.out.println("Sent! " + sendMessage);
                    } catch (IOException e) {
                    }
                }
            }
        }
    }
}
