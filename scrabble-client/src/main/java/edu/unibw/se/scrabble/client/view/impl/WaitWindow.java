package edu.unibw.se.scrabble.client.view.impl;

import edu.unibw.se.scrabble.common.base.ReturnValues;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Arrays;

public class WaitWindow extends Stage {
    private final Label heading = new Label("Scrabble");
    private final Label waitLabel = new Label("Waiting for Players ...");
    private final Button startButton = new Button("Start Game");
    private final Label gameId = new Label();
    private final VBox vBox1 = new VBox();
    private final VBox vBox2 = new VBox();
    private final VBox vBox3 = new VBox();

    {
        setTitle("Scrabble");

        vBox1.setAlignment(Pos.CENTER);
        vBox1.setSpacing(30);

        vBox2.setAlignment(Pos.CENTER);
        vBox2.setSpacing(30);

        vBox3.setAlignment(Pos.CENTER);
        vBox3.setSpacing(5);

        heading.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 80));
        waitLabel.setFont(Font.font("Arial", 20));

        vBox3.getChildren().addAll(heading, gameId);
        vBox1.getChildren().addAll(vBox3, waitLabel, vBox2, startButton);

        setScene(new Scene(vBox1));
    }

    public WaitWindow(FxView mainView) {

        gameId.setText("GameID: " + mainView.getGameId());



        //TODO: Callback, usersInSession
        Label playerA = new Label();
        playerA.setText(Arrays.toString(ViewControl.usernames));

        setOnCloseRequest(event -> {
            close();
            Platform.exit();
            event.consume();
            System.exit(0);
        });

        startButton.setOnAction((event) -> {
            String message;
            ReturnValues.ReturnStartGame result = ViewControl.clientConnect.startGame();
            if (result != ReturnValues.ReturnStartGame.SUCCESSFUL) {
                message = getErrorMessageFromServer(result);
                showAlert(message);
            } else {
                mainView.setWindowState(WindowState.GAME);
                close();
            }
        });
    }

    private String getErrorMessageFromServer(ReturnValues.ReturnStartGame result) {
        return switch (result) {
            case NETWORK_FAILURE -> "NETWORK_FAILURE";
            case USER_NOT_THE_HOST -> "USER_NOT_THE_HOST";
            case USER_ALONE_IN_SESSION -> "USER_ALONE_IN_SESSION";
            case FAILURE -> "FAILURE: Server problem, please call support.";
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
