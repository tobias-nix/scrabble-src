package edu.unibw.se.scrabble.common.base;

import java.io.Serializable;

public class ReturnValues {
    private ReturnValues() {

    }

    public enum ReturnLoginUser {
        NETWORK_FAILURE, DATABASE_FAILURE, USERNAME_NOT_IN_DATABASE, WRONG_PASSWORD, INVALID_USERNAME,
        INVALID_PASSWORD, FAILURE, SUCCESSFUL
    }

    public enum ReturnRegisterUser {
        NETWORK_FAILURE, DATABASE_FAILURE, USERNAME_ALREADY_EXISTS, INVALID_USERNAME, INVALID_PASSWORD, FAILURE,
        SUCCESSFUL
    }

    public record ReturnStatistics(ReturnStatisticsState state, Statistics userStatistics) implements Serializable {
    }

    public enum ReturnStatisticsState {
        DATABASE_FAILURE, NETWORK_FAILURE, FAILURE, SUCCESSFUL
    }

    public record ReturnCreateSession(ReturnCreateSessionState state, int gameID) implements Serializable {
    }

    public enum ReturnCreateSessionState {
        NETWORK_FAILURE, USER_ALREADY_IN_SESSION, SESSION_LIMIT_REACHED, FAILURE, SUCCESSFUL
    }

    public enum ReturnJoinSession {
        NETWORK_FAILURE, USER_ALREADY_IN_SESSION, GAME_ID_INVALID, TOO_MANY_USERS, FAILURE, SUCCESSFUL
    }

    public enum ReturnStartGame {
        NETWORK_FAILURE, USER_NOT_THE_HOST, USER_ALONE_IN_SESSION, FAILURE, SUCCESSFUL
    }

    public enum ReturnSelectAction {
        NETWORK_FAILURE, LESS_THAN_SEVEN_TILES_IN_BAG, FAILURE, SUCCESSFUL
    }

    public enum ReturnPlaceTile {
        NETWORK_FAILURE, TILE_NOT_ON_RACK, SQUARE_OCCUPIED, POSITION_NOT_ALLOWED, FAILURE, SUCCESSFUL
    }

    public enum ReturnSwapTile {
        NETWORK_FAILURE, TILE_NOT_ON_RACK, FAILURE, SUCCESSFUL
    }

    public enum ReturnEndTurn {
        NETWORK_FAILURE, FAILURE, SUCCESSFUL
    }

    public enum ReturnSendPlayerVote {
        FAILURE, NETWORK_FAILURE, SUCCESSFUL
    }
}
