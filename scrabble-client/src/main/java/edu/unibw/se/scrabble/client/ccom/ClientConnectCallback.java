package edu.unibw.se.scrabble.client.ccom;

import edu.unibw.se.scrabble.common.base.GameData;

public interface ClientConnectCallback {
    void usersInSession(String[] usernames); //Dialog "New user joined session"

    void sendGameState(char[] rackTiles, char[] swapTiles, GameData gameData);

    ReturnPlayerVote vote(String[] placedWords);
    enum ReturnPlayerVote {
        REJECTED,
        CONFIRMED
    }
}
