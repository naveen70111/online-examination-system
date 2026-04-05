package com.onlineexam.ui.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import com.onlineexam.data.ExamStore;
import com.onlineexam.ui.LoginUI;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(760, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(208, 214, 222));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(46, 174, 108));
        header.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("ADMIN DASHBOARD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(new Color(233, 245, 237));
        header.add(titleLabel, BorderLayout.CENTER);

        JPanel cardPanel = new JPanel(new GridLayout(1, 4, 24, 0));
        cardPanel.setOpaque(false);
        cardPanel.setBorder(new EmptyBorder(80, 48, 70, 48));

        JButton addQuestion = createButton("<html><center>Add<br>Question</center></html>", new Color(74, 136, 188));
        addQuestion.addActionListener(e -> new AddQuestionUI());

        JButton viewQuestions = createButton("<html><center>View<br>Questions</center></html>", new Color(251, 140, 0));
        viewQuestions.addActionListener(e -> new ViewQuestionsUI());

        JButton studentsCount = createButton("<html><center>Students<br>Attempted</center></html>", new Color(138, 43, 226));
        studentsCount.addActionListener(e -> showStudentsAttemptedDetails());

        JButton logout = createButton("Logout", new Color(226, 0, 72));
        logout.addActionListener(e -> {
            dispose();
            new LoginUI();
        });

        cardPanel.add(addQuestion);
        cardPanel.add(viewQuestions);
        cardPanel.add(studentsCount);
        cardPanel.add(logout);

        root.add(header, BorderLayout.NORTH);
        root.add(cardPanel, BorderLayout.CENTER);

        add(root);
        setVisible(true);
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(173, 182, 191), 2),
                new EmptyBorder(18, 12, 18, 12)
        ));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setVerticalAlignment(SwingConstants.CENTER);
        return btn;
    }

    private void showStudentsAttemptedDetails() {
        int[] subjects = {
                ExamStore.JAVA,
                ExamStore.DBMS,
                ExamStore.SQL,
                ExamStore.APTITUDE
        };

        JPanel dialogPanel = new JPanel(new BorderLayout(10, 10));
        dialogPanel.setBackground(new Color(244, 248, 255));
        dialogPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel heading = new JLabel("Students Attempted Report (Subject Wise)");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 16));
        heading.setForeground(new Color(26, 82, 118));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(new Color(234, 244, 255));
        tabbedPane.setForeground(new Color(28, 40, 51));
        for (int subjectId : subjects) {
            String subjectName = ExamStore.getSubjectName(subjectId);
            tabbedPane.addTab(subjectName, buildSubjectReportPanel(subjectId));
            int tabIndex = tabbedPane.getTabCount() - 1;
            tabbedPane.setBackgroundAt(tabIndex, getSubjectColor(subjectId));
            tabbedPane.setForegroundAt(tabIndex, new Color(22, 37, 57));
        }

        tabbedPane.setPreferredSize(new Dimension(640, 380));
        dialogPanel.add(heading, BorderLayout.NORTH);
        dialogPanel.add(tabbedPane, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
                this,
                dialogPanel,
                "Students Attempted - Subject Wise",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private JPanel buildSubjectReportPanel(int subjectId) {
        List<ExamStore.AttemptRecord> records = ExamStore.getAttemptRecordsBySubject(subjectId);
        String subjectName = ExamStore.getSubjectName(subjectId);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        root.setBackground(new Color(255, 255, 255));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Subject: " + subjectName);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(new Color(21, 67, 96));

        JLabel count = new JLabel("Total Students Attempted: " + records.size());
        count.setFont(new Font("Segoe UI", Font.BOLD, 13));
        count.setForeground(new Color(0, 123, 255));

        header.add(title, BorderLayout.WEST);
        header.add(count, BorderLayout.EAST);

        String[] columns = {"S.No", "User ID", "Score", "Exam Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        int rowNo = 1;
        for (ExamStore.AttemptRecord record : records) {
            model.addRow(new Object[]{rowNo++, record.userId, record.score, record.attemptedDate});
        }

        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(217, 237, 255));
        table.getTableHeader().setForeground(new Color(21, 67, 96));
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(new Color(23, 32, 42));
        table.setBackground(new Color(252, 253, 255));
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground((row % 2 == 0) ? new Color(252, 253, 255) : new Color(241, 248, 255));
                }

                if (column == 2 && value != null) {
                    int score = 0;
                    try {
                        score = Integer.parseInt(value.toString());
                    } catch (NumberFormatException ignored) {
                    }

                    if (!isSelected) {
                        if (score >= 20) {
                            c.setForeground(new Color(27, 94, 32));
                        } else if (score >= 10) {
                            c.setForeground(new Color(183, 110, 0));
                        } else {
                            c.setForeground(new Color(198, 40, 40));
                        }
                    }
                } else if (!isSelected) {
                    c.setForeground(new Color(33, 47, 61));
                }

                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(170, 204, 240)));
        scrollPane.getViewport().setBackground(new Color(252, 253, 255));

        if (records.isEmpty()) {
            JLabel emptyLabel = new JLabel("No students attempted this subject yet.", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            emptyLabel.setForeground(new Color(46, 64, 83));
            root.add(header, BorderLayout.NORTH);
            root.add(emptyLabel, BorderLayout.CENTER);
            return root;
        }

        root.add(header, BorderLayout.NORTH);
        root.add(scrollPane, BorderLayout.CENTER);
        return root;
    }

    private Color getSubjectColor(int subjectId) {
        if (subjectId == ExamStore.JAVA) {
            return new Color(221, 236, 255);
        } else if (subjectId == ExamStore.DBMS) {
            return new Color(225, 245, 234);
        } else if (subjectId == ExamStore.SQL) {
            return new Color(255, 238, 224);
        }
        return new Color(240, 235, 255);
    }
}
