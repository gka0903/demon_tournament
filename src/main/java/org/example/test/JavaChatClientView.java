package org.example.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class JavaChatClientView extends JFrame {
    private JTextArea textArea;
    private JTextField txtInput;
    private String userName;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public JavaChatClientView(String userName, String serverAddress, int port) {
        this.userName = userName;

        setTitle("Java Chat Client");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        txtInput = new JTextField();
        JButton btnSend = new JButton("Send");
        inputPanel.add(txtInput, BorderLayout.CENTER);
        inputPanel.add(btnSend, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        btnSend.addActionListener(new SendAction());
        txtInput.addActionListener(new SendAction());

        connectToServer(serverAddress, port);

        setVisible(true);
        txtInput.requestFocus();
    }

    private void connectToServer(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(userName); // Send user name to server

            new Thread(new Listener()).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to connect to server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private class SendAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String message = txtInput.getText().trim();
            if (!message.isEmpty()) {
                out.println(message);
                txtInput.setText("");
            }
        }
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
