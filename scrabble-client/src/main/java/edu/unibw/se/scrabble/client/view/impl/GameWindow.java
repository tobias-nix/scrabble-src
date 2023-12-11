package edu.unibw.se.scrabble.client.view.impl;

import edu.unibw.se.scrabble.common.base.ReturnValues;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 @author Nix
 */

public class GameWindow extends Stage {
    private final Label heading = new Label("Scrabble");
    private final Button startButton = new Button("Start Game");
    private final Label gameId = new Label("GameID: ");
    private final VBox vBox1 = new VBox();
    private final VBox vBox2 = new VBox();
    private final VBox vBox3 = new VBox();
    private final HBox hBox1 = new HBox();
    private final HBox hBox2 = new HBox();
    private final HBox hBox3 = new HBox();
    private final HBox hBox4 = new HBox();
    private final HBox hBox5 = new HBox();

    {
        setTitle("Scrabble");

        ImageView board = new ImageView("board.png");

        board.setFitWidth(600);
        board.setFitHeight(600);

        vBox1.setAlignment(Pos.CENTER);
        vBox1.setSpacing(50);
        Label playerA = new Label();
        Label playerB = new Label();
        Label playerC = new Label();
        Label playerD = new Label();

        vBox1.getChildren().addAll(hBox2, hBox3, hBox4, hBox5);

        vBox2.setAlignment(Pos.CENTER);
        vBox2.setSpacing(5);

        vBox3.setAlignment(Pos.CENTER);
        vBox3.setSpacing(5);

        hBox1.setAlignment(Pos.CENTER);
        hBox1.setSpacing(30);

        hBox2.setSpacing(10);
        hBox3.setSpacing(10);
        hBox4.setSpacing(10);
        hBox5.setSpacing(10);

        heading.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 80));

        playerA.setFont(Font.font("Calibri", 40));
        playerB.setFont(Font.font("Calibri", 40));
        playerC.setFont(Font.font("Calibri", 40));
        playerD.setFont(Font.font("Calibri", 40));

        hBox1.getChildren().addAll(vBox1, board);
        vBox2.getChildren().addAll(heading, hBox1);

        StackPane root = new StackPane();
        root.getChildren().addAll(vBox2, gameId);
        StackPane.setAlignment(gameId, Pos.TOP_LEFT);

        setScene(new Scene(root));
    }

    public GameWindow(FxView mainView) {
        gameId.setText("GameID: " + mainView.getGameId());

        List<String> usernames = mainView.getGameData().usernames;
        List<Integer> scores = mainView.getGameData().score;
        List<Integer> rackTilesCounts = mainView.getGameData().countRackTiles;

        hBox2.getChildren().addAll(new Label(usernames.get(0)), new Label("Points: " + scores.get(0)), new Label("Letters: " + rackTilesCounts.get(0)));

        if (usernames.size() >= 2) {
            hBox3.getChildren().addAll(new Label(usernames.get(1)), new Label("Points: " + scores.get(1)), new Label("Letters: " + rackTilesCounts.get(1)));
        }
        if (usernames.size() >= 3) {
            hBox4.getChildren().addAll(new Label(usernames.get(2)), new Label("Points: " + scores.get(2)), new Label("Letters: " + rackTilesCounts.get(2)));
        }
        if (usernames.size() >= 4) {
            hBox5.getChildren().addAll(new Label(usernames.get(3)), new Label("Points: " + scores.get(3)), new Label("Letters: " + rackTilesCounts.get(3)));
        }

        highlightCurrentPlayer(usernames, mainView.getGameData().currentPlayer);
    }

    public void highlightCurrentPlayer(List<String> usernames, String currentPlayerName) {
        // Clear styles
        hBox2.setStyle("");
        hBox3.setStyle("");
        hBox4.setStyle("");
        hBox5.setStyle("");

        String borderStyle = "-fx-border-color: red; -fx-padding: 10px; -fx-border-width: 2px";

        // Highlight current player
        if (usernames.getFirst().equals(currentPlayerName)) {
            hBox2.setStyle(borderStyle);
        }else if (usernames.get(1).equals(currentPlayerName)) {
            hBox3.setStyle(borderStyle);
        }else if (usernames.get(2).equals(currentPlayerName)) {
            hBox4.setStyle(borderStyle);
        }else if (usernames.get(3).equals(currentPlayerName)) {
            hBox5.setStyle(borderStyle);
        }
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
