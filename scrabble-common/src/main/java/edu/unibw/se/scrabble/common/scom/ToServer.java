package edu.unibw.se.scrabble.common.scom;

import edu.unibw.se.scrabble.common.base.TileWithPosition;

import java.rmi.Remote;

public interface ToServer extends Remote {
    ReturnJoinSession joinSession(int gameID);

    enum ReturnJoinSession {
        GAMEID_INVALID,
        TOO_MANY_USERS,
        SUCCESSFUL
    }

    ReturnPlaceTiles placeTiles(TileWithPosition tileWithPosition);

    enum ReturnPlaceTiles {
        TILE_NOT_ON_RACK,
        SQUARE_OCCUPIED,
        POSITION_NOT_ALLOWED,
        SUCCESSFUL
    }
}
