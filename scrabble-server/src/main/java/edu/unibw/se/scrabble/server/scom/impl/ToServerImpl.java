package edu.unibw.se.scrabble.server.scom.impl;

import edu.unibw.se.scrabble.common.base.*;
import edu.unibw.se.scrabble.common.scom.ToClient;
import edu.unibw.se.scrabble.common.scom.ToServer;
import edu.unibw.se.scrabble.server.logic.ServerConnect;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Seegerer
 */
public class ToServerImpl extends UnicastRemoteObject implements ToServer {
    private final String username;
    final ToClient toClient;
    private final ServerConnect serverConnect;

    protected ToServerImpl(String username, ToClient toClient, ServerConnect serverConnect) throws RemoteException {
        this.username = username;
        this.toClient = toClient;
        this.serverConnect = serverConnect;
    }

    @Override
    public ReturnValues.ReturnStatistics getUserStatistics() throws RemoteException {
        return this.serverConnect.getUserStatistics(this.username);
    }

    @Override
    public ReturnValues.ReturnCreateSession createSession(LanguageSetting languageSetting) throws RemoteException {
        return this.serverConnect.createSession(languageSetting, this.username);
    }

    @Override
    public ReturnValues.ReturnJoinSession joinSession(int gameId) throws RemoteException {
        return this.serverConnect.joinSession(gameId, this.username);
    }

    @Override
    public ReturnValues.ReturnStartGame startGame() throws RemoteException {
        return this.serverConnect.startGame(this.username);
    }

    @Override
    public ReturnValues.ReturnSelectAction selectAction(ActionState actionState) throws RemoteException {
        return this.serverConnect.selectAction(actionState, this.username);
    }

    @Override
    public ReturnValues.ReturnPlaceTile placeTile(TileWithPosition tileWithPosition) throws RemoteException {
        return this.serverConnect.placeTile(tileWithPosition, this.username);
    }

    @Override
    public ReturnValues.ReturnSwapTile swapTile(char letter) throws RemoteException {
        return this.serverConnect.swapTile(letter, this.username);
    }

    @Override
    public ReturnValues.ReturnEndTurn endTurn() throws RemoteException {
        return this.serverConnect.endTurn(this.username);
    }

    @Override
    public ReturnValues.ReturnSendPlayerVote sendPlayerVote(PlayerVote playerVote) throws RemoteException {
        return this.serverConnect.sendPlayerVote(playerVote);
    }
}