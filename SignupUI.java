package com.onlineexam.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.onlineexam.dao.UserDAO;

public class SignupUI extends JFrame {

    public SignupUI() {
        setTitle("Signup");
        setSize(420, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(239, 246, 255));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(26, 82, 118));
        header.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("CREATE ACCOUNT", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(245, 249, 255));
        header.add(title, BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(5, 1, 8, 8));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 217, 232), 1),
                new EmptyBorder(18, 18, 18, 18)
        ));

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField user = new JTextField();
        user.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JPasswordField pass = new JPasswordField();
        pass.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton signup = new JButton("Create Account");
        signup.setFont(new Font("Segoe UI", Font.BOLD, 14));
        signup.setBackground(new Color(40, 180, 99));
        signup.setForeground(Color.WHITE);
        signup.setFocusPainted(false);
        signup.setCursor(new Cursor(Cursor.HAND_CURSOR));

        signup.addActionListener(e -> {
            String u = user.getText().trim();
            String p = new String(pass.getPassword()).trim();

            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields required");
                return;
            }

            boolean added = UserDAO.addUser(u, p, "student");

            if (!added) {
                JOptionPane.showMessageDialog(this, "User already exists!");
                return;
            }

            JOptionPane.showMessageDialog(this, "Signup successful!");
            dispose();
            new LoginUI();
        });

        form.add(userLabel);
        form.add(user);
        form.add(passLabel);
        form.add(pass);
        form.add(signup);

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);
        centerWrap.setBorder(new EmptyBorder(18, 24, 20, 24));
        centerWrap.add(form);

        root.add(header, BorderLayout.NORTH);
        root.add(centerWrap, BorderLayout.CENTER);

        add(root);
        setVisible(true);
    }
}
