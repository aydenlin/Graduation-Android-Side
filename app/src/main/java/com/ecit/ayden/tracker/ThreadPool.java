package com.ecit.ayden.tracker;

import java.net.Socket;
import java.util.ArrayList;

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
        threadPool.get(type).interrupt();
    }
}

/*
 *  Transfer packet between PacketSpace and Server only.
 */
class NetLine implements Runnable {

    private Network network = null;

    public NetLine(Network network_) {
        network = network_;
    }

    @Override
    public void run() {

        network.connecting();

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







}