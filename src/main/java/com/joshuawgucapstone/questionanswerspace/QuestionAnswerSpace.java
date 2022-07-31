package com.joshuawgucapstone.questionanswerspace;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class QuestionAnswerSpace extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(com.joshuawgucapstone.questionanswerspace.QuestionAnswerSpace.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 460);
        stage.setTitle("Question Answer Space");
        stage.setScene(scene);
        stage.show();
    }
}
