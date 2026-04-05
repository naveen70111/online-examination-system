package com.onlineexam.dao;

import java.sql.*;
import com.onlineexam.data.ExamStore;

public class QuestionDAO {

    private static final String URL =
           "jdbc:mysql://localhost:3306/online_exam_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "sruthi123";   // your MySQL password

    public static void loadQuestionsFromDB() {
        try {
            // ✅ STEP 1: LOAD MYSQL JDBC DRIVER (THIS WAS MISSING)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ STEP 2: CONNECT TO DATABASE
            Connection con = DriverManager.getConnection(URL, USER, PASS);

            // ✅ STEP 3: FETCH QUESTIONS
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM questions");

            // clear old questions before loading
            ExamStore.clearQuestions();

            while (rs.next()) {
                String question = rs.getString("question");

                String[] options = {
                        rs.getString("option1"),
                        rs.getString("option2"),
                        rs.getString("option3"),
                        rs.getString("option4")
                };

                int correctIndex = rs.getInt("correct_option") - 1;

                int subjectId = rs.getInt("subject_id");

                ExamStore.addQuestion(
                new ExamStore.Question(subjectId, question, options, correctIndex)
                );
            }

            System.out.println(
                    "Questions loaded from DB: " + ExamStore.getQuestionCount()
            );

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
