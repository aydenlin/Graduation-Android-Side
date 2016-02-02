package com.ecit.ayden.tracker;

import android.text.format.Time;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by ayden on 1/31/16.
 */
public class PacketSpace {
    public static ArrayBlockingQueue<Packet> sendQueue = new ArrayBlockingQueue<Packet>(10);
    public static ArrayBlockingQueue<Packet> recvQueue = new ArrayBlockingQueue<Packet>(10);


}

 class Packet {
    public byte type;
    public byte[] frame;
}

class PsManager extends Thread {
    @Override
    public void run() {

    }
}