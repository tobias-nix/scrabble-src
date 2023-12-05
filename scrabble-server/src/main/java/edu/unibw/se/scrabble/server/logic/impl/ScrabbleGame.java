package edu.unibw.se.scrabble.server.logic.impl;

import edu.unibw.se.scrabble.common.base.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ScrabbleGame {
    private final ArrayList<Player> players;
    private Player currentPlayer;
    private GameState gameState;
    private final ArrayList<ScrabbleTile> bag;
    private final ArrayList<TileWithPosition> placedTilesForSendGameData = new ArrayList<>();
    private final ScrabbleBoard scrabbleBoard = new ScrabbleBoard();
    private int passCounter = 0;

    static class Player {
        String username;
        int score = 0;
        ArrayList<ScrabbleTile> rackTiles = new ArrayList<>();
        ArrayList<ScrabbleTile> swapTiles = new ArrayList<>();
        PlayerState playerState = PlayerState.NOT_VOTED;

        Player(String username) {
            this.username = username;
        }
    }


    ScrabbleGame(ArrayList<String> usernames, LanguageSetting languageSetting) {
        this.players = new ArrayList<>(usernames.size());
        usernames.forEach(username -> this.players.add(new Player(username)));
        this.currentPlayer = players.getFirst();
        this.gameState = GameState.PLAY;
        this.bag = createBagFromLanguageSetting(languageSetting);
        players.forEach(this::drawFromBag);
    }


    Player getPlayerByUsername(String username) {
        return players.stream()
                .filter(player -> player.username.equals(username))
                .findFirst()
                .orElse(null); // Player not found
    }

    private ArrayList<ScrabbleTile> createBagFromLanguageSetting(LanguageSetting languageSetting) {
        String filePath = switch (languageSetting) {
            case LanguageSetting.ENGLISH ->
                    Objects.requireNonNull(ScrabbleBoard.class.getResource("/letterSetEnglish.csv")).getFile();
            case LanguageSetting.GERMAN ->
                    Objects.requireNonNull(ScrabbleBoard.class.getResource("/letterSetGerman.csv")).getFile();
        };

        ArrayList<ScrabbleTile> newBag = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineAsArray = line.split(",");
                Arrays.stream(lineAsArray).forEach(tileInformationAsArray -> {
                    String[] tileInformationSplit = tileInformationAsArray.split(";");

                    for (int i = 0; i < Integer.parseInt(tileInformationSplit[2]); i++) {
                        newBag.add(new ScrabbleTile(
                                tileInformationSplit[0].charAt(0),
                                Integer.parseInt(tileInformationSplit[1])
                        ));
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        Collections.shuffle(newBag);
        return newBag;
    }

    void drawFromBag(Player player) {
        while (player.rackTiles.size() < 7 && !this.bag.isEmpty()) {
            player.rackTiles.add(this.bag.removeFirst());
        }
    }

    String getCurrentPlayerUsername() {
        return this.currentPlayer.username;
    }

    void setGameState(ActionState actionState) {
        switch (actionState) {
            case PLACE -> this.gameState = GameState.PLACE;
            case PASS -> this.gameState = GameState.PASS;
            case SWAP -> this.gameState = GameState.SWAP;
        }
    }

    GameState getGameState() {
        return this.gameState;
    }

    char[] getPlayerRackTiles(String username) {
        Player player = this.getPlayerByUsername(username);
        char[] charArray = new char[player.rackTiles.size()];

        for (int i = 0; i < player.rackTiles.size(); i++) {
            charArray[i] = player.rackTiles.get(i).letter;
        }

        return charArray;
    }

    char[] getPlayerSwapTiles(String username) {
        Player player = this.getPlayerByUsername(username);
        char[] charArray = new char[player.swapTiles.size()];

        for (int i = 0; i < player.swapTiles.size(); i++) {
            charArray[i] = player.swapTiles.get(i).letter;
        }

        return charArray;
    }

    GameData getGameData(int gameId) {
        return new GameData(
                gameId,
                this.getPlayerUsernames(),
                this.getCurrentPlayerUsername(),
                this.getPlayerScores(),
                this.getPlayerVotes(),
                this.getCountRackTiles(),
                this.getCountSwapTiles(),
                this.placedTilesForSendGameData,
                this.bag.size(),
                this.gameState
        );
    }

    ArrayList<String> getPlayerUsernames() {
        return players.stream()
                .map(player -> player.username)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    ArrayList<Integer> getPlayerScores() {
        return players.stream()
                .map(player -> player.score)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    ArrayList<PlayerState> getPlayerVotes() {
        return players.stream()
                .map(player -> player.playerState)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    ArrayList<Integer> getCountRackTiles() {
        return players.stream()
                .map(player -> player.rackTiles.size())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    ArrayList<Integer> getCountSwapTiles() {
        return players.stream()
                .map(player -> player.swapTiles.size())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    int getBagSize() {
        return this.bag.size();
    }

    void returnPlacedAndSwapTilesToRack() {
        if (this.gameState.equals(GameState.SWAP)) {
            this.currentPlayer.rackTiles.addAll(currentPlayer.swapTiles);
        } else if (this.gameState.equals(GameState.PLACE)) {
            List<ScrabbleTile> returnedMoveTiles = this.scrabbleBoard.returnMoveTiles();
            this.currentPlayer.rackTiles.addAll(returnedMoveTiles);

            for (int i = 0; i < returnedMoveTiles.size(); i++) {
                this.placedTilesForSendGameData.removeLast();
            }
        }
    }

    boolean playerHasRackTileWithThisLetter(char letter) {
        for (char rackTileLetter : getPlayerRackTiles(this.currentPlayer.username)) {
            if (rackTileLetter == letter) {
                return true;
            }
        }
        return false;
    }

    boolean isPlaceAllowed(TileWithPosition tileWithPosition) {
        return scrabbleBoard.isPositionAllowed(tileWithPosition.column() - 1,
                tileWithPosition.row() - 1);
    }

    private ScrabbleTile getRackTileWithLetter(char letter) {
        return this.currentPlayer.rackTiles.stream()
                .filter(tile -> tile.letter == letter)
                .findFirst()
                .orElse(null);
    }

    void placeTile(TileWithPosition tileWithPosition) {
        ScrabbleTile foundTile = getRackTileWithLetter(tileWithPosition.letter());
        this.currentPlayer.rackTiles.remove(foundTile);
        scrabbleBoard.placeTile(tileWithPosition.column(), tileWithPosition.row(), foundTile);
        this.placedTilesForSendGameData.add(tileWithPosition);
    }

    void swapTile(char letter) {
        ScrabbleTile foundTile = getRackTileWithLetter(letter);
        this.currentPlayer.rackTiles.remove(foundTile);
        this.currentPlayer.swapTiles.add(foundTile);
    }

    void endTurnSwap() {
        this.drawFromBag(this.currentPlayer);
        this.bag.addAll(this.currentPlayer.swapTiles);
        this.switchCurrentPlayerToNext();
        this.passCounter = 0;
    }

    void endTurnPlace() {
        this.gameState = GameState.VOTE;
    }

    void endTurnPass() {
        this.passCounter++;
        this.switchCurrentPlayerToNext();
    }

    void endTurnGameOver() {
        this.gameState = GameState.GAME_OVER;
    }


    void switchCurrentPlayerToNext() {
        int index = this.players.indexOf(currentPlayer);
        this.currentPlayer = players.get((index + 1) % players.size());
    }

    int getPassCounter() {
        return this.passCounter;
    }

    ArrayList<PlayerState> getPlayerStates() {
        return this.players.stream()
                .map(player -> player.playerState)
                .collect(Collectors.toCollection(ArrayList::new));
    }


    void setPlayerState(PlayerVote playerVote, String username) {
        switch (playerVote) {
            case REJECTED -> this.getPlayerByUsername(username).playerState = PlayerState.REJECTED;
            case CONFIRMED -> this.getPlayerByUsername(username).playerState = PlayerState.CONFIRMED;
        }
    }

    public String[] getPlacedWords() {


        // TODO

        return this.scrabbleBoard.getPlacedWords().toArray(new String[0]);
    }

}
