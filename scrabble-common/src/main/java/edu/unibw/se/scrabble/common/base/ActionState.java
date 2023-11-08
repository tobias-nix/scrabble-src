package edu.unibw.se.scrabble.common.base;

public enum ActionState {
    PASS,
    SWAP,
    PLACE
}

// Option 1: Spieler entscheidet sich für Action und die anderen Optionen werden ausgegraut und können nicht mehr
// gewählt werden

//Option 2: Spieler darf munter durch alle Actions wechseln, auch wenn er bspw. schon Steine gelegt hat
// Dabei zu beachten: Bei jedem Aufruf von selectAction() muss in der Server Logic alle Steine, die auf MOVE Feldern
// liegen, zurückgesetzt aufs Rack gesetzt werden.
// Swap Tiles müssen zurück aufs Rack gesetzt werden.