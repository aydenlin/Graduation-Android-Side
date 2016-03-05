package com.ecit.ayden.tracker;

/*
 *  Thread that deal with all works except locate and UI.
 *  so which is named Worker.
 */
class Worker implements Runnable {

    private LocProvider locProvider = null;
    private CoreService coreService = null;
    private Network network = null;
    private Certification certification = null;
    private boolean pass = false;

    public Worker(CoreService coreService_, Network network_, Certification certification_,
                  LocProvider locProvider_) {
        network = network_;
        certification = certification_;
        coreService = coreService_;
        locProvider = locProvider_;
    }

    /*
     * check() method should confirm username and password
     * is match with server's.
     */
    private boolean check() {
        byte[] receive = null;

        certification.load();
        network.write(Packer.userAndPassPacket(certification.getUsername(), certification.getPassword(), certification.getIMEI()));
        receive = network.read();
        if (Packer.getType(receive) == Packer.CONFIRMED)
            return true;
        else if (Packer.getType(receive) == Packer.PASSWORD_ERROR)
            return false;
        else if (Packer.getType(receive) == Packer.NAME_ALREADY_USED)
            return false;
        else if (Packer.getType(receive) == Packer.NO_EXIST)
            return false;
        else
            return false;
    }

    private void register() {
        /* method of register */
        byte[] fromServer = null;
        Redo:
        while (true) {
            coreService.send("Please write down your info", coreService.CoreServiceDebugFilter);
            // Loop to waiting for input.
            while(certification.isNull()) {}
            network.write(Packer.userAndPassPacket(certification.getUsername(), certification.getPassword(), certification.getIMEI()));
            fromServer = network.read();
            if (Packer.getType(fromServer) == Packer.NAME_ALREADY_USED ||
                    Packer.getType(fromServer) == Packer.PASSWORD_ERROR) {
                continue Redo;
            } else if (Packer.getType(fromServer) == Packer.CONFIRMED) {
                certification.save();
                coreService.send("Confirmed", CoreService.CoreServiceDebugFilter);
                return;
            } else {
                coreService.send("worng type of packet received", CoreService.CoreServiceDebugFilter);
            }
        }
    }

    private void authorize() {
        if (check()) {
            return;
        } else {
            register();
        }
    }

    private void connect() {
        network.connecting();
    }



    private void work() {
        /* main working of worker thread */
        locProvider.start();
        while (true) {
            network.write(PacketSpace.QueueRetrive());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ie) {}
        }
    }

    @Override
    public void run() {
        connect();
        authorize();
        work();
    }
}