package com.nuwa.robot.r2022.emotionalabilitystudent.networking;

import android.util.Log;

import com.nuwa.robot.r2022.emotionalabilitystudent.listener.OnMessageReciveListener;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;

public class StudentClient extends WebSocketClient {
    OnMessageReciveListener onMessageReciveListener ;
    public static final String HANDSHAKE_MESSAGE = "HANDSHAKE_MESSAGE";
    private String ID = "Student";

    public StudentClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public StudentClient(URI serverURI , OnMessageReciveListener onMessageReciveListener) {
        super(serverURI);
        this.onMessageReciveListener =onMessageReciveListener;
    }
    public StudentClient(URI serverURI ) {
        super(serverURI);
    }

    public StudentClient(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("Hello, it is me. Mario :)");

        System.out.println("opened connection");

        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
    }

    @Override
    public void onMessage(String message) {
        System.out.println("received: " + message);
        onMessageReciveListener.OnMessageRecive(message);

//        try {
//            JSONObject jObj = new JSONObject(message);
//            String date = jObj.getString("messageForStudentKey");
//
//            Log.d("TAG", "onMessage:json string  "+date);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The close codes are documented in class org.java_websocket.framing.CloseFrame
        System.out.println(
                "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
                        + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }

//    public static void main(String[] args) throws URISyntaxException {
//        ExampleClient c = new ExampleClient(new URI(
//                "ws://localhost:8887")); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
//        c.connect();
//    }

}