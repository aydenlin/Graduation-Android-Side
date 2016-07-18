package com.ecit.ayden.tracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;

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
    private final IBinder mBinder = new coreBinder();
    private TelephonyManager tm;

    public CoreService() {
        isStarted = false;
        certification = new Certification();
        network = new Network();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    public class coreBinder extends Binder {
        CoreService getService() {
            return CoreService.this;
        }
    }

    public void setLocProvider(Context context) {
        locProvider = new LocProvider(context);
    }

    private void setWorker() {
        worker = new Thread(new Worker(this ,network, certification, locProvider));
    }

    public void send(String message, String intent_) {
        Intent intent = new Intent(intent_);
        if (message != null)
            intent.putExtra(CoreServiceKey, message);
        localBroadcastManager.sendBroadcast(intent);
    }

    public void start(Context context) {
        if (isStarted)
            return;
        if (certification.isNull()){
            certification.load();
        }
        setLocProvider(context);
        setWorker();
        worker.start();
        isStarted = true;
    }

    public void setCertifiInfo(String username, String pass, String imei) {
        setusername(username);
        setPassword(pass);
        setimei(imei);
        certification.save();
    }

    public void setIMEI(String imei) {
        setimei(imei);
    }

    private void setusername(String username) {
        certification.setUsername(username);
    }

    private void setPassword(String pass) {
        certification.setPassword(pass);
    }

    private void setimei(String imei) {
        certification.setIMEI(imei);
    }

    public String getimei() {
        return certification.getIMEI();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}