package com.ecit.ayden.tracker;

import android.util.Log;

/*
 *  Thread that deal with all works except locate and UI.
 *  so which is named Worker.
 */
class Worker implements Runnable {

    private LocProvider locProvider;
    private CoreService coreService;
    private Network network;
    private Certification certification;

    public Worker(CoreService coreService_, Network network_, Certification certification_, LocProvider locProvider_) {
        network = network_;
        certification = certification_;
        coreService = coreService_;
        locProvider = locProvider_;
    }

    private int authorize() {
        Log.i("TEST","authorize");
        int ret;
        /* method of register */
        byte flag = 0x00;
        byte[] fromServer = null;
        Redo:
        while (true) {
            // Loop to waiting for input.
            while (certification.isNull()) {
                Tools.Sleeping(); // Time interval is 5 sec
            }
            Log.i("TEST","In authorize after loop");
            if ((ret = network.write(Packer.cerPackage(certification.getUsername(), certification.getPassword(),
                    certification.getIMEI()))) != 0) {
                if (ret == -1)
                    Log.i("TEST", "write ioexception");
                if (ret== -2)
                    Log.i("TEST", "out is null");
                return -1;
            }
            Log.i("TEST", "In authorize after network.write");
            if ((fromServer = network.read()) == null) {
                Log.i("TEST", "In authorize read failed");
                return -1;
            }
            Log.i("TEST", "In authorize after network.read");
            if (Packer.getType(fromServer) == Packer.PASSWORD_ERROR) {
                coreService.send("Retype your pass quickly, if not hehe you know", CoreService.CoreServiceDebugFilter);
                certification.setNull();
                continue Redo;
            } else if (Packer.getType(fromServer) == Packer.CONFIRMED) {
                certification.save();
                coreService.send("Confirmed", CoreService.CoreServiceDebugFilter);
                return 0;
            } else {
                coreService.send("worng type of packet received", CoreService.CoreServiceDebugFilter);
            }
        }
    }

    private void connect() {
        Log.i("TEST","connect");
        network.connecting();
    }

    private int work() {
        Log.i("TEST","work");
        /* main working of worker thread */
        locProvider.start();
        Log.i("TEST", "locProvider start");
        while (true) {
            Log.i("TEST", "In work loop");
            byte[] packet = null;

            while (packet == null) {
                Log.i("TEST", "In work wait packetspace");
                packet = PacketSpace.QueueRetrive();
            }
            if (network.write(packet) != 0)
                return -1;
        }
    }

    @Override
    public void run() {
        while (true) {
            Log.i("TEST","run");
            connect();
            if (authorize() == -1)
                continue;
            if (work() == -1)
                continue;
        }
    }
}