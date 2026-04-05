package com.onlineexam.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import com.onlineexam.data.ExamStore;
import com.onlineexam.dao.UserDAO;
import com.onlineexam.ui.admin.AdminDashboard;
import com.onlineexam.ui.student.StudentDashboard;

public class LoginUI extends JFrame {

    public LoginUI() {

        setTitle("Online Exam System");
        setSize(500, 430);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(238, 244, 251));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(31, 97, 141));
        top.setBorder(new EmptyBorder(14, 12, 14, 12));

        JLabel title = new JLabel("ONLINE EXAM SYSTEM", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(245, 249, 255));
        top.add(title, BorderLayout.CENTER);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 216, 228), 1),
                new EmptyBorder(22, 22, 22, 22)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel roleLabel = new JLabel("Role");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roleLabel.setForeground(new Color(45, 62, 80));

        JLabel userLabel = new JLabel("User ID");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(new Color(45, 62, 80));

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passLabel.setForeground(new Color(45, 62, 80));

        gbc.gridx = 0;
        gbc.gridy = 0;
        card.add(roleLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"admin", "student"});
        roleBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(roleBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        card.add(userLabel, gbc);

        gbc.gridx = 1;
        JTextField userField = new JTextField();
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        card.add(passLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passField = new JPasswordField();
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 14, 0));
        btnPanel.setOpaque(false);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(41, 128, 185));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton signupBtn = new JButton("Signup");
        signupBtn.setBackground(new Color(39, 174, 96));
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        signupBtn.setFocusPainted(false);
        signupBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnPanel.add(loginBtn);
        btnPanel.add(signupBtn);
        card.add(btnPanel, gbc);

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);
        centerWrap.setBorder(new EmptyBorder(24, 28, 24, 28));
        centerWrap.add(card);

        loginBtn.addActionListener(e -> {

            String role = roleBox.getSelectedItem().toString();
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter username and password");
                return;
            }

            boolean valid = UserDAO.validateUser(username, password, role);

            if (!valid) {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid login details or role mismatch!",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            dispose();

            if (role.equals("admin")) {
                ExamStore.setCurrentUserId("");
                new AdminDashboard();
            } else {
                ExamStore.setCurrentUserId(username);
                new StudentDashboard();
            }
        });

        signupBtn.addActionListener(e -> {
            dispose();
            new SignupUI();
        });

        root.add(top, BorderLayout.NORTH);
        root.add(centerWrap, BorderLayout.CENTER);

        add(root);
        setVisible(true);
    }
}
