package devinfox;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateAnswer extends JFrame {
    private JTextArea textArea;
    private JLabel characterCountLabel;
    private String userId;
    private String queryId;

    public CreateAnswer(String userId, String queryId) {
        this.userId = userId;
        this.queryId = queryId;

        setTitle("Submit Answer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.getDocument().addDocumentListener(new CharacterCountDocumentListener());

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        characterCountLabel = new JLabel("Character Count: 0");
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitButtonListener());
        bottomPanel.add(characterCountLabel);
        bottomPanel.add(submitButton);

        JButton backButton = new JButton("Back");
        backButton.setActionCommand("backButton");
        backButton.addActionListener(new SubmitButtonListener());
        bottomPanel.add(backButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private class CharacterCountDocumentListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            updateCharacterCount();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateCharacterCount();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateCharacterCount();
        }

        private void updateCharacterCount() {
            int characterCount = textArea.getText().length();
            characterCountLabel.setText("Character Count: " + characterCount);
        }
    }

    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("Submit")) {
                String answer = textArea.getText();

                if (answer.length() > 0 && answer.length() <= 2000) {
                    // Insert answer into the answer_table
                    try {
                        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "sairaj");
                        PreparedStatement statement = connection.prepareStatement("INSERT INTO answer_table (query_id, user_id, answer) VALUES (?, ?, ?)");
                        statement.setString(1, queryId);
                        statement.setString(2, userId);
                        statement.setString(3, answer);
                        statement.executeUpdate();
                        statement.close();
                        connection.close();
                        JOptionPane.showMessageDialog(CreateAnswer.this, "Answer submitted!");

                        // Redirect to MainPageMember with userId
                        dispose();
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                new MainPageMember(userId).setVisible(true);
                            }
                        });
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(CreateAnswer.this, "Error: Failed to submit answer.");
                    }
                } else {
                    JOptionPane.showMessageDialog(CreateAnswer.this,
                            "Invalid character count. Please enter between 1 and 2000 characters.");
                }
            } else if (command.equals("backButton")) {
                dispose(); // Close the CreateAnswer frame
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new MainPageMember(userId).setVisible(true);
                    }
                });
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CreateAnswer("sampleUserId", "sampleQueryId");
            }
        });
    }
}
