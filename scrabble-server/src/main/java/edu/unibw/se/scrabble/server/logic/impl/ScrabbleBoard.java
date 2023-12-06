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

    ArrayList<Word> getPlacedWords() {
        ArrayList<Word> placedWords = new ArrayList<>();
        WordDirection wordDirection;

        ArrayList<int[]> moveTilePositions = new ArrayList<>();

        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                if (this.gameBoard[row][col].getSquareState().equals(SquareState.MOVE)) {
                    moveTilePositions.add(new int[]{row, col});
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
            if (placedWordForOtherTiles.word().length() > 2) {
                placedWords.add(placedWordForOtherTiles);
            }
        });
        return placedWords;
    }

    private WordDirection getWordDirection(List<int[]> moveTilePositions) {
        if (moveTilePositions.size() == 1) {
            int firstMoveTileRow = moveTilePositions.get(0)[0];
            int firstMoveTileCol = moveTilePositions.get(0)[1];

            if (hasOccupiedSquare(firstMoveTileRow - 1, firstMoveTileCol) || hasOccupiedSquare(firstMoveTileRow + 1, firstMoveTileCol)) {
                return WordDirection.DOWN;
            } else if (hasOccupiedSquare(firstMoveTileRow, firstMoveTileCol - 1) || hasOccupiedSquare(firstMoveTileRow, firstMoveTileCol + 1)) {
                return WordDirection.RIGHT;
            }
        } else if (moveTilePositions.get(0)[0] == moveTilePositions.get(1)[0]) {
            // wenn die reihe vom ersten und zweiten Move teil in der liste gleich sind, dann liegt das wort waagrecht
            return WordDirection.RIGHT;
        }
        return WordDirection.DOWN;
    }

    private boolean hasOccupiedSquare(int row, int col) {
        return col >= 0 && col < 15 && row >= 0 && row < 15 &&
                this.gameBoard[row][col].getSquareState().equals(SquareState.OCCUPIED);
    }


    private enum WordDirection {
        DOWN, RIGHT
    }

    private int[] searchForFirstLetterOfWord(int[] positionOfFirstTile, WordDirection wordDirection) {
        int positionOfFirstTileRow = positionOfFirstTile[0];
        int positionOfFirstTileCol = positionOfFirstTile[1];

        if (wordDirection.equals(WordDirection.DOWN)) {
            while (positionOfFirstTileCol != 0 &&
                    !this.gameBoard[positionOfFirstTileRow][positionOfFirstTileCol - 1].getSquareState().equals(SquareState.FREE)) {
                positionOfFirstTileCol -= 1;
            }
        } else {
            while (positionOfFirstTileRow != 0 &&
                    !this.gameBoard[positionOfFirstTileRow - 1][positionOfFirstTileCol].getSquareState().equals(SquareState.FREE)) {
                positionOfFirstTileRow -= 1;
            }
        }
        return new int[]{positionOfFirstTileRow, positionOfFirstTileCol};
    }

    private Word constructPlacedWord(int[] positionOfFirstTile, WordDirection wordDirection) {
        StringBuilder placedWord = new StringBuilder();
        int wordFactor = 1;
        int wordScore = 0;

        int tileRow = positionOfFirstTile[0];
        int tileCol = positionOfFirstTile[1];

        if (wordDirection.equals(WordDirection.DOWN)) {
            while (tileRow != 15 &&
                    !this.gameBoard[tileRow][tileCol].getSquareState().equals(SquareState.FREE)) {
                ScrabbleSquare scrabbleSquare = this.gameBoard[tileRow][tileCol];
                placedWord.append(scrabbleSquare.scrabbleTile.letter);

                if(scrabbleSquare.squareState.equals(SquareState.MOVE)) {
                    wordScore += scrabbleSquare.scrabbleTile.value * scrabbleSquare.letterFactor;
                    wordFactor *= scrabbleSquare.wordFactor;
                } else {
                    wordScore = scrabbleSquare.scrabbleTile.value;
                }
                tileRow += 1;
            }
        } else {
            while (tileCol != 15 &&
                    !this.gameBoard[tileRow][tileCol].getSquareState().equals(SquareState.FREE)) {
                ScrabbleSquare scrabbleSquare = this.gameBoard[tileRow][tileCol];
                placedWord.append(scrabbleSquare.scrabbleTile.letter);

                if(scrabbleSquare.squareState.equals(SquareState.MOVE)) {
                    wordScore += scrabbleSquare.scrabbleTile.value * scrabbleSquare.letterFactor;
                    wordFactor *= scrabbleSquare.wordFactor;
                } else {
                    wordScore = scrabbleSquare.scrabbleTile.value;
                }
                tileCol += 1;
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

    boolean isSquareFree(int row, int column) {
        return gameBoard[row][column].getSquareState() == SquareState.FREE;
    }

    boolean hasNeighbour(int row, int column) {
        return getNeighbourState(row, column).contains(SquareState.MOVE) ||
                getNeighbourState(row, column).contains(SquareState.OCCUPIED);
    }

    void placeTile(int row, int column, ScrabbleTile scrabbleTile) {
        this.gameBoard[row][column].placeScrabbleTile(scrabbleTile);
        this.gameBoard[row][column].setSquareStateToMove();
    }

    private List<SquareState> getNeighbourState(int row, int column) {
        ArrayList<SquareState> neighbourStates = new ArrayList<>();

        int[][] neighbours = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] neighbour : neighbours) {
            int neighbourRow = row + neighbour[0];
            int neighbourColumn = column + neighbour[1];

            if (neighbourPositionIsValid(neighbourRow, neighbourColumn)) {
                neighbourStates.add(gameBoard[neighbourRow][neighbourColumn].getSquareState());
            }
        }
        return neighbourStates;
    }

    private boolean neighbourPositionIsValid(int row, int column) {
        return row >= 0 && row < 15 && column >= 0 && column < 15;
    }

    void setAllMoveTilesToOccupied() {
        for (ScrabbleSquare[] row : this.gameBoard) {
            for (ScrabbleSquare square : row) {
                if (square.getSquareState().equals(SquareState.MOVE)) {
                    square.setSquareStateToOccupied();
                }
            }
        }
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
        System.out.println(Arrays.deepToString(gameBoard)
                .replace("], ", "]\n")
                .replace("null", "|_/_|")
                .replace("[[", "\n[")
                .replace("]]", "]\n"));
    }
}