package edu.unibw.se.scrabble.common.scom;

import edu.unibw.se.scrabble.common.base.GameData;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ToClient extends Remote {
    void usersInSession(String[] usernames) throws RemoteException;

    void sendGameState(char[] rackTiles, char[] swapTiles, GameData gameData) throws RemoteException;

    ReturnPlayerVote vote(String[] placedWords) throws RemoteException;
    enum ReturnPlayerVote {
        REJECTED,
        CONFIRMED
    }
}
