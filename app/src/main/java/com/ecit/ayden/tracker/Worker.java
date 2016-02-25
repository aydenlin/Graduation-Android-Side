package com.ecit.ayden.tracker;

import android.widget.Toast;

import java.io.IOException;

/*
 *  Thread that deal with all works except locate and UI.
 *  so which is named Worker.
 */
class Worker implements Runnable {

    private CoreService coreService = null;
    private Network network = null;
    private Certification certification = null;
    private boolean pass = false;

    public Worker(CoreService coreService_, Network network_, Certification certification_) {
        network = network_;
        certification = certification_;
    }

    private boolean check() {
        return certification.check();
    }

    private void connect() {
        network.connecting();
    }

    private void register() {
        /* method of register */
        byte[] fromServer = new byte[Packer.COMMAND_LENGTH];
        Redo:
        while (true) {
            coreService.send("Please write down your info", coreService.CoreServiceDebugFilter);
            while(certification.isNull()) {}
            network.write(Packer.userAndPassPacket(certification.getUsername(), certification.getPassword()));
            network.read(fromServer);
            if (Packer.getType(fromServer) == Packer.NAME_ALREADY_USED ||
                    Packer.getType(fromServer) == Packer.PASSWORD_ERROR) {
                continue Redo;
            } else if (Packer.getType(fromServer) == Packer.CONFIRMED) {
                return;
            } else {
                coreService.send("worng type of packet received", CoreService.CoreServiceDebugFilter);
            }
        }
    }

    private void work() {
        /* main working of worker thread */
        Re:
        while (true) {
            network.write(PacketSpace.QueueRetrive());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ie) {
              continue Re;
            }
        }
    }

    @Override
    public void run() {
        connect();
        if (pass = check()) {
            // There is no problem, then try to communicate with Server.
            work();
        } else {
            // Checking fail, need to register..
            register();
            work();
        }
    }
}