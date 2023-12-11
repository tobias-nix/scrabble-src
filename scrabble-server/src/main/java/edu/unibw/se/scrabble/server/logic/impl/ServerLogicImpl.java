package edu.unibw.se.scrabble.server.logic.impl;

import edu.unibw.se.scrabble.common.base.*;
import edu.unibw.se.scrabble.server.data.ScrabbleData;
import edu.unibw.se.scrabble.server.logic.ServerConnect;
import edu.unibw.se.scrabble.server.logic.ServerConnectCallback;
import edu.unibw.se.scrabble.server.logic.ServerLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Bößendörfer, Kompalka, Seegerer
 */
public class ServerLogicImpl implements ServerLogic, ServerConnect {
    private ScrabbleData scrabbleData = null;
    private ServerConnectCallback serverConnectCallback = null;
    private final HashMap<LanguageSetting, HashSet<String>> allDictionaries = createDictionaries();
    private final HashMap<Integer, Session> mapGameIdToSession = new HashMap<>();
    private final HashMap<String, Integer> mapUsernameToGameId = new HashMap<>();
    private final static int GAME_ID_LIMIT = 99999;

    @Override
    synchronized public ServerConnect getServerConnect() {
        return this;
    }

    @Override
    synchronized public void setScrabbleData(ScrabbleData scrabbleData) {
        this.scrabbleData = scrabbleData;
    }

    @Override
    synchronized public void setServerConnectCallback(ServerConnectCallback serverConnectCallback) {
        this.serverConnectCallback = serverConnectCallback;
    }

