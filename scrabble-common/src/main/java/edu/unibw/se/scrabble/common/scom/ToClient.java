package edu.unibw.se.scrabble.common.scom;

import edu.unibw.se.scrabble.common.base.GameData;

import java.rmi.Remote;

public interface ToClient extends Remote {
    void usersInSession(String[] usernames);
    void sendGameState(char[] rackTiles, char[] swapTiles, GameData gameData);

    ReturnPlayerVote vote(String[] placedWords);
    enum ReturnPlayerVote {
        REJECTED,
        CONFIRMED
    }
}
