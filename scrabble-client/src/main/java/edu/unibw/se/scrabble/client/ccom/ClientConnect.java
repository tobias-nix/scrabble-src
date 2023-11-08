package edu.unibw.se.scrabble.client.ccom;

import edu.unibw.se.scrabble.common.base.TileWithPosition;

public interface ClientConnect {
    void setClientConnectCallback(ClientConnectCallback clientConnectCallback);

    ReturnJoinSession joinSession(int gameID);

    enum ReturnJoinSession {
        NETWORK_FAILURE, //Remote Failure try and catch
        // DATABASE_FAILURE,
        GAMEID_INVALID,
        TOO_MANY_USERS,
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
}
