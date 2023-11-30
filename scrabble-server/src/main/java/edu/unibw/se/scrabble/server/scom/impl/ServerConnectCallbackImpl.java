package edu.unibw.se.scrabble.server.scom.impl;

import edu.unibw.se.scrabble.common.base.GameData;
import edu.unibw.se.scrabble.common.scom.ToClient;
import edu.unibw.se.scrabble.server.logic.ServerConnectCallback;

import java.rmi.RemoteException;
import java.util.Arrays;

public class ServerConnectCallbackImpl implements ServerConnectCallback {
    private final ServerCommunicationImpl serverCommunicationImpl;
    public ServerConnectCallbackImpl(ServerCommunicationImpl serverCommunicationImpl) {
        this.serverCommunicationImpl = serverCommunicationImpl;
    }

    @Override
    public void usersInSession(String[] usernames) {
        if (usernames == null) {
            return;
        }
        Arrays.stream(usernames).forEach(user -> {
            ToClient toClient = this.serverCommunicationImpl.getToClientFromUsername(user);
            (new Thread(() -> {
                try {
                    toClient.usersInSession(usernames);
                    System.out.println(user + " is here");
                } catch (RemoteException e) {
                    this.serverCommunicationImpl.removeSession(user);
                }
            })).start();
        });
    }

    @Override
    public void sendGameState(String username, char[] rackTiles, char[] swapTiles, GameData gameData) {
        if (username == null || rackTiles == null || swapTiles == null || gameData == null) {
            return;
        }
        ToClient toClient = this.serverCommunicationImpl.getToClientFromUsername(username);
        (new Thread(() -> {
            try {
                toClient.sendGameState(rackTiles, swapTiles, gameData);
            } catch (RemoteException e) {
                this.serverCommunicationImpl.removeSession(username);
            }
        })).start();
    }

    @Override
    public void vote(String username, String[] placedWords) {
        if (username == null || placedWords == null) {
            return;
        }
        ToClient toClient = this.serverCommunicationImpl.getToClientFromUsername(username);
        (new Thread(() -> {
            try {
                toClient.vote(placedWords);
            } catch (RemoteException e) {
                this.serverCommunicationImpl.removeSession(username);
            }
        })).start();
    }
}
