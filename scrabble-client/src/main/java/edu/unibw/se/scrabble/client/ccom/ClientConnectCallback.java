package edu.unibw.se.scrabble.client.ccom;

import edu.unibw.se.scrabble.common.base.GameState;

public interface ClientConnectCallback {
    void usersInSession(String[] usernames); //Dialog "New user joined session"

    void sendGameState(char[] rackTiles, char[] swapTiles, GameState gameState);
}
