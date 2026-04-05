package com.onlineexam.ui.student;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.onlineexam.data.ExamStore;

public class ReviewUI extends JFrame {

    public ReviewUI() {

        setTitle("Review Answers");
        setSize(820, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(241, 246, 252));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 73, 94));
        header.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JLabel title = new JLabel("REVIEW ANSWERS", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(246, 248, 250));
        header.add(title, BorderLayout.CENTER);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setBackground(Color.WHITE);
        area.setForeground(new Color(33, 47, 61));
        area.setMargin(new Insets(14, 14, 14, 14));

        List<ExamStore.Question> qs = ExamModule.getQuestionsList();

        for (int i = 0; i < qs.size(); i++) {

            ExamStore.Question q = qs.get(i);

            area.append("Q" + (i + 1) + ". " + q.text + "\n");

            for (int j = 0; j < 4; j++) {
                area.append("   " + (j + 1) + ") " + q.options[j] + "\n");
            }

            Integer ans = ExamStore.studentAnswers.get(i);

            area.append("Your Answer: " +
                    (ans == null ? "Not Answered" : "Option " + (ans + 1)) + "\n");

            area.append("Correct Answer: Option " + (q.correctIndex + 1));

            area.append("\n----------------------------------------\n");
        }

        JButton back = new JButton("Back to Dashboard");
        back.setFont(new Font("Segoe UI", Font.BOLD, 14));
        back.setBackground(new Color(41, 128, 185));
        back.setForeground(Color.WHITE);
        back.setFocusPainted(false);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));

        back.addActionListener(e -> {
            dispose();
            new StudentDashboard();
        });

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createEmptyBorder(14, 16, 10, 16));

        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(241, 246, 252));
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        bottom.add(back);

        root.add(header, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        add(root);
        setVisible(true);
    }
}
