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

    public GameData(int gameID, String[] usernames, String currentPlayer, int[] score, PlayerState[] playerVotes,
                    int[] countRackTiles, int[] countSwapTiles, TileWithPosition[] square,
                    int bagSize, GameState state) {
        this.gameID = gameID;
        // Redundant usage of the unmodifiableList Wrapper: Collections.unmodifiableList(List.of(usernames));
        // List.of already returns unmodifiable list
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

    /*
    public static void main(String[] args) {
        GameData gameData = new GameData(
                123,
                new String[]{"abc", "def"},
                "abc",
                new int[]{2, 3},
                new PlayerState[]{PlayerState.NOT_VOTED, PlayerState.CONFIRMED},
                new int[]{2, 3},
                new int[]{2, 3},
                new TileWithPosition[]{new TileWithPosition('a', 5, 7)}, 9,
                GameState.PASS);

        // gameData.countRackTiles.add(7);
        gameData.usernames.add("ingo");
    }
    */

}