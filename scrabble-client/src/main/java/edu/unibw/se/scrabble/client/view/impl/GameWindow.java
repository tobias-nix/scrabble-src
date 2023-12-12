package edu.unibw.se.scrabble.client.view.impl;

import edu.unibw.se.scrabble.common.base.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.Optional;

/**
 * @author Nix
 */

public class GameWindow extends Stage {
    private final Label heading = new Label("Scrabble");
    private final Label gameId = new Label("GameID: ");
    private final Label bagSize = new Label();
    private final Button pass = new Button("PASS");
    private final Button swap = new Button("SWAP");
    private final Button place = new Button("PLACE");
    private final Button endTurn = new Button("END TURN");
    private final GridPane boardGrid = new GridPane();
    private final VBox vBox1 = new VBox();
    private final VBox vBox2 = new VBox();
    private final VBox vBox3 = new VBox();
    private final VBox vBox4 = new VBox();
    private final HBox hBox1 = new HBox();
    private final HBox hBox2 = new HBox();
    private final HBox hBox3 = new HBox();
    private final HBox hBox4 = new HBox();
    private final HBox hBox5 = new HBox();
    private final HBox hBox6 = new HBox();
    private final HBox hBox7 = new HBox();
    private final HBox hBox8 = new HBox();
    private final HBox hBox9 = new HBox();

