package edu.unibw.se.scrabble.common.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class GameData implements Serializable {
    public final int gameID;
    public final List<String> usernames;
    public final String currentPlayer;
    public final List<Integer> score;
    public final List<PlayerState> playerVotes;
    public final List<Integer> countRackTiles;
    public final List<Integer> countSwapTiles;
    public final List<TileWithPosition> tilesWithPositions;
    public final int bagSize;
    public final GameState state;
    public static GameData TEST_GAMEDATA =
            new GameData(12345,
                    new ArrayList<String>(List.of("karl", "paul", "berta", "anna")),
                    "paul",
                    new ArrayList<>(List.of(10, 20, 30, 40)),
                    new ArrayList<>(List.of(PlayerState.NOT_VOTED, PlayerState.NOT_VOTED,
                            PlayerState.NOT_VOTED, PlayerState.NOT_VOTED)),
                    new ArrayList<>(List.of(7, 7, 7, 7)),
                    new ArrayList<>(List.of(0, 0, 0, 0)),
                    new ArrayList<>(List.of(new TileWithPosition('A', 8, 8))),
                    50,
                    GameState.PLAY);

    public GameData(int gameID, List<String> usernames, String currentPlayer, List<Integer> score,
                    List<PlayerState> playerVotes, List<Integer> countRackTiles, List<Integer> countSwapTiles,
                    List<TileWithPosition> tilesWithPositions, int bagSize, GameState state) {
        this.gameID = gameID;
        this.usernames = usernames;
        this.currentPlayer = currentPlayer;
        this.score = score;
        this.playerVotes = playerVotes;
        this.countRackTiles = countRackTiles;
        this.countSwapTiles = countSwapTiles;
        this.tilesWithPositions = tilesWithPositions;
        this.bagSize = bagSize;
        this.state = state;
    }

    @Override
    public String toString() {
        return gameID + " " + usernames + " " + currentPlayer + " " + score + " " + playerVotes + " " + countRackTiles
                + " " + countSwapTiles + " " + tilesWithPositions + " " + bagSize + " " + state;
    }
}