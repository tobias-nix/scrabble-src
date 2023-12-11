package edu.unibw.se.scrabble.server.logic.impl;

import edu.unibw.se.scrabble.common.base.GameData;
import edu.unibw.se.scrabble.common.base.LanguageSetting;

import java.util.ArrayList;

/**
 * @author Bößendörfer, Kompalka, Seegerer
 */
public class Session {
    final int gameId;
    final ArrayList<String> users = new ArrayList<>();
    ScrabbleGame scrabbleGame = null;
    final LanguageSetting languageSetting;

    public Session(String username, int gameId, LanguageSetting languageSetting) {
        this.users.add(username);
        this.gameId = gameId;
        this.languageSetting = languageSetting;
    }

    //Getter and Setter methods aren't really necessary but improve readability of code.
    boolean hasGameStarted() {
        return scrabbleGame != null;
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
