package com.ecit.ayden.tracker;

import android.telephony.TelephonyManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/*
 * An object that holds information of such things that
 * will be used in certificate, such as username, password IMEI.
 */
public class Certification {

    private static String username = null;
    private static String password = null;
    private static String IMEI = null; 
    private static final String CERTIFICATION_PATH = "/sdcard/Tracker/Certification_info";

    private TelephonyManager tm;
    public Certification(TelephonyManager tm_) {
        tm = tm_;
    }

    public boolean isNull() {
        if (username == null || password == null)
            return true;
        else
            return false;
    }

    public void save() throws IOException {
        File file = new File(CERTIFICATION_PATH);
        String newLine = "\n";
        if (!file.exists())
            file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        out.write(username.getBytes());
        out.write(newLine.getBytes());
        out.write(password.getBytes());
    }

    public boolean check() {
        File file = new File(CERTIFICATION_PATH);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public String getIMEI() throws SecurityException {
        try {
            if (IMEI == null)
                IMEI = tm.getDeviceId();
            return IMEI;
        } catch (SecurityException se) {
            throw se;
        }
    }

    public static void setUsername(String username_) {
        username = username_;
    }

    public static String getUsername() {
        return username;
    }

    public static void setPassword(String password_) {
        password = password_;
    }

    public static String getPassword() {
        return password;
    }
}
