package com.ecit.ayden.tracker;

import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/*
 * An object that holds information of such things that
 * will be used in certificate, such as username, password IMEI.
 */
public class Certification {

    private final String CERTIFICATION_PATH = "/sdcard/Tracker/Certification_info";
    private String username;
    private String password;
    private String IMEI;
    private static byte tempID;
    private TelephonyManager tm;

    public Certification(TelephonyManager tm_) {
        tm = tm_;
        username = null;
        password = null;
        IMEI = null;
        tempID = 0x00;
    }

    public boolean isNull() {
        if (username == null || password == null)
            return true;
        else
            return false;
    }

    public void save() {
        File file = new File(CERTIFICATION_PATH);
        String newLine = "\n";
        try {
            if (!file.exists())
                file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            out.write(username.getBytes());
            out.write(newLine.getBytes());
            out.write(password.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        File file = new File(CERTIFICATION_PATH);

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bin = new BufferedReader(fileReader);
            if (check_file_exist()) {
                username = bin.readLine();
                password = bin.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean check_file_exist() {
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

    public void setTempID(byte tempID_) { tempID = tempID_; }

    public byte getTempID() { return tempID; }

    public void setUsername(String username_) { username = username_; }

    public String getUsername() { return username; }

    public void setPassword(String password_) { password = password_; }

    public String getPassword() { return password; }
}
