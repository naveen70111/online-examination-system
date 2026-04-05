package com.onlineexam.data;

import java.util.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExamStore {

    // ================= SUBJECT CONSTANTS =================
    public static final int JAVA = 1;
    public static final int DBMS = 2;
    public static final int SQL = 3;
    public static final int APTITUDE = 4;

    // ================= IN-MEMORY QUESTIONS =================
    private static final List<Question> questions = new ArrayList<>();

    // ✅ ADD QUESTION
    public static void addQuestion(Question q) {
        questions.add(q);
    }

    // ✅ GET ALL QUESTIONS
    public static List<Question> getQuestions() {
        return questions;
    }

    // ✅ CLEAR QUESTIONS
    public static void clearQuestions() {
        questions.clear();
    }

    // ✅ GET TOTAL COUNT
    public static int getQuestionCount() {
        return questions.size();
    }

    // ================= SELECTED SUBJECT =================
    private static int selectedSubjectId = 0;

    public static void setSelectedSubject(int subjectId) {
        selectedSubjectId = subjectId;
    }

    public static int getSelectedSubject() {
        return selectedSubjectId;
    }

    // ================= GET SUBJECT NAME =================
    public static String getSubjectName(int id) {
        switch (id) {
            case 1: return "Java";
            case 2: return "DBMS";
            case 3: return "SQL";
            case 4: return "Aptitude";
            default: return "Unknown";
        }
    }

    // ================= SUBJECT FILTER =================
    public static List<Question> getQuestionsBySubject(int subjectId) {
        List<Question> filtered = new ArrayList<>();
        for (Question q : questions) {
            if (q.subjectId == subjectId) {
                filtered.add(q);
            }
        }
        return filtered;
    }

    // ================= EXAM DATA =================
    public static Map<Integer, Integer> studentAnswers = new HashMap<>();
    public static int score = 0;
    private static String currentUserId = "";

    public static void resetExam() {
        studentAnswers.clear();
        score = 0;
    }

    // ================= CURRENT STUDENT =================
    public static void setCurrentUserId(String userId) {
        currentUserId = userId == null ? "" : userId.trim();
    }

    public static String getCurrentUserId() {
        return currentUserId;
    }

    // ================= STUDENT COUNT =================
    private static int studentsAttempted = 0;
    private static final List<AttemptRecord> attemptRecords = new ArrayList<>();

    public static void incrementAttempt() {
        studentsAttempted++;
    }

    public static int getStudentsAttempted() {
        return studentsAttempted;
    }

    public static void recordAttempt(String userId, int subjectId, int scoreValue) {
        incrementAttempt();
        attemptRecords.add(new AttemptRecord(userId, subjectId, scoreValue, getCurrentDateTime()));
    }

    public static void clearAttemptRecords() {
        attemptRecords.clear();
        studentsAttempted = 0;
    }

    public static void addAttemptRecordFromDB(String userId, int subjectId, int scoreValue) {
        attemptRecords.add(new AttemptRecord(userId, subjectId, scoreValue, "-"));
        studentsAttempted = attemptRecords.size();
    }

    public static void addAttemptRecordFromDB(String userId, int subjectId, int scoreValue, String attemptedDate) {
        attemptRecords.add(new AttemptRecord(userId, subjectId, scoreValue, attemptedDate));
        studentsAttempted = attemptRecords.size();
    }

    private static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return LocalDateTime.now().format(formatter);
    }

    public static List<AttemptRecord> getAttemptRecords() {
        return new ArrayList<>(attemptRecords);
    }

    public static List<AttemptRecord> getAttemptRecordsBySubject(int subjectId) {
        List<AttemptRecord> filtered = new ArrayList<>();
        for (AttemptRecord record : attemptRecords) {
            if (record.subjectId == subjectId) {
                filtered.add(record);
            }
        }
        return filtered;
    }

    // ================= QUESTION MODEL =================
    public static class Question {
        public int subjectId;
        public String text;
        public String[] options;
        public int correctIndex;

        public Question(int subjectId, String text,
                        String[] options, int correctIndex) {
            this.subjectId = subjectId;
            this.text = text;
            this.options = options;
            this.correctIndex = correctIndex;
        }
    }

    public static class AttemptRecord {
        public String userId;
        public int subjectId;
        public int score;
        public String attemptedDate;

        public AttemptRecord(String userId, int subjectId, int score) {
            this(userId, subjectId, score, "-");
        }

        public AttemptRecord(String userId, int subjectId, int score, String attemptedDate) {
            this.userId = (userId == null || userId.trim().isEmpty()) ? "Unknown" : userId.trim();
            this.subjectId = subjectId;
            this.score = score;
            this.attemptedDate = (attemptedDate == null || attemptedDate.trim().isEmpty()) ? "-" : attemptedDate.trim();
        }
    }
}
