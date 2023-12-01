package edu.unibw.se.scrabble.common.base;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public final class GameData implements Serializable {
    public final int gameID;
    public final List<String> usernames;
    public final String currentPlayer;
    public final List<Integer> score;
    public final List<PlayerState> playerVotes;
    public final List<Integer> countRackTiles;
    public final List<Integer> countSwapTiles;
    public final List<TileWithPosition> square;
    public final int bagSize;
    public final GameState state;
    public static GameData TEST_GAMEDATA =
            new GameData(12345,
                    new String[]{"karl", "paul", "berta", "anna"},
                    "paul", new int[]{10, 20, 30, 40},
                    new PlayerState[]{PlayerState.NOT_VOTED, PlayerState.NOT_VOTED,
                            PlayerState.NOT_VOTED, PlayerState.NOT_VOTED},
                    new int[]{7, 7, 7, 7},
                    new int[]{0, 0, 0, 0},
                    new TileWithPosition[]{new TileWithPosition('a', 8, 8)},
                    50,
                    GameState.PLAY);

    public GameData(int gameID, String[] usernames, String currentPlayer, int[] score, PlayerState[] playerVotes,
                    int[] countRackTiles, int[] countSwapTiles, TileWithPosition[] square,
                    int bagSize, GameState state) {
        this.gameID = gameID;
        this.usernames = List.of(usernames);
        this.currentPlayer = currentPlayer;
        this.score = Arrays.stream(score).boxed().toList();
        this.playerVotes = List.of(playerVotes);
        this.countRackTiles = Arrays.stream(countRackTiles).boxed().toList();
        this.countSwapTiles = Arrays.stream(countSwapTiles).boxed().toList();
        this.square = List.of(square);
        this.bagSize = bagSize;
        this.state = state;
    }
}