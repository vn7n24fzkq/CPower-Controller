package io.github.vn7n24fzkq.cpower;

import io.github.vn7n24fzkq.cpower.content.InstantMessage;
import io.github.vn7n24fzkq.cpower.exception.CPowerSettingException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class CPowerController {
    Socket socket;
    CPowerSendPacket cPowerPacket = new CPowerSendPacket();
    BufferedInputStream in;
    BufferedOutputStream out;

    public boolean send(InstantMessage instantMessage) throws IOException {
        cPowerPacket.setContent(instantMessage);
        if (!isConnected()) {
            throw new SocketException("Not connect yet");
        }

        out.write(cPowerPacket.getData());
        out.flush();
        int i = 0;
        in.read(new byte[4]); // read ID code
        byte[] packetSize = new byte[2];
        in.read(packetSize); // read packet size
        int size = PacketUtils.byteArrayToShort(packetSize);
        in.read(new byte[2]); // read reservation byte
        byte[] rec = new byte[size];
        in.read(rec);
        return rec[5] > 0;
    }

    /**
     * @param ip   example : "192.168.1.50"
     * @param port example : 5200
     * @param mask example : "255.255.255.255"
     * @throws IOException Socket connect error
     * @throws CPowerSettingException Invalid mask format
     */
    public void connect(String ip, int port, String mask) throws IOException, CPowerSettingException {
        if (isConnected()) { // if already exist a connect, we disconnect it
            disconnect();
        }
        socket = new Socket(ip, port);
        String[] maskArray = mask.split("\\.");
        if (maskArray.length != 4) {
            throw new CPowerSettingException("Invalid mask format");
        } else {
            byte[] maskIntArray = new byte[4];
            for (int i = 0; i < maskIntArray.length; i++) {
                maskIntArray[i] = Byte.parseByte(maskArray[i]);
            }
            cPowerPacket.setMask(maskIntArray[0], maskIntArray[1], maskIntArray[2], maskIntArray[3]);
        }

        in = new BufferedInputStream(socket.getInputStream());
        out = new BufferedOutputStream(socket.getOutputStream());
    }

    public boolean isConnected() {
        return (socket != null) && socket.isConnected();
    }

    public void disconnect() throws IOException {
        socket.close();
    }

    public void setTimeout(int timeout) throws SocketException {
        socket.setSoTimeout(timeout);
    }

    public int getTimeout() throws SocketException {
        return socket.getSoTimeout();
    }
}
