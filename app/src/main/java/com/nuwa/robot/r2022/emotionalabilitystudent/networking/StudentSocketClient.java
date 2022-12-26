package com.nuwa.robot.r2022.emotionalabilitystudent.networking;

import android.os.Build;

import com.nuwa.robot.r2022.emotionalabilitystudent.listener.OnMessageReciveListener;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class StudentSocketClient extends WebSocketClient  {

    public static final int CONNECTED = 1;
    public static final int DISCONNECTED = 2;
    public static final int RECONNECTING = 2;



    public static final String HANDSHAKE_MESSAGE = "HANDSHAKE_MESSAGE";
    private String serverUrl = "ws://localhost:8887";
    private String ID = "Student";
    public int connectionState = 2;
    private int trialLimit = 5;
    private  long pingPeriod = 10000; // 10s
    private  boolean isConnectionOpened = false ;
    private  Thread connectionLiveThread ;
    private OnMessageReciveListener onMessageReciveListener ;


    int connectionTries = 0 ;
    int connectionTriesLimit = 3 ;

    private static StudentSocketClient studentSocketClient ;
    static List<IOnMessageListener> onMessageListeners = new ArrayList<>();


    public static StudentSocketClient getInstance(URI serverURI) {
     if (studentSocketClient == null)
         studentSocketClient = new StudentSocketClient( serverURI);
        return studentSocketClient;
    }

    public StudentSocketClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public StudentSocketClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("Hello, it is me. Mario :)");
        System.out.println("new connection opened");
        send(ID + ":;:" + HANDSHAKE_MESSAGE);

        connectionState = CONNECTED ;
        isConnectionOpened = true;
        send("Message");


    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        connectionState = DISCONNECTED ;
        if (connectionLiveThread != null){
            connectionLiveThread.interrupt();
        }
        System.out.println("closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(String message) {
//        if (message.contains(":;:")){
//            String [] arr = message.split(":;:");
//            if (arr[0].equals(ID)){
//                // This is handshake message :)
//                connectionState = CONNECTED ;
//                connectionTries--;
//                System.out.println("CONNECTED");
//
//            }
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            onMessageListeners.forEach(i -> i.onMessage(message));
        }

//        onMessageReciveListener.OnMessageRecive(message);
        System.out.println("received message: " + message);
    }

    @Override
    public void onMessage(ByteBuffer message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            onMessageListeners.forEach(i -> i.onMessage(message));
        }

        System.out.println("received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            onMessageListeners.forEach(i -> i.onError(ex));
        }

        System.err.println("an error occurred:" + ex);
    }

    public int startConnection()  {
        for (int i = 0; i <= trialLimit; i++) {
            System.out.println("startConnection = " + connectionState );
            if (connectionState != CONNECTED) {

                        close();

//                    client = new EmptyClient(new URI(serverUrl));
                   connect();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                startConnectionLiveThread();
                System.out.println("CONNECTED __ __ __ ");
                return CONNECTED;
            }
        }
        if (connectionState == CONNECTED){
            startConnectionLiveThread();
            return CONNECTED ;
        }
        return DISCONNECTED ;
//        throw new TimeoutException();
    }



    private void startConnectionLiveThread(){
        connectionLiveThread =  new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                    send(ID + ":;:" + HANDSHAKE_MESSAGE);
                    connectionTries++;

                        Thread.sleep(pingPeriod);
                        System.out.println("connectionTries= " + connectionTries);
                        if (connectionTries >= connectionTriesLimit) {
                            connectionState = DISCONNECTED;
                            System.out.println("DISCONNECTED");
                        } else if (connectionState > 1 && connectionTries < connectionTriesLimit) {
                            connectionState = RECONNECTING;
                            System.out.println("RECONNECTING");
                        }
                    } catch (Exception e) {
                        close();
//                        e.printStackTrace();
                    }
                }
            }
        });
       connectionLiveThread.start();

    }

    public interface IOnMessageListener{
        void onMessage(String msg);
        void onMessage(ByteBuffer message);
        void onError(Exception ex) ;
    }

    public static void addIOnMessageListener(IOnMessageListener i){
        onMessageListeners.add(i);
    }
    public static void removeIOnMessageListener(IOnMessageListener i){
        onMessageListeners.remove(i);
    }



    public static void main(String[] args) throws URISyntaxException {


    }
}
