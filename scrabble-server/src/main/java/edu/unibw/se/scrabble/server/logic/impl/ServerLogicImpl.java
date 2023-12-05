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
        if (username == null || this.scrabbleData == null) {
            return new ReturnValues.ReturnStatistics(ReturnValues.ReturnStatisticsState.FAILURE, null);
        }
        return new ReturnValues.ReturnStatistics(ReturnValues.ReturnStatisticsState.SUCCESSFUL,
                this.scrabbleData.getUserStatistics(username));
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

    private int generateGameId() {
        Random r = new Random();
        int newGameId;
        do {
            newGameId = r.nextInt(100000 - 1) + 1;
        } while (this.mapGameIdToSession.containsKey(newGameId));

        return newGameId;
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

    private Session getSessionWithUsername(String username) {
        return this.mapGameIdToSession.get(this.mapUsernameToGameId.get(username));
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
        session.getUserUsernames().forEach(player ->
                serverConnectCallback.sendGameData(
                        player,
                        session.getRackTilesWithUsername(player),
                        session.getSwapTilesWithUsername(player),
                        session.getGameData())
        );
    }

    @Override
    public ReturnValues.ReturnSelectAction selectAction(ActionState actionState, String username) {
        if (username == null || actionState == null || !this.mapUsernameToGameId.containsKey(username)) {
            return ReturnValues.ReturnSelectAction.FAILURE;
        }
        Session session = this.getSessionWithUsername(username);
        if (!session.hasGameStarted()) {
            return ReturnValues.ReturnSelectAction.FAILURE;
        }

        ScrabbleGame scrabbleGame = session.getScrabbleGame();
        GameState currentGameState = scrabbleGame.getGameState();
        if (!currentGameState.equals(GameState.PLAY) &&
                !currentGameState.equals(GameState.PLACE) &&
                !currentGameState.equals(GameState.SWAP) &&
                !currentGameState.equals(GameState.PASS)) {
            return ReturnValues.ReturnSelectAction.FAILURE;
        }

        if (actionState.equals(ActionState.SWAP) && scrabbleGame.getBagSize() < 7) {
            return ReturnValues.ReturnSelectAction.LESS_THAN_SEVEN_TILES_IN_BAG;
        }

        scrabbleGame.returnPlacedAndSwapTilesToRack();

        scrabbleGame.setGameState(actionState);

        sendGameData(session);
        return ReturnValues.ReturnSelectAction.SUCCESSFUL;
    }

    @Override
    public ReturnValues.ReturnPlaceTile placeTile(TileWithPosition tileWithPosition, String username) {
        // TODO erstes Place muss aufs sternfeld gehen.
        if (username == null || tileWithPosition == null || !this.mapUsernameToGameId.containsKey(username)) {
            return ReturnValues.ReturnPlaceTile.FAILURE;
        }
        Session session = this.getSessionWithUsername(username);
        if (!session.hasGameStarted()) {
            return ReturnValues.ReturnPlaceTile.FAILURE;
        }

        ScrabbleGame scrabbleGame = session.getScrabbleGame();

        if (scrabbleGame.getGameState() != GameState.PLACE ||
                !Objects.equals(scrabbleGame.getCurrentPlayerUsername(), username)) {
            return ReturnValues.ReturnPlaceTile.FAILURE;
        }

        if (scrabbleGame.playerHasRackTileWithThisLetter(tileWithPosition.letter())) {
            return ReturnValues.ReturnPlaceTile.FAILURE;
        }

        if (!scrabbleGame.isPlaceAllowed(tileWithPosition)) {
            return ReturnValues.ReturnPlaceTile.POSITION_NOT_ALLOWED;
        }

        scrabbleGame.placeTile(tileWithPosition);

        this.sendGameData(session);
        return ReturnValues.ReturnPlaceTile.SUCCESSFUL;
    }

    @Override
    public ReturnValues.ReturnSwapTile swapTile(char letter, String username) {
        if (!Character.isLetter(letter) || username == null) {
            return ReturnValues.ReturnSwapTile.FAILURE;
        }
        Session session = this.getSessionWithUsername(username);
        if (!session.hasGameStarted()) {
            return ReturnValues.ReturnSwapTile.FAILURE;
        }

        ScrabbleGame scrabbleGame = session.getScrabbleGame();

        if (scrabbleGame.getGameState() != GameState.SWAP ||
                !Objects.equals(scrabbleGame.getCurrentPlayerUsername(), username)) {
            return ReturnValues.ReturnSwapTile.FAILURE;
        }

        if (scrabbleGame.playerHasRackTileWithThisLetter(letter)) {
            return ReturnValues.ReturnSwapTile.FAILURE;
        }

        scrabbleGame.swapTile(letter);

        this.sendGameData(session);

        return ReturnValues.ReturnSwapTile.SUCCESSFUL;
    }

    @Override
    public ReturnValues.ReturnEndTurn endTurn(String username) {
        if (username == null) {
            return ReturnValues.ReturnEndTurn.FAILURE;
        }
        Session session = this.getSessionWithUsername(username);
        if (!session.hasGameStarted()) {
            return ReturnValues.ReturnEndTurn.FAILURE;
        }

        ScrabbleGame scrabbleGame = session.getScrabbleGame();

        if (scrabbleGame.getGameState() != GameState.SWAP ||
                !Objects.equals(scrabbleGame.getCurrentPlayerUsername(), username)) {
            return ReturnValues.ReturnEndTurn.FAILURE;
        }

        switch (scrabbleGame.getGameState()) {
            case SWAP:
                scrabbleGame.endTurnSwap();
                return ReturnValues.ReturnEndTurn.SUCCESSFUL;
            case PLACE:
                /*session.getUserUsernames().forEach(player ->
                        serverConnectCallback.vote(player, scrabbleGame.getPlacedWords())
                ); // NOTFALLS DAS HIER da wirds an alle geschickt */
                session.getUserUsernames().stream()
                        .filter(player -> !player.equals(scrabbleGame.getCurrentPlayerUsername()))
                        .forEach(player ->
                        serverConnectCallback.vote(player, scrabbleGame.getPlacedWords())
                );
                scrabbleGame.endTurnPlace();
                return ReturnValues.ReturnEndTurn.SUCCESSFUL;
            case PASS:
                if (scrabbleGame.getPassCounter() < session.getNumberOfUsers() * 2 - 1) {
                    scrabbleGame.endTurnPass();
                } else {
                    scrabbleGame.endTurnGameOver();
                    this.sendGameData(session);

                    List<Integer> score = session.getGameData().score;
                    List<String> usernames = session.getUserUsernames();

                    for (int i = 0; i < session.getNumberOfUsers(); i++) {
                        String player = usernames.get(i);
                        Statistics statistics = scrabbleData.getUserStatistics(player);
                        int newGamesPlayed = statistics.gamesPlayed() + 1;
                        int newGamesWon = Collections.max(score).equals(score.get(i)) ? statistics.gamesWon() + 1 : statistics.gamesWon();
                        int newHighestScore = Math.max(statistics.highestScore(), score.get(i));
                        int newTotalScore = statistics.totalScore() + score.get(i);

                        scrabbleData.saveUserStatistics(player, new Statistics(newGamesPlayed, newGamesWon, newHighestScore, newTotalScore));
                    }
                }
                return ReturnValues.ReturnEndTurn.SUCCESSFUL;
        }
        return ReturnValues.ReturnEndTurn.FAILURE;
    }

    @Override
    public ReturnValues.ReturnSendPlayerVote sendPlayerVote(PlayerVote playerVote, String username) {
        if (playerVote == null) {
            return ReturnValues.ReturnSendPlayerVote.FAILURE;
        }

        ScrabbleGame scrabbleGame = getSessionWithUsername(username).getScrabbleGame();

        if (scrabbleGame.getGameState() != GameState.VOTE) {
            return ReturnValues.ReturnSendPlayerVote.FAILURE;
        }

        scrabbleGame.setPlayerState(playerVote, username);



        // TODO Wie sollen wir das vote system umsetzten, wenn 4 Leute gleichzeitig was vom Server wollen?
        while (!scrabbleGame.getPlayerStates().contains(PlayerState.NOT_VOTED)) {
            try {
                wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
                throw new RuntimeException(e);
            }
        }




        // Alle player wieder auf not voted setzen
        return null;
    }

    @Override
    public void informAboutUserLogin(String username) {
        if (mapUsernameToGameId.containsKey(username)) {
            if (getSessionWithUsername(username).hasGameStarted()) {
                sendGameData(getSessionWithUsername(username));
            }
        }
    }


    // TODO: schnelle hässliche lösung für tests, hier sollte eigentlich die csv eingelesen werden
    @Override
    public void setServerState(int numberOfSessionsActive) {

    }
}
