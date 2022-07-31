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
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    Stage stage;
    Parent scene;
    Database db = new Database();
    @FXML
    private TextField groupName;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Label errorMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMessage.setText("");
    }

    public void onLoginButtonClick(ActionEvent event) throws IOException, SQLException {
        errorMessage.setText("");
        if(groupName.getText().isEmpty() || username.getText().isEmpty() || password.getText().isEmpty()){
            errorMessage.setText("All fields are required!");
        }else {
            User user = areValidCredentials(groupName.getText(), username.getText(), password.getText());
            if (user == null) {
                errorMessage.setText("Incorrect Groupname, Username, or Password!");
            } else {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("account_home_view.fxml"));
                scene = loader.load();
                AccountHomeController controller = loader.getController();
                controller.initializeData(user);
                stage.setScene(new Scene(scene));
            }
        }
    }

    private User areValidCredentials(String groupname, String username, String password) throws SQLException {
        return db.validateLogin(groupname, username, password);
    }
}