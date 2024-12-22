package org.example.test;

import org.example.select.CharacterSelectGUI;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class GameClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameClient::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Game Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JButton startButton = new JButton("GAME START");
        frame.add(startButton);
        frame.setVisible(true);

        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 30000);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                startButton.addActionListener(e -> {
                    frame.dispose();
                    new CharacterSelectGUI(socket, out, in);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

