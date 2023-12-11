package edu.unibw.se.scrabble.server.logic.impl;

/**
 * @author Bößendörfer, Kompalka, Seegerer
 */
class ScrabbleTile {
    char letter;
    int value;

    ScrabbleTile(char letter, int value) {
        this.letter = letter;
        this.value = value;
    }

    @Override
    public String toString() {
        return "|" + letter + "/" + value + "|";
    }
}
