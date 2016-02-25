package com.ecit.ayden.tracker;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by ayden on 1/31/16.
 */
public class  PacketSpace {

    private static final int capacity = 100;
    private static ArrayBlockingQueue<byte[]> Queue = new ArrayBlockingQueue<byte[]>(capacity);

    public static int isSomethingInSend() {
        return Queue.size();
    }

    public static boolean isSendEmpty() {
        return Queue.isEmpty();
    }

    public static void QueueAdd(byte[] p) {
        Queue.add(p);
    }

    public static byte[] QueueRetrive() {
        return Queue.poll();
    }
}


