package com.ecit.ayden.tracker;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by ayden on 1/26/16.
 */
public class NetworkService {
    boolean isStarted = false;
    NetThread netThread = new NetThread();

    private void start() {
        netThread.start();
        isStarted = true;
    }
}

class NetThread extends Thread {

    // Socket relateed...
    InetSocketAddress sockaddr = new InetSocketAddress("192.168.1.102", 1555);
    Socket socket = new Socket();

    @Override
    public void run() {

        // Connecting until successed.
        connecting();

            /*
              * Sleep until works is appear...might interrupt
              * by other Service.
              */
        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
            }
        }
    }


    private void connecting() {
        while (true) {
            try {
                if (socket.isClosed())
                    throw new IOException();
                if (socket.isConnected())
                    return;
                socket.connect(sockaddr);
            } catch (IOException e) {
                socket = new Socket();
            }
        }
    }

    private void connectionChecking() {
        if (socket.isClosed() || socket.isConnected())
            connecting();
    }
}

