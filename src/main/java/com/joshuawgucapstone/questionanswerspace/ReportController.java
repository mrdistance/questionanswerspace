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

public class ReportController implements Initializable {

    Stage stage;
    Parent scene;
    Database db = new Database();
    User user;
    @FXML
    private TextField searchBox;
    @FXML
    private TableColumn<?, ?> timestamp;
    @FXML
    private TableColumn<?, ?> quizName;
    @FXML
    private TableColumn<?, ?> username;
    @FXML
    private TableColumn<?, ?> score;
    @FXML
    private TableView<Report> reportsTable;

    public void initializeData(User user){
        this.user = user;
        timestamp.setCellValueFactory(new PropertyValueFactory<>("TimeStamp"));
        quizName.setCellValueFactory(new PropertyValueFactory<>("QuizName"));
        username.setCellValueFactory(new PropertyValueFactory<>("UserName"));
        score.setCellValueFactory(new PropertyValueFactory<>("Score"));
        try {
            ObservableList<Report> reports = db.getReports(user);
            filterReports(reports);
            reportsTable.setItems(reports);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void back(ActionEvent event) throws IOException {
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("account_home_view.fxml"));
        scene = loader.load();
        AccountHomeController controller = loader.getController();
        controller.initializeData(user);
        stage.setScene(new Scene(scene));
    }

    public void search(ActionEvent event) throws SQLException {
            ObservableList<Report> reports = db.getSpecifiedReports(searchBox.getText());
            filterReports(reports);
            reportsTable.setItems(reports);
    }

    private void filterReports(ObservableList<Report> reports) throws SQLException {
        String admin = db.getAdmin(user.AccountId());
        for(Report report : reports){
            String creator = report.getUserName();
            if(!(user.UserName().equals(creator) || (user.UserName().equals(admin) && report.getAccountId() == user.AccountId()))) {
                Report filteredReport = new Report(report.getTimeStamp(), report.getAccountId(), "Anonymous", report.getQuizName(), report.getScore());
                reports.set(reports.indexOf(report), filteredReport);
            }
        }
    }
}
