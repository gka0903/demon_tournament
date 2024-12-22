package org.example.select;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.example.card.Card;
import org.example.characterEnum.CharacterCardList;
import org.example.connect.CardSelectionAndChatView;

public class JavaChatClientMain extends JFrame {
    private JTextField txtUserName;
    private JTextField txtServerAddress;
    private JTextField txtPort;

    public JavaChatClientMain() {
        setTitle("Chat Client Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("User Name:"));
        txtUserName = new JTextField();
        add(txtUserName);

        add(new JLabel("Server Address:"));
        txtServerAddress = new JTextField("127.0.0.1");
        add(txtServerAddress);

        add(new JLabel("Port:"));
        txtPort = new JTextField("30000");
        add(txtPort);

        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ConnectAction());
        add(btnConnect);

        setVisible(true);
    }

    private class ConnectAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userName = txtUserName.getText().trim();
            String serverAddress = txtServerAddress.getText().trim();
            int port = Integer.parseInt(txtPort.getText().trim());

            List<Card> cardList;
            if (userName.equals("INUYASHA")) {
                cardList = CharacterCardList.INUYASHA.getCards();
            } else {
                cardList = CharacterCardList.SESSHOMARU.getCards();
            }

            new CardSelectionAndChatView(userName, serverAddress, port, cardList);
            dispose(); // Close the login window
        }
    }

    public static void main(String[] args) {
        new JavaChatClientMain();
    }
}
