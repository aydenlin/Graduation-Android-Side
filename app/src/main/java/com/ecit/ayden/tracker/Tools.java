package com.ecit.ayden.tracker;

/**
 * Created by ayden on 1/25/16.
 */
public class Tools {
    public static byte[] double2byteA(double val) {
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
    public static void Be2Le(byte[] bytes) {
        int length = bytes.length;
        int index = 0;
        byte changer;

        while (index < length/2) {
            changer = bytes[index];
            bytes[index] = bytes[length - index - 1];
            bytes[length - index - 1] = changer;
            index++;
        }
    }

    public static void Sleeping() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {}
    }
}
