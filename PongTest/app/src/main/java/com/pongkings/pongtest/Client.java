package com.pongkings.pongtest;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    static Socket socket;
    static DataOutputStream out;
    static DataInputStream in;
    GamePanel myGamePanel;

    public Client(GamePanel gp) throws Exception{
        myGamePanel = gp;
       // System.out.println("Connecting...");
        socket = new Socket("10.145.177.246", 7777);
       // System.out.println("Connection successful");
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        Input input = new Input(in, myGamePanel);
        Thread thread = new Thread(input);
        thread.start();
        Output output = new Output(out, myGamePanel);
        Thread thread2 = new Thread(output);
        thread.start();
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
        while(true){
            try{
                String message = in.readUTF();
                myGamePanel.setEnemyPlayerY(Integer.parseInt(message));

            } catch(IOException e){
            }
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
        myPlayerY = gp.getPlayerY();
    }

    public void run(){
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Enter your name and then press enter.");
//        String name = sc.nextLine();
//        out.writeUTF(name);
        while(true){
            int grabY = myGamePanel.getPlayerY();
            if(myPlayerY != grabY) {
                myPlayerY = grabY;
                String sendMessage = "" + myPlayerY;
                try {
                    out.writeUTF(sendMessage);
                }catch(IOException e){}
            }
        }
    }
}
