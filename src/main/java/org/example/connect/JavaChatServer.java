package org.example.connect;

import java.io.*;
import java.net.*;
import java.util.*;

public class JavaChatServer {
    private static final int PORT = 30000; // 기본 포트 번호
    private static Set<ClientHandler> clients = new HashSet<>();
    private static List<String> firstClientList = null;
    private static List<String> secondClientList = null;

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

    public synchronized static void receiveList(List<String> clientList, ClientHandler sender) {
        if (firstClientList == null) {
            firstClientList = clientList;
        } else if (secondClientList == null) {
            secondClientList = clientList;

            // Broadcast both lists to all clients
            for (ClientHandler client : clients) {
                client.sendLists(firstClientList, secondClientList);
            }

            // Reset for next round
            firstClientList = null;
            secondClientList = null;
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

                // Read and process incoming lists
                String receivedList;
                while ((receivedList = in.readLine()) != null) {
                    System.out.println("Received list from client: " + receivedList);
                    List<String> clientList = Arrays.asList(receivedList.split(","));
                    receiveList(clientList, this);
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

        public void sendLists(List<String> list1, List<String> list2) {
            out.println("List 1: " + String.join(",", list1));
            out.println("List 2: " + String.join(",", list2));
        }
    }
}
