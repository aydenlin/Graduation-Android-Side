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
import com.ecit.ayden.tracker.ui.RegisterDialog;

public class MainActivity extends AppCompatActivity {

    // Dialog for initializing the user use this app first time.
    private RegisterDialog dialog = null;
    private BroadcastReceiver InitializingReceiver = null;
    private Button start = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start Button.
        start = (Button) findViewById(R.id.button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CoreService.class);
                startService(intent);
            }
        });

        InitializingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                dialog = new RegisterDialog();
                dialog.show(getFragmentManager(), "Initializing");
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(InitializingReceiver, new IntentFilter(CoreService.CoreServiceFilter));
    }
}

