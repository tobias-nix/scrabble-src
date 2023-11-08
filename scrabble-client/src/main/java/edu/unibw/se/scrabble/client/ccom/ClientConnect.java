package edu.unibw.se.scrabble.client.ccom;

import edu.unibw.se.scrabble.common.base.ActionState;
import edu.unibw.se.scrabble.common.base.TileWithPosition;

public interface ClientConnect {
    void setClientConnectCallback(ClientConnectCallback clientConnectCallback);

    // Fürs Hauptmenü und für Game_Over und zurück zum Hauptmenü: getUserStatistics()

    ReturnCreateSession createSession();
    record ReturnCreateSession(ReturnCreateSessionState state, int gameID) {}
    enum ReturnCreateSessionState {
        NETWORK_FAILURE,
        USER_ALREADY_IN_SESSION,
        SESSION_LIMIT_REACHED,
        SUCCESSFUL
    }

    ReturnJoinSession joinSession(int gameID);
    enum ReturnJoinSession {
        NETWORK_FAILURE, //Remote Failure try and catch
        // DATABASE_FAILURE,
        GAME_ID_INVALID,
        TOO_MANY_USERS,
        SUCCESSFUL
    }

    ReturnStartGame startGame();
    enum ReturnStartGame {
        NETWORK_FAILURE,
        YOU_ARE_ALONE_SO_TERRIBLY_ALONE_YOU_LOSER_GET_A_LIFE_AND_STOP_PLAYING_SCRABBLE, //player is alone is session
        SUCCESSFUL
    }

    ReturnSelectAction selectAction(ActionState actionState);
    enum ReturnSelectAction {
        NETWORK_FAILURE,
        LESS_THAN_SEVEN_TILES_IN_BAG,
        SUCCESSFUL
    }

    // ReturnPlaceTiles placeTiles(char letter, int row, int column); //Alternative record für alle 3
    ReturnPlaceTiles placeTiles(TileWithPosition tileWithPosition);
    enum ReturnPlaceTiles {
        NETWORK_FAILURE,
        TILE_NOT_ON_RACK,
        SQUARE_OCCUPIED,
        POSITION_NOT_ALLOWED,
        SUCCESSFUL
    }

    ReturnEndTurn endTurn();
    enum ReturnEndTurn {
        NETWORK_FAILURE,
        SUCCESSFUL
    }
}
