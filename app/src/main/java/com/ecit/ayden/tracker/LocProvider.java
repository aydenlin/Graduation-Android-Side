package com.ecit.ayden.tracker;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/*
  * Use to obtain location, it doesn't not hold the
  *  status of location
  */
public class LocProvider {

    // true if locating started, in another case locating stopd.
    private boolean alreadyStarted = false;

    private LocationClient locationClient = null;
    private BDLocationListener mListener = null;
    private double latitude = 0;
    private double longtitude = 0;
    private Context context = null;

    public LocProvider(Context context_) {
        context = context_;
    }

    public void start() {
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
            byte[] locPackert = Packer.locPacket(longtitude, latitude);
            PacketSpace.QueueAdd(locPackert);
        }
    }

    private void setOptions() {
        // locate every 10 sec.
        int span = 10000;
        LocationClientOption option = new LocationClientOption();
        // In Hight accuracy mode, gps, data traffic, will be used.
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setScanSpan(span);
        locationClient.setLocOption(option);
    }
}
