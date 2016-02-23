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
            byte[] locPackert = Packer.locPacket(longtitude, latitude);
            PacketSpace.sendQueueAdd(locPackert);
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
