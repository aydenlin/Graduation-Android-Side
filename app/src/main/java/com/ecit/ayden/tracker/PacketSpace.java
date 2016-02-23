package com.ecit.ayden.tracker;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by ayden on 1/31/16.
 */
public class  PacketSpace {

    /* STATUS is true if its in connected state, false in another case */
    private boolean STATUS = false;
    private static final int capacity = 100;
    private static final int certifiCapacity = 10;
    private static ArrayBlockingQueue<byte[]> sendQueue = new ArrayBlockingQueue<byte[]>(capacity);

    public static int isSomethingInSend() {
        return sendQueue.size();
    }

    public static boolean isSendEmpty() {
        return sendQueue.isEmpty();
    }

    public static void sendQueueAdd(byte[] p) {
        sendQueue.add(p);
    }

    public static byte[] sendQueueRetrive() {
        return sendQueue.poll();
    }
}


