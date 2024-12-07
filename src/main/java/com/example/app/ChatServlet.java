package com.example.app;

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.IOException;

public class ChatServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 处理前端页面请求
        request.getRequestDispatcher("/index.html").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String message = request.getParameter("message");
        // 这里可以将消息发送到 WebSocket 或者传统 Socket
        System.out.println("收到消息：" + message);
        // 将消息传递给 WebSocketServer 或 SocketServer进行处理
    }
}

