package com.ecit.ayden.tracker;

import android.telephony.TelephonyManager;
import android.util.Log;

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

    private final String CERTIFICATION_PATH = "/sdcard/Tracker";
    private final String CERTIFICATION_FILE = "/sdcard/Tracker/Certification_info";
    private String username;
    private String password;
    private String IMEI;

    public Certification()  {
        username = null;
        password = null;
        IMEI = null;
    }

    public void setNull() {
        username = null;
        password = null;
    }

    public boolean isNull() {
        if (username == null || password == null)
            return true;
        else
            return false;
    }

    public void save() {
        File file = new File(CERTIFICATION_FILE);
        File path = new File(CERTIFICATION_PATH);
        String newLine = "\n";
        try {
            if (!path.exists()) {
                path.mkdirs();
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            out.write(username.getBytes());
            out.write(newLine.getBytes());
            out.write(password.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        File file = new File(CERTIFICATION_FILE);

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
        File file = new File(CERTIFICATION_FILE);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public void setIMEI(String imei) {
        IMEI = imei;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setUsername(String username_) { username = username_; }

    public String getUsername() { return username; }

    public void setPassword(String password_) { password = password_; }

    public String getPassword() { return password; }
}
