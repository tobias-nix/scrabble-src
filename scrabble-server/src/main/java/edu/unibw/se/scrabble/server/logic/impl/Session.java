package edu.unibw.se.scrabble.server.logic.impl;

import edu.unibw.se.scrabble.common.base.GameData;
import edu.unibw.se.scrabble.common.base.LanguageSetting;

import java.util.ArrayList;

public class Session {
    private final int gameId;
    private final ArrayList<String> users = new ArrayList<>();
    private ScrabbleGame scrabbleGame;
    private final LanguageSetting languageSetting;

    public Session(String username, int gameId, LanguageSetting languageSetting) {
        this.users.add(username);
        this.gameId = gameId;
        this.languageSetting = languageSetting;
    }

    int getNumberOfUsers() {
        return users.size();
    }

    void addUser(String username) {
        users.add(username);
    }

    ArrayList<String> getUserUsernames() {
        return this.users;
    }

    char[] getRackTilesWithUsername(String username) {
        return this.scrabbleGame.getPlayerRackTiles(username);
    }

    char[] getSwapTilesWithUsername(String username) {
        return this.scrabbleGame.getPlayerSwapTiles(username);
    }

    ScrabbleGame getScrabbleGame() {
        return this.scrabbleGame;
    }

    void startGame() {
        this.scrabbleGame = new ScrabbleGame(users, languageSetting);
    }

    GameData getGameData() {
        return this.scrabbleGame.getGameData(this.gameId);
    }
}
