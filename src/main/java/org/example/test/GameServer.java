package org.example.test;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer extends JFrame {
    private JTextArea logArea;

    public GameServer() {
        setTitle("Game Server");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        setVisible(true);

        // 서버 시작
        new Thread(this::startServer).start();
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(30001)) {
            log("Server is running...");

            // 두 클라이언트 연결
            log("Waiting for Player 1...");
            Socket player1Socket = serverSocket.accept();
            log("Player 1 connected.");
            ObjectOutputStream player1Out = new ObjectOutputStream(player1Socket.getOutputStream());
            ObjectInputStream player1In = new ObjectInputStream(player1Socket.getInputStream());

            log("Waiting for Player 2...");
            Socket player2Socket = serverSocket.accept();
            log("Player 2 connected.");
            ObjectOutputStream player2Out = new ObjectOutputStream(player2Socket.getOutputStream());
            ObjectInputStream player2In = new ObjectInputStream(player2Socket.getInputStream());

            // 캐릭터 선택 처리
            handleCharacterSelection(player1In, player1Out, player2In, player2Out);

        } catch (IOException e) {
            log("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleCharacterSelection(ObjectInputStream player1In, ObjectOutputStream player1Out,
                                          ObjectInputStream player2In, ObjectOutputStream player2Out) {
        try {
            // 각 클라이언트로부터 캐릭터 객체 수신
            String player1Character = (String) player1In.readObject();
            String player2Character = (String) player2In.readObject();

            log("Player 1 selected: " + player1Character);
            log("Player 2 selected: " + player2Character);

            // 상대방 캐릭터 정보를 각 클라이언트로 전송
            player1Out.writeObject(player2Character);
            player1Out.flush();
            player2Out.writeObject(player1Character);
            player2Out.flush();

        } catch (IOException | ClassNotFoundException e) {
            log("Error handling character selection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }


    public static void main(String[] args) {
        new GameServer();
    }
}

