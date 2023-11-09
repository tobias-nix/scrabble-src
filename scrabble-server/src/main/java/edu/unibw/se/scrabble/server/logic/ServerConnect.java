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

    ReturnCreateSession createSession(String username);
    record ReturnCreateSession(ReturnCreateSessionState state, int gameID) {}
    enum ReturnCreateSessionState {
        USER_ALREADY_IN_SESSION,
        SESSION_LIMIT_REACHED,
        SUCCESSFUL
    }

    ReturnJoinSession joinSession(int gameID, String username);
    enum ReturnJoinSession {
        GAME_ID_INVALID,
        TOO_MANY_USERS,
        SUCCESSFUL
    }

    ReturnStartGame startGame(String username);
    enum ReturnStartGame {
        PLAYER_ALONE_IN_SESSION,
        SUCCESSFUL
    }

    ReturnSelectAction selectAction(ActionState actionState, String username);
    enum ReturnSelectAction {
        LESS_THAN_SEVEN_TILES_IN_BAG,
        SUCCESSFUL
    }

    ReturnPlaceTiles placeTiles(TileWithPosition tileWithPosition, String username);
    enum ReturnPlaceTiles {
        TILE_NOT_ON_RACK,
        SQUARE_OCCUPIED,
        POSITION_NOT_ALLOWED,
        SUCCESSFUL
    }

    ReturnEndTurn endTurn(String username);
    enum ReturnEndTurn {
        SUCCESSFUL
    }
}
