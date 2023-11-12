package edu.unibw.se.scrabble.server.logic;

import edu.unibw.se.scrabble.common.base.ActionState;
import edu.unibw.se.scrabble.common.base.Statistics;
import edu.unibw.se.scrabble.common.base.TileWithPosition;
import edu.unibw.se.scrabble.common.scom.ToServer;

/**
 * Interface ServerConnect which provides methods to play the game scrabble or to navigate in the main menu.
 */
public interface ServerConnect {
    /**
     * Add ServerConnectCallback Interface to ServerConnect so Server can send data back to clients.
     *
     * @param serverConnectCallback the interface {@link ServerConnectCallback}
     */
    void setServerConnectCallback(ServerConnectCallback serverConnectCallback);

    /**
     * Gets statistics of a given username from the database.
     *
     * @param username the user's username
     * @return record {@link ReturnStatistics} consisting of {@link ReturnStatisticsState}return state and {@link Statistics} Object
     */
    ReturnStatistics getUserStatistics(String username);
    record ReturnStatistics(ReturnStatisticsState state, Statistics userStatistics) {}
    enum ReturnStatisticsState {
        DATABASE_FAILURE,
        SUCCESSFUL
    }

    /**
     * Creates a new session for the player if we have less than 10 aktive sessions.
     * <p>
     * Never returns {@code null}.
     *
     * @param username the user's username
     * @return {@link ReturnCreateSessionState} enum type, depending on error type or success.
     */
    ReturnCreateSession createSession(String username);
    record ReturnCreateSession(ReturnCreateSessionState state, int gameID) {}
    enum ReturnCreateSessionState {
        USER_ALREADY_IN_SESSION,
        SESSION_LIMIT_REACHED,
        SUCCESSFUL
    }

    /**
     * Connects the player to the session if the gameID exists and it is enough space.
     * <p>
     * Never returns {@code null}.
     *
     * @param gameID the ID of the game
     * @param username the user's username
     * @return {@link ReturnJoinSession} enum type, depending on error type or success.
     */
    ReturnJoinSession joinSession(int gameID, String username);
    enum ReturnJoinSession {
        GAME_ID_INVALID,
        TOO_MANY_USERS,
        SUCCESSFUL
    }

    /**
     * Starts the game if it has at least 2 player in the session.
     * <p>
     * Never returns {@code null}.
     *
     * @param username the user's username
     * @return {@link ReturnStartGame} enum type, depending on error type or success.
     */
    ReturnStartGame startGame(String username);
    enum ReturnStartGame {
        PLAYER_ALONE_IN_SESSION,
        SUCCESSFUL
    }

    /**
     * Selects one of the action States.
     * <p>
     * Never returns {@code null}.
     *
     * @param actionState includes SWAP, PASS and PLACE
     * @param username the user's username
     * @return {@link ReturnSelectAction} enum type,  depending on error type or success.
     */
    ReturnSelectAction selectAction(ActionState actionState, String username);
    enum ReturnSelectAction {
        LESS_THAN_SEVEN_TILES_IN_BAG,
        SUCCESSFUL
    }

    /**
     * Places a tile of a player if it's a valid position.
     * <p>
     * Never returns {@code null}.
     *
     * @param tileWithPosition a tile with the exact position on the board (row and column)
     * @param username the user's username
     * @return {@link ReturnPlaceTiles} enum type, depending on error type or success.
     */
    ReturnPlaceTiles placeTiles(TileWithPosition tileWithPosition, String username);
    enum ReturnPlaceTiles {
        TILE_NOT_ON_RACK,
        SQUARE_OCCUPIED,
        POSITION_NOT_ALLOWED,
        SUCCESSFUL
    }

    /**
     * Ends the turn of a player.
     * <p>
     * Never returns {@code null}.
     *
     * @param username the user's username
     * @return {@link ReturnEndTurn} enum type successful
     */
    ReturnEndTurn endTurn(String username);
    enum ReturnEndTurn {
        SUCCESSFUL
    }
}
