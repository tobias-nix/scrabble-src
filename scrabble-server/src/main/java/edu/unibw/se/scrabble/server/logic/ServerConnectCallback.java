package edu.unibw.se.scrabble.server.logic;

import edu.unibw.se.scrabble.common.base.GameData;

public interface ServerConnectCallback {
    void usersInSession(String[] usernames);

    // if swapTiles is empty, send empty array instead of null-pointer
    // game state is different for every user -> rackTiles
    void sendGameData(String username, char[] rackTiles, char[] swapTiles, GameData gameData);

    ReturnPlayerVote vote(String username, String[] placedWords);
    enum ReturnPlayerVote {
        REJECTED,
        CONFIRMED
    }
}
