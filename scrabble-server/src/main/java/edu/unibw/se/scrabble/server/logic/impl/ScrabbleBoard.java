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
    final ScrabbleSquare[][] gameBoard = createScrabbleGameBoard();

    public ScrabbleBoard() {
        //gameBoard[1][0].placeScrabbleTile(new ScrabbleTile('A', 2));

    }

    List<Word> getPlacedWords() {
        ArrayList<Word> placedWords = new ArrayList<>();
        WordDirection wordDirection = null;

        ArrayList<int[]> moveTilePositions = new ArrayList<>();

        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                if (this.gameBoard[col][row].getSquareState().equals(SquareState.MOVE)) {
                    moveTilePositions.add(new int[]{col, row});
                }
            }
        }
        wordDirection = this.getWordDirection(moveTilePositions);

        int[] positionOfFirstLetter = this.searchForFirstLetterOfWord(moveTilePositions.get(0), wordDirection);
        Word placedWord = constructPlacedWord(positionOfFirstLetter, wordDirection);
        placedWords.add(placedWord);

        wordDirection = wordDirection.equals(WordDirection.DOWN) ? WordDirection.RIGHT : WordDirection.DOWN;

        WordDirection finalWordDirection = wordDirection;
        moveTilePositions.forEach(position -> {
            int[] positionOfFirstLetterForOtherTiles = this.searchForFirstLetterOfWord(moveTilePositions.get(0), finalWordDirection);
            Word placedWordForOtherTiles = constructPlacedWord(positionOfFirstLetterForOtherTiles, finalWordDirection);
            placedWords.add(placedWordForOtherTiles);
        });
        return placedWords;
    }

    private WordDirection getWordDirection(List<int[]> moveTilePositions) {
        if (moveTilePositions.size() == 1) {
            int firstMoveTileCol = moveTilePositions.get(0)[0];
            int firstMoveTileRow = moveTilePositions.get(0)[1];

            if (hasOccupiedSquare(firstMoveTileCol, firstMoveTileRow - 1) || hasOccupiedSquare(firstMoveTileCol, firstMoveTileRow + 1)) {
                return WordDirection.DOWN;
            } else if (hasOccupiedSquare(firstMoveTileCol - 1, firstMoveTileRow) || hasOccupiedSquare(firstMoveTileCol + 1, firstMoveTileRow)) {
                return WordDirection.RIGHT;
            }
        } else if (moveTilePositions.get(0)[0] == moveTilePositions.get(1)[0]) {
            return WordDirection.DOWN;
        }

        return WordDirection.RIGHT;
    }

    private boolean hasOccupiedSquare(int col, int row) {
        return col >= 0 && col < 15 && row >= 0 && row < 15 &&
                this.gameBoard[col][row].getSquareState().equals(SquareState.OCCUPIED);
    }


    private enum WordDirection {
        DOWN, RIGHT
    }

    private int[] searchForFirstLetterOfWord(int[] positionOfFirstTile, WordDirection wordDirection) {
        if (wordDirection.equals(WordDirection.DOWN)) {
            while (positionOfFirstTile[1] != 0 &&
                    !this.gameBoard[positionOfFirstTile[0]][positionOfFirstTile[1] - 1].getSquareState().equals(SquareState.FREE)) {
                positionOfFirstTile[1] = positionOfFirstTile[1] - 1;
            }
        } else {
            while (positionOfFirstTile[0] != 0 &&
                    !this.gameBoard[positionOfFirstTile[1] - 1][positionOfFirstTile[1]].getSquareState().equals(SquareState.FREE)) {
                positionOfFirstTile[0] = positionOfFirstTile[0] - 1;
            }
        }
        return positionOfFirstTile;
    }

    private Word constructPlacedWord(int[] positionOfFirstTile, WordDirection wordDirection) {
        StringBuilder placedWord = new StringBuilder();
        int wordFactor = 1;
        int wordScore = 0;
        if (wordDirection.equals(WordDirection.DOWN)) {
            while (positionOfFirstTile[1] != 14 &&
                    !this.gameBoard[positionOfFirstTile[0]][positionOfFirstTile[1] + 1].getSquareState().equals(SquareState.FREE)) {
                positionOfFirstTile[1] = positionOfFirstTile[1] + 1;
                ScrabbleSquare scrabbleSquare = this.gameBoard[positionOfFirstTile[0]][positionOfFirstTile[1]];
                placedWord.append(scrabbleSquare.scrabbleTile.letter);
                wordScore += scrabbleSquare.scrabbleTile.value * scrabbleSquare.letterFactor;
                wordFactor *= scrabbleSquare.wordFactor;
            }
        } else {
            while (positionOfFirstTile[0] != 14 &&
                    !this.gameBoard[positionOfFirstTile[0] + 1][positionOfFirstTile[1]].getSquareState().equals(SquareState.FREE)) {
                positionOfFirstTile[0] = positionOfFirstTile[0] + 1;
                ScrabbleSquare scrabbleSquare = this.gameBoard[positionOfFirstTile[0]][positionOfFirstTile[1]];
                placedWord.append(scrabbleSquare.scrabbleTile.letter);
                wordScore += scrabbleSquare.scrabbleTile.value * scrabbleSquare.letterFactor;
                wordFactor *= scrabbleSquare.wordFactor;
            }
        }
        return new Word(placedWord.toString(), wordScore * wordFactor);
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

    boolean isSquareFree(int column, int row) {
        return gameBoard[column][row].getSquareState() == SquareState.FREE;
    }

    boolean hasNeighbour(int column, int row) {
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
        System.out.println(Arrays.deepToString(gameBoard).replace("], ", "]\n").replace("null", "|_/_|"));
    }
}