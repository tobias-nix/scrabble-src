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

public class ClientCommunicationImpl implements ClientCommunication, ClientConnect {
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
        NetworkConnect.ReturnLoginNetwork ret;
        try {
            ret = networkConnect.loginUser(username, password, new ToClientImpl(this.cbc));
        } catch (RemoteException e) {
            return ReturnValues.ReturnLoginUser.NETWORK_FAILURE;
        }
        this.toServer = ret.toServer;
        return ret.state;
    }

    @Override
    public ReturnValues.ReturnRegisterUser registerUser(String username, String password) {
        ReturnValues.ReturnRegisterUser rru;
        try {
            rru = networkConnect.registerUser(username, password);
        } catch (RemoteException e) {
            return ReturnValues.ReturnRegisterUser.NETWORK_FAILURE;
        }
        return rru;
    }

    @Override
    public ReturnValues.ReturnStatistics getUserStatistics() {
        if (toServer == null) {
            return new ReturnValues.ReturnStatistics(ReturnValues.ReturnStatisticsState.NETWORK_FAILURE, null);
        }
        ReturnValues.ReturnStatistics rs;
        try {
            rs = toServer.getUserStatistics();
        } catch (RemoteException e) {
            return new ReturnValues.ReturnStatistics(ReturnValues.ReturnStatisticsState.NETWORK_FAILURE, null);
        }
        return rs;
    }

    @Override
    public ReturnValues.ReturnCreateSession createSession() {
        if (toServer == null) {
            return new ReturnValues.ReturnCreateSession(ReturnValues.ReturnCreateSessionState.NETWORK_FAILURE, -1);
        }
        ReturnValues.ReturnCreateSession rcs;
        try {
            rcs = toServer.createSession();
        } catch (RemoteException e) {
            return new ReturnValues.ReturnCreateSession(ReturnValues.ReturnCreateSessionState.NETWORK_FAILURE, -1);
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
            return ReturnValues.ReturnJoinSession.NETWORK_FAILURE;
        }
    }

    @Override
    public ReturnValues.ReturnStartGame startGame() {
        if (toServer == null) {
            return ReturnValues.ReturnStartGame.NETWORK_FAILURE;
        }
        try {
            return toServer.startGame();
        } catch (RemoteException e) {
            return ReturnValues.ReturnStartGame.NETWORK_FAILURE;
        }
    }

    @Override
    public ReturnValues.ReturnSelectAction selectAction(ActionState actionState) {
        if (toServer == null) {
            return ReturnValues.ReturnSelectAction.NETWORK_FAILURE;
        }
        try {
            return toServer.selectAction(actionState);
        } catch (RemoteException e) {
            return ReturnValues.ReturnSelectAction.NETWORK_FAILURE;
        }
    }

    @Override
    public ReturnValues.ReturnPlaceTile placeTile(TileWithPosition tileWithPosition) {
        if (toServer == null) {
            return ReturnValues.ReturnPlaceTile.NETWORK_FAILURE;
        }
        try {
            return toServer.placeTile(tileWithPosition);
        } catch (RemoteException e) {
            return ReturnValues.ReturnPlaceTile.NETWORK_FAILURE;
        }    }

    @Override
    public ReturnValues.ReturnSwapTile swapTile(char letter) {
        if (toServer == null) {
            return ReturnValues.ReturnSwapTile.NETWORK_FAILURE;
        }
        try {
            return toServer.swapTile(letter);
        } catch (RemoteException e) {
            return ReturnValues.ReturnSwapTile.NETWORK_FAILURE;
        }
    }

    @Override
    public ReturnValues.ReturnEndTurn endTurn() {
        if (toServer == null) {
            return ReturnValues.ReturnEndTurn.NETWORK_FAILURE;
        }
        try {
            return toServer.endTurn();
        } catch (RemoteException e) {
            return ReturnValues.ReturnEndTurn.NETWORK_FAILURE;
        }
    }

    @Override
    public ReturnValues.ReturnSendPlayerVote sendPlayerVote(PlayerVote playerVote) {
        if (toServer == null) {
            return ReturnValues.ReturnSendPlayerVote.NETWORK_FAILURE;
        }
        try {
            return this.toServer.sendPlayerVote(playerVote);
        } catch (RemoteException e) {
            return ReturnValues.ReturnSendPlayerVote.NETWORK_FAILURE;
        }
    }
}
