package com.joshuawgucapstone.questionanswerspace;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class QuizEditController implements Initializable {

    Stage stage;
    Parent scene;
    Database db = new Database();
    private boolean publicModifier;
    private User user;
    private Quiz quiz;
    private ObservableList<QuestionStub> displayQuestions;
    private ObservableList<QuestionStub> dbQuestions; 

    @FXML
    private Label errorMessage;
    @FXML
    private TextField quizName;
    @FXML
    private TextField quizTopic;
    @FXML
    private TableColumn<QuestionStub, ?> questionColumn;
    @FXML
    private TableColumn<QuestionStub, ?> answerColumn;
    @FXML
    private TableView<QuestionStub> questionsTable;
    @FXML
    private CheckBox isPublic;

    //fixme duplicating questions in database and duplicating questions in quiz edit every time question add save is pressed and not showing questions if quiz was new and is null
    public void initializeData(Quiz quiz, User user, ObservableList<QuestionStub> displayQuestions) throws SQLException {
        this.displayQuestions = displayQuestions;
        dbQuestions = FXCollections.observableArrayList();
        errorMessage.setText("");
        this.quiz = quiz;
        this.user = user;
        publicModifier = false;
        if(quiz != null){
            dbQuestions = db.getQuestionsForQuiz(quiz);
            publicModifier = quiz.isPublic();
            if(displayQuestions.size() <= 1){
                displayQuestions.addAll(dbQuestions);
            }
            quizName.setText(quiz.getTitle());
            quizTopic.setText(quiz.getTopic());
            isPublic.setSelected(quiz.isPublic());
            questionColumn.setCellValueFactory(new PropertyValueFactory<>("Text"));
            answerColumn.setCellValueFactory(new PropertyValueFactory<>("Answer"));
            questionsTable.setItems(displayQuestions);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void openQuestionAdd(ActionEvent event) throws IOException {
        if(quiz == null){
            if(!(quizName.getText().isEmpty() && quizTopic.getText().isEmpty())){
                quiz = new Quiz(0, user.AccountId(), quizTopic.getText(), quizName.getText(), user.UserName(), isPublic.isSelected());
            }
            else{
                quiz = new Quiz(0, user.AccountId(), "", "", user.UserName(), isPublic.isSelected());
            }
        }
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("question_add_view.fxml"));
        scene = loader.load();
        QuestionAddController controller = loader.getController();
        controller.initializeData(quiz, user, displayQuestions);
        stage.setScene(new Scene(scene));
    }

    public void removeQuestion(ActionEvent event) throws SQLException {
        QuestionStub question = questionsTable.getSelectionModel().getSelectedItem();
        if(!(question == null)){
            displayQuestions.remove(question);
            if(!(quiz.getQuizId() == 0 || question.getId() == 0)) {
                db.removeQuizQuestionReference(quiz.getQuizId(), question.getId());
            }
            questionsTable.setItems(displayQuestions);
        }
    }

    public void cancel(ActionEvent event) throws IOException {
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("account_home_view.fxml"));
        scene = loader.load();
        AccountHomeController controller = loader.getController();
        controller.initializeData(user);
        stage.setScene(new Scene(scene));
    }

    public void save(ActionEvent event) throws IOException, SQLException {
        if(quizName.getText().isEmpty() || quizTopic.getText().isEmpty()){
            errorMessage.setText("All fields are required to save a quiz!");
        }
        else {
            saveQuiz();
            updateExistingQuestions();
            saveNewQuestions();
            changeScene(event);
        }
    }
    
    private void saveQuiz() throws SQLException {
        if(quiz.getQuizId() == 0){
            quiz = new Quiz(quiz.getQuizId(), user.AccountId(), quizTopic.getText(), quizName.getText(), user.UserName(), isPublic.isSelected());
            quiz.setQuizId(db.addQuiz(quiz));
        }else {
            quiz.setTopic(quizTopic.getText());
            quiz.setTitle(quizName.getText());
            quiz.setPublic(isPublic.isSelected());
            db.updateQuiz(quiz);
        }
    }
    
    private void updateExistingQuestions() throws SQLException {
        if(publicModifier != quiz.isPublic()){
            for(QuestionStub question : dbQuestions){
                db.updateQuestion(question.getId(), quiz.isPublic());
            }
        }
    }
    
    private void saveNewQuestions() throws SQLException{
        for(QuestionStub question : displayQuestions){
            if(!(dbQuestions.contains(question))){
                if(question.isPublic() != quiz.isPublic()){
                    question.setPublic(quiz.isPublic());
                }
                int questionId = db.addQuestion(question);
                db.addQuizQuestionReference(quiz.getQuizId(), questionId);
            }
        }
    }
    
    private void changeScene(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("account_home_view.fxml"));
        scene = loader.load();
        AccountHomeController controller = loader.getController();
        controller.initializeData(user);
        stage.setScene(new Scene(scene));
    }
}
