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

    public int getNumberOfUsers() {
        return users.size();
    }

    public void addUser(String username) {
        users.add(username);
    }

    public ArrayList<String> getUserUsernames() {
        return this.users;
    }

    public char[] getRackTilesWithUsername(String username) {
        return this.scrabbleGame.getPlayerRackTiles(username);
    }

    public char[] getSwapTilesWithUsername(String username) {
        return this.scrabbleGame.getPlayerSwapTiles(username);
    }

    public ScrabbleGame getScrabbleGame() {
        return this.scrabbleGame;
    }

    public GameData startGame() {
        this.scrabbleGame = new ScrabbleGame(users, languageSetting);
        return this.scrabbleGame.getGameData(this.gameId);
    }
}
