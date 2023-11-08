package edu.unibw.se.scrabble.server.logic;

import edu.unibw.se.scrabble.common.base.TileWithPosition;
import edu.unibw.se.scrabble.common.scom.ToServer;

public interface ServerConnect {
    void setServerConnectCallback(ServerConnectCallback serverConnectCallback);

    ReturnJoinSession joinSession(int gameID, String name);

    enum ReturnJoinSession {
        DATABASE_FAILURE,
        GAMEID_INVALID,
        TOO_MANY_USERS,
        SUCCESSFUL
    }

    ReturnPlaceTiles placeTiles(TileWithPosition tileWithPosition, String name);

    enum ReturnPlaceTiles {
        TILE_NOT_ON_RACK,
        SQUARE_OCCUPIED,
        POSITION_NOT_ALLOWED,
        SUCCESSFUL
    }
}
