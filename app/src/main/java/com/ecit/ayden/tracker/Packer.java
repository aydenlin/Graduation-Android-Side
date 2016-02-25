package com.ecit.ayden.tracker;

/*
 * Packer is an object deal with packet relate jobs,
 * such as to know what is the type of a packet,
 * or to package several values into a packet.
 */
public class Packer {

    public static final int MAX_LENGTH_OF_PACKET = 1024;
    public static final int COMMAND_LENGTH = 1;
    // packet type constants.
    public static final byte NAME_ALREADY_USED = 0X01;
    public static final byte PASSWORD_ERROR = 0x02;
    public static final byte CONFIRMED = 0x03;

    // Location package function.
    public static byte[] locPacket(double longtitude, double latitude) {
        byte[] longtitude_b = Tools.double2byteA(longtitude);
        byte[] latitude_b = Tools.double2byteA(latitude);
        byte[] packet = new byte[longtitude_b.length + latitude_b.length];

        System.arraycopy(longtitude_b, 0, packet, 0, longtitude_b.length);
        System.arraycopy(latitude_b, 0, packet, longtitude_b.length, latitude_b.length);
        return packet;
    }

    public static byte[] userAndPassPacket(String username, String password) {
        byte[] packet = null;

        return packet;
    }

    // use to get type of packet, and It's only one byte of type of packet.
    public static byte getType(byte[] packet) {
        byte type = packet[0];
        return type;
    }
}
