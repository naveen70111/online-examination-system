package com.onlineexam.ui.student;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.List;
import com.onlineexam.data.ExamStore;

public class SubjectSelectionUI extends JFrame {

    public SubjectSelectionUI() {

        setTitle("Select Subject");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(240, 244, 248));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 73, 94));
        header.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JLabel titleLabel = new JLabel("SELECT SUBJECT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(245, 247, 250));
        header.add(titleLabel, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        panel.setBackground(new Color(240, 244, 248));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        Color[] subjectColors = {
                new Color(52, 152, 219),   // Java
                new Color(46, 204, 113),   // DBMS
                new Color(230, 126, 34),   // Aptitude
                new Color(155, 89, 182)    // SQL
        };
        int subjectColorIndex = 0;

        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/online_exam_db?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "sruthi123"
            );

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
            "SELECT * FROM subjects ORDER BY FIELD(subject_name,'Java','DBMS','Aptitude','SQL')"
            );
            boolean hasSubjects = false;

            while (rs.next()) {

                hasSubjects = true;

                int id = rs.getInt("id");
                String name = rs.getString("subject_name");

                JButton btn = new JButton(name);
                Color subjectColor = subjectColors[subjectColorIndex % subjectColors.length];
                subjectColorIndex++;

                btn.setBackground(subjectColor);
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
                btn.setFocusPainted(false);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(subjectColor.darker(), 2),
                        BorderFactory.createEmptyBorder(14, 16, 14, 16)
                ));

                btn.addActionListener(e -> {

                    // ✅ Store selected subject
                    ExamStore.setSelectedSubject(id);

                    // ✅ Check if questions exist for this subject
                    List<ExamStore.Question> questions =
                            ExamStore.getQuestionsBySubject(id);

                    if (questions.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "No questions available for " + name + " subject!");
                        return;
                    }

                    // ✅ If questions available → continue
                    dispose();
                    new InstructionsUI();
                });

                panel.add(btn);
            }

            if (!hasSubjects) {
                JOptionPane.showMessageDialog(this,
                        "No subjects available!",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(240, 244, 248));

        root.add(header, BorderLayout.NORTH);
        root.add(scrollPane, BorderLayout.CENTER);
        add(root);
        setVisible(true);
    }
}
