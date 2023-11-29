package edu.unibw.se.scrabble.client.ccom.impl;

import edu.unibw.se.scrabble.client.ccom.ClientConnectCallback;
import edu.unibw.se.scrabble.common.base.GameData;
import edu.unibw.se.scrabble.common.scom.ToClient;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
public class ExampleToClient extends UnicastRemoteObject implements ToClient {
private final ClientConnectCallback callback;
    protected ExampleToClient(ClientConnectCallback callback) throws RemoteException {

        this.callback = callback;
    }

    @Override
    public void usersInSession(String[] usernames) throws RemoteException {
        callback.usersInSession(usernames);
    }

    @Override
    public void sendGameState(char[] rackTiles, char[] swapTiles, GameData gameData) throws RemoteException {

    }

    @Override
    public void vote(String[] placedWords) throws RemoteException {
    }
}
