package com.joshuawgucapstone.questionanswerspace;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;


public class Database {

    private final String protocol = "jdbc";
    private final String vendor = ":mysql:";
    private final String location = "//qasdb.cer835ojy6i3.us-east-1.rds.amazonaws.com/";
    private final String databaseName = "QuestionAnswerSpace";
    private final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private final String userName = ""; // dbUsername
    private String password = ""; // dbPassword
    private Connection connection;  // Connection Interface


    //Constructor
    public Database() {

    }

    //========================================Connection Methods========================================================

    /**
     * This method establishes a connection with the database
     *
     * @return the connection that was established
     */
    private Connection getConnection() {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }

        return this.connection;
    }

    /**
     * This method closes the connection with the database
     */
    private void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    private ResultSet query(PreparedStatement ps) throws SQLException {
        return ps.executeQuery();
    }

    public int update(PreparedStatement ps) throws SQLException{
        return ps.executeUpdate();
    }

    //================================================================= LOGIN =========================================================================

    public User validateLogin(String groupName, String username, String password) throws SQLException {
        int accountId = getAccount(groupName, password);
        if(accountId != -1){
            String userName = "";
            if(!username.equals(getAdmin(accountId))){
                userName = getUser(accountId, username);
            }
            else{
                userName = username;
            }
            if(!userName.equals("invalid")){
                return new User(userName,accountId);
            }
        }
        return null;
    }

    private int getAccount(String groupName, String password) throws SQLException {
        String query = "Select * from Account where GroupName = ?";
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, groupName);
        ResultSet rs = query(ps);
        if(!rs.next()){
            closeConnection();
            return -1;
        }
        if(!password.equals(rs.getString(3))){
            closeConnection();
            return -1;
        }
        int accountId = rs.getInt(1);
        closeConnection();
        return accountId;
    }

    private String getUser(int accountId, String username) throws SQLException {
        String query = "Select * from User where accountId = ? and Username = ?";
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, accountId);
        ps.setString(2, username);
        ResultSet rs = query(ps);
        if(!rs.next()){
            closeConnection();
            return "invalid";
        }
        closeConnection();
        return username;
    }

    public String getAdmin(int accountId) throws SQLException {
        String query = "Select * from Account where accountId = ?";
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, accountId);
        ResultSet rs = query(ps);
        if(!rs.next()){
            closeConnection();
            return "invalid";
        }
        String admin = rs.getString(5);
        closeConnection();
        return admin;
    }

    //=====================================================================  QUIZZES =======================================================

    public ObservableList<Quiz> getQuizzes(User user) throws SQLException {
        ObservableList<Quiz> quizzes = FXCollections.observableArrayList();
        String query = "Select * from Quiz where AccountId = ? and (IsPublic = 1 || CreatedBy = ?)";
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, user.AccountId());
        ps.setString(2, user.UserName());
        ResultSet rs = query(ps);
        while(rs.next()){
            int quizId = rs.getInt(1);
            String topic = rs.getString(3);
            String title = rs.getString(4);
            String createdBy = rs.getString(5);
            int isPublic = rs.getInt(6);
            Quiz quiz = new Quiz(quizId, user.AccountId(), topic, title, createdBy, isPublic == 1);
            quizzes.add(quiz);
        }
        closeConnection();
        return quizzes;
    }

    public ObservableList<Quiz> getSpecifiedQuizzes(String input, User user) throws SQLException {
        ObservableList<Quiz> quizzes = FXCollections.observableArrayList();
        String query = "Select * from Quiz where Title LIKE ? and (IsPublic = 1 || CreatedBy = ?)";
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, "%"+input+"%");
        ps.setString(2, user.UserName());
        ResultSet rs = query(ps);
        while(rs.next()){
            int quizId = rs.getInt(1);
            String topic = rs.getString(3);
            String title = rs.getString(4);
            String createdBy = rs.getString(5);
            int isPublic = rs.getInt(6);
            Quiz quiz = new Quiz(quizId, user.AccountId(), topic, title, createdBy, isPublic == 1);
            quizzes.add(quiz);
        }
        closeConnection();
        return quizzes;
    }

    public int addQuiz(Quiz quiz) throws SQLException {
        String query = "Insert Into Quiz(AccountId, Topic, Title, CreatedBy, IsPublic) Values(?, ?, ?, ?, ?)";
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, quiz.getAccountId());
        ps.setString(2, quiz.getTopic());
        ps.setString(3, quiz.getTitle());
        ps.setString(4, quiz.getAuthor());
        ps.setBoolean(5, quiz.isPublic());
        update(ps);
        int id = 0;
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            id = generatedKeys.getInt(1);
        }
        closeConnection();
        return id;
    }

    public void updateQuiz(Quiz quiz) throws SQLException {
        String query = "Update Quiz SET Topic = ?, Title = ?, IsPublic = ? WHERE QuizId = ?";
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, quiz.getTopic());
        ps.setString(2, quiz.getTitle());
        ps.setBoolean(3, quiz.isPublic());
        ps.setInt(4, quiz.getQuizId());
        update(ps);
        closeConnection();
    }

    public void deleteQuiz(Quiz quiz) throws SQLException {
        String query = "Delete From Quiz where QuizId = ?";
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1,quiz.getQuizId());
        update(ps);
        closeConnection();
    }

    //================================================================= Questions ===========================================================

    public ObservableList<QuestionStub> getQuestionsForQuiz(Quiz quiz) throws SQLException{
        ObservableList<QuestionStub> questions = FXCollections.observableArrayList();
        String query = "Select * from Question WHERE QuestionId IN" +
                "(SELECT QuestionId FROM Quiz_Question Where QuizId = ?)";
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, quiz.getQuizId());
        ResultSet rs = query(ps);
        while(rs.next()){
            int questionId = rs.getInt(1);
            String text = rs.getString(2);
            String answer = rs.getString(3);
            boolean isPublic = rs.getBoolean(4);
            QuestionStub stub = new QuestionStub(questionId, text, answer, isPublic);
            questions.add(stub);
        }
        closeConnection();
        return questions;
    }

    public int addQuestion(QuestionStub stub) throws SQLException {
        String query = "Insert Into Question(Text, Answer, IsPublic) Values(?, ?, ?)";
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, stub.getText());
        ps.setString(2, stub.getAnswer());
        ps.setBoolean(3, stub.isPublic());
        update(ps);
        int id = 0;
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            id = generatedKeys.getInt(1);
        }
        closeConnection();
        return id;
    }

    public void updateQuestion(int questionId, boolean publicModifier) throws SQLException {
        String query = "Update Question SET IsPublic = ? WHERE QuestionId = ?";
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setBoolean(1, publicModifier);
        ps.setInt(2, questionId);
        update(ps);
        closeConnection();
    }

    //================================================================ Quiz Question =======================================================

    public void addQuizQuestionReference(int quizId, int questionId) throws SQLException {
        String query = "Insert into Quiz_Question values(?, ?)";
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, quizId);
        ps.setInt(2, questionId);
        update(ps);
        closeConnection();
    }

    public void removeQuizQuestionReference(int quizId, int questionId) throws SQLException{
        String query = "delete from Quiz_Question where quizId = ? and questionId = ?";
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, quizId);
        ps.setInt(2, questionId);
        update(ps);
        closeConnection();
    }
    //================================================================ Reports =============================================================
    public ObservableList<Report> getReports(User user) throws SQLException{
        ObservableList<Report> reports = FXCollections.observableArrayList();
        String query = "Select * from Report where AccountId = ?";
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, user.AccountId());
        ResultSet rs = query(ps);
        while(rs.next()){
            int accountId = rs.getInt(2);
            String name = rs.getString(3);
            Timestamp dateTimeStamp = rs.getTimestamp(4);
            String generatedBy = rs.getString(5);
            String score = rs.getString(6);
            Report report = new Report(dateTimeStamp, accountId, generatedBy, name, score);
            reports.add(report);
        }
        closeConnection();
        return reports;
    }

    public ObservableList<Report> getSpecifiedReports(String quizName) throws SQLException {
        ObservableList<Report> reports = FXCollections.observableArrayList();
        String query = "Select * from Report where QuizName LIKE ?";
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, "%"+quizName+"%");
        ResultSet rs = query(ps);
        while(rs.next()){
            int accountId = rs.getInt(2);
            String quiz = rs.getString(3);
            Timestamp dateTimeStamp = rs.getTimestamp(4);
            String generatedBy = rs.getString(5);
            String score = rs.getString(6);
            Report report = new Report(dateTimeStamp, accountId, generatedBy,quiz, score);
            reports.add(report);
        }
        closeConnection();
        return reports;
    }

    public void addReport(Report report) throws SQLException {
        String query = "Insert Into Report(AccountId, QuizName, DateTimeStamp, GeneratedBy, Score) Values(?, ?, ?, ?, ?)";
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, report.getAccountId());
        ps.setString(2, report.getQuizName());
        ps.setTimestamp(3, report.getTimeStamp());
        ps.setString(4, report.getUserName());
        ps.setString(5, report.getScore());
        update(ps);
        closeConnection();
    }
}
