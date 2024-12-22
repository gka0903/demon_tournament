package org.example.game;

import java.util.Arrays;
import java.util.Collections;
import org.example.card.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.example.character.Character;
import org.example.characterEnum.CharacterCardList;
import org.example.characterEnum.CharacterEnum;
import org.example.field.PlayField;
import org.example.field.SceneCardPanel;
import org.example.select.HealthEnergyBarPanel;

public class CardSelectionAndChatView extends JFrame {
    private JTextArea chatArea;
    private JTextField chatInput;
    private GameManager gameManager;
    private List<JButton> cardButtons;
    private List<Card> cardList;
    private List<Card> selectedCards;
    private JLabel[] cardSlots;
    private ChooseCardSelectionPanel cardSelectionPanel;
    private JButton clearButton;
    private JButton sendButton;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String userName;

    public CardSelectionAndChatView(String userName, String serverAddress, int port, List<Card> cardList) {
        this.userName = userName;
        this.cardList = cardList;
        this.selectedCards = new ArrayList<>();
        this.gameManager = new GameManager(100, 100, 100, 100, new Point(0, 1), new Point(3, 1));

        setTitle("Card Selection and Chat");
        setSize(1000, 640);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cardSelectionPanel = new ChooseCardSelectionPanel(cardList);
        add(cardSelectionPanel, BorderLayout.NORTH);

        JPanel chatPanel = new JPanel(new BorderLayout());
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        chatPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel chatInputPanel = new JPanel(new BorderLayout());
        chatInput = new JTextField();
        sendButton = new JButton("Send Cards");
        chatInputPanel.add(chatInput, BorderLayout.CENTER);
        chatInputPanel.add(sendButton, BorderLayout.EAST);
        chatPanel.add(chatInputPanel, BorderLayout.SOUTH);

        add(chatPanel, BorderLayout.CENTER);

        // 서버 연결
        connectToServer(serverAddress, port);

        clearButton = cardSelectionPanel.getClearButton();
        clearButton.addActionListener(e -> cardSelectionPanel.clearSelection());
        sendButton.addActionListener(e -> sendSelectedCards(cardSelectionPanel.getSelectedCardIndexes()));

        new Thread(new Listener()).start();

        setVisible(true);
    }

