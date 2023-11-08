package edu.unibw.se.scrabble.common.scom;

import edu.unibw.se.scrabble.common.base.ActionState;
import edu.unibw.se.scrabble.common.base.TileWithPosition;

import java.rmi.Remote;

public interface ToServer extends Remote {
    ReturnCreateSession createSession();
    record ReturnCreateSession(ReturnCreateSessionState state, int gameID) {}
    enum ReturnCreateSessionState {
        USER_ALREADY_IN_SESSION,
        SESSION_LIMIT_REACHED,
        SUCCESSFUL
    }

    ReturnJoinSession joinSession(int gameID);
    enum ReturnJoinSession {
        GAME_ID_INVALID,
        TOO_MANY_USERS,
        SUCCESSFUL
    }

    ReturnStartGame startGame();
    enum ReturnStartGame {
        NETWORK_FAILURE,
        PLAYER_ALONE_IN_SESSION,
        SUCCESSFUL
    }

    ReturnSelectAction selectAction(ActionState actionState);
    enum ReturnSelectAction {
        LESS_THAN_SEVEN_TILES_IN_BAG,
        SUCCESSFUL
    }

    ReturnPlaceTiles placeTiles(TileWithPosition tileWithPosition);
    enum ReturnPlaceTiles {
        TILE_NOT_ON_RACK,
        SQUARE_OCCUPIED,
        POSITION_NOT_ALLOWED,
        SUCCESSFUL
    }

    ReturnEndTurn endTurn();
    enum ReturnEndTurn {
        SUCCESSFUL
    }
}
