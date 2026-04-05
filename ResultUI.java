package com.onlineexam.ui.student;

import javax.swing.*;
import java.awt.*;
import com.onlineexam.data.ExamStore;

public class ResultUI extends JFrame {

    public ResultUI() {
        setTitle("Exam Result");
        setSize(500, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        int subjectId = ExamStore.getSelectedSubject();

        int totalQuestions =
                ExamStore.getQuestionsBySubject(subjectId).size();

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(236, 243, 250));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(93, 173, 226));
        top.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JLabel title = new JLabel("EXAM RESULT", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(246, 251, 255));
        top.add(title, BorderLayout.CENTER);

        JLabel scoreLabel = new JLabel(
                "Score: " + ExamStore.score + " / " + totalQuestions,
                SwingConstants.CENTER
        );
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        scoreLabel.setForeground(new Color(27, 79, 114));

        JButton review = new JButton("Review Answers");
        styleBtn(review, new Color(155, 89, 182));

        JButton back = new JButton("Back to Dashboard");
        styleBtn(back, new Color(41, 128, 185));

        review.addActionListener(e -> {
            dispose();
            new ReviewUI();
        });

        back.addActionListener(e -> {
            dispose();
            new StudentDashboard();
        });

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(22, 18, 18, 18));
        center.add(scoreLabel, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));
        btnPanel.add(review);
        btnPanel.add(back);

        center.add(btnPanel, BorderLayout.CENTER);

        root.add(top, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);

        add(root);
        setVisible(true);
    }

    private void styleBtn(JButton btn, Color color) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(170, 48));
    }
}
