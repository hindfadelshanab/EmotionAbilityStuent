package com.nuwa.robot.r2022.emotionalabilitystudent.networking;

import android.util.Log;

import com.nuwa.robot.r2022.emotionalabilitystudent.listener.OnMessageReciveListener;
import com.nuwa.robot.r2022.emotionalabilitystudent.utils.Constants;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientThread implements Runnable {

    private Socket socket;
    private BufferedReader input;
    private String serverIP ;

    private static volatile ClientThread INSTANCE = null;
    public   static  Thread thread ;
    OnMessageReciveListener onMessageReciveListener;

private ClientThread() {

}

    public static ClientThread getInstance() {
        if(INSTANCE == null ) {
            synchronized (ClientThread.class) {
                if (INSTANCE == null ) {
                    INSTANCE = new ClientThread();
                    if (thread == null){
                        new Thread(INSTANCE).start();
                    }else {
                        if (thread.isInterrupted()){
                            thread.start();
                        }else {
                            return INSTANCE ;
                        }
                    }
                }



            }
        }
        return INSTANCE;
    }


    public  void init(String serverIP , OnMessageReciveListener onMessageReciveListener){
    this.serverIP = serverIP;
    this.onMessageReciveListener = onMessageReciveListener;
    }

    @Override
    public void run() {

        try {

                InetAddress serverAddr = InetAddress.getByName(serverIP);

                socket = new Socket(serverAddr, Constants.SERVER_PORT);
                while (!Thread.currentThread().isInterrupted()) {
                    this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = input.readLine();
//                    if (message!=null) {
//                        onMessageReciveListener.OnMessageRecive(message.toString());
//                    }else {
////                        Toast.makeText(homeActivity, "No Data", Toast.LENGTH_SHORT).show();
//                    }
                    Log.d("ClientThread", "message:  " + message);

//                    Log.d("TAG message", "run: "+message);
                }


        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ClientThread", "run IOException: " +e.getMessage());
        }

    }
    public void sendMessageJson(String key ,final String message){

        new Thread(() -> {
            try {
                if (null != socket && message!=null) {
                    JSONObject json = new JSONObject();
                    json.put(key, message);




                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())),
                            true);
                    out.println(json.toString());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


    }

    public void sendMessage(final String message) {
        new Thread(() -> {
            try {
                if (null != socket) {

                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())),
                            true);
                    out.println(message);
//                    Log.d("TAG", "sendMessage: " +message);

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG", "sendMessage cccc: " + e.getMessage());

            }
        }).start();
    }

}
