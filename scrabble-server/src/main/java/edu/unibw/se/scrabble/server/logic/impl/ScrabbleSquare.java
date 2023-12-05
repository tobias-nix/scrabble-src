package edu.unibw.se.scrabble.server.logic.impl;

import edu.unibw.se.scrabble.common.base.SquareState;

class ScrabbleSquare {
    SquareState squareState;
    final int wordFactor;
    final int letterFactor;
    ScrabbleTile scrabbleTile;

    ScrabbleSquare(int wordFactor, int letterFactor) {
        this.wordFactor = wordFactor;
        this.letterFactor = letterFactor;
        this.squareState = SquareState.FREE;
        this.scrabbleTile = null;
    }
    ScrabbleTile removeScrabbleTile() {
        ScrabbleTile tmp = this.scrabbleTile;
        this.scrabbleTile = null;
        return tmp;
    }

    SquareState getSquareState() {
        return this.squareState;
    }

    void setSquareStateToFree() {
        this.squareState = SquareState.FREE;
    }

    void setSquareStateToOccupied() {
        this.squareState = SquareState.OCCUPIED;
    }

    void setSquareStateToMove() {
        this.squareState = SquareState.MOVE;
    }

    void placeScrabbleTile(ScrabbleTile scrabbleTile) {
        this.scrabbleTile = scrabbleTile;
    }

    @Override
    public String toString() {
        return wordFactor + "#" + letterFactor + "#" + scrabbleTile;
    }
    char getLetter() {
        return this.scrabbleTile.letter;
    }
}
