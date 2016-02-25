package com.ecit.ayden.tracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Dialog for initializing the user use this app first time.
    private BroadcastReceiver debugReceiver = null;
    private Button start = null;
    private Button register = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start Button.
        start = (Button) findViewById(R.id.startButton);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CoreService.class);
                startService(intent);
            }
        });

        register = (Button)findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            EditText editUser = (EditText)findViewById(R.id.editUser);
            EditText editPass = (EditText)findViewById(R.id.editPassword);
            EditText editRetype = (EditText)findViewById(R.id.editPassRe);
            String username = null;
            String password = null;
            @Override
            public void onClick(View v) {
                username = editUser.getText().toString();
                password = editPass.getText().toString();
                if (password != editRetype.getText().toString()) {
                    Toast.makeText(MainActivity.this, "password you enter is different in two times", Toast.LENGTH_SHORT).show();
                } else {
                    Certification.setUsername(username);
                    Certification.setPassword(password);
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
        LocalBroadcastManager.getInstance(this).
                registerReceiver(debugReceiver, new IntentFilter(CoreService.CoreServiceDebugFilter));
    }
}