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

public class ViewAnswersMember extends JFrame {
    private JPanel mainPanel;
    private String queryId;
    private String userID;
    
    public ViewAnswersMember(String queryId, String userID) {
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
                    "SELECT user_id, answer FROM answer_table WHERE query_id = '" + queryId + "'");

            // Display each answer in the main panel
            while (resultSet.next()) {
                String userQId = resultSet.getString("user_id");
                String answer = resultSet.getString("answer");

                // Create a panel for each answer
                JPanel answerPanel = new JPanel();
                answerPanel.setLayout(new BorderLayout());
                answerPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

                JLabel userIdLabel = new JLabel("User ID: " + userQId);
                userIdLabel.setFont(new Font("Arial", Font.PLAIN, 12));

                JLabel answerLabel = new JLabel("Answer: " + answer);
                answerLabel.setFont(new Font("Arial", Font.BOLD, 14));

                JButton deleteButton = new JButton("Delete");
                deleteButton.setFont(new Font("Arial", Font.PLAIN, 12));
                deleteButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	if(userID.equals(userQId)) {
                    			int confirm = JOptionPane.showConfirmDialog(
	                            ViewAnswersMember.this,
	                            "Are you sure you want to delete this answer?",
	                            "Confirm Delete",
	                            JOptionPane.YES_NO_OPTION
	                        );
	
	                        if (confirm == JOptionPane.YES_OPTION) {
	                            // Perform the deletion from the database
	                            deleteAnswer(answer);
	                            // Remove the answer panel from the main panel
	                            mainPanel.remove(answerPanel);
	                            // Repaint the main panel to reflect the changes
	                            mainPanel.revalidate();
	                            mainPanel.repaint();
	                        }
                    	}
                    	else
                    	{
                    		JOptionPane.showMessageDialog(
                                    ViewAnswersMember.this,
                                    "You can only delete your own queries.",
                                    "Unauthorized Deletion",
                                    JOptionPane.WARNING_MESSAGE
                            );
                    	}
                    }
                });

                JPanel buttonPanel = new JPanel(new BorderLayout());
                buttonPanel.add(deleteButton, BorderLayout.EAST);

                answerPanel.add(userIdLabel, BorderLayout.NORTH);
                answerPanel.add(answerLabel, BorderLayout.CENTER);
                answerPanel.add(buttonPanel, BorderLayout.SOUTH);

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

    private void deleteAnswer(String answer) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "sairaj");
            Statement statement = connection.createStatement();

            // Delete the answer from the answer_table
            String deleteAnswer = "DELETE FROM answer_table WHERE answer = '" + answer + "'";
            statement.executeUpdate(deleteAnswer);

            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: Failed to delete the answer.");
        }
    }

    private void addBackButton() {
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the ViewAnswersMember frame
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
                new ViewAnswersMember("sampleQueryId", "");
            }
        });
    }
}
