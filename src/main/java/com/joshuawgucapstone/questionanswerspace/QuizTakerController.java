package com.joshuawgucapstone.questionanswerspace;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

public class QuizTakerController implements Initializable {

    Stage stage;
    Parent scene;
    Database db = new Database();
    private User user;
    private Quiz quiz;
    private List<QuestionStub> _questions;
    private int questionNumber;
    private QuizBuilder builder;
    private double score;
    private List<IQuestion> _quizQuestions;
    private List<String> summaryData;
    @FXML
    private Label intro;
    @FXML
    private Button startButton;
    @FXML
    private Button exitButton;
    @FXML
    private Label questionText;
    @FXML
    private Button answerOne;
    @FXML
    private Button answerTwo;
    @FXML
    private Button answerThree;
    @FXML
    private Button answerFour;
    @FXML
    private TextField entryText;
    @FXML
    private Button nextButton;



    public void initializeData(User user, Quiz quiz) throws SQLException {
        answerOne.setVisible(false);
        answerTwo.setVisible(false);
        answerThree.setVisible(false);
        answerFour.setVisible(false);
        questionText.setVisible(false);
        entryText.setVisible(false);
        nextButton.setVisible(false);
        summaryData = new ArrayList<>();
        this.user = user;
        this.quiz = quiz;
        questionNumber = -1;
        _questions = db.getQuestionsForQuiz(quiz);
        intro.setText("Quiz Name : " + quiz.getTitle() +
                "\nNumber of Questions : " + _questions.size() +
                "\nCreated By : " + quiz.getAuthor());
        builder = new QuizBuilder(_questions);
        _quizQuestions = builder.GetQuestionsWithAnswers();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void next(ActionEvent event) throws SQLException, IOException {
        String userAnswer;
        String result;
        if(!(event.getSource().equals(startButton))) {
            if (event.getSource().equals(answerOne)) {
                userAnswer = answerOne.getText();
            } else if (event.getSource().equals(answerTwo)) {
                userAnswer = answerTwo.getText();
            } else if (event.getSource().equals(answerThree)) {
                userAnswer = answerThree.getText();
            } else if (event.getSource().equals(answerFour)) {
                userAnswer = answerFour.getText();
            } else{
                userAnswer = entryText.getText();
            }
            String answer = _questions.get(questionNumber).getAnswer();
            if (userAnswer.equalsIgnoreCase(answer)) {
                result = "CORRECT!";
                score++;
            } else {
                result = "INCORRECT!";
            }
            summaryData.add("Question Number " + (questionNumber+1) + ":      " + result + "\nQuestion:   " + _questions.get(questionNumber).getText()+"\nCorrect Answer:  "+_questions.get(questionNumber).getAnswer() + "\nYour Answer:   " + userAnswer);
        }
        questionNumber++;
        setUpScene(event);
    }

    private void setUpScene(ActionEvent event) throws IOException, SQLException {
        intro.setVisible(false);
        exitButton.setVisible(false);
        startButton.setVisible(false);
        answerOne.setVisible(false);
        answerTwo.setVisible(false);
        answerThree.setVisible(false);
        answerFour.setVisible(false);
        questionText.setVisible(false);
        entryText.setVisible(false);
        nextButton.setVisible(false);
        if(questionNumber >= 0 && questionNumber < _questions.size()){
            questionText.setVisible(true);
            IQuestion question = _quizQuestions.get(questionNumber);
            if(question instanceof TrueFalseQuestion){
                questionText.setText(((TrueFalseQuestion) question).QuestionStub.getText());
                answerOne.setVisible(true);
                answerOne.setText("TRUE");
                answerTwo.setVisible(true);
                answerTwo.setText("FALSE");
            }else if(question instanceof FillInTheBlankQuestion){
                entryText.setText("");
                String questionWords = ((FillInTheBlankQuestion) question).QuestionStub.getText();
                String questionAnswer = ((FillInTheBlankQuestion) question).QuestionStub.getAnswer();
                String redactedText = questionWords.replace(questionAnswer, "  ________ ");
                questionText.setText(redactedText);
                entryText.setVisible(true);
                nextButton.setVisible(true);
            }else if(question instanceof MultipleChoiceNumberQuestion){
                questionText.setText(((MultipleChoiceNumberQuestion) question).QuestionStub.getText());
                List<String> answers = ((MultipleChoiceNumberQuestion) question).AnswerList;
                answerOne.setVisible(true);
                answerOne.setText(answers.get(0));
                answerTwo.setVisible(true);
                answerTwo.setText(answers.get(1));
                answerThree.setVisible(true);
                answerThree.setText(answers.get(2));
                answerFour.setVisible(true);
                answerFour.setText(answers.get(3));
            }else{
                questionText.setText(((MultipleChoiceWordQuestion) question).QuestionStub.getText());
                List<String> answers = ((MultipleChoiceWordQuestion) question).AnswerList;
                answerOne.setVisible(true);
                answerOne.setText(answers.get(0));
                answerTwo.setVisible(true);
                answerTwo.setText(answers.get(1));
                answerThree.setVisible(true);
                answerThree.setText(answers.get(2));
                answerFour.setVisible(true);
                answerFour.setText(answers.get(3));
            }
        }
        else{
            DecimalFormat df = new DecimalFormat("0.0");
            score = (score/_questions.size())*100;
            String scoreString = df.format(score) + "%";
            summaryData.add(0, "SCORE : " +scoreString);
            summaryData.add(0, quiz.getTitle());
            Report report = new Report(new Timestamp(System.currentTimeMillis()), user.AccountId(), user.UserName(), quiz.getTitle(), scoreString);
            db.addReport(report);
            stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("quiz_summary_view.fxml"));
            scene = loader.load();
            QuizSummaryController controller = loader.getController();
            controller.initializeData(user, summaryData);
            stage.setScene(new Scene(scene));
        }
    }

    public void exit(ActionEvent event) throws IOException {
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("account_home_view.fxml"));
        scene = loader.load();
        AccountHomeController controller = loader.getController();
        controller.initializeData(user);
        stage.setScene(new Scene(scene));
    }
}
