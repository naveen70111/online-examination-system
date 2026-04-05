package com.onlineexam.ui.admin;

import com.onlineexam.data.ExamStore;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ViewQuestionsUI extends JFrame {

    public ViewQuestionsUI() {

        setTitle("View Questions - Subject Wise");
        setSize(820, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(240, 245, 251));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(22, 160, 133));
        header.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JLabel title = new JLabel("VIEW QUESTIONS", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(247, 252, 249));
        header.add(title, BorderLayout.CENTER);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setBackground(Color.WHITE);
        area.setForeground(new Color(33, 47, 61));
        area.setMargin(new Insets(14, 14, 14, 14));

        int[] subjects = {
                ExamStore.JAVA,
                ExamStore.DBMS,
                ExamStore.SQL,
                ExamStore.APTITUDE
        };

        for (int subjectId : subjects) {

            List<ExamStore.Question> questions =
                    ExamStore.getQuestionsBySubject(subjectId);

            area.append("\n=====================================\n");
            area.append("SUBJECT: " + ExamStore.getSubjectName(subjectId) + "\n");
            area.append("=====================================\n\n");

            int qNo = 1;

            for (ExamStore.Question q : questions) {

                area.append("Q" + qNo++ + ". " + q.text + "\n");

                for (int i = 0; i < q.options.length; i++) {
                    area.append("   " + (i + 1) + ") " + q.options[i] + "\n");
                }

                area.append("   Correct Answer: Option "
                        + (q.correctIndex + 1) + "\n");

                area.append("------------------------------------------\n");
            }
        }

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createEmptyBorder(14, 16, 16, 16));
        scroll.getViewport().setBackground(Color.WHITE);

        root.add(header, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);

        add(root);
        setVisible(true);
    }
}
