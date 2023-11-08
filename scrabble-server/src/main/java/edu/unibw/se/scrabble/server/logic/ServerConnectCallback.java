package edu.unibw.se.scrabble.server.logic;

import edu.unibw.se.scrabble.common.base.Board;

public interface ServerConnectCallback {
    void usersInSession(String[] usernames);

    // if swapTiles is empty, send empty array instead of null-pointer
    // game state is different for every user -> rackTiles
    void sendGameState(String name, char[] rackTiles, char[] swapTiles, Board board);
}
