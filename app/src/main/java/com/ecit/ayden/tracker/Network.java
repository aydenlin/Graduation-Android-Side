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

    private static final String ServerAddress = "121.40.234.38";
    private static final int ServerPort = 1555;
    private  boolean connected;
    private  Socket socket;
    private  InputStream in;
    private  OutputStream out;
    private  InetSocketAddress sockaddr;


    public Network() {
        socket = new Socket();
        connected = false;
        in = null;
        out =null;
        sockaddr = new InetSocketAddress(ServerAddress, ServerPort);
    }

    public Socket getSocket() {
        return socket;
    }

    public InetSocketAddress getSockaddr() {
        return sockaddr;
    }

    public boolean isConnected() {
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

    public byte[] read() {
        byte[] result = new byte[Packer.MAX_LENGTH_OF_PACKET];
        if (in != null) {
            try {
                in.read(result);
            } catch (IOException e) {
                /*
                 * codes deal with IOException we should
                 * deal with IOException in read method and
                 * it give a clean and easy to use interface to
                 * upper level.
                 */
                e.printStackTrace();
            }
        } else {
            /* Method of in is non-initilize */
        }
        return result;
    }

    public void write(byte[] packet) {
        if (out == null) {
            try {
                out.write(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            /* OutputStream object is null, write is impossible. */

        }
    }
}






