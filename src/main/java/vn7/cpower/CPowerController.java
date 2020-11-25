package vn7.cpower;

import vn7.cpower.content.InstantMessage;

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

    public void connect(String ip, int port) throws IOException {
        if (isConnected()) { // if already exist a connect, we disconnect it
            disconnect();
        }
        socket = new Socket(ip, port);
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
