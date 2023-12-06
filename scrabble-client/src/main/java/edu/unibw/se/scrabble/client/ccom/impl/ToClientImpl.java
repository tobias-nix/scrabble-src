package edu.unibw.se.scrabble.client.ccom.impl;

import edu.unibw.se.scrabble.client.ccom.ClientConnectCallback;
import edu.unibw.se.scrabble.common.base.GameData;
import edu.unibw.se.scrabble.common.scom.ToClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Seegerer
 */
public class ToClientImpl extends UnicastRemoteObject implements ToClient {
    private ClientConnectCallback callback;

    protected ToClientImpl(ClientConnectCallback callback) throws RemoteException {
        this.callback = callback;
    }

    void setClientConnectCallback(ClientConnectCallback clientConnectCallback) {
        this.callback = clientConnectCallback;
    }

    @Override
    public void usersInSession(String[] usernames) throws RemoteException {
        callback.usersInSession(usernames);
    }

    @Override
    public void sendGameData(char[] rackTiles, char[] swapTiles, GameData gameData) throws RemoteException {
        callback.sendGameData(rackTiles, swapTiles, gameData);
    }

    @Override
    public void vote(String[] placedWords) throws RemoteException {
        callback.vote(placedWords);
    }
}
