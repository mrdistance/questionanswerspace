package com.joshuawgucapstone.questionanswerspace;

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
import java.util.List;
import java.util.ResourceBundle;

public class QuizSummaryController implements Initializable {

    Stage stage;
    Parent scene;
    private User user;
    private List<String> summaryData;
    @FXML
    private Label quizTitle;
    @FXML
    private Label quizScore;
    @FXML
    private TextArea summarySpace;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initializeData(User user, List<String> summaryData){
        this.user = user;
        this.summaryData = summaryData;
        quizTitle.setText(summaryData.get(0));
        quizScore.setText(summaryData.get(1));
        StringBuilder builder = new StringBuilder();
        for(int i = 2; i < summaryData.size(); i++) {
            builder.append(summaryData.get((i)));
            builder.append("\n\n");
        }
        summarySpace.appendText(builder.toString());
        summarySpace.setEditable(false);
    }

    public void home(ActionEvent event) throws IOException {
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("account_home_view.fxml"));
        scene = loader.load();
        AccountHomeController controller = loader.getController();
        controller.initializeData(user);
        stage.setScene(new Scene(scene));
    }
}
