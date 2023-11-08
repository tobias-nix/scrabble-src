package edu.unibw.se.scrabble.common.base;

public class GameData {
    int gameID;
    String[] usernames;
    String currentPlayer; //Alternative: Current player is usernames[0] and rotates
    int[] score;
    PlayerState[] playerVotes;
    int[] countRackTiles;
    int[] countSwapTiles;
    TileWithPosition[] square;
    int bagSize;
    GameState state;
}
