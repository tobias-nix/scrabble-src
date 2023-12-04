package edu.unibw.se.scrabble.server.logic.impl;

import edu.unibw.se.scrabble.common.base.*;
import edu.unibw.se.scrabble.server.data.ScrabbleData;
import edu.unibw.se.scrabble.server.logic.ServerConnect;
import edu.unibw.se.scrabble.server.logic.ServerConnectCallback;
import edu.unibw.se.scrabble.server.logic.ServerLogic;

import java.util.*;

public class ServerLogicImpl implements ServerLogic, ServerConnect {
    private ScrabbleData scrabbleData = null;
    private ServerConnectCallback serverConnectCallback = null;

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

    private final HashMap<Integer, Session> mapGameIdToSession = new HashMap<>();
    private final HashMap<String, Integer> mapUsernameToGameId = new HashMap<>();


    @Override
    public ReturnValues.ReturnStatistics getUserStatistics(String username) {
        if (username == null) {
            return new ReturnValues.ReturnStatistics(ReturnValues.ReturnStatisticsState.FAILURE, null);
        }
        return new ReturnValues.ReturnStatistics(ReturnValues.ReturnStatisticsState.SUCCESSFUL,
                scrabbleData.getUserStatistics(username));
    }

    @Override
    public ReturnValues.ReturnCreateSession createSession(LanguageSetting languageSetting, String username) {
        if (username == null) {
            return new ReturnValues.ReturnCreateSession(ReturnValues.ReturnCreateSessionState.FAILURE, -1);
        }
        if (this.mapGameIdToSession.size() >= 10) {
            return new ReturnValues.ReturnCreateSession(ReturnValues.ReturnCreateSessionState.SESSION_LIMIT_REACHED, -1);
        }
        if (this.mapUsernameToGameId.containsKey(username)) {
            return new ReturnValues.ReturnCreateSession(ReturnValues.ReturnCreateSessionState.USER_ALREADY_IN_SESSION, -1);
        }

        int generatedGameId = generateGameId();
        Session newSession = new Session(username, generatedGameId, languageSetting);

        mapUsernameToGameId.put(username, generatedGameId);
        mapGameIdToSession.put(generatedGameId, newSession);

        return new ReturnValues.ReturnCreateSession(ReturnValues.ReturnCreateSessionState.SUCCESSFUL, generatedGameId);
    }

    @Override
    public ReturnValues.ReturnJoinSession joinSession(int gameId, String username) {
        if (username == null) {
            return ReturnValues.ReturnJoinSession.FAILURE;
        }
        if (gameId < 0 || gameId > 99999 || !this.mapGameIdToSession.containsKey(gameId)) {
            return ReturnValues.ReturnJoinSession.GAME_ID_INVALID;
        }
        if (this.mapUsernameToGameId.containsKey(username)) {
            return ReturnValues.ReturnJoinSession.USER_ALREADY_IN_SESSION;
        }

        Session session = mapGameIdToSession.get(gameId);
        if (session.getNumberOfUsers() > 3) {
            return ReturnValues.ReturnJoinSession.TOO_MANY_USERS;
        }

        session.addUser(username);
        mapUsernameToGameId.put(username, gameId);

        serverConnectCallback.usersInSession(session.getUserUsernames().toArray(new String[0]));
        return ReturnValues.ReturnJoinSession.SUCCESSFUL;
    }

    @Override
    public ReturnValues.ReturnStartGame startGame(String username) {
        if (username == null || !this.mapUsernameToGameId.containsKey(username)) {
            return ReturnValues.ReturnStartGame.FAILURE;
        }
        Session session = getSessionWithUsername(username);
        if (session.getNumberOfUsers() < 2) {
            return ReturnValues.ReturnStartGame.USER_ALONE_IN_SESSION;
        }
        if (!Objects.equals(session.getUserUsernames().getFirst(), username)) {
            return ReturnValues.ReturnStartGame.USER_NOT_THE_HOST;
        }
        session.startGame();

        sendGameData(session);

        return ReturnValues.ReturnStartGame.SUCCESSFUL;
    }

    private void sendGameData(Session session) {
        session.getUserUsernames().forEach(player -> {
            serverConnectCallback.sendGameData(
                    player,
                    session.getRackTilesWithUsername(player),
                    session.getSwapTilesWithUsername(player),
                    session.getGameData());
        });
    }

    @Override
    public ReturnValues.ReturnSelectAction selectAction(ActionState actionState, String username) {
        serverConnectCallback.sendGameData(null, null, null, null);
        return ReturnValues.ReturnSelectAction.SUCCESSFUL;
    }

    @Override
    public ReturnValues.ReturnPlaceTile placeTile(TileWithPosition tileWithPosition, String username) {

        ScrabbleGame scrabbleGame = getScrabbleGameFromMap(username);
        if (scrabbleGame.getGameState() != GameState.PLACE ||
                !Objects.equals(scrabbleGame.getCurrentPlayerUsername(), username)) {
            return ReturnValues.ReturnPlaceTile.FAILURE;
        }

        if (scrabbleGame.isRackTiles(tileWithPosition.letter())) {
            return ReturnValues.ReturnPlaceTile.FAILURE;
        }

        if (!scrabbleGame.isPlaceAllowed(tileWithPosition)) {
            return ReturnValues.ReturnPlaceTile.POSITION_NOT_ALLOWED;
        }

        scrabbleGame.placeTile(tileWithPosition);

        // TODO send gamestate individuell anpassen
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

    @Override
    public ReturnValues.ReturnSendPlayerVote sendPlayerVote(PlayerVote playerVote) {
        return null;
    }

    @Override
    public void informAboutUserLogin(String username) {
        if (mapUsernameToGameId.containsKey(username)) {
            if(getSessionWithUsername(username).hasGameStarted()) {
                sendGameData(getSessionWithUsername(username));
            }
        }
    }

    private ScrabbleGame getScrabbleGameFromMap(String username) {
        return this.mapGameIdToSession.get(this.mapUsernameToGameId.get(username)).getScrabbleGame();
    }

    private Session getSessionWithUsername(String username) {
        return this.mapGameIdToSession.get(this.mapUsernameToGameId.get(username));
    }

    private int generateGameId() {
        Random r = new Random();
        int newGameId;
        do {
            newGameId = r.nextInt(100000 - 1) + 1;
        } while (this.mapGameIdToSession.containsKey(newGameId));

        return newGameId;
    }


    // TODO: schnelle hässliche lösung für tests, hier sollte eigentlich die csv eingelesen werden
    @Override
    public void setServerState(int numberOfSessionsActive) {

    }
}
