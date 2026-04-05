package com.onlineexam.ui.admin;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import com.onlineexam.data.ExamStore;

public class AddQuestionUI extends JFrame {

    private JTextField questionField;
    private JTextField[] optionFields;
    private JComboBox<String> correctAnswerBox;
    private JComboBox<String> subjectBox;

    public AddQuestionUI() {

        setTitle("Add Question");
        setSize(680, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(241, 246, 252));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 73, 94));
        header.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JLabel title = new JLabel("ADD QUESTION", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(246, 248, 250));
        header.add(title, BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(9, 2, 12, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 219, 230), 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel subjectLabel = new JLabel("Select Subject:");
        subjectLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(subjectLabel);

        subjectBox = new JComboBox<>(new String[]{"Java", "DBMS", "Aptitude", "SQL"});
        subjectBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(subjectBox);

        JLabel questionLabel = new JLabel("Question:");
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(questionLabel);

        questionField = new JTextField();
        questionField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(questionField);

        optionFields = new JTextField[4];
        for (int i = 0; i < 4; i++) {
            JLabel optionLabel = new JLabel("Option " + (i + 1) + ":");
            optionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            panel.add(optionLabel);

            optionFields[i] = new JTextField();
            optionFields[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            panel.add(optionFields[i]);
        }

        JLabel correctLabel = new JLabel("Correct Answer:");
        correctLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(correctLabel);

        correctAnswerBox = new JComboBox<>(new String[]{"Option 1", "Option 2", "Option 3", "Option 4"});
        correctAnswerBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(correctAnswerBox);

        JButton addBtn = new JButton("Add Question");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBtn.setBackground(new Color(46, 204, 113));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(e -> addQuestion());

        panel.add(new JLabel(""));
        panel.add(addBtn);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createEmptyBorder(18, 22, 20, 22));
        wrap.add(panel, BorderLayout.CENTER);

        root.add(header, BorderLayout.NORTH);
        root.add(wrap, BorderLayout.CENTER);

        add(root);
        setVisible(true);
    }

    private void addQuestion() {

        String question = questionField.getText().trim();
        if (question.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter question!");
            return;
        }

        String[] options = new String[4];
        for (int i = 0; i < 4; i++) {
            options[i] = optionFields[i].getText().trim();
            if (options[i].isEmpty()) {
                JOptionPane.showMessageDialog(this, "All options required!");
                return;
            }
        }

        int correctIndex = correctAnswerBox.getSelectedIndex();
        int selectedIndex = subjectBox.getSelectedIndex();
        int subjectId;
        if (selectedIndex == 0) {
            subjectId = ExamStore.JAVA;
        } else if (selectedIndex == 1) {
            subjectId = ExamStore.DBMS;
        } else if (selectedIndex == 2) {
            subjectId = ExamStore.APTITUDE;
        } else {
            subjectId = ExamStore.SQL;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/online_exam_db?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "sruthi123"
            );

            PreparedStatement check = con.prepareStatement(
                    "SELECT COUNT(*) FROM questions WHERE subject_id=?"
            );
            check.setInt(1, subjectId);
            ResultSet rs = check.executeQuery();
            rs.next();

            if (rs.getInt(1) >= 30) {
                JOptionPane.showMessageDialog(this,
                        "Maximum 30 questions allowed per subject!");
                con.close();
                return;
            }

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO questions(subject_id, question, option1, option2, option3, option4, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );

            ps.setInt(1, subjectId);
            ps.setString(2, question);
            ps.setString(3, options[0]);
            ps.setString(4, options[1]);
            ps.setString(5, options[2]);
            ps.setString(6, options[3]);
            ps.setInt(7, correctIndex + 1);

            ps.executeUpdate();
            con.close();

            JOptionPane.showMessageDialog(this, "Question Added Successfully!");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        questionField.setText("");
        for (JTextField f : optionFields) {
            f.setText("");
        }
    }
}
