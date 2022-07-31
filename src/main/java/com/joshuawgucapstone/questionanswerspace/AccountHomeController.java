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
import java.util.ResourceBundle;

public class AccountHomeController implements Initializable {

    Stage stage;
    Parent scene;
    Database db = new Database();
    private User user;

    @FXML
    private TableView<Quiz> quizTable;
    @FXML
    private TableColumn<?, ?> quizName = new TableColumn<>();
    @FXML
    private TableColumn<?, ?> createdBy = new TableColumn<>();
    @FXML
    private Label errorMessage;
    @FXML
    private TextField searchBox;

    void initializeData(User user) {
        this.user = user;
        errorMessage.setText("");
        quizName.setCellValueFactory(new PropertyValueFactory<>("Title"));
        createdBy.setCellValueFactory(new PropertyValueFactory<>("Author"));
        try {
            quizTable.setItems(db.getQuizzes(this.user));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
    }

    public void search(ActionEvent event) throws SQLException {
        errorMessage.setText("");
        String input = searchBox.getText();
        quizTable.setItems(db.getSpecifiedQuizzes(input, user));
    }

    public void takeQuiz(ActionEvent event) throws IOException, SQLException {
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("quiz_taker_view.fxml"));
        scene = loader.load();
        QuizTakerController controller = loader.getController();
        if(quizTable.getSelectionModel().getSelectedItem() != null) {
            Quiz quiz = quizTable.getSelectionModel().getSelectedItem();
            controller.initializeData(user, quiz);
            stage.setScene(new Scene(scene));
        }else {
            errorMessage.setText("Select the Quiz You Wish to Take");
        }
    }

    public void editQuiz(ActionEvent event) throws IOException, SQLException {
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("quiz_edit_view.fxml"));
        scene = loader.load();
        QuizEditController controller = loader.getController();
        if(quizTable.getSelectionModel().getSelectedItem() != null) {
            Quiz quiz = quizTable.getSelectionModel().getSelectedItem();
            if(user.UserName().equals(quiz.getAuthor())) {
                controller.initializeData(quiz, user, FXCollections.observableArrayList());
                stage.setScene(new Scene(scene));
            }
            else{
                errorMessage.setText("Not authorized to edit this quiz");
            }
        }else {
            errorMessage.setText("Select the Quiz You Wish to Modify");
        }
    }

    public void createQuiz(ActionEvent event) throws IOException, SQLException {
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("quiz_edit_view.fxml"));
        scene = loader.load();
        QuizEditController controller = loader.getController();
        controller.initializeData(null, user, FXCollections.observableArrayList());
        stage.setScene(new Scene(scene));
    }

    public void deleteQuiz(ActionEvent event) throws SQLException {
        Quiz quiz = quizTable.getSelectionModel().getSelectedItem();
        if(quiz != null) {
            if(user.UserName().equals(quiz.getAuthor())) {
                db.deleteQuiz(quiz);
                quizTable.setItems(db.getQuizzes(user));
            }
            else{
                errorMessage.setText("Not authorized to Delete this quiz");
            }
        }else {
            errorMessage.setText("Select the Quiz You Wish to Delete");
        }
        errorMessage.setText(quiz.getTitle() + ": Deleted Successfully");
    }

    public void viewReports(ActionEvent event) throws IOException {
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("report_view.fxml"));
        scene = loader.load();
        ReportController controller = loader.getController();
        controller.initializeData(user);
        stage.setScene(new Scene(scene));
    }

    public void logout(ActionEvent event) throws IOException {
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        stage.setScene(new Scene(scene));
    }
}
