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

public class createQuestion extends JFrame {
    private JTextArea textArea;
    private JLabel characterCountLabel;
    private JTextField queryIdTextField;
    private String userId;

    public createQuestion(String userId) {
        this.userId = userId;

        setTitle("Ask Question");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel queryIdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel queryIdLabel = new JLabel("Query ID:");
        queryIdTextField = new JTextField(10);
        queryIdPanel.add(queryIdLabel);
        queryIdPanel.add(queryIdTextField);

        JPanel questionPanel = new JPanel(new BorderLayout());
        JLabel questionLabel = new JLabel("Question:");
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        characterCountLabel = new JLabel("Characters: 0");
        questionPanel.add(questionLabel, BorderLayout.NORTH);
        questionPanel.add(scrollPane, BorderLayout.CENTER);
        questionPanel.add(characterCountLabel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton askButton = new JButton("Ask");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(askButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(queryIdPanel, BorderLayout.NORTH);
        mainPanel.add(questionPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        askButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String queryId = queryIdTextField.getText();
                String question = textArea.getText();

                if (queryId.isEmpty() || question.isEmpty()) {
                    JOptionPane.showMessageDialog(createQuestion.this, "Please enter both Query ID and Question.");
                } else {
                    saveQuestion(queryId, question);
                    dispose(); // Close the createQuestion frame
                    new MainPageMember(userId);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the createQuestion frame
                new MainPageMember(userId); // Redirect to MainPageMember passing the userId
            }
        });

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateCharacterCount();
            }

            public void removeUpdate(DocumentEvent e) {
                updateCharacterCount();
            }

            public void changedUpdate(DocumentEvent e) {
                updateCharacterCount();
            }
        });

        setVisible(true);
    }

    private void updateCharacterCount() {
        int characterCount = textArea.getText().length();
        characterCountLabel.setText("Characters: " + characterCount);
    }

    private void saveQuestion(String queryId, String question) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "sairaj");
            PreparedStatement statement = connection.prepareStatement("INSERT INTO query_table (query_id, query, user_id) VALUES (?, ?, ?)");
            statement.setString(1, queryId);
            statement.setString(2, question);
            statement.setString(3, userId);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: Failed to save the question to the database.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new createQuestion("sampleUserId");
            }
        });
    }
}
