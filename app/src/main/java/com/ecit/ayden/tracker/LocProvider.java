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
    private boolean alreadyStarted;
    private LocationClient locationClient;
    private BDLocationListener mListener;
    private double latitude;
    private double longtitude;
    private byte tempID;

    public LocProvider(Context context) {
        alreadyStarted = false;
        locationClient = new LocationClient(context);
        mListener = new MyLocationListener();
        latitude = 0;
        longtitude = 0;
        tempID = 0;
    }

    public void start(byte id) {
        setTempID(id);
        locationClient.registerLocationListener(mListener);
        setOptions();
        locatingStart();
    }

    public void setTempID(byte id) {
        tempID = id;
    }

    public byte getTempID() {
        return tempID;
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
            byte[] locPackert = Packer.locPacket(longtitude, latitude, tempID);
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
