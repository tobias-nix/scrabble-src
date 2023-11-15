package edu.unibw.se.scrabble.client.ccom.impl;

import edu.unibw.se.scrabble.client.ccom.ClientCommunication;
import edu.unibw.se.scrabble.client.ccom.ClientConnect;
import edu.unibw.se.scrabble.client.ccom.ClientConnectCallback;
import edu.unibw.se.scrabble.common.base.ActionState;
import edu.unibw.se.scrabble.common.base.ReturnValues;
import edu.unibw.se.scrabble.common.base.TileWithPosition;
import edu.unibw.se.scrabble.common.scom.NetworkConnect;

public class ExampleClientCommunikation implements ClientCommunication,ClientConnect {
    @Override
    public ClientConnect getClientConnect() {
        return this;
    }

    @Override
    public void setNetworkConnect(NetworkConnect networkConnect) {

    }

    @Override
    public void setClientConnectCallback(ClientConnectCallback clientConnectCallback) {

    }

    @Override
    public ReturnValues.ReturnLoginUser loginUser(String username, String password) {
        return ReturnValues.ReturnLoginUser.SUCCESSFUL;
    }

    @Override
    public ReturnValues.ReturnRegisterUser registerUser(String username, String password) {
        return null;
    }

    @Override
    public ReturnValues.ReturnStatistics getUserStatistics() {
        return null;
    }

    @Override
    public ReturnValues.ReturnCreateSession createSession() {
        return null;
    }

    @Override
    public ReturnValues.ReturnJoinSession joinSession(int gameID) {
        return ReturnValues.ReturnJoinSession.SUCCESSFUL;
    }

    @Override
    public ReturnValues.ReturnStartGame startGame() {
        return null;
    }

    @Override
    public ReturnValues.ReturnSelectAction selectAction(ActionState actionState) {
        return null;
    }

    @Override
    public ReturnValues.ReturnPlaceTile placeTile(TileWithPosition tileWithPosition) {
        return null;
    }

    @Override
    public ReturnValues.ReturnSwapTile swapTile(char letter) {
        return null;
    }

    @Override
    public ReturnValues.ReturnEndTurn endTurn() {
        return null;
    }
}
