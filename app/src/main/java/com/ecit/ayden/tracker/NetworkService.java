package com.ecit.ayden.tracker;

import android.renderscript.ScriptGroup;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by ayden on 1/26/16.
 */
public class NetworkService {

    // This is use to prevent multiple instance of NetThread
    private static boolean Started = false;
    private static int batch = 5;
    private static boolean idle = false;
    private static boolean connected = false;
    NetThread netThread = new NetThread();

    public static int getBatch() {
        return batch;
    }

    public static boolean isConnected() {
        return connected;
    }

    public static boolean isIdle() {
        return idle;
    }

    private void start() {
        if (Started)
            return;
        netThread.start();
        Started = true;
    }

    class NetThread extends Thread {

        private Socket socket = new Socket();
        private InetSocketAddress sockaddr = new InetSocketAddress("192.168.1.102", 1555);
        private OutputStream out;
        private InputStream in;
        @Override
        public void run() {
            /*
             * Connecting until successed,
             * it also initialize an OutputStream
             */
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
                    in = socket.getInputStream();
                    out = socket.getOutputStream();
                    break;
                } catch (IOException e) {
                    socket = new Socket();
                }
            }
        }

        private void connectionChecking() {
            if (socket.isClosed() || socket.isConnected())
                connecting();
        }

        public void read() {

        }

        public void write() {

        }
    }
}



