package devinfox;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;

    public LoginPage() {
        setTitle("Login To Dev-Infomaniac");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        panel.add(new JLabel("User ID:"));
        userIdField = new JTextField();
        panel.add(userIdField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        panel.add(loginButton);
        
        JButton homeButton = new JButton("Return to Home");
        homeButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		start start = new start();
        		start.setVisible(true);
        		LoginPage.this.dispose();
        	}
        });
        panel.add(homeButton);

        getContentPane().add(panel);
    }

    private void login() {
        String userId = userIdField.getText();
        String password = new String(passwordField.getPassword());
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "sairaj");
            String query = "SELECT * FROM user_info WHERE user_id = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userId);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");

                MainPageMember mainPageMember = new MainPageMember(userId);
                mainPageMember.setVisible(true);
                dispose(); // Close the login page
            } else {
                JOptionPane.showMessageDialog(this, "Invalid user ID or password");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: Failed to login.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginPage loginPage = new LoginPage();
                loginPage.setVisible(true);
            }
        });
    }
}
