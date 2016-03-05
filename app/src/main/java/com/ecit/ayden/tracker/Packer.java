package com.ecit.ayden.tracker;

/*
 * Packer is an object deal with packet relate jobs,
 * such as to know what is the type of a packet,
 * or to package several values into a packet.
 */
public class Packer {

    public static final int MAX_LENGTH_OF_PACKET = 1024;
    public static final int TYPE_LENGTH = 1;
    public static final int TEMP_ID_LENGTH = 1;
    public static final int IMEI_LENGTH = 15;
    public static final int USERNAME_LENGTH = 5;
    public static final int PASSWORD_LENGTH = 6;
    // packet type constants.
    public static final byte CERTIFICATION_PACKET = 0x01;
    public static final byte LOCATION_PACKET = 0x02;
    public static final byte RECONNECT = 0X03;
    public static final byte NAME_ALREADY_USED = 0x04;
    public static final byte PASSWORD_ERROR = 0x05;
    public static final byte CONFIRMED = 0x06;
    public static final byte NO_EXIST = 0x07;

    // Location package function.
    public static byte[] locPacket(double longtitude, double latitude, byte tempID) {
        byte[] longtitude_b = Tools.double2byteA(longtitude);
        byte[] latitude_b = Tools.double2byteA(latitude);
        byte[] packet = new byte[TYPE_LENGTH + TEMP_ID_LENGTH + longtitude_b.length + latitude_b.length];

        packet[0] = LOCATION_PACKET;
        packet[1] = tempID;
        System.arraycopy(longtitude_b, 0, packet, TYPE_LENGTH + TEMP_ID_LENGTH , longtitude_b.length);
        System.arraycopy(latitude_b, 0, packet, longtitude_b.length + TYPE_LENGTH + TEMP_ID_LENGTH, latitude_b.length);
        return packet;
    }

    public static byte[] userAndPass(String username, String password, String imei) {
        return cerPackage(CERTIFICATION_PACKET, username, password, imei);
    }

    public static byte[] reconnectPacket(String username, String password, String imei) {
        return cerPackage(RECONNECT, username, password, imei);
    }

    public static byte[] cerPackage(byte flag, String username, String password, String imei) {
        byte[] packet = new byte[TYPE_LENGTH + USERNAME_LENGTH + PASSWORD_LENGTH + IMEI_LENGTH];
        packet[0] = flag;

        System.arraycopy(username.getBytes(), 0, packet, TYPE_LENGTH, USERNAME_LENGTH);
        System.arraycopy(password.getBytes(), 0, packet, TYPE_LENGTH + USERNAME_LENGTH, PASSWORD_LENGTH);
        System.arraycopy(imei.getBytes(), 0, packet, TYPE_LENGTH + USERNAME_LENGTH + PASSWORD_LENGTH,
                IMEI_LENGTH);
        return packet;
    }

    // use to get type of packet, and It's only one byte of type of packet.
    public static byte getType(byte[] packet) {
        byte type = packet[0];
        return type;
    }
}
