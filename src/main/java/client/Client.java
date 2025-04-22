package client;

import models.Product;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Base64;
import java.io.IOException;

public class Client {
    private DatagramChannel datagramChannel;
    private final String HOST;
    private final int PORT;
    private final boolean isBlocking;

    public Client(String host, int port, boolean isBlocking) {
        this.HOST = host;
        this.PORT = port;
        this.isBlocking = isBlocking;
    }

    public void open() throws IOException {
        this.datagramChannel = DatagramChannel.open();
        this.datagramChannel.configureBlocking(this.isBlocking);
    }

    public void sendCommand(String command) throws IOException {
        byte[] request = command.getBytes();
        this.sendRequest(request);
    }

    public void sendProduct(Product product) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(product);
            objectOutputStream.flush();
            byte[] objectBytes = byteArrayOutputStream.toByteArray();
            sendRequest(objectBytes);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void sendRequest(byte[] message) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(message);
        SocketAddress serverAdress = new InetSocketAddress(this.HOST, this.PORT);
        this.datagramChannel.send(buffer, serverAdress);
        System.out.println("Request send to server");
    }
    public Object receiveResponse(int timeout) throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(65536);
        SocketAddress serverAddress = new InetSocketAddress(this.HOST, this.PORT);
        if(!isBlocking) {
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < timeout) {
                buffer.clear();
                SocketAddress senderAdress = this.datagramChannel.receive(buffer);
                if (senderAdress != null) {
                    buffer.flip();
                    byte[] receivedBytes = new byte[buffer.remaining()];
                    buffer.get(receivedBytes);
                    try {
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(receivedBytes);
                        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                        Object receivedObject = objectInputStream.readObject();

                        if(receivedObject instanceof Product) {
                            return receivedObject;
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        return new String(receivedBytes);
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Client interrupted while working.");
                }
            }
            throw new IOException("Timeout error");
        } else {
            buffer.clear();
            byte[] dataBytes = new byte[buffer.remaining()];
            buffer.get(dataBytes);
            String request = new String(dataBytes);
            return request;
        }
    }

    public void close() throws IOException {
        if(this.datagramChannel != null) {
            this.datagramChannel.close();
            System.out.println("Client finish work.");
        }
    }

}
