package com.ecit.ayden.tracker;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by ayden on 2/21/16.
 */
public class Certification {

    private static String username = null;
    private static String password = null;
    private static String IMEI = null;

    private TelephonyManager tm;
    public Certification(TelephonyManager tm_) {
        tm = tm_;
        IMEI = tm.getDeviceId();
    }

    public String getIMEI() throws SecurityException {
        try {
            IMEI = tm.getDeviceId();
            return IMEI;
        } catch (SecurityException se) {
            throw se;
        }
    }

    public void setUsername(String username_) {
        username = username_;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password_) {
        password = password_;
    }

    public String getPassword() {
        return password;
    }
}
