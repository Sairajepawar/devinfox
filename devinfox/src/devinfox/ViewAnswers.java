package devinfox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class ViewAnswers extends JFrame {
    private JPanel mainPanel;
    private String queryId;
    private String userID;
    
    public ViewAnswers(String queryId, String userID) {
        this.queryId = queryId;
        this.userID = userID;
        
        setTitle("Dev-Infomaniac");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        displayAnswersFromTable();
        addBackButton();

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        getContentPane().add(scrollPane);

        setVisible(true);
    }

    private void displayAnswersFromTable() {
        // Connect to the database and fetch answers for the given queryId from the answer_table
        try {
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system",
                    "sairaj");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
            	    "SELECT user_id, answer FROM answer_table WHERE query_id = " + "'" + this.queryId + "'");

            // Display each answer in the main panel
            while (resultSet.next()) {
                String userCode = resultSet.getString("user_id");
                String answer = resultSet.getString("answer");

                // Create a panel for each answer
                JPanel answerPanel = new JPanel();
                answerPanel.setLayout(new BorderLayout());
                answerPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

                JLabel userIdLabel = new JLabel("User ID: " + userCode);
                userIdLabel.setFont(new Font("Arial", Font.PLAIN, 12));

                JLabel answerLabel = new JLabel("Answer: " + answer);
                answerLabel.setFont(new Font("Arial", Font.BOLD, 14));

                answerPanel.add(userIdLabel, BorderLayout.NORTH);
                answerPanel.add(answerLabel, BorderLayout.CENTER);

                mainPanel.add(answerPanel);
                mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between answer panels
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: Failed to fetch answers from the database.");
        }
    }

    private void addBackButton() {
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the ViewAnswers frame
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (userID.equals("")) {
                            new MainPageGuest(); // Redirect to MainPageGuest
                        } else {
                            new MainPageMember(userID); // Redirect to MainPageMember with the userID
                        }
                    }
                });
            }
        });

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonPanel.add(backButton, BorderLayout.WEST);

        mainPanel.add(buttonPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ViewAnswers("sampleQueryId", "");
            }
        });
    }
}
