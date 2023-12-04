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
    // TODO bag
    private final ArrayList<TileWithPosition> placedTilesForSendGameData = new ArrayList<>();
    private final ScrabbleBoard scrabbleBoard = new ScrabbleBoard();

    static class Player {
        String username;
        int score = 0;
        ArrayList<ScrabbleTile> rackTiles = new ArrayList<>();
        ArrayList<ScrabbleTile> swapTiles = new ArrayList<>();
        PlayerState playerVote = PlayerState.NOT_VOTED;

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

    private void drawFromBag(Player player) {
        while (player.rackTiles.size() < 7 && !this.bag.isEmpty()) {
            player.rackTiles.add(this.bag.removeFirst());
        }
    }


    void switchCurrentPlayerToNext() {
        int index = this.players.indexOf(currentPlayer);
        this.currentPlayer = players.get((index + 1) % players.size());
    }

    String getCurrentPlayerUsername() {
        return this.currentPlayer.username;
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
                .map(player -> player.playerVote)
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


    boolean isRackTiles(char letter) {
        return true;/*
        ArrayList<ScrabbleTile> rackTiles = mapUsernameToRackTiles.get(this.currentPlayer);
        final boolean[] flag = {false};
        rackTiles.forEach(tile -> {
            if (tile.letter == letter) {
                flag[0] = true;
            }
        });
        return flag[0];*/
    }

    boolean isPlaceAllowed(TileWithPosition tileWithPosition) {
        return scrabbleBoard.isPositionAllowed(tileWithPosition.column() - 1,
                tileWithPosition.row() - 1);
    }

    void placeTile(TileWithPosition tileWithPosition) {
        return;/*
        ArrayList<ScrabbleTile> rackTiles = mapUsernameToRackTiles.get(this.currentPlayer);

        ScrabbleTile scrabbleTile;
        for (int i = 0; i < rackTiles.size(); i++) {
            scrabbleTile = rackTiles.get(i);
            if (scrabbleTile.letter == tileWithPosition.letter()) {
                scrabbleTile = rackTiles.remove(i);
                scrabbleBoard.placeTile(tileWithPosition.column() - 1,
                        tileWithPosition.row() - 1, scrabbleTile);
                this.placedTilesForSendGameData.add(tileWithPosition);


                break;
            }
        }*/
    }

}
