package com.ecit.ayden.tracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;

import java.io.File;

/**
 * Created by ayden on 1/29/16.
 */
public class CoreService extends Service {

    /* Flag use to prevent multipcall to onStartCommand */
    private boolean isStarted = false;

    /*
      * IntentFilter for other components to recognize brocast
      * from CoreService and Key to get data from intent.
      */
    public static final String CoreServiceKey = "com.ecit.ayden.tracker.CoreService.MESSAGE";
    public static final String CoreServiceDebugFilter = "com.ecit.ayden.tracker.coreService.DEBUG";
    //Messenger for communication between MainActivity and CoreService.
    private Messenger messenger = null;

    // BroadcastManager  use to  send intent to UI thread and make changes on UI
    private static LocalBroadcastManager localBroadcastManager = null;

    private LocProvider locProvider = null;
    private Certification certification = null;
    private Network network = null;
    private Thread worker = null;

    public void send(String message, String intent_) {
        Intent intent = new Intent(intent_);
        if (message != null)
            intent.putExtra(CoreServiceKey, message);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void Initializing() {
        certification = new Certification((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE));
        locProvider = new LocProvider(CoreService.this);
        network = new Network();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        worker = new Thread(new Worker(this ,network, certification, locProvider));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isStarted)
            return START_STICKY;
        Initializing();
        isStarted = true;
        return START_STICKY;
    }

}