    private void connectToServer(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(userName + " has joined the chat.");
            chatArea.append("Connected to server.\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to connect to server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private Character createCharacterFromData(List<String> receivedData) {
        // 받은 데이터가 최소 8개인지 확인 (이름, 카드 인덱스 3개, 상태 값 4개)
        if (receivedData.size() < 8) {
            return null;
        }

        try {
            // 캐릭터 이름과 선택된 카드 인덱스 추출
            String characterName = receivedData.get(0).trim();
            List<Integer> selectedCardIndices = Arrays.asList(
                    Integer.parseInt(receivedData.get(1).trim()), // 첫 번째 카드 인덱스
                    Integer.parseInt(receivedData.get(2).trim()), // 두 번째 카드 인덱스
                    Integer.parseInt(receivedData.get(3).trim())  // 세 번째 카드 인덱스
            );

            // 캐릭터 상태 값 추출 (HP, EN, x 좌표, y 좌표)
            int hp = Integer.parseInt(receivedData.get(4).trim());
            int en = Integer.parseInt(receivedData.get(5).trim());
            int x = Integer.parseInt(receivedData.get(6).trim());
            int y = Integer.parseInt(receivedData.get(7).trim());

            // 캐릭터 이름에 따라 카드 리스트 가져오기
            List<Card> allCards = CharacterCardList.valueOf(characterName.toUpperCase()).getCards();

            // 선택된 카드 리스트 생성
            List<Card> selectedCards = new ArrayList<>();
            for (int index : selectedCardIndices) {
                if (index >= 0 && index < allCards.size()) {
                    selectedCards.add(allCards.get(index)); // 유효한 인덱스인 경우 추가
                } else {
                    throw new IllegalArgumentException("카드 인덱스가 유효하지 않습니다: " + index);
                }
            }

            // 캐릭터 생성 및 상태 값 설정
            Character character = CharacterEnum.getCharacterByName(characterName);
            character.setHealth(hp); // HP 설정
            character.setStamina(en); // EN 설정
            character.setGridPosition(new Point(x, y)); // 위치 설정
            for (Card card : selectedCards) { // 선택된 카드 설정
                character.addCard(card);
            }

            return character; // 완성된 캐릭터 반환
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("캐릭터 데이터 파싱 중 오류 발생: " + receivedData, e);
        }
    }

    private void sendSelectedCards(List<String> cardNames) {
        if (cardNames.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No cards selected to send!", "Empty Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // GameManager 상태를 문자열로 변환
        String gameManagerData = String.format("%d, %d, %d, %d / %d, %d, %d, %d",
                gameManager.getHp1(), gameManager.getEn1(), gameManager.getP1().x, gameManager.getP1().y,
                gameManager.getHp2(), gameManager.getEn2(), gameManager.getP2().x, gameManager.getP2().y);

        String[] splitData = gameManagerData.split("/");

        // 플레이어 1과 플레이어 2 데이터 분리
        String player1Data = splitData[0].trim(); // 첫 번째 데이터
        String player2Data = splitData[1].trim(); // 두 번째 데이터

        String data;
        if (userName.equals("INUYASHA")) {
            data = player1Data;
        } else {
            data = player2Data;
        }

        String listString = String.join(",", cardNames); // 카드 리스트를 쉼표로 연결
        String message = userName + ", " + listString + ", " + data; // 유저 네임과 카드 리스트를 쉼표로 연결
        out.println(message); // 서버로 전송
        chatArea.append("Sent: " + message + "\n"); // 채팅창에 출력
    }

    private class Listener implements Runnable {
        @Override
        public void run() {
            try {
                String message1 = null;
                String message2 = null;

                while ((message1 = in.readLine()) != null && (message2 = in.readLine()) != null) {
                    if (!isValidGameData(message1) || !isValidGameData(message2)) {
                        System.out.println("Ignored non-game message:");
                        System.out.println("Message1: " + message1);
                        System.out.println("Message2: " + message2);
                        continue; // 메시지가 유효하지 않으면 무시
                    }
                    // 가공 예시: 리스트 두 개를 합쳐 새 리스트 생성
                    List<String> list1 = Arrays.asList(message1.split(","));
                    List<String> list2 = Arrays.asList(message2.split(","));

                    Character inu, sho;

                    System.out.println("list1 = " + list1);
                    System.out.println("list2 = " + list2);

                    if (list1.get(0).equals("INUYASHA")) {
                        inu = createCharacterFromData(list1);
                        sho = createCharacterFromData(list2);
                    } else {
                        inu = createCharacterFromData(list2);
                        sho = createCharacterFromData(list1);
                    }

                    animation(inu, sho, cardSelectionPanel);

                    // 새로운 리스트로 가공
                    List<String> combinedList = new ArrayList<>(list1);
                    combinedList.addAll(list2);

                    chatArea.append("Combined List: " + String.join(", ", combinedList) + "\n");
                    chatArea.setCaretPosition(chatArea.getDocument().getLength());
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(CardSelectionAndChatView.this, "Disconnected from server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }

        private boolean isValidGameData(String message) {
            // ','로 분리 후 8개 이상의 데이터가 있어야 유효
            String[] parts = message.split(",");
            if (parts.length < 8) {
                return false; // 데이터가 8개 미만이면 유효하지 않음
            }
            try {
                // 첫 번째 값이 캐릭터 이름인지 확인 (INUYASHA, SESSHOMARU 등)
                String characterName = parts[0].trim();
                CharacterEnum.valueOf(characterName.toUpperCase()); // Enum으로 변환 가능 여부 확인

                // 숫자 값 확인 (카드 인덱스 및 상태 값)
                for (int i = 1; i < 8; i++) {
                    Integer.parseInt(parts[i].trim()); // 정수 변환 시도
                }
                return true; // 모든 조건 충족 시 유효
            } catch (IllegalArgumentException e) {
                return false; // 변환 실패 시 유효하지 않음
            }
        }
    }

    public void animation(Character inu, Character sho, ChooseCardSelectionPanel cardSelectionPanel) {
        // 메인 프레임 생성
        JFrame frame = new JFrame("Grid Movement with GIF Swap and SceneCard Animation Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 800); // 화면 크기 설정 (너비 100%, 높이 80%)

        // 프레임의 레이아웃을 BoxLayout으로 설정 (세로 방향)
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        // PlayField 설정 (중간 50%)
        PlayField gamePanel = new PlayField(inu, sho);

        CharacterStateManager manager = new CharacterStateManager(inu, sho);
        manager.processCards();

        List<int[]> statsList = manager.getStateHistory();

        Point[] finalPositions = manager.getFinalPositions();

        updateGameManager(gameManager, statsList, finalPositions);

        // Health and Energy Bar Panel 설정 (상단 20%)
        HealthEnergyBarPanel healthEnergyBarPanel = new HealthEnergyBarPanel(statsList);
        healthEnergyBarPanel.setMaximumSize(new Dimension(frame.getWidth(), (int) (frame.getHeight() * 0.2)));

        gamePanel.setMaximumSize(new Dimension(frame.getWidth(), (int) (frame.getHeight() * 0.5)));

        System.out.println("inu.getCardList() = " + inu.getCardList());
        System.out.println("sho.getCardList() = " + sho.getCardList());

        List<Card> reversedInuCardList = new ArrayList<>(inu.getCardList());
        Collections.reverse(reversedInuCardList);

        // SceneCardPanel 설정 (하단 30%)
        SceneCardPanel sceneCardPanel = new SceneCardPanel();
        sceneCardPanel.initializeSceneCards(reversedInuCardList, sho.getCardList());
        sceneCardPanel.setMaximumSize(new Dimension(frame.getWidth(), (int) (frame.getHeight() * 0.25)));

        // "Return to Card Selection" 버튼 추가
        JButton returnToSelectionButton = new JButton("Return to Card Selection");
        returnToSelectionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnToSelectionButton.setPreferredSize(new Dimension(150, 40)); // 적절한 크기 설정
        returnToSelectionButton.addActionListener(e -> {
            frame.dispose(); // 애니메이션 창 닫기
            setVisible(true); // 카드 선택 창 다시 표시
            cardSelectionPanel.clearSelection(); // 카드 선택 초기화
        });

        // 24초 후 버튼 활성화
        Timer enableButtonTimer = new Timer(24000, e -> returnToSelectionButton.setEnabled(true));
        enableButtonTimer.setRepeats(false); // 한 번만 실행
        enableButtonTimer.start();

        // 패널에 여백 제거
        gamePanel.setBorder(BorderFactory.createEmptyBorder());
        sceneCardPanel.setBorder(BorderFactory.createEmptyBorder());

        // 패널을 프레임에 추가
        frame.add(healthEnergyBarPanel); // 상단 패널
        frame.add(gamePanel); // 중간 패널
        frame.add(sceneCardPanel); // 하단 패널
        frame.add(returnToSelectionButton); // 버튼 추가

        // 프레임 표시
        frame.setVisible(true);
        gamePanel.requestFocusInWindow(); // PlayField에 포커스를 맞춤
    }

    public void updateGameManager(GameManager gameManager, List<int[]> statsList, Point[] points) {
        if (statsList.isEmpty() || points == null || points.length != 2) {
            throw new IllegalArgumentException("Invalid statsList or points data");
        }

        int[] finalStats = statsList.get(statsList.size() - 1); // 마지막 상태 가져오기
        Point player1Position = points[0]; // 플레이어 1의 최종 위치
        Point player2Position = points[1]; // 플레이어 2의 최종 위치

        // GameManager 상태 갱신
        gameManager.setHp1(finalStats[0]);
        gameManager.setEn1(finalStats[1]);
        gameManager.setP1(player1Position);

        gameManager.setHp2(finalStats[2]);
        gameManager.setEn2(finalStats[3]);
        gameManager.setP2(player2Position);
    }


    // Inner Class for Card Selection Panel
    private class ChooseCardSelectionPanel extends JPanel {
        private List<JButton> cardButtons;
        private List<Card> cardList;
        private List<Card> selectedCards;
        private JLabel[] cardSlots;
        private JButton clearButton;

        public ChooseCardSelectionPanel(List<Card> cardList) {
            this.cardList = cardList;
            this.selectedCards = new ArrayList<>();
            setLayout(new BorderLayout());

            JPanel cardsGrid = new JPanel(new GridLayout(2, 4));
            cardButtons = new ArrayList<>();
            initCards(cardsGrid);

            JPanel bottomPanel = new JPanel(new BorderLayout());

            JPanel cardSlotPanel = new JPanel(new GridLayout(1, 3, 10, 0));
            cardSlots = new JLabel[3];
            for (int i = 0; i < 3; i++) {
                cardSlots[i] = new JLabel("Place Card Here", SwingConstants.CENTER);
                cardSlots[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                cardSlots[i].setPreferredSize(new Dimension(150, 200));
                cardSlots[i].setHorizontalAlignment(SwingConstants.CENTER);
                cardSlots[i].setVerticalAlignment(SwingConstants.CENTER);
                cardSlotPanel.add(cardSlots[i]);
            }
            cardSlotPanel.setBorder(BorderFactory.createTitledBorder("Selected Cards"));

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            buttonPanel.setPreferredSize(new Dimension(200, 150));

            clearButton = new JButton("Clear");
            clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            clearButton.addActionListener(e -> clearSelection());

            buttonPanel.add(Box.createVerticalGlue());
            buttonPanel.add(clearButton);
            buttonPanel.add(Box.createVerticalGlue());

            bottomPanel.add(cardSlotPanel, BorderLayout.CENTER);
            bottomPanel.add(buttonPanel, BorderLayout.EAST);

            add(cardsGrid, BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);
        }

        private void initCards(JPanel cardsGrid) {
            for (int i = 0; i < cardList.size(); i++) {
                Card card = cardList.get(i);
                ImageIcon cardImage = card.getCardImage();
                Image img = cardImage.getImage();
                Image scaledImage = img.getScaledInstance(120, 150, Image.SCALE_SMOOTH); // 새로운 크기
                JButton cardButton = new JButton(new ImageIcon(scaledImage));

                cardButton.setToolTipText("<html>Type: " + card.getCardType() +
                        "<br>DM: " + card.getCardData().getDamage() +
                        "<br>EN: " + card.getCardData().getStamina() + "</html>");
                cardButton.setPreferredSize(new Dimension(150, 180));
                cardButton.addActionListener(new CardSelectionListener(cardButton, i));
                cardButtons.add(cardButton);
                cardsGrid.add(cardButton);
            }
        }

        private class CardSelectionListener implements ActionListener {
            private final JButton cardButton;
            private final int cardIndex;

            public CardSelectionListener(JButton cardButton, int cardIndex) {
                this.cardButton = cardButton;
                this.cardIndex = cardIndex;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedCards.size() < 3) {
                    cardButton.setEnabled(false);
                    selectedCards.add(cardList.get(cardIndex));
                    updateCardSlots();
                } else {
                    JOptionPane.showMessageDialog(null, "All slots are full! Clear the slots to add more cards.", "Slots Full", JOptionPane.WARNING_MESSAGE);
                }
            }
        }

        private void updateCardSlots() {
            for (int i = 0; i < 3; i++) {
                if (i < selectedCards.size()) {
                    Card card = selectedCards.get(i);
                    Image img = card.getCardImage().getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
                    cardSlots[i].setIcon(new ImageIcon(img));
                    cardSlots[i].setText("");
                } else {
                    cardSlots[i].setIcon(null);
                    cardSlots[i].setText("Place Card Here");
                }
            }
        }

        public void clearSelection() {
            selectedCards.clear();
            for (JButton button : cardButtons) {
                button.setEnabled(true);
            }
            updateCardSlots();
        }

        public List<String> getSelectedCardIndexes() {
            List<String> cardIndexes = new ArrayList<>();
            for (Card card : selectedCards) {
                int index = cardList.indexOf(card); // 카드 리스트에서 인덱스 찾기
                if (index >= 0) {
                    cardIndexes.add(String.valueOf(index)); // 정수를 문자열로 변환하여 추가
                }
            }
            return cardIndexes;
        }

        public JButton getClearButton() {
            return clearButton;
        }
    }

    public static void main(String[] args) {

        List<Card> cardList = CharacterCardList.SESSHOMARU.getCards();
        new CardSelectionAndChatView("Player1", "127.0.0.1", 30000, cardList);
    }
}
