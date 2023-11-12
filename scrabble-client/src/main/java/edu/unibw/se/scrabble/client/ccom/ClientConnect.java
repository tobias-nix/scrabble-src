package edu.unibw.se.scrabble.client.ccom;

import edu.unibw.se.scrabble.common.base.ActionState;
import edu.unibw.se.scrabble.common.base.Statistics;
import edu.unibw.se.scrabble.common.base.TileWithPosition;

/**
 * Interface to provide methods for client to server communication
 */

public interface ClientConnect {

    /**
     * Add ClientConnectCallback interface to ClientConnect so that clientCommunication can send data to the Client
     *
     * @param clientConnectCallback the information to be sent to the client back
     */
    void setClientConnectCallback(ClientConnectCallback clientConnectCallback);

    /**
     * Authenticates the users login credentials by sending it to the server.
     * <p>
     * Never returns {@code null}
     *
     * @param username              the user's username
     * @param password              the user's password
     * @param clientConnectCallback the information to be sent to the client back
     * @return {@link ReturnLogin} enum type, depending on error type or success.
     */
    ReturnLogin loginUser(String username, String password, ClientConnectCallback clientConnectCallback);

    enum ReturnLogin {
        NETWORK_FAILURE,
        DATABASE_FAILURE,
        USERNAME_NOT_IN_DATABASE,
        WRONG_PASSWORD,
        SUCCESSFUL
    }

    /**
     * Creates new user dataset by sending it to the server.
     * <p>
     * Never returns {@code null}
     *
     * @param username              the user's username
     * @param password              the user's password
     * @param clientConnectCallback the information to be sent to the client back
     * @return {@link ReturnRegister} enum type, depending on error type or success.
     */
    ReturnRegister registerUser(String username, String password, ClientConnectCallback clientConnectCallback);

    enum ReturnRegister {
        NETWORK_FAILURE,
        DATABASE_FAILURE,
        USERNAME_ALREADY_EXISTS,
        SUCCESSFUL
    }

    /**
     * Returns statistics of the user.
     * <p>
     * Never returns {@code null}.
     *
     * @return the interface {@link ReturnStatisticsState} and the class {@link Statistics}
     */
    ReturnStatistics getUserStatistics();
    record ReturnStatistics(ReturnStatisticsState state, Statistics userStatistics) {}
    enum ReturnStatisticsState {
        NETWORK_FAILURE,
        DATABASE_FAILURE,
        SUCCESSFUL
    }

    /**
     * Creates a new Session.
     * <p>
     * Never returns {@code null}.
     *
     * @return the interface {@link ReturnCreateSessionState} and the integer gameId
     */
    ReturnCreateSession createSession();
    record ReturnCreateSession(ReturnCreateSessionState state, int gameID) {}
    enum ReturnCreateSessionState {
        NETWORK_FAILURE,
        USER_ALREADY_IN_SESSION,
        SESSION_LIMIT_REACHED,
        SUCCESSFUL
    }

    /**
     * Adds the client to an existing session.
     * <p>
     * Never returns {@code null}.
     *
     * @param gameID the gameId of the session the client will join
     * @return {@link ReturnJoinSession} enum type, depending on error type or success.
     */
    ReturnJoinSession joinSession(int gameID);
    enum ReturnJoinSession {
        NETWORK_FAILURE, //Remote Failure try and catch
        // DATABASE_FAILURE,
        GAME_ID_INVALID,
        TOO_MANY_USERS,
        SUCCESSFUL
    }


    /**
     * Starts a new game from a session.
     * <p>
     * Never returns {@code null}.
     *
     * @return {@link ReturnStartGame} enum type, depending on error type or success.
     */
    ReturnStartGame startGame();
    enum ReturnStartGame {
        NETWORK_FAILURE,
        YOU_ARE_ALONE_SO_TERRIBLY_ALONE_YOU_LOSER_GET_A_LIFE_AND_STOP_PLAYING_SCRABBLE, //player is alone is session
        SUCCESSFUL
    }

    /**
     * The client can choose between three actions: "PASS", "SWAP", "PLACE".
     * <p>
     * Never returns {@code null}.
     *
     * @param actionState the selected action of the client
     * @return {@link ReturnSelectAction} enum type, depending on error type or success.
     */
    ReturnSelectAction selectAction(ActionState actionState);
    enum ReturnSelectAction {
        NETWORK_FAILURE,
        LESS_THAN_SEVEN_TILES_IN_BAG,
        SUCCESSFUL
    }

    // ReturnPlaceTiles placeTiles(char letter, int row, int column); //Alternative record für alle 3

    /**
     * The client puts a tile on the board.
     * <p>
     * Never returns {@code null}
     *
     * @param tileWithPosition the tile with the position information
     * @return {@link ReturnPlaceTiles} enum type, depending on error type or success.
     */
    ReturnPlaceTiles placeTiles(TileWithPosition tileWithPosition);
    enum ReturnPlaceTiles {
        NETWORK_FAILURE,
        TILE_NOT_ON_RACK,
        SQUARE_OCCUPIED,
        POSITION_NOT_ALLOWED,
        SUCCESSFUL
    }

    /**
     * A client completes his turn.
     * <p>
     * Never returns {@code null}
     *
     * @return {@link ReturnEndTurn} enum type, depending on error type or success.
     */
    ReturnEndTurn endTurn();
    enum ReturnEndTurn {
        NETWORK_FAILURE,
        SUCCESSFUL
    }
}
