package edu.unibw.se.scrabble.common.base;

public class Board {
    int gameID;
    String[] usernames;
    String currentPlayer; //Alternative: Current player is usernames[0] and rotates
    int[] score;
    PlayerState[] vote;
    int[] countRackTiles;
    int[] countSwapTiles;
    TileWithPosition[] square; //current board state rename to board?
    int bagSize;
    GameState state;
}
