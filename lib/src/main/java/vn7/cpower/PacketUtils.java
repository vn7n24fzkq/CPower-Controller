package vn7.cpower;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PacketUtils {

    public static short byteArrayToShort(byte[] array) {
        short result = array[1];
        result <<= 8;
        result += array[0];
        return result;
    }

    public static byte[] getShortArray(short number) {
        byte[] array = new byte[2];
        array[0] = ((byte) (number & 0xFF));
        array[1] = ((byte) ((number >> 8) & 0xFF));
        return array;
    }

    public static byte[] getLittleEndian(int number) {
        byte[] array = new byte[2];
        ByteBuffer bf = ByteBuffer.allocate(4);
        bf.order(ByteOrder.LITTLE_ENDIAN);
        bf.putInt(number);
        bf.flip();
        bf.get(array);
        return array;
    }
}
