package com.ecit.ayden.tracker;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by ayden on 1/26/16.
 */
public class NetworkService {
    private static int batch = 5;
    private static boolean isStarted = false;
    public static boolean isIdle = false;
    NetThread netThread = new NetThread();

    public static int getBatch() {
        return batch;
    }

    private void start() {
        if (isStarted)
            return;
        netThread.start();
        isStarted = true;
    }

    class NetThread extends Thread {

        // Socket relateed...
        InetSocketAddress sockaddr = new InetSocketAddress("192.168.1.102", 1555);
        Socket socket = new Socket();
        OutputStream out;

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
}



