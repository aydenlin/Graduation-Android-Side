package com.ecit.ayden.tracker;

import android.util.ArraySet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/*
  * This class is use for managing
   * all thread except main.
  */
public class ThreadPool {
    public static final byte PSWORKER_THREAD = 0x01;
    public static final byte NETWORK_THREAD = 0x01;

    private static ArrayList<Thread> threadPool = new ArrayList<Thread>();

    public static void addPsWorker(Thread thread) {
        threadPool.add(PSWORKER_THREAD, thread);
    }

    public static void addNetwork(Thread thread) {
        threadPool.add(NETWORK_THREAD, thread);
    }

    public static void Interrupt(int type) {
        (threadPool.get(type)).interrupt();
    }
}
