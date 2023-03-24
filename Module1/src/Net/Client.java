package Net;

import Utils.BinaryWriter;
import com.thecherno.raincloud.serialization.RCDatabase;
import com.thecherno.raincloud.serialization.SerializationUtils;

import java.io.IOException;
import java.net.*;

public class Client {

    private final static byte[] PACKET_HEADER = new byte[] { 0x40, 0x40 };
    private final static byte PACKET_TYPE_CONNECT = 0x01;
    private final static byte PACKET_TYPE_LEVEL_SWAP = 0x04;

    public enum Error{
        NONE, INVALID_HOST, SOCKET_EXCEPTION,
    }

    private String ipAddress;
    private int port;
    private Error errorCode = Error.NONE;

    private InetAddress serverAddress;

    private DatagramSocket socket;


    /*
    @param host
    Format: 192.168.1.1:5000
     */
    public Client (String host){
        String[] parts = host.split(":");
        if (parts.length != 2){
            errorCode = Error.INVALID_HOST;
            return;
        }
        ipAddress = parts[0];
        try {
            port = Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            errorCode = Error.INVALID_HOST;
            return;
        }
    }

    /*
    @param host
    Format: 192.168.1.1
    @param port
    Format: 5000
    */
    public Client (String host, int port){
        this.ipAddress = host;
        this.port = port;
    }

    public boolean connect(){
        try {
            serverAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            errorCode = Error.INVALID_HOST;
            return false;
        }

        try {
            socket = new DatagramSocket(); // empty constructor means it uses a random port that is available
        } catch (SocketException e) {
            e.printStackTrace();
            errorCode = Error.SOCKET_EXCEPTION;
            return false;
        }
        sendConnectionPacket();
        return true;
    }

    private void sendConnectionPacket(){
        BinaryWriter writer = new BinaryWriter();
        writer.write(PACKET_HEADER);
        writer.write(PACKET_TYPE_CONNECT);
        send(writer.getBuffer());
    }

    // TODO test this (debug it)
    public void sendLevelSwapPacket(short levelRef){
        BinaryWriter writer = new BinaryWriter();
        writer.write(PACKET_HEADER);
        writer.write(PACKET_TYPE_LEVEL_SWAP);
        byte[] dest = new byte[2]; // shorts store whole numbers from -32,768 to 32,767
        SerializationUtils.writeBytes(dest, 0, levelRef);
        writer.write(dest);
        send(writer.getBuffer());
    }

    public void send(byte[] data){
        assert (socket.isConnected());
        // Could break data into smaller chunks
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(RCDatabase database){
        byte[] data = new byte[database.getSize()];
        database.getBytes(data, 0);
        send(data);
    }

    public Error getErrorCode() {
        return errorCode;
    }

    public DatagramSocket getSocket() {
        return socket;
    }
}
