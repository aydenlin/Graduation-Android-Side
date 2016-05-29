package test;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import com.ecit.ayden.tracker.PacketSpace;
import com.ecit.ayden.tracker.Certification;
import com.ecit.ayden.tracker.Tools;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.ecit.ayden.tracker.Packer;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by ayden on 5/27/16.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class TrackerTest {

    @Test
    public void unitTest() {
        packetSpaceTesting();
        certificationTesting();
        packetTesting();
    }

    private void packetSpaceTesting() {
        byte[] ba = "ayden".getBytes();
        byte[] bb = "12345".getBytes();
        PacketSpace.QueueAdd(ba);
        PacketSpace.QueueAdd(bb);
        assertThat(ba.equals(PacketSpace.QueueRetrive()), is(true));
        assertThat(bb.equals(PacketSpace.QueueRetrive()),is(true));
    }

    private void certificationTesting() {
        Certification cer = new Certification();
        cer.setUsername("aydenn");
        cer.setPassword("123456");
        cer.setIMEI("0123456789abcde");
        cer.save();
    }

    private void packetTesting() {
        byte[] packet = Packer.cerPackage("aydenn", "123456", "0123456789abcde");

        byte[] username = new byte[Packer.PACKET_CERTIFI_USERNAME_LENGTH];
        byte[] password = new byte[Packer.PACKET_CERTIFI_PASSWORD_LENGTH];
        byte[] imei = new byte[Packer.PACKET_CERTIFI_IMEI_LENGTH];

        Tools.Be2Le(username);
        Tools.Be2Le(password);
        Tools.Be2Le(imei);

        System.arraycopy(packet, Packer.PACKET_CERTIFI_USERNAME_POS, username, 0, Packer.PACKET_CERTIFI_USERNAME_LENGTH);
        System.arraycopy(packet, Packer.PACKET_CERTIFI_PASSWORD_POS, password, 0, Packer.PACKET_CERTIFI_PASSWORD_LENGTH);
        System.arraycopy(packet, Packer.PACKET_CERTIFI_IMEI_POS, imei, 0, Packer.PACKET_CERTIFI_IMEI_LENGTH);
    }
}
