package edu.unibw.se.scrabble.client.ccom.impl;

import edu.unibw.se.scrabble.client.ccom.ClientConnectCallback;
import edu.unibw.se.scrabble.common.base.GameData;
import edu.unibw.se.scrabble.common.base.ReturnValues;
import edu.unibw.se.scrabble.common.scom.ToClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ToClientImpl extends UnicastRemoteObject implements ToClient {
    private final ClientConnectCallback callback;

    protected ToClientImpl(ClientConnectCallback callback) throws RemoteException {
        this.callback = callback;
    }

    // TODO: Habe nur ganz stumpf die callback methoden aufgerufen. Muss ich hier was prüfen?
    @Override
    public void usersInSession(String[] usernames) throws RemoteException {
        callback.usersInSession(usernames);
    }

    @Override
    public void sendGameState(char[] rackTiles, char[] swapTiles, GameData gameData) throws RemoteException {
        callback.sendGameState(rackTiles, swapTiles, gameData);
    }

    @Override
    public ReturnValues.ReturnPlayerVote vote(String[] placedWords) throws RemoteException {
        return callback.vote(placedWords);
    }
}
