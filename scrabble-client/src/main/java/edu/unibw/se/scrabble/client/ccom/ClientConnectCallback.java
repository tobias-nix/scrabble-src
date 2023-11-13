package edu.unibw.se.scrabble.client.ccom;

import edu.unibw.se.scrabble.common.base.GameData;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnPlayerVote;

public interface ClientConnectCallback {
    void usersInSession(String[] usernames); //Dialog "New user joined session"

    void sendGameState(char[] rackTiles, char[] swapTiles, GameData gameData);

    ReturnPlayerVote vote(String[] placedWords);
}
