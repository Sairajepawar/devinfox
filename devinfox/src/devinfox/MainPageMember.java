package devinfox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class MainPageMember extends JFrame {
    private JPanel mainPanel;
    private String userId;

    public MainPageMember(String userId) {
        this.userId = userId;

        setTitle("Dev-Infomaniac");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        addAskQueryButton(); // Add the "Ask Query" button to the frame
        displayMessagesFromTable();

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        getContentPane().add(scrollPane);

        setVisible(true);
    }

    private void addAskQueryButton() {
        JButton askQueryButton = createButton("Ask Query");
        askQueryButton.setFont(new Font("Arial", Font.BOLD, 18));
        askQueryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle "Ask Query" button click event
                dispose(); // Close the MainPageMember frame
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new createQuestion(userId); // Redirect to CreateQuestion and pass the userId
                    }
                });
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(askQueryButton);

        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between the button and messages
    }

    private void deleteButtonActionPerformed(ActionEvent e, String queryId, String userQId) {
        if (userQId.equals(userId)) {
            int confirm = JOptionPane.showConfirmDialog(
                    MainPageMember.this,
                    "Are you sure you want to delete this query?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                // Perform the deletion from the database
                deleteQuery(queryId);
                // Dispose the current MainPageMember frame
                dispose();
                // Create a new instance of MainPageMember with the updated query list
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new MainPageMember(userId);
                    }
                });
            }
        } else {
            JOptionPane.showMessageDialog(
                    MainPageMember.this,
                    "You can only delete your own queries.",
                    "Unauthorized Deletion",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void displayMessagesFromTable() {
        // Connect to the database and fetch messages from the query_table
        try {
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system",
                    "sairaj");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT query_id, query, user_id FROM query_table");

            // Display each message in the main panel with buttons
            while (resultSet.next()) {
                String query = resultSet.getString("query");
                String userQId = resultSet.getString("user_id");
                String queryId = resultSet.getString("query_id");

                // Create a panel for each query with buttons
                JPanel queryPanel = new JPanel();
                queryPanel.setLayout(new BorderLayout());
                queryPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

                JLabel userQIdLabel = new JLabel("User ID: " + userQId);
                userQIdLabel.setFont(new Font("Arial", Font.PLAIN, 12));

                JLabel queryLabel = new JLabel("Query: " + query);
                queryLabel.setFont(new Font("Arial", Font.BOLD, 14));

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

                JButton answerButton = createButton("Answer");
                JButton viewAnswerButton = createButton("View Answer");
                JButton deleteButton = createButton("Delete");

                answerButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Redirect to CreateAnswer page and pass the userId and queryId
                        new CreateAnswer(userId, queryId);
                        dispose(); // Close the MainPageMember frame
                    }
                });
                viewAnswerButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Redirect to ViewAnswersMember page and pass the queryId and userId
                    	System.out.println(queryId);
                        new ViewAnswersMember(queryId, userId);
                        dispose(); // Close the MainPageMember frame
                    }
                });
                deleteButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Handle delete button click event
                        deleteButtonActionPerformed(e, queryId, userQId);
                    }
                });

                // Add mouse listener to enable right-click functionality for the delete button
                deleteButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            // Show a confirmation dialog for right-click delete
                            deleteButton.doClick();
                        }
                    }
                });

                buttonPanel.add(answerButton);
                buttonPanel.add(viewAnswerButton);
                buttonPanel.add(deleteButton);

                queryPanel.add(userQIdLabel, BorderLayout.NORTH);
                queryPanel.add(queryLabel, BorderLayout.CENTER);
                queryPanel.add(buttonPanel, BorderLayout.SOUTH);

                mainPanel.add(queryPanel);
                mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between panels
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: Failed to fetch messages from the database.");
        }
    }

    private void deleteQuery(String queryId) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "sairaj");
            Statement statement = connection.createStatement();

            // Delete the query from the query_table
            String deleteQuery = "DELETE FROM query_table WHERE query_id = '" + queryId + "'";
            statement.executeUpdate(deleteQuery);

            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: Failed to delete the query.");
        }
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 25));
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainPageMember("sampleUserId");
            }
        });
    }
}
