package com.onlineexam.main;

import javax.swing.SwingUtilities;
import com.onlineexam.ui.LoginUI;
import com.onlineexam.dao.QuestionDAO;
import com.onlineexam.dao.AttemptDAO;

public class Main {
    public static void main(String[] args) {

        // Load questions from DB FIRST
        QuestionDAO.loadQuestionsFromDB();
        AttemptDAO.initializeAttemptTable();
        AttemptDAO.loadAttemptsFromDB();

        // Start UI
        SwingUtilities.invokeLater(() -> {
            new LoginUI();
        });
    }
}
