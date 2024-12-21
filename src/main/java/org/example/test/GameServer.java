package org.example.test;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(30000)) {
            System.out.println("Server is running...");

            // 두 클라이언트 소켓
            Socket player1Socket = null;
            Socket player2Socket = null;

            // 입출력 스트림
            ObjectInputStream player1In = null;
            ObjectOutputStream player1Out = null;
            ObjectInputStream player2In = null;
            ObjectOutputStream player2Out = null;

            // 첫 번째 플레이어 연결
            System.out.println("Waiting for Player 1...");
            player1Socket = serverSocket.accept();
            System.out.println("Player 1 connected.");
            player1Out = new ObjectOutputStream(player1Socket.getOutputStream());
            player1In = new ObjectInputStream(player1Socket.getInputStream());

            // 두 번째 플레이어 연결
            System.out.println("Waiting for Player 2...");
            player2Socket = serverSocket.accept();
            System.out.println("Player 2 connected.");
            player2Out = new ObjectOutputStream(player2Socket.getOutputStream());
            player2In = new ObjectInputStream(player2Socket.getInputStream());

            // 수신 및 중계 스레드
            Thread player1Listener = createListenerThread(player1In, player2Out, "Player 1");
            Thread player2Listener = createListenerThread(player2In, player1Out, "Player 2");

            player1Listener.start();
            player2Listener.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Thread createListenerThread(ObjectInputStream in, ObjectOutputStream out, String playerName) {
        return new Thread(() -> {
            try {
                while (true) {
                    Object message = in.readObject();
                    System.out.println(playerName + " sent: " + message);
                    out.writeObject(message);
                    out.flush();
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(playerName + " disconnected.");
            }
        });
    }
}
