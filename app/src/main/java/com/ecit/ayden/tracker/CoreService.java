package com.ecit.ayden.tracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

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

    // BroadcastManager  use to  send intent to UI thread and make changes on UI
    LocalBroadcastManager localBroadcastManager = null;
    /*
      * Use to obtain location info, it will append location info
      * into Packetspace's send Queue.
      */
    LocProvider locProvider = null;

    /*
      * Messenger that wait PacketSpace thread's msg and call
      * Handler to deal with it .
      */
    public static Messenger mMessenger = null;

    /*
     * NetworkService instance, you can use it to start Network Thread
     * all about Network Thread you shuould read doc in NetworkService.java.
      */
    private NetworkService networkService = null;

    private void certificationStart(String userName) {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        PacketTransfer.CerPacket(userName, imei);
    }


    private void send(String message) {
        Intent intent = new Intent(CoreServiceFilter);
        if (message != null)
            intent.putExtra(CoreServiceKey, message);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void Initializing() {
        mMessenger = new Messenger(new CoreHandler());
        locProvider = new LocProvider();
        networkService = new NetworkService();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isStarted)
            return START_STICKY;
        Initializing();
        isStarted = true;
        return START_STICKY;
    }


    /*
      * CoreHandler is use to handle received
      * messages, but in fact CoreHandler is do
      *  nothing just redirect the message to
      * certain module and the module will
      * deal with it.
      */
    public class CoreHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Packet p = (Packet) msg.obj;
            switch(p.type) {
                case Packet.LOCATION_PACKET:
                    break;
                case Packet.CERTIFICATION_PACKET:
                    break;
                default:
                    return;
            }
        }
    }


}

/* Runnable for Certification thread creation. */
class  Certification {
    private String IMEI = null;
    private Context context;
    private TelephonyManager tm;

    public Certification(Context context_, TelephonyManager tm_) {
        context = context_;
        tm = tm_;
    }

    public void getIMEI() {
        try {
            IMEI = tm.getDeviceId();
        } catch (SecurityException e) {
            Toast toast = new Toast(context);
            toast.setText("Permission READ_PHONE_STATE is required");
        }
    }

    public  void sendIMEI() {

    }
}

/*
  * Use to obtain location, it doesn't not hold the
  *  status of location
  */
class LocProvider {

    private boolean alreadyStarted = false;

    private LocationClient locationClient;
    private BDLocationListener mListener;
    private double latitude;
    private double longtitude;

    public void start(Context context) {
        locationClient = new LocationClient(context);
        mListener = new MyLocationListener();
        locationClient.registerLocationListener(mListener);
        setOptions();
        locatingStart();
    }

    public double getLongtitude() {
        return longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    private void locatingStart() {
        locationClient.start();
    }

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            longtitude = location.getLongitude();
            latitude = location.getLatitude();
        }
    }

    private void setOptions() {
        int span = 10000;
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setScanSpan(span);
        locationClient.setLocOption(option);
    }
}

/*
  * A function set include functions
  * that use to transfer data into raw bits.
  * and order, packet  the bits by byte by the meaning of it.
  */
class PacketTransfer {
     // Location packet function.
     public static Packet locPacket(double longtitude, double latitude) {
         Packet packet = null;

         return packet;
     }

    // Certification data packet function.
    public static Packet CerPacket(String userName, String password) {
        Packet packet = null;
        return packet;
    }
}