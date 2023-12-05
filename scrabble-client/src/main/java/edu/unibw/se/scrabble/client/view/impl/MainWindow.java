package edu.unibw.se.scrabble.client.view.impl;

import edu.unibw.se.scrabble.common.base.LanguageSetting;
import edu.unibw.se.scrabble.common.base.ReturnValues;
import edu.unibw.se.scrabble.common.base.Statistics;
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

public class MainWindow extends Stage {
    private final Label heading = new Label("Hello");
    private final Label pointsLabel = new Label("Points:");
    private final Label winLosLabel = new Label("Win/Los Ratio:");
    private final TextField gameIdField = new TextField();
    private final Label lastPointsLabel = new Label("Last Points:");
    private final Button joinButton = new Button("Join Game");
    private final Button createButton = new Button("Create Game");
    private final ToggleGroup group = new ToggleGroup();
    private final RadioButton radioButtonGerman = new RadioButton("German");
    private final RadioButton radioButtonEnglish = new RadioButton("English");
    private final VBox vBox1 = new VBox();
    private final VBox vBox2 = new VBox();
    private final VBox vBox3 = new VBox();
    private final HBox hBox1 = new HBox();
    private final HBox hBox2 = new HBox();
    private final HBox hBox3 = new HBox();

    {
        setTitle("Scrabble");

        hBox1.setAlignment(Pos.TOP_CENTER);
        hBox1.setSpacing(20);

        hBox2.setAlignment(Pos.CENTER);
        hBox2.setSpacing(10);

        hBox3.setAlignment(Pos.CENTER);
        hBox3.setSpacing(40);

        vBox1.setAlignment(Pos.CENTER);
        vBox1.setSpacing(30);

        vBox2.setAlignment(Pos.CENTER);
        vBox2.setSpacing(10);

        vBox3.setAlignment(Pos.CENTER);
        vBox3.setSpacing(10);

        gameIdField.setText("GameID");

        heading.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 80));

        radioButtonGerman.setToggleGroup(group);
        radioButtonGerman.setSelected(true);
        radioButtonEnglish.setToggleGroup(group);

        hBox1.getChildren().addAll(pointsLabel, lastPointsLabel, winLosLabel);
        vBox2.getChildren().addAll(gameIdField, joinButton);
        vBox3.getChildren().addAll(radioButtonGerman, radioButtonEnglish);
        hBox2.getChildren().addAll(createButton, vBox3);
        hBox3.getChildren().addAll(vBox2, hBox2);
        vBox1.getChildren().addAll(heading, hBox1, hBox3);

        setScene(new Scene(vBox1));
    }

    public MainWindow(FxView mainView) {

        heading.setText("Hello " + mainView.getUsername());

        String messageStatistics;
        ReturnValues.ReturnStatistics resultStatistics = ViewControl.clientConnect.getUserStatistics();

        if (resultStatistics.state() != ReturnValues.ReturnStatisticsState.SUCCESSFUL) {
            messageStatistics = getStatisticsErrorMessageFromServer(resultStatistics.state());
            showAlert(messageStatistics);
        } else {
            updateUserStatisticsLabels(resultStatistics.userStatistics());
        }

        setOnCloseRequest(event -> {
            close();
            Platform.exit();
            event.consume();
            System.exit(0);
        });

        createButton.setOnAction((event) -> {
            String messageCreate;
            RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
            ReturnValues.ReturnCreateSession result;

            if (radioButtonGerman.equals(selectedRadioButton)) {
                result = ViewControl.clientConnect.createSession(LanguageSetting.GERMAN);
            } else {
                result = ViewControl.clientConnect.createSession(LanguageSetting.ENGLISH);
            }
            if (result.state() != ReturnValues.ReturnCreateSessionState.SUCCESSFUL) {
                messageCreate = getCreateErrorMessageFromServer(result.state());
                showAlert(messageCreate);
            } else {
                mainView.setGameId(result.gameID());
                mainView.setWindowState(WindowState.WAIT);
                close();
            }
        });

        joinButton.setOnAction((event) -> {
            String gameIdText = gameIdField.getText();
            int gameId = Integer.parseInt(gameIdText);

            String messageJoin = validateId(gameIdText);

            if (messageJoin != null) {
                showAlert(messageJoin);
            } else {
                ReturnValues.ReturnJoinSession result = ViewControl.clientConnect.joinSession(gameId);
                if (result != ReturnValues.ReturnJoinSession.SUCCESSFUL) {
                    messageJoin = getJoinErrorMessageFromServer(result);
                    showAlert(messageJoin);
                } else {
                    mainView.setWindowState(WindowState.WAIT);
                    close();
                }
            }
        });
    }

    private void updateUserStatisticsLabels(Statistics userStatistics) {
        pointsLabel.setText("Total Score: " + userStatistics.totalScore());
        lastPointsLabel.setText("Highest Score: " + userStatistics.highestScore());

        int gamesPlayed = userStatistics.gamesPlayed();
        int gamesWon = userStatistics.gamesWon();
        double winLossRatio = (gamesPlayed > 0) ? ((double) gamesWon / (gamesPlayed - gamesWon)) : 0.0;

        String formattedWinLossRatio = String.format("%.2f", winLossRatio);
        winLosLabel.setText("Win/Loss Ratio: " + formattedWinLossRatio);
    }

    private String validateId(String gameId) {
        if (gameId.length() < 5) {
            return "GameID too short";
        } else if (gameId.length() > 5) {
            return "GameID too long";
        } else if (!gameId.matches("[0-9]+")) {
            return "Invalid characters in GameID";
        } else {
            return null;
        }
    }

    private String getStatisticsErrorMessageFromServer(ReturnValues.ReturnStatisticsState result) {
        return switch (result) {
            case NETWORK_FAILURE -> "NETWORK_FAILURE: Statistics Server problem, please call support.";
            case DATABASE_FAILURE -> "DATABASE_FAILURE";
            case FAILURE -> "FAILURE";
            default -> "DEFAULT: Unknown error, please call support";
        };
    }

    private String getCreateErrorMessageFromServer(ReturnValues.ReturnCreateSessionState result) {
        return switch (result) {
            case USER_ALREADY_IN_SESSION -> "USER_ALREADY_IN_SESSION: You have already joined this game.";
            case SESSION_LIMIT_REACHED -> "SESSION_LIMIT_REACHED: The maximum number of games has been reached.";
            case NETWORK_FAILURE, FAILURE -> "NETWORK_FAILURE, FAILURE: Server problem, please call support.";
            default -> "DEFAULT: Unknown error, please call support";
        };
    }

    private String getJoinErrorMessageFromServer(ReturnValues.ReturnJoinSession result) {
        return switch (result) {
            case USER_ALREADY_IN_SESSION -> "USER_ALREADY_IN_SESSION: Please register first";
            case GAME_ID_INVALID -> "GAME_ID_INVALID: This game doesn't exist";
            case TOO_MANY_USERS -> "TOO_MANY_USERS: This game is full";
            case NETWORK_FAILURE, FAILURE -> "NETWORK_FAILURE, FAILURE: Server problem, please call support.";
            default -> "DEFAULT: Unknown error, please call support";
        };
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Create session information");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }
}