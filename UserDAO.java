package com.onlineexam.dao;

import java.sql.*;

public class UserDAO {

    private static final String URL =
            "jdbc:mysql://localhost:3306/online_exam_db";
    private static final String USER = "root";
    private static final String PASS = "sruthi123";

    // ✅ ADD USER (SIGNUP)
    public static boolean addUser(String username, String password, String role) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASS);

            // 🔍 Check if user already exists
            PreparedStatement check =
                    con.prepareStatement(
                            "SELECT id FROM users WHERE username = ?"
                    );
            check.setString(1, username);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                con.close();
                return false; // ❌ user exists
            }

            // ✅ Insert new user
            PreparedStatement insert =
                    con.prepareStatement(
                            "INSERT INTO users (username, password, role) VALUES (?, ?, ?)"
                    );
            insert.setString(1, username);
            insert.setString(2, password);
            insert.setString(3, role);
            insert.executeUpdate();

            con.close();
            return true; // ✅ success

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ LOGIN VALIDATION
    public static boolean validateUser(
            String username, String password, String role
    ) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASS);

            PreparedStatement ps =
                    con.prepareStatement(
                            "SELECT id FROM users WHERE username=? AND password=? AND role=?"
                    );
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);

            ResultSet rs = ps.executeQuery();
            boolean valid = rs.next();

            con.close();
            return valid;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
