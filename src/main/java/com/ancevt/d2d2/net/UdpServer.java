package com.ancevt.d2d2.net;


import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UdpServer {

    private final int port;
    private final DatagramSocket socket;
    private final Thread listenerThread;
    private final Gson gson = new Gson();
    private volatile boolean running = true;

    // Чтобы избежать обработки дубликатов
    private final Set<Long> processedIds = ConcurrentHashMap.newKeySet();

    public UdpServer(int port) throws SocketException {
        this.port = port;
        this.socket = new DatagramSocket(port);
        this.listenerThread = new Thread(this::listenLoop, "UdpServer-Listener");
    }

    public void start() {
        running = true;
        listenerThread.start();
        System.out.println("UDP server started on port " + port);
    }

    public void stop() {
        running = false;
        socket.close();
        System.out.println("UDP server stopped.");
    }

    private void listenLoop() {
        byte[] buffer = new byte[8192];

        while (running) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            try {
                socket.receive(packet);
                String json = new String(packet.getData(), 0, packet.getLength());
                Message msg = gson.fromJson(json, Message.class);

                switch (msg.type) {
                    case FAST -> handleFast(msg);
                    case RELIABLE -> handleReliable(msg, packet.getAddress(), packet.getPort());
                    case ACK -> System.out.println("Received ACK: " + msg.id);
                }

            } catch (IOException e) {
                if (running) {
                    System.err.println("Server error: " + e.getMessage());
                }
            }
        }
    }

    private void handleFast(Message msg) {
        System.out.println("[FAST] " + new String(msg.payload));
    }

    private void handleReliable(Message msg, InetAddress address, int port) throws IOException {
        if (processedIds.contains(msg.id)) {
            System.out.println("[RELIABLE DUPLICATE] Ignored ID: " + msg.id);
            sendAck(msg.id, address, port);  // всё равно отвечаем ACK
            return;
        }

        System.out.println("[RELIABLE] " + new String(msg.payload));
        processedIds.add(msg.id);

        // Отправка ACK
        sendAck(msg.id, address, port);
    }

    private void sendAck(long id, InetAddress address, int port) throws IOException {
        Message ack = Message.ack(id);
        byte[] data = gson.toJson(ack).getBytes();
        DatagramPacket ackPacket = new DatagramPacket(data, data.length, address, port);
        socket.send(ackPacket);
    }

    public static void main(String[] args) {

    }
}
