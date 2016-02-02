package com.ecit.ayden.tracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by ayden on 1/29/16.
 */
public class CoreService extends Service {

    /*
      * Use to obtain location info, it will append location info
      * into Packetspace's send Queue.
      */
    LocProvider locProvider = new LocProvider();
    Messenger mMessenger = new Messenger(new CoreHandler());


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public class CoreHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
}

/* Runnable for Certification thread creation. */
class Certification  implements Runnable {
    @Override
    public void run() {

    }
}

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

    class PacketTransfer {

        // Location packet function.
        public static Packet locPacket(double longtitude, double latitude) {
            Packet packet = null;

            return packet;
        }

        // Certification data packet function.
        public static Packet CerPacket() {
            Packet packet = null;

            return packet;
        }
    }