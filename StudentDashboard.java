package com.onlineexam.ui.student;

import javax.swing.*;
import java.awt.*;
import com.onlineexam.ui.LoginUI;

public class StudentDashboard extends JFrame {

    public StudentDashboard() {
        setTitle("Student Dashboard");
        setSize(760, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(208, 214, 222));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(46, 174, 108));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("STUDENT DASHBOARD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(new Color(233, 245, 237));
        header.add(titleLabel, BorderLayout.CENTER);

        JPanel cardPanel = new JPanel(new GridLayout(1, 4, 24, 0));
        cardPanel.setOpaque(false);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(80, 48, 70, 48));

        JButton startExam = createBtn("<html><center>Start<br>Exam</center></html>", new Color(74, 136, 188));
        JButton result = createBtn("<html><center>View<br>Result</center></html>", new Color(251, 140, 0));
        JButton review = createBtn("<html><center>Review<br>Answers</center></html>", new Color(138, 43, 226));
        JButton logout = createBtn("Logout", new Color(226, 0, 72));

        startExam.addActionListener(e -> {
            dispose();
            new SubjectSelectionUI();
        });

        result.addActionListener(e -> new ResultUI());
        review.addActionListener(e -> new ReviewUI());

        logout.addActionListener(e -> {
            dispose();
            new LoginUI();
        });

        cardPanel.add(startExam);
        cardPanel.add(result);
        cardPanel.add(review);
        cardPanel.add(logout);

        root.add(header, BorderLayout.NORTH);
        root.add(cardPanel, BorderLayout.CENTER);

        add(root);
        setVisible(true);
    }

    private JButton createBtn(String text, Color color) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 20));
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(173, 182, 191), 2),
                BorderFactory.createEmptyBorder(18, 12, 18, 12)
        ));
        b.setHorizontalAlignment(SwingConstants.CENTER);
        b.setVerticalAlignment(SwingConstants.CENTER);
        return b;
    }
}
