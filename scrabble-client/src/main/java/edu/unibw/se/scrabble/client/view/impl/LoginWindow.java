package edu.unibw.se.scrabble.client.view.impl;

import edu.unibw.se.scrabble.common.base.ReturnValues;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginWindow extends Stage {
    private final Label heading = new Label("Scrabble");
    private final Label nameLabel = new Label("Username");
    private final TextField nameField = new TextField();
    private final Label passwordLabel = new Label("Password");
    private final PasswordField passwordField = new PasswordField();
    private final Button loginButton = new Button("Login");
    private final Button registerButton = new Button("Register");
    private final VBox vBox = new VBox();
    private final HBox hBoxN = new HBox();
    private final HBox hBoxP = new HBox();
    private final HBox hBoxT = new HBox();
    private final HBox hBoxB = new HBox();

    {
        setTitle("Scrabble");

        hBoxN.setAlignment(Pos.CENTER);
        hBoxN.setSpacing(10);

        hBoxP.setAlignment(Pos.CENTER);
        hBoxP.setSpacing(10);

        hBoxT.setAlignment(Pos.CENTER);
        hBoxT.setSpacing(40);

        hBoxB.setAlignment(Pos.CENTER);
        hBoxB.setSpacing(20);

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(30);

        heading.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 80));
        nameLabel.setFont(Font.font("Arial", 20));
        passwordLabel.setFont(Font.font("Arial", 20));

        hBoxN.getChildren().addAll(nameLabel, nameField);
        hBoxP.getChildren().addAll(passwordLabel, passwordField);
        hBoxT.getChildren().addAll(hBoxN, hBoxP);
        hBoxB.getChildren().addAll(loginButton, registerButton);
        vBox.getChildren().addAll(heading, hBoxT, hBoxB);

        setScene(new Scene(vBox));
    }

    public LoginWindow(FxView mainView) {

        setOnCloseRequest(event -> { //weg
            close();
            Platform.exit();
            event.consume();
            System.exit(0);
        });

        registerButton.setOnAction((event) -> {
            mainView.setWindowState(WindowState.REGISTER);

            close();
        });

        loginButton.setOnAction((event) -> {
            String userName = nameField.getText();

            String message = validateUsername(userName);

            if (message != null) {
                showAlert(message);
            } else {
                String password = passwordField.getText();
                message = validatePassword(password);
                if (message != null) {
                    showAlert(message);
                } else {
                    ReturnValues.ReturnLoginUser result = ViewControl.clientConnect.loginUser(userName, password);
                    if (result != ReturnValues.ReturnLoginUser.SUCCESSFUL) {
                        message = getErrorMessageFromServer(result);
                        showAlert(message);
                    } else {
                        mainView.setUsername(userName);
                        mainView.setWindowState(WindowState.MAIN);
                        close();
                    }
                }
            }
        });
    }

    private String validateUsername(String userName) {
        if (userName.length() < 4) {
            return "Username too short";
        } else if (userName.length() > 15) {
            return "Username too long";
        } else if (!userName.matches("[a-zA-Z0-9_]+")) {
            return "Invalid characters in username";
        } else {
            return null;
        }
    }

    private String validatePassword(String password) {
        if (password.length() < 8) {
            return "Password too short";
        } else if (password.length() > 20) {
            return "Password too long";
        } else if (!password.matches("[a-zA-Z0-9?!$%&/()*]+")) {
            return "Invalid characters in password";
        } else {
            return null;
        }
    }

    private String getErrorMessageFromServer(ReturnValues.ReturnLoginUser result) {
        return switch (result) {
            case USERNAME_NOT_IN_DATABASE -> "USERNAME_NOT_IN_DATABASE: Please register first";
            case WRONG_PASSWORD -> "WRONG_PASSWORD: Wrong password!";
            case INVALID_USERNAME -> "INVALID_USERNAME: Please take a look at the username regulations";
            case INVALID_PASSWORD -> "INVALID_PASSWORD: Please take a look at the password regulations";
            case NETWORK_FAILURE, DATABASE_FAILURE, FAILURE ->
                    "NETWORK_FAILURE, DATABASE_FAILURE, FAILURE: Server problem, please call support.";
            default -> "DEFAULT: Unknown error, please call support";
        };
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login information");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }
}