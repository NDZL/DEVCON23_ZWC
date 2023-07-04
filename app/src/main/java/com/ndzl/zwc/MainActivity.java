package com.ndzl.zwc;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowMetrics;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/*
* ZEBRA WORKSTATION CONNECT EXERCISER - USING BETA APIs AVAILABLE AS OF JUNE 2023
* In the Beta version of Workstation Connect, the administrator must use XML sent to the AccessMgr CSP
(for example, through StageNow) or Managed Configurations sent to OemConfig, to grant third-party
applications the right to submit the configuration to Workstation Connect as JSON strings. A new method
will be required in post-Beta versions.
* 
* */
public class MainActivity extends AppCompatActivity {

    private final static String TAG1 = "LIFECYCLE";
    String last_activity_state ="N/A";
    TextView tvOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tvOut = findViewById(R.id.txtOut);
        tvOut.setText( largeScreenInfo() );
        Log.i(TAG1, "onCreate");
        last_activity_state = "onCreate";
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG1, "onStart");
        last_activity_state = "onStart";
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG1, "onResume");
        last_activity_state = "onResume";
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG1, "onPause");
        last_activity_state = "onPause";
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG1, "onStop");
        last_activity_state = "onStop";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG1, "onDestroy");
        last_activity_state = "onDestroy";
    }



    public void bt1click(View v) {
        ActivityOptions ao = ActivityOptions.makeBasic();
        ao.setLaunchDisplayId(  ao.getLaunchDisplayId()  );
        Bundle bao = ao.toBundle();

        Intent intent= new Intent(getBaseContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent, bao);
    }

    public void bt2click(View v) {


        ActivityOptions ao = ActivityOptions.makeBasic();

        int cur_display = getDisplay().getDisplayId();
        int other_display = -1;
        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();
        for (Display _d:displays) {
            if(_d.getDisplayId() != cur_display) {
                other_display = _d.getDisplayId();
                break;
            }
        }

        if(other_display>-1) {
            ao.setLaunchDisplayId(other_display);
            Bundle bao = ao.toBundle();

            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(intent, bao);
        }
    }


    public void btClick_HDLauncher(View v) {
        //Launch on 2nd display if available
        ActivityOptions ao = ActivityOptions.makeBasic();
        int other_display_id = -1;
        int cur_display_id = getDisplay().getDisplayId();
        if(cur_display_id>0){
            other_display_id = cur_display_id;
        } else {
            DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
            Display[] displays = displayManager.getDisplays();
            for (Display _d : displays) {
                if (_d.getDisplayId() >0 ) {
                    other_display_id = _d.getDisplayId();
                    break;
                }
            }
        }

        ao.setLaunchDisplayId(other_display_id);
        Bundle bao = ao.toBundle();
        Intent intent= new Intent(getBaseContext(), HDLauncherActivity.class);
        intent.setFlags(  Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent, bao);
    }
    String largeScreenInfo(){
        StringBuilder _sb = new StringBuilder();

        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();
        for (Display _d:displays) {
           _sb.append( "DISPLAY ID="+_d.getDisplayId()+" " ) ;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                _sb.append( _d.getDeviceProductInfo().getProductId()+"\n" ) ;
            }
            _sb.append( "NAME="+_d.getName()+" " ) ;

            _sb.append("\n");
        }

        Display activityDisplay = getDisplay();
        _sb.append( "ACTIVITY DISPLAY ID="+activityDisplay.getDisplayId()+"\n" ) ;

        _sb.append("\n");
        WindowMetrics maximumWindowMetrics = getWindowManager().getMaximumWindowMetrics();
        WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();
        _sb.append( "METRICS MAX="+maximumWindowMetrics.getBounds().width()+"x"+maximumWindowMetrics.getBounds().height()+"\n" ) ;
        _sb.append( "METRICS CURRENT="+windowMetrics.getBounds().width()+"x"+windowMetrics.getBounds().height()+"\n" ) ;
        _sb.append("\n");

        _sb.append( "IS TOP ACTIVITY="+_IS_TOP_RESUMED_ACTIVITY+"\n");
        _sb.append( "LAST ACTIVITY STATE="+last_activity_state);

        return _sb.toString();
    }

    boolean _IS_TOP_RESUMED_ACTIVITY=false;
    @Override
    public void onTopResumedActivityChanged(boolean topResumed) {
        if (topResumed) {
            _IS_TOP_RESUMED_ACTIVITY = true;
        } else {
            _IS_TOP_RESUMED_ACTIVITY = false;
        }

        tvOut.setText( largeScreenInfo() );
    }


    /** Option 1: Bind to service through intent */
/*  private void  bindToZVAServiceThroughIntent() { Intent intent = new Intent();
        intent.setClassName(getAppPackageName(),
                "com.zebra.workstationconnect.developerservice.DeviceManagementService");
        bindService(intent, this, BIND_AUTO_CREATE); }*/

    /** Option 2: Bind to
     service through component */
/*  private void bindToZVAServiceThroughComponent() { Intent intent = new Intent();
        intent.setComponent(new ComponentName(getAppPackageName(),
                "com.zebra.workstationconnect.developerservice.DeviceManagementService"));
        bindService(intent, this, BIND_AUTO_CREATE); }*/

    /** Get app package name
     Add QUERY_ALL_PACKAGES permission in the AndroidManifest file in order to
     provide package visibility in Android 11 <uses-permission
     android:name="android.permission.QUERY_ALL_PACKAGES" /> * @return Package
     name */
    private String getAppPackageName() {
        String appPackageName = "";
        PackageManager pm = this.getPackageManager();

        final Intent secondaryIntent = new Intent(Intent.ACTION_MAIN, null);
        secondaryIntent.addCategory(Intent.CATEGORY_SECONDARY_HOME);
        final List<ResolveInfo> appsList = pm.queryIntentActivities(secondaryIntent, 0);

        for (ResolveInfo resolveInfo : appsList) {
            if (resolveInfo.activityInfo.packageName.contains(PACKAGE_NAME)) {
                appPackageName = resolveInfo.activityInfo.packageName;
            }
        }
        return appPackageName;
    }




    }