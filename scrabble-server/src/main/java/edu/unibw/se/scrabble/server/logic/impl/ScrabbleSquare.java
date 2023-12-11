package edu.unibw.se.scrabble.server.logic.impl;

import edu.unibw.se.scrabble.common.base.SquareState;

/**
 * @author Bößendörfer, Kompalka, Seegerer
 */
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

    //Getter and Setter methods aren't really necessary but improve readability of code.
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

    @Override
    public String toString() {
        return wordFactor + "#" + letterFactor + "#" + scrabbleTile;
    }
}