    {
        setTitle("Scrabble");

        ImageView board = new ImageView("board.png");

        board.setFitWidth(600);
        board.setFitHeight(600);

        bagSize.setAlignment(Pos.CENTER);
        endTurn.setAlignment(Pos.CENTER);

        vBox1.setAlignment(Pos.CENTER);
        vBox1.setSpacing(50);


        vBox1.getChildren().addAll(hBox5, hBox4, hBox3, hBox2, vBox4);

        vBox2.setAlignment(Pos.CENTER);
        vBox2.setSpacing(5);

        vBox3.setAlignment(Pos.CENTER);
        vBox3.setSpacing(5);

        vBox4.setAlignment(Pos.CENTER);
        vBox4.setSpacing(10);

        hBox1.setSpacing(30);

        hBox2.setSpacing(10);
        hBox3.setSpacing(10);
        hBox4.setSpacing(10);
        hBox5.setSpacing(10);
        hBox6.setSpacing(10);
        hBox7.setSpacing(2);
        hBox7.setAlignment(Pos.CENTER);
        hBox6.setAlignment(Pos.CENTER);
        hBox6.getChildren().addAll(pass, swap, place);
        hBox8.setAlignment(Pos.CENTER);
        hBox8.setSpacing(2);

        heading.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 80));


        hBox9.getChildren().addAll(bagSize, hBox8);
        hBox1.getChildren().addAll(vBox1, board);
        vBox2.getChildren().addAll(heading, hBox1, hBox9);

        AnchorPane root = new AnchorPane();
        root.getChildren().addAll(vBox2, gameId, board, boardGrid);
        AnchorPane.setRightAnchor(board, 50.0);
        AnchorPane.setTopAnchor(board, 60.0);
        AnchorPane.setTopAnchor(gameId, 10.0);
        AnchorPane.setLeftAnchor(gameId, 10.0);
        AnchorPane.setRightAnchor(boardGrid, 75.0);
        AnchorPane.setTopAnchor(boardGrid, 85.0);
        setScene(new Scene(root));
    }

    public GameWindow(FxView mainView) {
        gameId.setText("GameID: " + mainView.getGameData().gameID + "/" + mainView.getUsername());
        bagSize.setText("Bag: " + mainView.getGameData().bagSize);


        List<String> usernames = mainView.getGameData().usernames;
        List<Integer> scores = mainView.getGameData().score;
        List<Integer> rackTilesCounts = mainView.getGameData().countRackTiles;

        Label playerA = new Label();
        Label pointsA = new Label();
        Label letterA = new Label();
        playerA.setText(usernames.getFirst());
        pointsA.setText("Points: " + scores.getFirst());
        letterA.setText("Letters: " + rackTilesCounts.getFirst());
        playerA.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 30));
        pointsA.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 30));
        letterA.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 30));
        hBox2.getChildren().addAll(playerA, pointsA, letterA);

        if (usernames.size() >= 2) {
            Label playerB = new Label();
            Label pointsB = new Label();
            Label letterB = new Label();
            playerB.setText(usernames.get(1));
            pointsB.setText("Points: " + scores.get(1));
            letterB.setText("Letters: " + rackTilesCounts.get(1));
            playerB.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 30));
            pointsB.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 30));
            letterB.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 30));
            hBox3.getChildren().addAll(playerB, pointsB, letterB);
        }
        if (usernames.size() >= 3) {
            Label playerC = new Label();
            Label pointsC = new Label();
            Label letterC = new Label();
            playerC.setText(usernames.get(2));
            pointsC.setText("Points: " + scores.get(2));
            letterC.setText("Letters: " + rackTilesCounts.get(2));
            playerC.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 30));
            pointsC.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 30));
            letterC.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 30));
            hBox4.getChildren().addAll(playerC, pointsC, letterC);
        }
        if (usernames.size() >= 4) {
            Label playerD = new Label();
            Label pointsD = new Label();
            Label letterD = new Label();
            playerD.setText(usernames.get(3));
            pointsD.setText("Points: " + scores.get(3));
            letterD.setText("Letters: " + rackTilesCounts.get(3));
            playerD.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 30));
            pointsD.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 30));
            letterD.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 30));
            hBox5.getChildren().addAll(playerD, pointsD, letterD);
        }

        borderPlayer(usernames, mainView.getGameData().currentPlayer);

        char[] rackTiles = mainView.getRackTiles();

        int length = rackTiles.length;

        for (int i = 0; i < length; i++) {
            char letter = rackTiles[i];
            String imgPath;
            if (letter == '_') {
                imgPath = letter + ".jpg";
            } else {
                imgPath = Character.toUpperCase(letter) + ".jpg";
            }
            ImageView imageView = new ImageView(imgPath);
            imageView.setFitHeight(35);
            imageView.setFitWidth(35);

            imageView.setOnDragDetected(event -> {
                Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(String.valueOf(letter));
                db.setContent(content);
                db.setDragView(imageView.snapshot(null, null));
                event.consume();
            });

            hBox7.getChildren().add(imageView);
        }

        vBox4.getChildren().addAll(hBox7, hBox6, endTurn);

        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 15; j++) {
                Rectangle boardRectangle = new Rectangle(36, 36);
                boardRectangle.setFill(Color.TRANSPARENT);
                boardRectangle.setStroke(Color.BLACK);
                boardRectangle.setStrokeWidth(0.01);


                    boardRectangle.setOnDragOver(e -> {
                        if (e.getDragboard().hasString()) {
                            e.acceptTransferModes(TransferMode.MOVE);
                        }
                        e.consume();
                    });

                    boardRectangle.setOnDragDropped(e -> {
                        if (e.getDragboard().hasString()) {
                            String letter = e.getDragboard().getString();
                            if (mainView.getGameData().state == GameState.PLACE) {
                                int rowIndex = GridPane.getRowIndex(boardRectangle);
                                int colIndex = GridPane.getColumnIndex(boardRectangle)+1;
                                if (isValidMove(letter, rowIndex, colIndex)) {
                                    ImageView imageView = createTileImageView(letter);
                                    boardGrid.add(imageView, colIndex, rowIndex);
                                }
                            }
                        }
                        e.consume();
                    });
                    boardGrid.add(boardRectangle, i, j);
            }
        }

        for (TileWithPosition tile : mainView.getGameData().tilesWithPositions) {
            String imgPath;
            if (tile.letter() == '_') {
                imgPath = tile.letter() + ".jpg";
            } else {
                imgPath = Character.toUpperCase(tile.letter()) + ".jpg";
            }

            Image image = new Image(imgPath);
            Rectangle rect = (Rectangle) getNodeByRowColumnIndex(tile.row() , tile.column()-1, boardGrid);

            if (rect != null) {
                // Set the image directly on the Rectangle
                rect.setFill(new ImagePattern(image));
            }
        }

        char[] swapTiles = mainView.getSwapTiles();
        int[] currentIndex = {0};

        for (int i = 0; i < 7; i++) {
            Rectangle swapRectangle = new Rectangle(40, 40);
            swapRectangle.setFill(Color.TRANSPARENT);
            swapRectangle.setStroke(Color.BLACK);
            swapRectangle.setStrokeWidth(2.0);


                swapRectangle.setOnDragOver(e -> {
                    if (e.getDragboard().hasString()) {
                        e.acceptTransferModes(TransferMode.MOVE);
                    }
                    e.consume();
                });

                swapRectangle.setOnDragDropped(e -> {
                    if (e.getDragboard().hasString()) {
                        String letter = e.getDragboard().getString();
                        if (isValidPlacementForSwapAction(letter) && mainView.getGameData().state == GameState.SWAP) {
                            ImageView imageView = createTileImageView(letter);
                            StackPane stackPane = new StackPane(imageView);

                            hBox8.getChildren().set(currentIndex[0], stackPane);
                            currentIndex[0]++;
                        }
                    }
                    e.consume();
                });

            hBox8.getChildren().add(swapRectangle);


            ImageView imageView = null;
            if (i < swapTiles.length) {
                char tile = swapTiles[i];
                String imgPath;

                if (tile == '_') {
                    imgPath = tile + ".jpg";
                } else {
                    imgPath = Character.toUpperCase(tile) + ".jpg";
                }

                imageView = new ImageView(imgPath);
                imageView.setFitHeight(36);
                imageView.setFitWidth(36);
                currentIndex[0]++;
            }

            StackPane stack = new StackPane();
            stack.getChildren().add(swapRectangle);

            if (imageView != null) {
                stack.getChildren().add(imageView);
            }

            hBox8.getChildren().add(stack);
        }

        if (mainView.getGameData().state == GameState.VOTE) {
            Platform.runLater(() -> {
            String[] voteWords = mainView.getPlacedWords();
            StringBuilder content = new StringBuilder("Words Voted: ");
            for (String word : voteWords) {
                content.append(word).append(", ");
            }
            content = new StringBuilder(content.substring(0, content.length() - 2));

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Vote");
                alert.setHeaderText(null);
                alert.setContentText(content.toString());
                alert.initStyle(StageStyle.UTILITY);

                ButtonType blameButton = new ButtonType("BLAME");
                ButtonType okButton = new ButtonType("OKAY");

                alert.getButtonTypes().setAll(blameButton, okButton);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == blameButton) {
                    ViewControl.clientConnect.sendPlayerVote(PlayerVote.REJECTED);
                } else if (result.isPresent() && result.get() == okButton) {
                    ViewControl.clientConnect.sendPlayerVote(PlayerVote.CONFIRMED);
                }
            });
        }

        pass.setOnAction((event) -> {
            ReturnValues.ReturnSelectAction result = ViewControl.clientConnect.selectAction(ActionState.PASS);
            if (result != ReturnValues.ReturnSelectAction.SUCCESSFUL) {
                showAlert(getSelectErrorMessageFromServer(result));
            }
        });

        swap.setOnAction((event) -> {
            ReturnValues.ReturnSelectAction result = ViewControl.clientConnect.selectAction(ActionState.SWAP);
            if (result != ReturnValues.ReturnSelectAction.SUCCESSFUL) {
                showAlert(getSelectErrorMessageFromServer(result));
            }
        });

        place.setOnAction((event) -> {
            ReturnValues.ReturnSelectAction result = ViewControl.clientConnect.selectAction(ActionState.PLACE);
            if (result != ReturnValues.ReturnSelectAction.SUCCESSFUL) {
                showAlert(getSelectErrorMessageFromServer(result));
            }
        });

        endTurn.setOnAction((event) -> {
            ReturnValues.ReturnEndTurn result = ViewControl.clientConnect.endTurn();
            if (result != ReturnValues.ReturnEndTurn.SUCCESSFUL) {
                showAlert(getEndTurnErrorMessageFromServer(result));
            } else {
                ViewControl.clientConnect.sendPlayerVote(PlayerVote.REJECTED);
                /*Platform.runLater(() -> {
                    String[] voteWords = mainView.getPlacedWords();
                    StringBuilder content = new StringBuilder("Words Voted: ");
                    for (String word : voteWords) {
                        content.append(word).append(", ");
                    }
                    content = new StringBuilder(content.substring(0, content.length() - 2));

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Vote");
                    alert.setHeaderText(null);
                    alert.setContentText(content.toString());
                    alert.initStyle(StageStyle.UTILITY);

                    ButtonType blameButton = new ButtonType("BLAME");
                    ButtonType okButton = new ButtonType("OKAY");

                    alert.getButtonTypes().setAll(blameButton, okButton);

                    Optional<ButtonType> answer = alert.showAndWait();

                    if (answer.isPresent() && answer.get() == blameButton) {
                        ViewControl.clientConnect.sendPlayerVote(PlayerVote.REJECTED);
                    } else if (answer.isPresent() && answer.get() == okButton) {
                        ViewControl.clientConnect.sendPlayerVote(PlayerVote.CONFIRMED);
                    }
                });*/
            }
        });
    }

    public static Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    public void borderPlayer(List<String> usernames, String currentPlayerName) {
        String normalBorderStyle = "-fx-border-color: black; -fx-padding: 10px; -fx-border-width: 2px";
        String currentBorderStyle = "-fx-border-color: red; -fx-padding: 10px; -fx-border-width: 2px";

        // Remove previously set border style
        hBox2.setStyle("");
        hBox3.setStyle("");
        hBox4.setStyle("");
        hBox5.setStyle("");

        // Set border styles for actual players
        if (!usernames.isEmpty()) {
            hBox2.setStyle(normalBorderStyle);
            if (usernames.getFirst().equals(currentPlayerName)) {
                hBox2.setStyle(currentBorderStyle);
            }
        }
        if (usernames.size() > 1) {
            hBox3.setStyle(normalBorderStyle);
            if (usernames.get(1).equals(currentPlayerName)) {
                hBox3.setStyle(currentBorderStyle);
            }
        }
        if (usernames.size() > 2) {
            hBox4.setStyle(normalBorderStyle);
            if (usernames.get(2).equals(currentPlayerName)) {
                hBox4.setStyle(currentBorderStyle);
            }
        }
        if (usernames.size() > 3) {
            hBox5.setStyle(normalBorderStyle);
            if (usernames.get(3).equals(currentPlayerName)) {
                hBox5.setStyle(currentBorderStyle);
            }
        }
    }

    private ImageView createTileImageView(String letter) {
        char tile = letter.charAt(0);
        String imgPath;
        if (tile == '_') {
            imgPath = tile + ".jpg";
        } else {
            imgPath = Character.toUpperCase(tile) + ".jpg";
        }

        ImageView imageView = new ImageView(imgPath);
        imageView.setFitHeight(36);
        imageView.setFitWidth(36);

        return imageView;
    }

    private boolean isValidMove(String letter, int row, int column) {
        ReturnValues.ReturnPlaceTile result = ViewControl.clientConnect.placeTile(new TileWithPosition(letter.charAt(0), row, column));
        if (result != ReturnValues.ReturnPlaceTile.SUCCESSFUL) {
            showAlert(getPlaceErrorMessageFromServer(result));
        } else {
            return true;
        }
        return false;
    }

    private boolean isValidPlacementForSwapAction(String letter) {
        ReturnValues.ReturnSwapTile result = ViewControl.clientConnect.swapTile(letter.charAt(0));
        if (result != ReturnValues.ReturnSwapTile.SUCCESSFUL) {
            showAlert(getSwapErrorMessageFromServer(result));
        } else {
            return true;
        }
        return false;
    }

    private String getSelectErrorMessageFromServer(ReturnValues.ReturnSelectAction result) {
        return switch (result) {
            case NETWORK_FAILURE -> "NETWORK_FAILURE";
            case LESS_THAN_SEVEN_TILES_IN_BAG -> "LESS_THAN_SEVEN_TILES_IN_BAG";
            case FAILURE -> "FAILURE: Server problem, please call support.";
            default -> "DEFAULT: Unknown error, please call support";
        };
    }

    private String getSwapErrorMessageFromServer(ReturnValues.ReturnSwapTile result) {
        return switch (result) {
            case NETWORK_FAILURE -> "NETWORK_FAILURE";
            case TILE_NOT_ON_RACK -> "TILE_NOT_ON_RACK";
            case FAILURE -> "FAILURE: Server problem, please call support.";
            default -> "DEFAULT: Unknown error, please call support";
        };
    }

    private String getPlaceErrorMessageFromServer(ReturnValues.ReturnPlaceTile result) {
        return switch (result) {
            case NETWORK_FAILURE -> "NETWORK_FAILURE";
            case TILE_NOT_ON_RACK -> "TILE_NOT_ON_RACK";
            case SQUARE_OCCUPIED -> "SQUARE_OCCUPIED";
            case POSITION_NOT_ALLOWED -> "POSITION_NOT_ALLOWED";
            case FAILURE -> "FAILURE: Server problem, please call support.";
            default -> "DEFAULT: Unknown error, please call support";
        };
    }

    private String getEndTurnErrorMessageFromServer(ReturnValues.ReturnEndTurn result) {
        return switch (result) {
            case NETWORK_FAILURE -> "NETWORK_FAILURE";
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
