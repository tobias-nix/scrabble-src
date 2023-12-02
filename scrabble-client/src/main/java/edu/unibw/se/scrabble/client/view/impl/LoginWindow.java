package edu.unibw.se.scrabble.client.view.impl;

import edu.unibw.se.scrabble.common.base.ReturnValues;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginWindow extends Stage{
    private final FxView mainView;
    private final TextField nameField = new TextField();
    private final Label passwordLabel = new Label("Password");
    private final PasswordField passwordField = new PasswordField();
    private final Button loginButton = new Button("Login");
    private final Button registerButton = new Button("Register");
    private final GridPane grid = new GridPane();

    {
        setTitle("Scrabble");
        nameField.setText("username");
        grid.add(nameField, 1, 0, 2 ,1);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1, 2 ,1);
        grid.add(loginButton, 2, 2);
        grid.add(registerButton, 3,2);

        setScene(new Scene(grid));
    }

    public LoginWindow(FxView mainView) {
        this.mainView = mainView;

        setOnCloseRequest((event) -> {
            mainView.setWindowState(WindowState.EXIT);
        });
        registerButton.setOnAction((event) -> {
            mainView.setWindowState(WindowState.REGISTER);
            close();
        });

        loginButton.setOnAction((event) -> {
            ReturnValues.ReturnLoginUser result = ViewControl.clientConnect.loginUser(
                    nameField.getText(), passwordField.getText());

            if (result != ReturnValues.ReturnLoginUser.SUCCESSFUL) {
                String message = switch (result) {
                    case USERNAME_NOT_IN_DATABASE -> "Please register first";
                    case WRONG_PASSWORD -> "Wrong password!";
                    case INVALID_PASSWORD -> "To many users are logged in, please try later.";
                    case NETWORK_FAILURE, DATABASE_FAILURE, FAILURE -> "Server problem, please call the support.";
                    default -> "Unknown error, please call the support";
                };

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message from chat");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.initStyle(StageStyle.UTILITY);
                alert.showAndWait();

            } else {
                mainView.setWindowState(WindowState.MAIN);
                close();
            }
        });
    }


/*
    @Override
    public void show() {
        /*
        Stage stage = new Stage();

        // Set up the title label
        titleLabel.setFont(new Font("Arial", 24));
        GridPane.setHalignment(titleLabel, HPos.CENTER);

        // Set up the grid
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Add components to the grid
        grid.add(titleLabel, 0, 0, 2, 1);
        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(loginButton, 0, 3);
        grid.add(registerButton, 1, 3);

        // Set up event handlers
        loginButton.setOnAction(event -> handleLogin());
        registerButton.setOnAction(event -> handleRegister());

        // Set up the stage
        stage.setTitle("Login");
        stage.setScene(new Scene(grid));
        stage.show();
    }

    @Override
    public void hide() {
        // Optional: Implement cleanup or hide logic
    }

    @Override
    public void onCloseRequest() {
        // Optional: Implement logic to handle window close event
    }

    private void handleLogin() {
        // Perform login logic using ChatLogic
        ChatLogic.LoginUserReturn result = JavaFXViewControl.chatLogic.loginUser(
                nameField.getText(),
                Password.hashAndClearPassword(nameField.getText(), passwordField.getText().toCharArray())
        );

        handleLoginResult(result);
    }

    private void handleRegister() {
        // Switch to the Register state
        mainView.switchToWindowState(WindowState.REGISTER);
    }

    private void handleLoginResult(ReturnValues.ReturnLoginUser result) {
        if (result != ReturnValues.ReturnLoginUser.SUCCESSFUL) {
            String message = switch (result) {
                case NETWORK_FAILURE -> "N";
                case DATABASE_FAILURE -> "D";
                case USERNAME_NOT_IN_DATABASE -> "U";
                case WRONG_PASSWORD -> "W";
                case INVALID_USERNAME, INVALID_PASSWORD, FAILURE -> "I";
                default -> "NO";
            };

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.initStyle(StageStyle.UTILITY);
            alert.showAndWait();
        } else {
            // Switch to the Game state
            mainView.switchToWindowState(WindowState.MAIN);
        }
    }*/
}
