package devinfox;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class MainPageGuest extends JFrame {
    private JPanel mainPanel;

    public MainPageGuest() {

        setTitle("Dev-Infomaniac");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        displayMessagesFromTable();

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        getContentPane().add(scrollPane);

        setVisible(true);
    }

    private void displayMessagesFromTable() {
        // Connect to the database and fetch messages from the query_table
        try {
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system",
                    "sairaj");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT query_id, query, user_id FROM query_table");

            // Display each message in the main panel with view answer button
            while (resultSet.next()) {
                String query = resultSet.getString("query");
                String userQId = resultSet.getString("user_id");
                String queryId = resultSet.getString("query_id");

                // Create a panel for each query with view answer button
                JPanel queryPanel = new JPanel();
                queryPanel.setLayout(new BorderLayout());
                queryPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

                JLabel userQIdLabel = new JLabel("User ID: " + userQId);
                userQIdLabel.setFont(new Font("Arial", Font.PLAIN, 12));

                JLabel queryLabel = new JLabel("Query: " + query);
                queryLabel.setFont(new Font("Arial", Font.BOLD, 14));

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

                JButton viewAnswerButton = createButton("View Answer");

                viewAnswerButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Redirect to ViewAnswers page and pass the queryId
                        new ViewAnswers(queryId,"");
                        dispose(); // Close the MainPageGuest frame
                    }
                });

                buttonPanel.add(Box.createHorizontalGlue());
                buttonPanel.add(viewAnswerButton);

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
                new MainPageGuest();
            }
        });
    }
}
