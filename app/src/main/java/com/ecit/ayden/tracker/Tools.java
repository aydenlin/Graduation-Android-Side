package com.ecit.ayden.tracker;

/**
 * Created by ayden on 1/25/16.
 */
public class Tools {
    private static byte[] double2byteA(double val) {
        byte[] box = new byte[8];
        long val_long = Double.doubleToRawLongBits(val);

        box[0] = (byte)(val_long & 0xff);
        box[1] = (byte)((val_long >> 8) & 0xff);
        box[2] = (byte)((val_long >> 16) & 0xff);
        box[3] = (byte)((val_long >> 24) & 0xff);
        box[4] = (byte)((val_long >> 32) & 0xff);
        box[5] = (byte)((val_long >> 40) & 0xff);
        box[6] = (byte)((val_long >> 48) & 0xff);
        box[7] = (byte)((val_long >> 56) & 0xff);
        return box;
    }

    public static byte[] locPacket(double longtitude, double latitude) {
        byte[] longtitude_b = double2byteA(longtitude);
        byte[] latitude_b = double2byteA(latitude);
        byte[] packet = new byte[longtitude_b.length + latitude_b.length];

        System.arraycopy(longtitude_b,0, packet, 0, longtitude_b.length);
        System.arraycopy(latitude_b, 0, packet, longtitude_b.length, latitude_b.length);
        return packet;
    }
}
