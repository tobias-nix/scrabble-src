package edu.unibw.se.scrabble.client.view.impl;

public class GameWindow {
    private final FxView mainView;

    GameWindow(FxView mainView) {
        this.mainView = mainView;
    }

    //private GridPane createScrabbleBoard() {
    //    GridPane board = new GridPane();
    //    board.setAlignment(Pos.CENTER);
//
    //    // Erstelle das 15x15 Scrabble-Brett
    //    for (int row = 0; row < 15; row++) {
    //        for (int col = 0; col < 15; col++) {
    //            Rectangle cell = new Rectangle(40, 40, Color.BEIGE);
    //            Text letter = new Text("");
    //            letter.setFont(new Font(18));
//
    //            // Füge die Zelle (Rectangle und Text) zum GridPane hinzu
    //            board.add(cell, col, row);
    //            board.add(letter, col, row);
//
    //            // Füge dem Zellen-Array (optional) Maße hinzu
    //            cell.setOnMouseClicked(event -> handleCellClick(cell, letter));
    //        }
    //    }
//
    //    return board;
    //}

}
