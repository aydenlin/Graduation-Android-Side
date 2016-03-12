package com.ecit.ayden.tracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;

/**
 * Created by ayden on 1/29/16.
 */
public class CoreService extends Service {

    /*
  * IntentFilter for other components to recognize brocast
  * from CoreService and Key to get data from intent.
  */
    public static final String CoreServiceKey = "com.ecit.ayden.tracker.CoreService.MESSAGE";
    public static final String CoreServiceDebugFilter = "com.ecit.ayden.tracker.coreService.DEBUG";

    /* Flag use to prevent multipcall to onStartCommand */
    private boolean isStarted;
    // BroadcastManager  use to  send intent to UI thread and make changes on UI
    private static LocalBroadcastManager localBroadcastManager;
    private LocProvider locProvider;
    private Certification certification;
    private Network network;
    private Thread worker;

    public CoreService() {
        isStarted = false;
        certification = new Certification((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE));
        locProvider = new LocProvider(CoreService.this);
        network = new Network();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        worker = new Thread(new Worker(this ,network, certification, locProvider));
    }

    public void send(String message, String intent_) {
        Intent intent = new Intent(intent_);
        if (message != null)
            intent.putExtra(CoreServiceKey, message);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void start() {
        worker.start();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isStarted)
            return START_STICKY;
        start();
        isStarted = true;
        return START_STICKY;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}