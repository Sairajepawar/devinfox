package devinfox;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class SignInPage extends JFrame {
    private JTextField userIdField, nameField, emailField, contactField;
    private JPasswordField passwordField;

    public SignInPage() {
        setTitle("Sign In To Dev-Infomaniac");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 250);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        panel.add(new JLabel("User ID:"));
        userIdField = new JTextField();
        panel.add(userIdField);

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Email ID:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Contact Number:"));
        contactField = new JTextField();
        panel.add(contactField);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });
        panel.add(signUpButton);
        
        JButton homeButton = new JButton("Return to Home");
        homeButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		start start = new start();
        		start.setVisible(true);
        		SignInPage.this.dispose();
        	}
        });
        panel.add(homeButton);
        
        getContentPane().add(panel);
    }

    private void signUp() {
        String userId = userIdField.getText();
        String name = nameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();
        String contact = contactField.getText();
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "sairaj");
            String query = "INSERT INTO user_info(user_id, name, password, email_id, contact_number) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userId);
            statement.setString(2, name);
            statement.setString(3, password);
            statement.setString(4, email);
            statement.setString(5, contact);
            statement.executeUpdate();
            statement.close();
            connection.close();
            JOptionPane.showMessageDialog(this, "Sign up successful!");
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: Failed to sign up.");
        }
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SignInPage signInPage = new SignInPage();
                signInPage.setVisible(true);
            }
        });
    }
}
