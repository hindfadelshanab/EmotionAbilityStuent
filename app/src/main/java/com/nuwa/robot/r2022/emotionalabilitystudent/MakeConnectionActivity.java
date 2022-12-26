package com.nuwa.robot.r2022.emotionalabilitystudent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.util.InetAddressUtils;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nuwa.robot.r2022.emotionalabilitystudent.listener.OnMessageReciveListener;
import com.nuwa.robot.r2022.emotionalabilitystudent.model.Phase;
import com.nuwa.robot.r2022.emotionalabilitystudent.networking.ClientThread;
import com.nuwa.robot.r2022.emotionalabilitystudent.networking.StudentClient;
import com.nuwa.robot.r2022.emotionalabilitystudent.networking.StudentSocketClient;
import com.nuwa.robot.r2022.emotionalabilitystudent.utils.Constants;
import com.nuwa.robot.r2022.emotionalabilitystudent.utils.PreferenceManager;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

public class MakeConnectionActivity extends AppCompatActivity   {
    public   String SERVER_IP ;
    private PreferenceManager preferenceManager ;
    Button button ;
    StudentSocketClient client ;
    Gson gson ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_connection);

        gson =new Gson();





        preferenceManager = new PreferenceManager(this);
        IntentIntegrator intentIntegrator = new IntentIntegrator(MakeConnectionActivity.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
        if (result != null) {
            SERVER_IP = result.getContents();
            try {
                Log.d("TAG", "onActivityResult:SERVER_IP " +SERVER_IP);

                client = StudentSocketClient.getInstance(new URI("ws://"+SERVER_IP+":8887") );


            } catch (URISyntaxException e) {
                e.printStackTrace();
                Log.d("TAGee", "onActivityResult: e"+e.getMessage());

            }
            client.connect();

            String myIp = getMyIPAddress().substring(0,getMyIPAddress().lastIndexOf("."));
            String serverIp = SERVER_IP.substring(0,SERVER_IP.lastIndexOf("."));
            if (myIp.equals(serverIp)){
                showDialog();
            }else {
                Toast.makeText(this, "Please make sure that the device is connected to the same network as the robot app", Toast.LENGTH_LONG).show();
                IntentIntegrator intentIntegrator = new IntentIntegrator(MakeConnectionActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
            }
          

        }
    }
    private void showDialog() {

        LottieDialog dialog = new LottieDialog(MakeConnectionActivity.this)
                .setAnimation(R.raw.successfully_connected)
                .setAnimationRepeatCount(LottieDialog.INFINITE)
                .setAutoPlayAnimation(true)
                .setMessageColor(Color.BLACK)
                .setMessage("Successfully connected")
                .setDialogBackground(Color.WHITE)
                .setCancelable(false)
                .setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent =new Intent(MakeConnectionActivity.this , MainActivity.class);
                                intent.putExtra("ip" ,SERVER_IP) ;
                                preferenceManager.putString(Constants.IPKEY,SERVER_IP);
                                startActivity(intent);

                            }
                        } , 2000);
                    }
                }) ;

        dialog.show();
    }
    public static String getMyIPAddress() {//P.S there might be better way to get your IP address (NetworkInfo) could do it.
        String myIP = null;
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (isIPv4)
                            myIP = sAddr;
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return myIP;
    }

//
//    @Override
//    public void OnMessageRecive(String Message) {
//        try {
//            JSONObject jObj = new JSONObject(Message);
//            String date = jObj.getString("messa geForStudentKey");
//            Phase phase =    gson.fromJson(date , Phase.class);
//            Log.d("TAG", "OnMessageRecive: " +phase.getId());
//            Log.d("TAG", "OnMessageRecive: " +phase.getQuestioncontent().getTitle());
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (date!=null)
//
//                    button.setText(date);
//                    Log.d("TAG", "onMessage:json string  "+date);
//qwq
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}