    synchronized private HashMap<LanguageSetting, HashSet<String>> createDictionaries() {
        HashMap<LanguageSetting, HashSet<String>> allDictionaries = new HashMap<>();
        for (LanguageSetting language : LanguageSetting.values()) {
            HashSet<String> newHashSetDict = new HashSet<>();
            Path dictPath;
            try {
                dictPath = Paths.get(Objects.requireNonNull(ServerLogicImpl.class.getResource("/" + language.toString().toLowerCase() + "_dict.txt")).toURI()).toAbsolutePath();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            try (BufferedReader reader = Files.newBufferedReader(dictPath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    newHashSetDict.add(line.trim().toUpperCase());
                }
            } catch (IOException e) {
                e.printStackTrace(System.err);
                throw new RuntimeException();
            }
            allDictionaries.put(language, newHashSetDict);
        }
        return allDictionaries;
    }


    @Override
    synchronized public ReturnValues.ReturnStatistics getUserStatistics(String username) {
        if (username == null || this.scrabbleData == null) {
            return new ReturnValues.ReturnStatistics(ReturnValues.ReturnStatisticsState.FAILURE, null);
        }
        return new ReturnValues.ReturnStatistics(ReturnValues.ReturnStatisticsState.SUCCESSFUL,
                this.scrabbleData.getUserStatistics(username));
    }

    @Override
    synchronized public ReturnValues.ReturnCreateSession createSession(LanguageSetting languageSetting, String username) {
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

        this.serverConnectCallback.usersInSession(newSession.getUserUsernames().toArray(new String[0]));

        return new ReturnValues.ReturnCreateSession(ReturnValues.ReturnCreateSessionState.SUCCESSFUL, generatedGameId);
    }

    synchronized private int generateGameId() {
        Random r = new Random();
        int newGameId;
        do {
            newGameId = r.nextInt(GAME_ID_LIMIT) + 1;
        } while (this.mapGameIdToSession.containsKey(newGameId));

        return newGameId;
    }

    @Override
    synchronized public ReturnValues.ReturnJoinSession joinSession(int gameId, String username) {
        if (username == null) {
            return ReturnValues.ReturnJoinSession.FAILURE;
        }
        if (gameId < 0 || gameId > GAME_ID_LIMIT || !this.mapGameIdToSession.containsKey(gameId)) {
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

    synchronized private Session getSessionWithUsername(String username) {
        return this.mapGameIdToSession.get(this.mapUsernameToGameId.get(username));
    }

    @Override
    synchronized public ReturnValues.ReturnStartGame startGame(String username) {
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

    synchronized private void sendGameData(Session session) {
        session.getUserUsernames().forEach(player ->
                serverConnectCallback.sendGameData(
                        player,
                        session.getRackTilesWithUsername(player),
                        session.getSwapTilesWithUsername(player),
                        session.getGameData())
        );
        // session.scrabbleGame.printGameBoard();
    }

    @Override
    synchronized public ReturnValues.ReturnSelectAction selectAction(ActionState actionState, String username) {
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

        scrabbleGame.setGameStateByActionState(actionState);

        sendGameData(session);
        return ReturnValues.ReturnSelectAction.SUCCESSFUL;
    }

    @Override
    synchronized public ReturnValues.ReturnPlaceTile placeTile(TileWithPosition tileWithPosition, String username) {
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

        if (!scrabbleGame.playerHasRackTileWithThisLetter(tileWithPosition.letter())) {
            return ReturnValues.ReturnPlaceTile.TILE_NOT_ON_RACK;
        }

        if (!scrabbleGame.isSquareFree(tileWithPosition)) {
            return ReturnValues.ReturnPlaceTile.SQUARE_OCCUPIED;
        }

        // Special case: First tile has to be placed on middle square
        if (scrabbleGame.isSquareFree(new TileWithPosition(' ', 8, 8)) &&
                (tileWithPosition.row() != 8 && tileWithPosition.column() != 8)) {
            return ReturnValues.ReturnPlaceTile.POSITION_NOT_ALLOWED;
        }
        // Special case: During first turn, game must not check if tile as neighbours
        if (scrabbleGame.isSquareFree(new TileWithPosition(' ', 8, 8)) &&
                (tileWithPosition.row() == 8 && tileWithPosition.column() == 8)) {
            scrabbleGame.placeTile(tileWithPosition);
            this.sendGameData(session);
            return ReturnValues.ReturnPlaceTile.SUCCESSFUL;
        }

        if (!scrabbleGame.hasNeighbour(tileWithPosition)) {
            return ReturnValues.ReturnPlaceTile.POSITION_NOT_ALLOWED;
        }

        scrabbleGame.placeTile(tileWithPosition);

        this.sendGameData(session);
        return ReturnValues.ReturnPlaceTile.SUCCESSFUL;
    }

    @Override
    synchronized public ReturnValues.ReturnSwapTile swapTile(char letter, String username) {
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

        if (!scrabbleGame.playerHasRackTileWithThisLetter(letter)) {
            return ReturnValues.ReturnSwapTile.TILE_NOT_ON_RACK;
        }

        scrabbleGame.swapTile(letter);

        this.sendGameData(session);

        return ReturnValues.ReturnSwapTile.SUCCESSFUL;
    }

    @Override
    synchronized public ReturnValues.ReturnEndTurn endTurn(String username) {
        if (username == null) {
            return ReturnValues.ReturnEndTurn.FAILURE;
        }
        Session session = this.getSessionWithUsername(username);
        if (!session.hasGameStarted()) {
            return ReturnValues.ReturnEndTurn.FAILURE;
        }

        ScrabbleGame scrabbleGame = session.getScrabbleGame();

        if (!(scrabbleGame.getGameState() == GameState.SWAP ||
                scrabbleGame.getGameState() == GameState.PLACE ||
                scrabbleGame.getGameState() == GameState.PASS) ||
                !Objects.equals(scrabbleGame.getCurrentPlayerUsername(), username)) {
            return ReturnValues.ReturnEndTurn.FAILURE;
        }

        if (scrabbleGame.getPlayerRackTiles(username).length == 7) {
            scrabbleGame.setGameStateByActionState(ActionState.PASS);
        }


        switch (scrabbleGame.getGameState()) {
            case SWAP:
                scrabbleGame.endTurnSwap();
                this.sendGameData(session);
                return ReturnValues.ReturnEndTurn.SUCCESSFUL;
            case PLACE:
                scrabbleGame.endTurnPlace();

                session.getUserUsernames().stream()
                        .filter(player -> !player.equals(scrabbleGame.getCurrentPlayerUsername()))
                        .forEach(player ->
                                serverConnectCallback.vote(player, scrabbleGame.getPlacedWords().toArray(String[]::new))
                        );
                scrabbleGame.setPlayerStateWithPlayerVote(PlayerVote.CONFIRMED, username);
                return ReturnValues.ReturnEndTurn.SUCCESSFUL;
            case PASS:
                if (scrabbleGame.getPassCounter() < session.getNumberOfUsers() * 2 - 1) {
                    scrabbleGame.endTurnPass();
                    this.sendGameData(session);
                } else {
                    this.endGame(session, scrabbleGame);
                }
                return ReturnValues.ReturnEndTurn.SUCCESSFUL;
        }
        return ReturnValues.ReturnEndTurn.FAILURE;
    }

    @Override
    synchronized public ReturnValues.ReturnSendPlayerVote sendPlayerVote(PlayerVote playerVote, String username) {
        if (playerVote == null) {
            return ReturnValues.ReturnSendPlayerVote.FAILURE;
        }

        Session session = getSessionWithUsername(username);
        ScrabbleGame scrabbleGame = session.getScrabbleGame();

        if (scrabbleGame.getGameState() != GameState.VOTE) {
            return ReturnValues.ReturnSendPlayerVote.FAILURE;
        }

        scrabbleGame.setPlayerStateWithPlayerVote(playerVote, username);


        if (scrabbleGame.getPlayerStates().contains(PlayerState.REJECTED)) {
            if (this.checkPlacedWords(session)) {
                // Word got rejected, Word is in Dictionary
                scrabbleGame.endVotePlacedWordsOk();
            } else {
                // Word got rejected, Word is NOT in Dictionary
                scrabbleGame.endVotePlacedWordsNotOk();
            }
            this.sendGameData(session);
            return ReturnValues.ReturnSendPlayerVote.SUCCESSFUL;
        }

        if (!scrabbleGame.getPlayerStates().contains(PlayerState.NOT_VOTED)) {
            this.sendGameData(session);
            return ReturnValues.ReturnSendPlayerVote.SUCCESSFUL;
        }

        // No one challenged the word
        scrabbleGame.endVotePlacedWordsOk();
        if (scrabbleGame.getBagSize() == 0 && scrabbleGame.playerHasNoRackTilesLeft()) {
            this.endGame(session, scrabbleGame);
        }
        this.sendGameData(session);
        return ReturnValues.ReturnSendPlayerVote.SUCCESSFUL;
    }

    synchronized void endGame(Session session, ScrabbleGame scrabbleGame) {
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

    synchronized boolean checkPlacedWords(Session session) {
        return this.allDictionaries.get(session.languageSetting).containsAll(session.getScrabbleGame().getPlacedWords());
    }

    @Override
    synchronized public void informAboutUserLogin(String username) {
        if (mapUsernameToGameId.containsKey(username)) {
            Session session = getSessionWithUsername(username);
            if (session.hasGameStarted()) {
                this.sendGameData(getSessionWithUsername(username));
            } else {
                serverConnectCallback.usersInSession(session.getUserUsernames().toArray(new String[0]));
            }
        }
    }

    @Override
    synchronized public void setServerState() {
        Path sessionPath;
        Path gamePath;
        try {
            sessionPath = Paths.get(Objects.requireNonNull(ServerLogicImpl.class.getResource("/session.csv")).toURI()).toAbsolutePath();
            gamePath = Paths.get(Objects.requireNonNull(ServerLogicImpl.class.getResource("/scrabble.csv")).toURI()).toAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader reader = Files.newBufferedReader(sessionPath)) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(",");
                int gameId = Integer.parseInt(parts[0].trim());
                LanguageSetting languageSetting = LanguageSetting.valueOf(parts[1].trim().toUpperCase());
                List<String> usernames = Arrays.stream(parts)
                        .skip(2)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList();

                Session newSession = new Session(usernames.getFirst(), gameId, languageSetting);
                mapUsernameToGameId.put(usernames.getFirst(), gameId);
                mapGameIdToSession.put(gameId, newSession);
                usernames.forEach(user -> {
                    this.joinSession(gameId, user);
                    mapUsernameToGameId.put(user, gameId);
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader reader = Files.newBufferedReader(gamePath)) {
            reader.readLine();
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(",", -1);
                int gameId = Integer.parseInt(parts[0].trim());
                GameState gameState = GameState.valueOf(parts[1].trim().toUpperCase());
                List<String> bag = parts[2].isEmpty() ? new ArrayList<>() : Arrays.asList(parts[2].split(";"));

                List<String> fixedTiles = parts[3].isEmpty() ? new ArrayList<>() : Arrays.asList(parts[3].split(";"));
                List<String> movedTiles = parts[4].isEmpty() ? new ArrayList<>() : Arrays.asList(parts[4].split(";"));

                List<Integer> scores = Arrays.stream(parts[5].split(";"))
                        .map(Integer::parseInt)
                        .toList();

                List<String> rackTiles = parts[6].isEmpty() ? new ArrayList<>() : Arrays.asList(parts[6].split(";"));
                List<String> swapTiles = parts[7].isEmpty() ? new ArrayList<>() : Arrays.asList(parts[7].split(";"));

                Session session = this.mapGameIdToSession.get(gameId);
                session.scrabbleGame = new ScrabbleGame(session.users, session.languageSetting, gameState, bag, fixedTiles, movedTiles, scores, rackTiles, swapTiles);

                this.sendGameData(session);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
