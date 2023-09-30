package devinfox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class guestPage extends JFrame {
    public guestPage() {
        setTitle("Dev-Infomaniac");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10)); // 2 rows, 1 column, with 10px spacing

        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action when Browse button is clicked
                MainPageGuest mainPageGuest = new MainPageGuest();
                mainPageGuest.setVisible(true);
                guestPage.this.dispose();
            }
        });
        panel.add(browseButton);

        JButton createAccountButton = new JButton("Create an Account");
        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action when Create Account button is clicked
                SignInPage signInPage = new SignInPage();
                signInPage.setVisible(true);
                guestPage.this.dispose();
            }
        });
        panel.add(createAccountButton);

        JButton homeButton = new JButton("Return to Home");
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action when home button is pressed
                start start = new start();
                start.setVisible(true);
                guestPage.this.dispose();
            }
        });
        panel.add(homeButton);
        
        getContentPane().add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                guestPage guestPage = new guestPage();
                guestPage.setVisible(true);
            }
        });
    }
}
