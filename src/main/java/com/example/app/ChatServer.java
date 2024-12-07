package com.example.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatServer {
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("服务器启动，等待客户端连接...");
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String userId;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                userId = UUID.randomUUID().toString();
                System.out.println("ID：" + userId);

                synchronized (clients) {
                    clients.add(this);
                }

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("收到消息：" + message);
                    if ("退出聊天".equals(message)) {
                        break;
                    }
                    broadcastMessage(message, userId);  // 使用反射广播消息
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (clients) {
                    clients.remove(this);
                }
            }
        }

        // 排除发送者
        private void broadcastMessage(String message, String senderId) {
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    if (!client.getUserId().equals(senderId)) {
                        client.sendMessage(message);
                    }
                }
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public String getUserId() {
            return userId;
        }
    }
}

