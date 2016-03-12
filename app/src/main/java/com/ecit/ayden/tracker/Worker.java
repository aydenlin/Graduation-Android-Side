package com.ecit.ayden.tracker;

/*
 *  Thread that deal with all works except locate and UI.
 *  so which is named Worker.
 */
class Worker implements Runnable {

    private LocProvider locProvider;
    private CoreService coreService;
    private Network network;
    private Certification certification;

    public Worker(CoreService coreService_, Network network_, Certification certification_,
                  LocProvider locProvider_) {
        network = network_;
        certification = certification_;
        coreService = coreService_;
        locProvider = locProvider_;
    }

    private void authorize() {
        /* method of register */
        byte flag = 0x00;
        byte[] fromServer = null;
        Redo:
        while (true) {
            if (flag == 0x01) {
                coreService.send("Please write down your info", coreService.CoreServiceDebugFilter);
                // Loop to waiting for input.
                while (certification.isNull()) {
                }
            }
            network.write(Packer.userAndPass(certification.getUsername(), certification.getPassword(),
                    certification.getIMEI()));
            fromServer = network.read();
            if (Packer.getType(fromServer) == Packer.NAME_ALREADY_USED ||
                    Packer.getType(fromServer) == Packer.PASSWORD_ERROR) {
                flag = 0x01;
                continue Redo;
            } else if (Packer.getType(fromServer) == Packer.CONFIRMED) {
                certification.setTempID(fromServer[1]);
                certification.save();
                coreService.send("Confirmed", CoreService.CoreServiceDebugFilter);
                return;
            } else {
                coreService.send("worng type of packet received", CoreService.CoreServiceDebugFilter);
            }
        }
    }

    private void connect() {
        network.connecting();
    }


    private void work() {
        /* main working of worker thread */
        locProvider.start(certification.getTempID());
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