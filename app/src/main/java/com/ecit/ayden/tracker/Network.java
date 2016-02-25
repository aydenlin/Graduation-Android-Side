package com.ecit.ayden.tracker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by ayden on 1/26/16.
 */
public class Network {

    private static final byte NORMAL_MODE = 0x01;
    private static final byte PRIORITY_MODE = 0x02;
    private byte mode = 0x00;

    private static boolean Started = false;
    private static boolean connected = false;
    private static final String ServerAddress = "121.40.234.38";
    private static final int ServerPort = 1555;
    private static Socket socket = null;
    private static InputStream in = null;
    private static OutputStream out = null;
    private static InetSocketAddress sockaddr  = new InetSocketAddress(ServerAddress, ServerPort);

    public Network() {
        mode = PRIORITY_MODE;

    }

    public static Socket getSocket() {
        return socket;
    }

    public static InetSocketAddress getSockaddr() {
        return sockaddr;
    }

    public static boolean isConnected() {
        return connected;
    }

    /*
    * Connecting until successed,
    * it also initialize an OutputStream
    */
    public void connecting() {
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

    /*
     * Check connection status, try to reconnect
     * if its socket is in disconnected state.
     */
    private void connectionChecking() {
        if (socket.isClosed() || socket.isConnected())
            connecting();
    }

    public void read(byte[] packet) {
        if (in != null) {
            byte[] result = new byte[Packer.MAX_LENGTH_OF_PACKET];
            try {
                in.read(result);
            } catch (IOException e) {
                /*
                 * codes deal with IOException we should
                 * deal with IOException in read method and
                 * it give a clean and easy to use interface to
                 * upper level.
                 */
            }
        } else {
            /* Method of in is non-initilize */
        }
    }

    public void write(byte[] packet) {
        if (out == null) {
            if (mode == NORMAL_MODE) {
            /*
             * In NORMAL_MODE Network module just
             * easily send location packet to Server.
             */

            } else if (mode == PRIORITY_MODE) {
                /*
                 * In PRIORITY_MODE Network module will send
                 * certification info in Certification module to Server.
                 */
            } else {
                /* mode number is undefined */
            }
        } else {
            /* OutputStream object is null, write is impossible. */

        }
    }
}






