package edu.unibw.se.scrabble.common.scom;

import edu.unibw.se.scrabble.common.base.GameState;

import java.rmi.Remote;

public interface ToClient extends Remote {
    void usersInSession(String[] usernames);
    void sendGameState(char[] rackTiles, char[] swapTiles, GameState gameState);
}
