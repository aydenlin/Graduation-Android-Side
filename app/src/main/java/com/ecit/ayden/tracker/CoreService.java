package com.ecit.ayden.tracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;

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
    public static final String CoreServiceFilter = "com.ecit.ayden.tracker.CoreService.INITIALIZING";
    public static final String CoreServiceKey = "com.ecit.ayden.tracker.CoreService.MESSAGE";

    //Messenger for communication between MainActivity and CoreService.
    private Messenger messenger = null;

    // BroadcastManager  use to  send intent to UI thread and make changes on UI
    LocalBroadcastManager localBroadcastManager = null;

    private LocProvider locProvider = null;
    private Certification certification = null;
    private Network network = null;

    private void send(String message) {
        Intent intent = new Intent(CoreServiceFilter);
        if (message != null)
            intent.putExtra(CoreServiceKey, message);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void Initializing() {
        certification = new Certification((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE));
        locProvider = new LocProvider();
        network = new Network();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    /*
 * CommandDeal is use to perform the operation specify
 * by Packet received.
 */
    public static void CommandDeal(byte[] packet) {
        byte type = Packer.getType(packet);

        switch(type) {
            case Packer.NEED_REGISTER:
                break;
            case Packer.NAME_ALREADY_USED:
                break;
            case Packer.PASSWORD_ERROR:
                break;
            case Packer.CONFIRMED:
                break;
        }
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