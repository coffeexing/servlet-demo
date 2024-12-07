package com.example.app;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WebSocketServer extends org.java_websocket.server.WebSocketServer {

    //存储映射
    private static Map<WebSocket, String> webSocketClients = new HashMap<>();

    public WebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("WebSocket 客户端连接：" + conn.getRemoteSocketAddress());

        String uniqueId = UUID.randomUUID().toString();
        webSocketClients.put(conn, uniqueId);
        conn.send("userId: " + uniqueId);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("WebSocket 客户端断开连接：" + conn.getRemoteSocketAddress());
        webSocketClients.remove(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("WebSocket 收到消息：" + message);
        String senderId = webSocketClients.get(conn);
        if (senderId == null) {
            senderId = message;
            webSocketClients.put(conn, senderId);
            return;  //跳过第一次的设置消息
        }
        broadcastToWebSocketClients(message, senderId);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket 服务器已启动！");
    }
    public static void broadcastToWebSocketClients(String message, String senderId) {
        for (WebSocket client : webSocketClients.keySet()) {
            if (!webSocketClients.get(client).equals(senderId)) {
                client.send(message);
            }
        }
    }

    public static void main(String[] args) {
        WebSocketServer server = new WebSocketServer(8081);
        server.start();
        System.out.println("WebSocket 服务器启动，监听端口：8081");
    }
}

