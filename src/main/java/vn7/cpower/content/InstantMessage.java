package vn7.cpower.content;

import vn7.cpower.PacketUtils;
import vn7.cpower.exception.CPowerSettingException;

import java.nio.charset.Charset;

public class InstantMessage extends SendContent {

    InstantMessageEffect effect = InstantMessageEffect.Draw;
    byte[] loopTime = new byte[]{0x00, 0x03};// if equals 0 then play until received new command
    byte[] width = new byte[]{0x00, 0x20};// if equals 0 then play until received new command
    byte[] height = new byte[]{0x00, 0x20};// if equals 0 then play until received new command
    byte speed = (byte) 0x00; // 00 to ff, 00 is the fastest
    byte stay = (byte)0x03; // sec
    byte colorAndFontSize = 0x72; // hi 4 bit is color, and low 4 bit is size
    String text = "";

    public void setEffect(InstantMessageEffect effect) {
        this.effect = effect;
    }

    public InstantMessageEffect getEffect() {
        return this.effect;
    }

    /**
     * Set loop times.
     * @param times If equals 0 then play until received new command
     */
    public void setLoopTime(short times) {
        this.loopTime = PacketUtils.getShortArray(times);
    }

    public short getLoopTime() {
        return PacketUtils.byteArrayToShort(this.loopTime);
    }

    public void setColor(boolean red, boolean green, boolean blue) {
        colorAndFontSize &= 0b00001111;
        if (red) {
            colorAndFontSize |= 0b00010000;
        }
        if (green) {
            colorAndFontSize |= 0b00100000;
        }
        if (blue) {
            colorAndFontSize |= 0b01000000;
        }
    }

    public void setFontSize(byte size) throws CPowerSettingException {
        if (size >= (byte) 0x08) {
            throw new CPowerSettingException("size should smaller than 8 !!");
        }
        colorAndFontSize &= 0b11110000;
        colorAndFontSize |=size;
    }

    public byte getColorAndFontSize() {
        return colorAndFontSize;
    }

    public void setWidth(short width) {
        this.width = PacketUtils.getShortArray(width);
    }

    public short getWidth() {
        return PacketUtils.byteArrayToShort(this.width);
    }

    public void setHeight(short height) {
        this.height = PacketUtils.getShortArray(height);
    }

    public short getHeight() {
        return PacketUtils.byteArrayToShort(this.height);
    }

    /**
     * Set speed
     * @param speed The smaller the value, the faster
     */
    public void setSpeed(byte speed) {
        this.speed = speed;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    private byte[] getTextByteArray() {
        return text.getBytes(Charset.forName("big5"));
    }

    @Override
    public byte[] getData() {
        byte[] textByteArray = getTextByteArray();
        byte[] data = new byte[24 + textByteArray.length];

        // I don't know what does these bytes do
        data[2] = 0x77;

        data[10] = width[1]; // width hi byte
        data[11] = width[0]; // width lo byte

        data[12] = height[1]; // height hi byte
        data[13] = height[0]; // height lo byte

        // set content length
        byte[] contentLengthInLittleEndian = PacketUtils.getLittleEndian(textByteArray.length + 22);
        data[0] = contentLengthInLittleEndian[0];
        data[1] = contentLengthInLittleEndian[1];

        data[5] = loopTime[0]; //set loop time
        data[6] = loopTime[1]; //set loop time
        data[14] = colorAndFontSize; // set color and size
        data[19] = effect.value; // set effect
        data[20] = speed; // set speed
        data[21] = stay;

        //set text length
        byte[] textLengthArray = PacketUtils.getShortArray((short) textByteArray.length);
        data[22] = textLengthArray[1];
        data[23] = textLengthArray[0];

        //set text
        int index = data.length - textByteArray.length;
        for (byte b : textByteArray) {
            data[index] = b;
            index += 1;
        }

        //set content checksum
        short contentCheck = 0;
        for (int i = 5; i <= 23; i++) {
            if (data[i] < 0)
                contentCheck += 256;
            contentCheck += data[i];
        }

        // calculate checksum after put all data into byte array
        byte[] contentCheckArray = PacketUtils.getShortArray(contentCheck);
        data[3] = contentCheckArray[1];
        data[4] = contentCheckArray[0];

        return data;
    }

}
