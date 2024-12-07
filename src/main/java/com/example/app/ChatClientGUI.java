package com.example.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClientGUI {
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static JTextArea messageArea;
    private static JTextField messageInput;
    private static String userId;

    public static void main(String[] args) {
        // 创建 JFrame
        JFrame frame = new JFrame("聊天应用");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null); // 窗口居中显示
        frame.setLayout(new BorderLayout());

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        messageInput = new JTextField();
        JButton sendButton = new JButton("发送");
        panel.add(messageInput, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        JButton exitButton = new JButton("退出");
        buttonPanel.add(exitButton);

        frame.add(panel, BorderLayout.SOUTH);
        frame.add(buttonPanel, BorderLayout.NORTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitChat();
            }
        });

        frame.setVisible(true);

        try {
            socket = new Socket("127.0.0.1", 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(new ReceiveMessage()).start();

            userId = "user_" + Math.random();

            out.println(userId);
            showMessage("连接成功！");
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("连接失败！");
        }
    }

    private static void sendMessage() {
        String message = messageInput.getText();
        if (message != null && !message.trim().isEmpty() && socket != null) {
            out.println(message);
            messageInput.setText("");
            showMessage("我：" + message);
        }
    }

    private static void showMessage(String message) {
        messageArea.append(message + "\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
    }

    private static void exitChat() {
        try {
            if (socket != null && !socket.isClosed()) {
                out.println("退出聊天");
                socket.close();
                showMessage("已退出聊天");
            }
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static class ReceiveMessage implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    if (!message.startsWith(userId)) {
                        showMessage("对方:" + message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

