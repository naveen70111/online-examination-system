package com.onlineexam.dao;

import com.onlineexam.data.ExamStore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AttemptDAO {

    private static final String URL =
            "jdbc:mysql://localhost:3306/online_exam_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata";
    private static final String USER = "root";
    private static final String PASS = "sruthi123";

    public static void initializeAttemptTable() {
        String sql = "CREATE TABLE IF NOT EXISTS exam_attempts ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "user_id VARCHAR(100) NOT NULL, "
                + "subject_id INT NOT NULL, "
                + "score INT NOT NULL, "
                + "attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ")";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean saveAttempt(String userId, int subjectId, int score) {
        String sql = "INSERT INTO exam_attempts(user_id, subject_id, score) VALUES (?, ?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, (userId == null || userId.trim().isEmpty()) ? "Unknown" : userId.trim());
            ps.setInt(2, subjectId);
            ps.setInt(3, score);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void loadAttemptsFromDB() {
        String sql = "SELECT user_id, subject_id, score, attempted_at FROM exam_attempts ORDER BY id";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            ExamStore.clearAttemptRecords();

            while (rs.next()) {
                String userId = rs.getString("user_id");
                int subjectId = rs.getInt("subject_id");
                int score = rs.getInt("score");
                Timestamp attemptedAt = rs.getTimestamp("attempted_at");
                String attemptedDate = formatDate(attemptedAt);
                ExamStore.addAttemptRecordFromDB(userId, subjectId, score, attemptedDate);
            }

            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     private static String formatDate(Timestamp timestamp) {
    if (timestamp == null) {
        return "-";
    }

    LocalDateTime dateTime = timestamp.toLocalDateTime();

    DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy | hh:mm a");

    return dateTime.format(formatter);
    }   
}
