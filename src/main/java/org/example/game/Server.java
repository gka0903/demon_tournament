package org.example.game;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 30000; // 기본 포트 번호
    private static Set<ClientHandler> clients = new HashSet<>();
    private static String firstClientData = null; // 첫 번째 클라이언트 데이터
    private static String secondClientData = null; // 두 번째 클라이언트 데이터

    public static void main(String[] args) {
        System.out.println("Chat server started...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized static void receiveData(String clientData, ClientHandler sender) {
        if (firstClientData == null) {
            firstClientData = clientData;
        } else if (secondClientData == null) {
            secondClientData = clientData;

            // 두 명의 데이터가 모두 수신되었을 때 클라이언트들에게 전송
            for (ClientHandler client : clients) {
                client.sendData(firstClientData, secondClientData);
            }

            // 리셋
            firstClientData = null;
            secondClientData = null;
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // 데이터 받기
                String receivedData;
                while ((receivedData = in.readLine()) != null) {
                    System.out.println("Received data from client: " + receivedData);
                    receiveData(receivedData, this);
                }
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clients.remove(this);
                System.out.println("Client disconnected.");
            }
        }

        public void sendData(String data1, String data2) {
            out.println(data1);
            out.println(data2);
        }
    }
}
