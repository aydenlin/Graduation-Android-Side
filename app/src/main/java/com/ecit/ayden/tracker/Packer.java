package com.ecit.ayden.tracker;

/**
 * Created by ayden on 2/5/16.
 */
public class Packer {
    public static final int MAX_LENGTH_OF_PACKET = 1024;
    public static final int TYPE_FIELD_LENGTH = 1;

    // packet type constants
    public static final byte NAME_ALREADY_USED = 0X01;
    public static final byte PASSWORD_ERROR = 0x02;
    public static final byte NEED_REGISTER = 0x03;
    public static final byte CONFIRMED = 0x04;

    // Location packet function.
    public static byte[] locPacket(double longtitude, double latitude) {
        byte[] longtitude_b = Tools.double2byteA(longtitude);
        byte[] latitude_b = Tools.double2byteA(latitude);
        byte[] packet = new byte[longtitude_b.length + latitude_b.length];

        System.arraycopy(longtitude_b, 0, packet, 0, longtitude_b.length);
        System.arraycopy(latitude_b, 0, packet, longtitude_b.length, latitude_b.length);
        return packet;
    }

    public static byte getType(byte[] packet) {
        byte type = packet[0];
        return type;
    }
}
