package devinfox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class start extends JFrame {
    public start(){
        setTitle("Dev-Infomaniac");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Devinfo");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(welcomeLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton guestButton = new JButton("Guest");
        guestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guestPage guestPage = new guestPage();
                guestPage.setVisible(true);
                start.this.dispose();
            }
        });
        buttonPanel.add(guestButton);

        JButton memberButton = new JButton("Member");
        memberButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginPage loginPage = new LoginPage();
                loginPage.setVisible(true);
                start.this.dispose();
            }
        });
        buttonPanel.add(memberButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                start start = new start();
                start.setVisible(true);
            }
        });
    }
}

