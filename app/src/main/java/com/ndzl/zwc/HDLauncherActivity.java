package com.ndzl.zwc;

import static android.widget.Toast.makeText;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
//import com.zebra.workstationconnect.IZVAService;
//https://developer.android.com/reference/android/hardware/display/DisplayManager#registerDisplayListener(android.hardware.display.DisplayManager.DisplayListener,%20android.os.Handler)

public class HDLauncherActivity extends AppCompatActivity  implements ServiceConnection {

    Intent starterIntent;
    private com.zebra.workstationconnect.IZVAService iServiceBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        starterIntent = getIntent();

        setContentView(R.layout.activity_hdlauncher);
        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        displayManager.registerDisplayListener(new DisplayManager.DisplayListener() {
            @Override
            public void onDisplayAdded(int displayId) {  //As a multi-screen-aware app, detect a 2nd screen when available
                //move this launcher to the 2nd screen when available
                finish();
                ActivityOptions ao =ActivityOptions.makeBasic();
                ao.setLaunchDisplayId(displayId);
                Bundle bao = ao.toBundle();
                starterIntent.setFlags(  Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(starterIntent, bao);
            }

            @Override
            public void onDisplayRemoved(int displayId) {

            }

            @Override
            public void onDisplayChanged(int displayId) {
                recreate();
            }
        }, null);


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }

    public void onClickbtn_STOREMANAGER(View v) {
        callProcessZVA("Config_MANAGER.json");
    }

    public void onClickbtn_OperatorOne(View v) {
        callProcessZVA("Config_CAMERA.json");
    }

    public void onClickbtn_OperatorTwo(View v) {
        callProcessZVA("Config_DIMENSIONING.json");
    }

    void callProcessZVA(String jsonConfig){
        try {
            if (iServiceBinder != null) {
                //String dataSet = loadJSONFromSDCard();

                String dataSet = loadJSONFromAsset( jsonConfig );
                String response = iServiceBinder.processZVARequest(dataSet);
                Log.i("callProcessZVA", "processZVARequest response=" + response);
            } else {
                Log.e("callProcessZVA", "res " + null);
                makeText(this, "Not Connected", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindtoZVAService();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.e("TAG", "res connecting");
        iServiceBinder = com.zebra.workstationconnect.IZVAService.Stub.asInterface(service);
        Log.e("TAG", "res connected");
        makeText(this, "Connected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        makeText(getApplicationContext(), "IPC server has disconnected unexpectedly", Toast.LENGTH_LONG).show();
        iServiceBinder = null;

    }
    String pkg="com.zebra.workstationconnect.release";
    private void bindtoZVAService() {
        Log.v("#ZWC#",pkg);
        Intent intent = new Intent(pkg);
        intent.setClassName(pkg, "com.zebra.workstationconnect.developerservice.DeviceManagementService");
        bindService(intent
                , this, BIND_AUTO_CREATE);
    }

    public String loadJSONFromAsset(String jsonAssetFileName) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(jsonAssetFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}