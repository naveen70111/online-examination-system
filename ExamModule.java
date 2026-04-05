package com.onlineexam.ui.student;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.onlineexam.data.ExamStore;
import com.onlineexam.dao.AttemptDAO;

public class ExamModule extends JFrame {

    private JLabel questionLabel, timerLabel;
    private JRadioButton[] options;
    private ButtonGroup group;
    private JButton nextBtn, submitBtn;

    // Question list (subject wise)
    private static List<ExamStore.Question> questions;

    private int index = 0;
    private int timeLeft;
    private javax.swing.Timer timer;

    // Getter for ReviewUI
    public static List<ExamStore.Question> getQuestionsList() {
        return questions;
    }

    public ExamModule() {

        // ================= RESET OLD EXAM DATA =================
        ExamStore.resetExam();

        // ================= LOAD SUBJECT QUESTIONS =================
        int subjectId = ExamStore.getSelectedSubject();

        questions = new ArrayList<>(
                ExamStore.getQuestionsBySubject(subjectId)
        );

        if (questions == null || questions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No questions available for this subject!");
            dispose();
            new StudentDashboard();
            return;
        }

        // Shuffle questions
        Collections.shuffle(questions);

        // 1 minute per question
        timeLeft = questions.size() * 60;

        // ================= FRAME SETTINGS =================
        setTitle("Online Exam - " + ExamStore.getSubjectName(subjectId));

        // Full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Remove title bar (real exam mode)
        setUndecorated(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(241, 246, 252));

        // ================= TOP PANEL =================
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(44, 62, 80));
        top.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        questionLabel.setForeground(new Color(245, 248, 250));

        timerLabel = new JLabel("Time Left: " + timeLeft + " sec");
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        timerLabel.setForeground(new Color(248, 196, 113));

        top.add(questionLabel, BorderLayout.WEST);
        top.add(timerLabel, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        // ================= OPTIONS PANEL =================
        JPanel center = new JPanel(new GridLayout(4, 1, 15, 15));
        center.setBackground(new Color(241, 246, 252));
        center.setBorder(BorderFactory.createEmptyBorder(14, 18, 10, 18));

        options = new JRadioButton[4];
        group = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(new Font("Segoe UI", Font.PLAIN, 20));
            options[i].setOpaque(true);
            options[i].setBackground(Color.WHITE);
            options[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(205, 216, 228), 1),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
            ));
            group.add(options[i]);
            center.add(options[i]);
        }

        add(center, BorderLayout.CENTER);

        // ================= BUTTON PANEL =================
        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(241, 246, 252));
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 0, 16, 0));

        nextBtn = new JButton("Next");
        nextBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nextBtn.setBackground(new Color(41, 128, 185));
        nextBtn.setForeground(Color.WHITE);
        nextBtn.setFocusPainted(false);
        nextBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextBtn.addActionListener(e -> nextQuestion());

        submitBtn = new JButton("Submit");
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        submitBtn.setBackground(new Color(39, 174, 96));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.setVisible(false);
        submitBtn.addActionListener(e -> {
            saveAnswer();
            finishExam();
        });

        bottom.add(nextBtn);
        bottom.add(submitBtn);

        add(bottom, BorderLayout.SOUTH);

        // ================= START =================
        startTimer();
        loadQuestion();

        setVisible(true);
    }

    // ================= LOAD QUESTION =================
    private void loadQuestion() {

        if (index >= questions.size()) {
            finishExam();
            return;
        }

        ExamStore.Question q = questions.get(index);

        questionLabel.setText("Q" + (index + 1) + ". " + q.text);

        for (int i = 0; i < 4; i++) {
            options[i].setText(q.options[i]);
        }

        group.clearSelection();

        if (index == questions.size() - 1) {
            nextBtn.setVisible(false);
            submitBtn.setVisible(true);
        } else {
            nextBtn.setVisible(true);
            submitBtn.setVisible(false);
        }
    }

    // ================= NEXT QUESTION =================
    private void nextQuestion() {
        saveAnswer();
        index++;
        loadQuestion();
    }

    // ================= SAVE ANSWER =================
    private void saveAnswer() {
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                ExamStore.studentAnswers.put(index, i);
            }
        }
    }

    // ================= TIMER =================
    private void startTimer() {
        timer = new javax.swing.Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time Left: " + timeLeft + " sec");

            if (timeLeft <= 0) {
                finishExam();
            }
        });
        timer.start();
    }

    // ================= FINISH EXAM =================
    private void finishExam() {

        if (timer != null)
            timer.stop();

        ExamStore.score = 0;

        // Score only for this subject (out of 30)
        for (int i = 0; i < questions.size(); i++) {
            Integer ans = ExamStore.studentAnswers.get(i);

            if (ans != null &&
                    ans == questions.get(i).correctIndex) {
                ExamStore.score++;
            }
        }

        ExamStore.recordAttempt(
                ExamStore.getCurrentUserId(),
                ExamStore.getSelectedSubject(),
                ExamStore.score
        );
        boolean saved = AttemptDAO.saveAttempt(
                ExamStore.getCurrentUserId(),
                ExamStore.getSelectedSubject(),
                ExamStore.score
        );
        if (!saved) {
            JOptionPane.showMessageDialog(
                    this,
                    "Result saved only for current session. Database save failed.",
                    "Database Error",
                    JOptionPane.WARNING_MESSAGE
            );
        }

        dispose();
        new ResultUI();   // Result will show score / 30
    }
}
