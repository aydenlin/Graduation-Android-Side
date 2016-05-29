package com.ecit.ayden.tracker;

/*
 * Packer is an object deal with packet relate jobs,
 * such as to know what is the type of a packet,
 * or to package several values into a packet.
 */
public class Packer {
    public static final int TYPE_LENGTH = 1;
    public static final int SUFFIX_LENGTH = 1;
    public static final int MAX_LENGTH_OF_PACKET = 30;
    public static final int LENGTH_OF_CONTROL_PACKET = 1;
    
    // packet type constants.
    public static final byte CERTIFICATION_PACKET = 0x01;
    public static final byte LOCATION_PACKET = 0x02;;
    public static final byte NAME_ALREADY_USED = 0x03;
    public static final byte PASSWORD_ERROR = 0x04;
    public static final byte CONFIRMED = 0x05;
    
    // Certification packet related variable
    public static final int PACKET_CERTIFI_IMEI_LENGTH = 15;
    public static final int PACKET_CERTIFI_USERNAME_LENGTH = 6;
    public static final int PACKET_CERTIFI_PASSWORD_LENGTH = 6;
    public static final int PACKET_CERTIFI_USERNAME_POS = TYPE_LENGTH;
    public static final int PACKET_CERTIFI_PASSWORD_POS = PACKET_CERTIFI_USERNAME_POS + PACKET_CERTIFI_USERNAME_LENGTH;
    public static final int PACKET_CERTIFI_IMEI_POS = PACKET_CERTIFI_PASSWORD_POS + PACKET_CERTIFI_PASSWORD_LENGTH;

    // Location packet realated variable
    public static final int PACKET_LOCAT_LONG_LENGTH = 8;
    public static final int PACKET_LOCAT_LATI_LENGTH = 8;
    public static final int PACKET_LOCAT_LONG_POS = TYPE_LENGTH;
    public static final int PACKET_LOCAT_LATI_POS = PACKET_LOCAT_LONG_POS + PACKET_LOCAT_LONG_LENGTH;

    // Location package function.
    public static byte[] locPacket(double longtitude, double latitude) {
        byte[] longtitude_b = Tools.double2byteA(longtitude);
        byte[] latitude_b = Tools.double2byteA(latitude);
        byte[] packet = new byte[TYPE_LENGTH + PACKET_LOCAT_LONG_LENGTH + PACKET_LOCAT_LATI_LENGTH + SUFFIX_LENGTH];

        packet[0] = LOCATION_PACKET;
        System.arraycopy(longtitude_b, 0, packet, PACKET_LOCAT_LONG_POS, PACKET_LOCAT_LONG_LENGTH);
        System.arraycopy(latitude_b, 0, packet, PACKET_LOCAT_LATI_POS, PACKET_LOCAT_LATI_LENGTH);
        packet[PACKET_LOCAT_LATI_POS +  PACKET_LOCAT_LATI_LENGTH] = '\0';
        return packet;
    }
/*
    public static byte[] userAndPass(String username, String password, String imei) {
        return cerPackage(CERTIFICATION_PACKET, username, password, imei);
    }
*/
    public static byte[] cerPackage(String username, String password, String imei) {
        byte[] packet = new byte[TYPE_LENGTH + PACKET_CERTIFI_USERNAME_LENGTH + PACKET_CERTIFI_PASSWORD_LENGTH + PACKET_CERTIFI_IMEI_LENGTH + SUFFIX_LENGTH];
        packet[0] = CERTIFICATION_PACKET;

        System.arraycopy(strToFixLenBytes(username, PACKET_CERTIFI_USERNAME_LENGTH), 0, packet, PACKET_CERTIFI_USERNAME_POS, PACKET_CERTIFI_USERNAME_LENGTH);
        System.arraycopy(strToFixLenBytes(password, PACKET_CERTIFI_PASSWORD_LENGTH), 0, packet, PACKET_CERTIFI_PASSWORD_POS, PACKET_CERTIFI_PASSWORD_LENGTH);
        System.arraycopy(strToFixLenBytes(imei, PACKET_CERTIFI_IMEI_LENGTH), 0, packet, PACKET_CERTIFI_IMEI_POS, PACKET_CERTIFI_IMEI_LENGTH);
        packet[PACKET_CERTIFI_IMEI_POS + PACKET_CERTIFI_IMEI_LENGTH] = '\0';
        return packet;
    }

    private static byte[] strToFixLenBytes(String src, int length) {
        int index = length - 1;
        int lowerbound = src.length();
        byte[] ret = new byte[length];
        System.arraycopy(src.getBytes(), 0, ret, 0, src.length());

        while (index > lowerbound-1) {
            ret[index] = ' ';
            index--;
        }
        return ret;
    }

    // use to get type of packet, and It's only one byte of type of packet.
    public static byte getType(byte[] packet) {
        byte type = packet[0];
        return type;
    }
}
