package com.ancevt.d2d2.net;



public class Demo {
    public static void main(String[] args) throws Exception {
        // Запускаем сервер
        UdpServer server = new UdpServer(9000);
        server.start();

        // Подождать, чтобы сервер успел подняться
        Thread.sleep(500);

        // Запускаем клиента
        UdpClient client = new UdpClient("localhost", 9000);

        // Отправим FAST сообщение
        client.sendFast("x=100;y=200".getBytes());

        // Отправим RELIABLE сообщение
        client.sendReliable("Hello from client (RELIABLE)".getBytes());

        // Немного подождём, чтобы всё обработалось
        Thread.sleep(1000);

        // Завершаем всё
        client.stop();
        server.stop();
    }
}
