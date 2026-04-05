package com.onlineexam.ui.student;

import javax.swing.*;
import java.awt.*;
import com.onlineexam.data.ExamStore;

public class InstructionsUI extends JFrame {

    public InstructionsUI() {
        setTitle("Exam Instructions");

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(16, 16));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(44, 62, 80));
        top.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel title = new JLabel("EXAM INSTRUCTIONS", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(245, 248, 250));
        top.add(title, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        area.setMargin(new Insets(24, 24, 24, 24));
        area.setBackground(new Color(250, 252, 255));
        area.setForeground(new Color(33, 47, 61));

        int subjectId = ExamStore.getSelectedSubject();

        int totalQuestions =
                ExamStore.getQuestionsBySubject(subjectId).size();

        area.setText(
                "EXAM INSTRUCTIONS\n\n" +
                        "1. Total Questions : " + totalQuestions + "\n" +
                        "2. Time Limit      : 30 Minutes\n" +
                        "3. Each question has 4 options\n" +
                        "4. No negative marking\n" +
                        "5. Do not close the exam window\n\n" +
                        "Click START EXAM to begin"
        );

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createEmptyBorder(14, 18, 8, 18));
        add(scroll, BorderLayout.CENTER);

        JButton startBtn = new JButton("START EXAM");
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        startBtn.setBackground(new Color(46, 204, 113));
        startBtn.setForeground(Color.WHITE);
        startBtn.setFocusPainted(false);
        startBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startBtn.setPreferredSize(new Dimension(220, 56));

        startBtn.addActionListener(e -> {
            dispose();
            ExamStore.resetExam();
            new ExamModule();
        });

        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(237, 242, 248));
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 0, 14, 0));
        bottom.add(startBtn);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }
}
