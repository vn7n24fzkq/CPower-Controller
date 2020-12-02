package io.github.vn7n24fzkq.cpower;

import io.github.vn7n24fzkq.cpower.content.SendContent;

public class CPowerSendPacket {
    byte[] IDCode = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
    int cardID = 0xff;
    final static byte PACKET_TYPE = 0x68; // NOTE: Recognition of this type of packet 0xE8 = (0x68 | 0x80), for the app 3.2 or below return 0x68, app 3.3 or above return 0xe8 to same as other protocol( such as “set time” protocol), so you can ignore the highest bit (0x80), then it works for all app version
    final static byte[] Reservation = new byte[]{0x00, 0x00};
    final static byte CARD_TYPE = 0x32;
    final static byte PROTOCOL_CODE = 0x74; // NOTE:different on the doc
    final static byte MARK = (byte) 0x11; // Additional information/ confirmation mark
    SendContent content;

    public byte[] getData() {
        int checksum = 0;
        byte[] contentData = content.getData();
        byte[] dataLength = PacketUtils.getLittleEndian(contentData.length + 11);
        checksum += PACKET_TYPE + CARD_TYPE + cardID + PROTOCOL_CODE + MARK;
        for (int i = 0; i < contentData.length; i++) {
            if (contentData[i] < 0)
                checksum += 256;
            checksum += contentData[i];
        }
        byte[] data = new byte[19 + contentData.length];
        data[0] = IDCode[3];
        data[1] = IDCode[2];
        data[2] = IDCode[1];
        data[3] = IDCode[0];
        data[4] = dataLength[0];
        data[5] = dataLength[1];
        data[6] = Reservation[1];
        data[7] = Reservation[0];
        data[8] = PACKET_TYPE;
        data[9] = CARD_TYPE;
        data[10] = (byte) cardID;
        data[11] = PROTOCOL_CODE;
        data[12] = MARK;
        for (int i = 0; i < contentData.length; i++) {
            data[i + 17] = contentData[i];
        }
        byte[] checksumByteArray = PacketUtils.getLittleEndian(checksum);
        data[data.length - 2] = checksumByteArray[0];
        data[data.length - 1] = checksumByteArray[1];
        return data;
    }

    public void setMask(byte mask1, byte mask2, byte mask3, byte mask4) {
        IDCode[0] = mask1;
        IDCode[1] = mask2;
        IDCode[2] = mask3;
        IDCode[3] = mask4;
    }

    public void setContent(SendContent content) {
        this.content = content;
    }

    public SendContent getContent() {
        return this.content;
    }
}
