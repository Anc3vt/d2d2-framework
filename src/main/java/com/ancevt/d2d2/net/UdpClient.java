package com.ancevt.d2d2.net;


import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class UdpClient {

    private final DatagramSocket socket;
    private final InetAddress serverAddress;
    private final int serverPort;
    private final Gson gson = new Gson();

    private final AtomicLong idGenerator = new AtomicLong();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final Map<Long, CompletableFuture<Void>> pendingAcks = new ConcurrentHashMap<>();
    private final Thread ackListenerThread;
    private volatile boolean running = true;

    public UdpClient(String host, int port) throws IOException {
        this.serverAddress = InetAddress.getByName(host);
        this.serverPort = port;
        this.socket = new DatagramSocket();  // client port = random
        this.ackListenerThread = new Thread(this::listenForAcks, "UdpClient-ACK-Listener");
        this.ackListenerThread.start();
    }

    public void stop() {
        running = false;
        socket.close();
        executor.shutdownNow();
    }

    public void sendFast(byte[] payload) throws IOException {
        Message msg = new Message(Message.Type.FAST, 0L, payload);
        sendMessage(msg);
    }

    public void sendReliable(byte[] payload) throws Exception {
        long id = idGenerator.incrementAndGet();
        Message msg = new Message(Message.Type.RELIABLE, id, payload);

        CompletableFuture<Void> ackFuture = new CompletableFuture<>();
        pendingAcks.put(id, ackFuture);

        final int maxRetries = 5;
        final long ackTimeoutMs = 500;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            sendMessage(msg);

            try {
                ackFuture.get(ackTimeoutMs, TimeUnit.MILLISECONDS);
                System.out.println("ACK received for message ID: " + id);
                return;
            } catch (TimeoutException e) {
                System.out.println("Retry " + attempt + " for message ID: " + id);
            }
        }

        pendingAcks.remove(id);
        throw new IOException("ACK not received after " + maxRetries + " attempts (ID: " + id + ")");
    }

    private void sendMessage(Message msg) throws IOException {
        byte[] data = gson.toJson(msg).getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
        socket.send(packet);
    }

    private void listenForAcks() {
        byte[] buffer = new byte[2048];

        while (running) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            try {
                socket.receive(packet);
                String json = new String(packet.getData(), 0, packet.getLength());
                Message ack = gson.fromJson(json, Message.class);

                if (ack.type == Message.Type.ACK) {
                    CompletableFuture<Void> future = pendingAcks.remove(ack.id);
                    if (future != null) {
                        future.complete(null);
                    }
                }

            } catch (IOException e) {
                if (running) {
                    System.err.println("Client ACK listener error: " + e.getMessage());
                }
            }
        }
    }
}
