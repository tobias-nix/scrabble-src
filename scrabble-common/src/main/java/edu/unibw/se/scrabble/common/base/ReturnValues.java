package edu.unibw.se.scrabble.common.base;

public class ReturnValues {
    private ReturnValues() {

    }

    public enum ReturnLoginUser {
        NETWORK_FAILURE, DATABASE_FAILURE, USERNAME_NOT_IN_DATABASE, WRONG_PASSWORD, SUCCESSFUL
    }

    public enum ReturnRegisterUser {
        NETWORK_FAILURE, DATABASE_FAILURE, USERNAME_ALREADY_EXISTS, SUCCESSFUL
    }

    public record ReturnStatistics(ReturnStatisticsState state, Statistics userStatistics) {
    }

    public enum ReturnStatisticsState {
        DATABASE_FAILURE, SUCCESSFUL
    }

    public record ReturnCreateSession(ReturnCreateSessionState state, int gameID) {
    }

    public enum ReturnCreateSessionState {
        NETWORK_FAILURE, USER_ALREADY_IN_SESSION, SESSION_LIMIT_REACHED, SUCCESSFUL
    }

    public enum ReturnJoinSession {
        NETWORK_FAILURE, GAME_ID_INVALID, TOO_MANY_USERS, SUCCESSFUL
    }

    public enum ReturnStartGame {
        NETWORK_FAILURE, PLAYER_ALONE_IN_SESSION, SUCCESSFUL
    }

    public enum ReturnSelectAction {
        NETWORK_FAILURE, LESS_THAN_SEVEN_TILES_IN_BAG, SUCCESSFUL
    }

    public enum ReturnPlaceTile {
        NETWORK_FAILURE, TILE_NOT_ON_RACK, SQUARE_OCCUPIED, POSITION_NOT_ALLOWED, SUCCESSFUL
    }

    public enum ReturnSwapTile {
        NETWORK_FAILURE, TILE_NOT_ON_RACK, SUCCESSFUL
    }

    public enum ReturnEndTurn {
        NETWORK_FAILURE, SUCCESSFUL
    }

    public enum ReturnPlayerVote {
        NETWORK_FAILURE, REJECTED, CONFIRMED
    }
}
