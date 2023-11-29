package edu.unibw.se.scrabble.client.ccom.impl;

import edu.unibw.se.scrabble.client.ccom.ClientCommunication;
import edu.unibw.se.scrabble.client.ccom.ClientConnect;
import edu.unibw.se.scrabble.client.ccom.ClientConnectCallback;
import edu.unibw.se.scrabble.common.base.ActionState;
import edu.unibw.se.scrabble.common.base.PlayerVote;
import edu.unibw.se.scrabble.common.base.ReturnValues;
import edu.unibw.se.scrabble.common.base.TileWithPosition;
import edu.unibw.se.scrabble.common.scom.NetworkConnect;
import edu.unibw.se.scrabble.common.scom.ToServer;

import java.rmi.RemoteException;

public class ClientCommunicationImpl implements ClientCommunication,ClientConnect {
    private NetworkConnect networkConnect = null;
    private ToServer toServer = null;
    private ClientConnectCallback cbc = null;
    @Override
    public ClientConnect getClientConnect() {
        return this;
    }

    @Override
    public void setNetworkConnect(NetworkConnect networkConnect) {
        this.networkConnect = networkConnect;
    }

    @Override
    public void setClientConnectCallback(ClientConnectCallback clientConnectCallback) {
        this.cbc = clientConnectCallback;

    }

    @Override
    public ReturnValues.ReturnLoginUser loginUser(String username, String password) {
        NetworkConnect.ReturnLoginNetwork ret = null;
        try {
            ret = networkConnect.loginUser(username, password, new ExampleToClient(this.cbc));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        this.toServer = ret.toServer;
        return ret.state;
    }

    @Override
    public ReturnValues.ReturnRegisterUser registerUser(String username, String password) {
        ReturnValues.ReturnRegisterUser rru = networkConnect.registerUser(username, password);
        return rru;
    }

    @Override
    public ReturnValues.ReturnStatistics getUserStatistics() {
        if (toServer == null) {
            return new ReturnValues.ReturnStatistics(ReturnValues.ReturnStatisticsState.NETWORK_FAILURE, null);
        }
        ReturnValues.ReturnStatistics rs = null;
        try {
            rs = toServer.getUserStatistics();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        //getUserStatistics();
        return rs;
    }

    @Override
    public ReturnValues.ReturnCreateSession createSession() {
        if (toServer == null) {
            return new ReturnValues.ReturnCreateSession(ReturnValues.ReturnCreateSessionState.NETWORK_FAILURE, 0);
        }
        ReturnValues.ReturnCreateSession rcs = null;
        try {
            rcs = toServer.createSession();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return rcs;
    }

    @Override
    public ReturnValues.ReturnJoinSession joinSession(int gameID) {
        if (toServer == null) {
            return ReturnValues.ReturnJoinSession.NETWORK_FAILURE;
        }
        try {
            return toServer.joinSession(gameID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        // return ReturnValues.ReturnJoinSession.SUCCESSFUL;
    }

    @Override
    public ReturnValues.ReturnStartGame startGame() {
        if (toServer == null) {
            return ReturnValues.ReturnStartGame.NETWORK_FAILURE;
        }
        try {
            return toServer.startGame();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public ReturnValues.ReturnSendPlayerVote sendPlayerVote(PlayerVote playerVote) {
        try {
            return this.toServer.sendPlayerVote(playerVote);
        } catch (RemoteException e) {
            return ReturnValues.ReturnSendPlayerVote.NETWORK_FAILURE;
        }
    }
}
