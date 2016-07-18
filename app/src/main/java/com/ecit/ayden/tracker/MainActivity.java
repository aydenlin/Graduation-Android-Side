package com.ecit.ayden.tracker;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.LocationListener;
import android.os.IBinder;
import android.renderscript.ScriptGroup;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ServiceConfigurationError;

public class MainActivity extends AppCompatActivity {

    private Button start = null;
    private Button register = null;
    // Dialog for initializing the user use this app first time.
    private BroadcastReceiver debugReceiver = null;
    private CoreService coreBindService;
    private boolean isBound = false;
    Intent intent;

    private ServiceConnection coreConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            coreBindService = ((CoreService.coreBinder)service).getService();
            Toast.makeText(MainActivity.this, "Bind to coreService has done", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            coreBindService = null;
            Toast.makeText(MainActivity.this, "Unbind to coreService has done", Toast.LENGTH_SHORT).show();
        }
    };

    private void doBindService() {
        bindService(new Intent(MainActivity.this, CoreService.class), coreConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    private void doUnbindService() {
        if (isBound) {
            unbindService(coreConnection);
        }
    }

    private String getDeviceId() {
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(MainActivity.this, CoreService.class);
        startService(intent);

        // Start Button.
        start = (Button) findViewById(R.id.startButton);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coreBindService.setIMEI(getDeviceId());
                coreBindService.start(getApplicationContext());
                Toast.makeText(MainActivity.this, "Start", Toast.LENGTH_SHORT).show();
            }
        });

        register = (Button)findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            String username = null;
            String password = null;
            EditText editUser = (EditText) findViewById(R.id.editUser);
            EditText editPass = (EditText) findViewById(R.id.editPassword);
            EditText editRetype = (EditText) findViewById(R.id.editPassRe);

            @Override
            public void onClick(View v) {
                username = editUser.getText().toString();
                password = editPass.getText().toString();
                if (!password.equals(editRetype.getText().toString())) {
                    Toast.makeText(MainActivity.this, "pass and retype not match", Toast.LENGTH_SHORT).show();
                } else {
                    coreBindService.setCertifiInfo(username, password, getDeviceId());
                }
            }
        });

        debugReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(MainActivity.this, intent.getDataString(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        doBindService();
        LocalBroadcastManager.getInstance(this).
                registerReceiver(debugReceiver, new IntentFilter(CoreService.CoreServiceDebugFilter));
    }

    @Override
    protected void onStop() {
        super.onStop();
        doUnbindService();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(debugReceiver);
    }
}
