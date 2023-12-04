package edu.unibw.se.scrabble.server.logic.impl;


import edu.unibw.se.scrabble.common.base.SquareState;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ScrabbleBoard {
    final private ScrabbleSquare[][] gameBoard = createScrabbleGameBoard();

    public ScrabbleBoard() {
        //gameBoard[1][0].placeScrabbleTile(new ScrabbleTile('A', 2));

    }

    List<ScrabbleTile> returnMoveTiles() {
        List<ScrabbleTile> moveTiles = new ArrayList<>();
        for (ScrabbleSquare[] scrabbleSquares : gameBoard) {
            for (ScrabbleSquare scrabbleSquare : scrabbleSquares) {
                if (scrabbleSquare.getSquareState().equals(SquareState.MOVE)) {
                    moveTiles.add(scrabbleSquare.removeScrabbleTile());
                    scrabbleSquare.setSquareStateToFree();
                }
            }
        }
        return moveTiles;
    }

    boolean isPositionAllowed(int column, int row) {
        if (gameBoard[column][row].getSquareState() != SquareState.FREE) {
            return false;
        }

        return getNeighbourState(column, row).contains(SquareState.MOVE) ||
                getNeighbourState(column, row).contains(SquareState.OCCUPIED);
    }

    void placeTile(int column, int row, ScrabbleTile scrabbleTile) {
        this.gameBoard[column][row].placeScrabbleTile(scrabbleTile);
        this.gameBoard[column][row].setSquareStateToMove();
    }

    private List<SquareState> getNeighbourState(int column, int row) {
        ArrayList<SquareState> neighbourStates = new ArrayList<>();

        int[][] neighbours = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] neighbour : neighbours) {
            int neighbourColumn = column + neighbour[0];
            int neighbourRow = row + neighbour[1];

            if (neighbourPositionIsValid(neighbourColumn, neighbourRow)) {
                neighbourStates.add(gameBoard[neighbourColumn][neighbourRow].getSquareState());
            }
        }

        return neighbourStates;
    }

    private boolean neighbourPositionIsValid(int column, int row) {
        return column >= 0 && column < 15 && row >= 0 && row < 15;
    }


    static ScrabbleSquare[][] createScrabbleGameBoard() {
        ScrabbleSquare[][] board = new ScrabbleSquare[15][15];

        String filePath = Objects.requireNonNull(ScrabbleBoard.class.getResource("/boardValues.csv")).getFile();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            bufferedReader.readLine(); //Erste Format LINE
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineAsArray = line.split(",");
                Arrays.stream(lineAsArray).forEach(scrabbleSquare -> {
                    int[] scrabbleSquareAsArray =
                            Arrays.stream(scrabbleSquare.split("#")).mapToInt(Integer::parseInt).toArray();
                    board[scrabbleSquareAsArray[0] - 1][scrabbleSquareAsArray[1] - 1] =
                            new ScrabbleSquare(scrabbleSquareAsArray[2], scrabbleSquareAsArray[3]);
                });
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return board;
    }

    public void printGameBoardInThePrettiestWayPossiblePlease() {
        System.out.println(Arrays.deepToString(gameBoard).replace("], ", "]\n"));
    }







    /*ScrabbleBoard(String gameString) {
        String[] gameData = gameString.split(",");
        gameID = Integer.parseInt(gameData[0]);
        //language
        //gameState
        //bag
        //fixedTiles
        // movedTiles
        // players
        // playersScore
        // rackTiles
        // swapTiles
        //TODO: CSV aufsplitten
    }*/
}