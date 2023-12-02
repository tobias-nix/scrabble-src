package edu.unibw.se.scrabble.server.logic.impl;

import edu.unibw.se.scrabble.common.base.SquareState;

class ScrabbleSquare {
    private SquareState squareState;

    private final int wordFactor;
    private final int letterFactor;
    private ScrabbleTile scrabbleTile;

    ScrabbleSquare(int wordFactor, int letterFactor) {
        this.wordFactor = wordFactor;
        this.letterFactor = letterFactor;
        this.squareState = SquareState.FREE;
        this.scrabbleTile = null;
    }

    SquareState getSquareState() {
        return this.squareState;
    }

    void changeSquareStateToFree() {
        this.squareState = SquareState.FREE;
    }

    void changeSquareStateToOccupied() {
        this.squareState = SquareState.OCCUPIED;
    }

    void changeSquareStateToMove() {
        this.squareState = SquareState.MOVE;
    }

    void placeScrabbleTile(ScrabbleTile scrabbleTile) {
        this.scrabbleTile = scrabbleTile;
    }

    @Override
    public String toString() {
        return wordFactor + "#" + letterFactor + "#" + scrabbleTile;
    }
}
