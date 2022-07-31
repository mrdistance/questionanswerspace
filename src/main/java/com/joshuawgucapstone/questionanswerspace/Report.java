package com.joshuawgucapstone.questionanswerspace;

import java.sql.Timestamp;

public class Report {
    private Timestamp TimeStamp;
    private int AccountId;
    private String UserName;
    private String QuizName;

    public Report(Timestamp timeStamp, int accountId, String userName, String quizName, String score) {
        TimeStamp = timeStamp;
        AccountId = accountId;
        UserName = userName;
        QuizName = quizName;
        Score = score;
    }

    public Timestamp getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        TimeStamp = timeStamp;
    }

    public int getAccountId() {
        return AccountId;
    }

    public void setAccountId(int accountId) {
        AccountId = accountId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getQuizName() {
        return QuizName;
    }

    public void setQuizName(String quizName) {
        QuizName = quizName;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    private String Score;


}
