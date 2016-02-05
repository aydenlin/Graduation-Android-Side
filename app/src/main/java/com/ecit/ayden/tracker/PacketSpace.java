package com.ecit.ayden.tracker;

import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.format.Time;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by ayden on 1/31/16.
 */
public class PacketSpace {

    private PsWorker psWorker = new PsWorker();
    /* STATUS is true if its in connected state, false in another case */
    private boolean STATUS = false;
    public static final int capacity = 100;
    public static final int certifiCapacity = 10;
    public static ArrayBlockingQueue<Packet> sendQueue = new ArrayBlockingQueue<Packet>(capacity);
    public static ArrayBlockingQueue<Packet> recvQueue = new ArrayBlockingQueue<Packet>(capacity);
    public static ArrayBlockingQueue<Packet> certifiQueue = new ArrayBlockingQueue<Packet>(certifiCapacity);

    public static int isSomethingInSend() {
        return sendQueue.size();
    }

    public static int isSomethingInrecv() {
        return recvQueue.size();
    }

    public static boolean isSendEmpty() {
        return sendQueue.isEmpty();
    }

    public static boolean isRecvEmpty() {
        return recvQueue.isEmpty();
    }

    public static boolean isCertifiEmpty() {
        return certifiQueue.isEmpty();
    }

    public static void sendQueueAdd(Packet p) {
        sendQueue.add(p);
    }

    public static Packet sendQueueRetrive() {
        return sendQueue.poll();
    }

    public static void recvQueueAdd(Packet p) {
        recvQueue.add(p);
    }

    public static Packet recvQueueRetrive() {
        return recvQueue.poll();
    }

    public static void certifiQueueAdd(Packet p) {
        certifiQueue.add(p);
    }

    public static Packet certifiQueueRetrive() {
        return certifiQueue.poll();
    }

    /*
  * Thread use to moniter PacketSpace
  * , it send a message to CoreService
  *  and NetWork thread while something
  *  is in the PacketSpace need to deal with.
  */
    public class PsWorker extends Thread {

        @Override
        public void run() {
            while (true) {
                if (!isSendEmpty()) {
                    /* Do serveral packet at a time */
                    if (isSomethingInSend() == NetworkService.getBatch() && NetworkService.isIdle)
                        ThreadPool.Interrupt(ThreadPool.NETWORK_THREAD);
                }
                if (!isRecvEmpty()) {
                    /* Something in recvQueue */
                    Object obj = (Object)recvQueueRetrive();
                    try {
                        CoreService.mMessenger.send(Message.obtain(null, 0, obj));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                if (!isCertifiEmpty()) {
                    /* Something in CertifiQueue */
                    if (NetworkService.isIdle)
                        ThreadPool.Interrupt(ThreadPool.NETWORK_THREAD);
                }
                try {
                  Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


 class Packet {
    /* Packet type values. */
     public final static byte LOCATION_PACKET = 0x01;
     public final static byte CERTIFICATION_PACKET = 0x02;

    public byte type;                 /* 1 byte */
    public byte[] identifier;   /* 2 bytes */
    public byte[] frame;           /* Not fixed, packet type dependent */
}

