package com.joshuawgucapstone.questionanswerspace;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class QuestionAddController implements Initializable {

    Stage stage;
    Parent scene;
    User user;
    Quiz quiz;
    ObservableList<QuestionStub> displayQuestions;

    @FXML
    private TextArea question;
    @FXML
    private TextArea answer;
    @FXML
    private Label errorMessage;

    public void initializeData(Quiz quiz, User user, ObservableList<QuestionStub> displayQuestions){
        this.displayQuestions = displayQuestions;
        errorMessage.setText("");
        this.user = user;
        this.quiz = quiz;
    }

    public void cancel(ActionEvent event) throws IOException, SQLException {
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("quiz_edit_view.fxml"));
        scene = loader.load();
        QuizEditController controller = loader.getController();
        controller.initializeData(quiz, user, displayQuestions);
        stage.setScene(new Scene(scene));
    }

    public void save(ActionEvent event) throws IOException, SQLException {
        if(question.getText().isEmpty() || answer.getText().isEmpty()){
            errorMessage.setText("All fields are required to add a question!");
        }
        else {
            QuestionStub questionStub = new QuestionStub(0, question.getText(), answer.getText(), false);
            stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("quiz_edit_view.fxml"));
            scene = loader.load();
            QuizEditController controller = loader.getController();
            displayQuestions.add(questionStub);
            controller.initializeData(quiz, user, displayQuestions);
            stage.setScene(new Scene(scene));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
