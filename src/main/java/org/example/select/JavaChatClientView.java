package org.example.select;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class JavaChatClientView extends JFrame {
    private JTextArea textArea;
    private String userName;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public JavaChatClientView(String userName, String serverAddress, int port, List<String> cardNames) {
        this.userName = userName;

        setTitle("Java Chat Client");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create text area to display messages
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Connect to server
        connectToServer(serverAddress, port);

        // Send the card list to the server
        sendCardList(cardNames);

        // Start a thread to listen for incoming messages
        new Thread(new Listener()).start();

        setVisible(true);
    }

    private void connectToServer(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Notify the server of the user's name
            out.println(userName + " has joined the chat.");
            textArea.append("Connected to server.\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to connect to server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void sendCardList(List<String> cardNames) {
        if (cardNames == null || cardNames.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No cards selected to send!", "Empty Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String listString = String.join(",", cardNames);
        out.println("Card List: " + listString);
        textArea.append("Sent Card List: " + listString + "\n");
    }

    private class Listener implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    textArea.append(message + "\n");
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(JavaChatClientView.this, "Disconnected from server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
    }
}
