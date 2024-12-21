package org.example.test;

import org.example.character.Character;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class GameClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameClient::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Game Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JButton attackButton = new JButton("Send Attack");
        JButton defenseButton = new JButton("Send Defense");
        JTextArea statusArea = new JTextArea();
        statusArea.setEditable(false);
        JScrollPane statusScrollPane = new JScrollPane(statusArea);

        frame.add(attackButton);
        frame.add(defenseButton);
        frame.add(statusScrollPane);

        frame.setVisible(true);

        // Start network connection
        new Thread(() -> startClient(statusArea, attackButton, defenseButton)).start();
    }

    private static void startClient(JTextArea statusArea, JButton attackButton, JButton defenseButton) {
        try {
            Socket socket = new Socket("127.0.0.1", 30000);
            statusArea.append("Connected to the server.\n");

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // 예제 캐릭터 생성
            Character myCharacter = new Character(
                    "Inuyasha",
                    "path/to/idle.png",
                    "path/to/move.png",
                    "path/to/heal.png",
                    "path/to/defense.png",
                    "path/to/attack1.png",
                    "path/to/attack2.png",
                    "path/to/hit.png",
                    new java.awt.Point(1, 1)
            );

            // 캐릭터를 SerializableCharacter로 변환 후 전송
            SerializableCharacter serializableCharacter = CharacterConverter.toSerializable(myCharacter);
            out.writeObject(serializableCharacter);
            out.flush();
            statusArea.append("Sent my character to the server.\n");

            // 서버로부터 상대방 캐릭터 수신
            SerializableCharacter opponentCharacter = (SerializableCharacter) in.readObject();
            statusArea.append("Received opponent's character: " + opponentCharacter.getName() + "\n");

            // 버튼 클릭 이벤트 처리
            attackButton.addActionListener(e -> sendAction(out, "Attack", statusArea));
            defenseButton.addActionListener(e -> sendAction(out, "Defense", statusArea));

            // 수신 스레드 시작
            new Thread(() -> receiveMessages(in, statusArea)).start();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void sendAction(ObjectOutputStream out, String action, JTextArea statusArea) {
        try {
            out.writeObject(action);
            out.flush();
            statusArea.append("Sent action: " + action + "\n");
        } catch (IOException e) {
            statusArea.append("Error sending action: " + action + "\n");
            e.printStackTrace();
        }
    }

    private static void receiveMessages(ObjectInputStream in, JTextArea statusArea) {
        try {
            while (true) {
                String message = (String) in.readObject();
                statusArea.append("Received message: " + message + "\n");
            }
        } catch (IOException | ClassNotFoundException e) {
            statusArea.append("Error receiving messages.\n");
            e.printStackTrace();
        }
    }
}
