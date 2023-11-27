package edu.unibw.se.scrabble.server.logic.impl;

import edu.unibw.se.scrabble.common.base.ActionState;
import edu.unibw.se.scrabble.common.base.ReturnValues;
import edu.unibw.se.scrabble.common.base.TileWithPosition;
import edu.unibw.se.scrabble.server.data.ScrabbleData;
import edu.unibw.se.scrabble.server.logic.ServerConnect;
import edu.unibw.se.scrabble.server.logic.ServerConnectCallback;
import edu.unibw.se.scrabble.server.logic.ServerLogic;

public class ServerLogicImpl implements ServerLogic, ServerConnect {
    private ScrabbleData scrabbleData = null;
    private ServerConnectCallback serverConnectCallback = null;

    int numberOfSessionsActive = 0;

    // TODO: setServerState braucht ganz viele Parameter, nicht nur session numbers. Das sollte man eh als Liste anlegen
    @Override
    public void setServerState(int numberOfSessionsActive) {
        this.numberOfSessionsActive = numberOfSessionsActive;
    }

    @Override
    public ServerConnect getServerConnect() {
        return this;
    }

    @Override
    public void setScrabbleData(ScrabbleData scrabbleData) {
        this.scrabbleData = scrabbleData;
    }


    @Override
    public void setServerConnectCallback(ServerConnectCallback serverConnectCallback) {
        this.serverConnectCallback = serverConnectCallback;
    }

    @Override
    public ReturnValues.ReturnStatistics getUserStatistics(String username) {
        return new ReturnValues.ReturnStatistics(ReturnValues.ReturnStatisticsState.SUCCESSFUL,
                scrabbleData.getUserStatistics(username));
    }

    @Override
    public ReturnValues.ReturnCreateSession createSession(String username) {
        if (this.numberOfSessionsActive < 10) {
            return new ReturnValues.ReturnCreateSession(
                    ReturnValues.ReturnCreateSessionState.SUCCESSFUL, 77777
            );
        } else {
            return new ReturnValues.ReturnCreateSession(
                    ReturnValues.ReturnCreateSessionState.SESSION_LIMIT_REACHED, -1
            );
        }
    }

    @Override
    public ReturnValues.ReturnJoinSession joinSession(int gameID, String username) {
        serverConnectCallback.usersInSession(null);
        return ReturnValues.ReturnJoinSession.SUCCESSFUL;
    }

    @Override
    public ReturnValues.ReturnStartGame startGame(String username) {
        serverConnectCallback.sendGameData(null, null, null, null);
        return ReturnValues.ReturnStartGame.SUCCESSFUL;
    }

    @Override
    public ReturnValues.ReturnSelectAction selectAction(ActionState actionState, String username) {
        serverConnectCallback.sendGameData(null, null, null, null);
        return ReturnValues.ReturnSelectAction.SUCCESSFUL;
    }

    @Override
    public ReturnValues.ReturnPlaceTile placeTile(TileWithPosition tileWithPosition, String username) {
        serverConnectCallback.sendGameData(null, null, null, null);
        return ReturnValues.ReturnPlaceTile.SUCCESSFUL;
    }

    @Override
    public ReturnValues.ReturnSwapTile swapTile(char letter, String username) {
        serverConnectCallback.sendGameData(null, null, null, null);
        return ReturnValues.ReturnSwapTile.SUCCESSFUL;
    }

    @Override
    public ReturnValues.ReturnEndTurn endTurn(String username) {
        return null;
    }
}
