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
    private ArrayList<Word> placedWordsInThisTurn = new ArrayList<>();

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

    ScrabbleGame(ArrayList<String> usernames, LanguageSetting languageSetting, GameState gameState, List<String> bag,
                 List<String> fixedTiles, List<String> movedTiles, List<Integer> scores, List<String> rackTiles, List<String> swapTiles) {
        this.players = new ArrayList<>(usernames.size());
        usernames.forEach(username -> this.players.add(new Player(username)));
        this.gameState = gameState;
        this.currentPlayer = players.getFirst();
        List<ScrabbleTile> allTiles = createBagFromLanguageSetting(languageSetting);

        this.bag = new ArrayList<>();
        bag.forEach(bagLetter -> {
            this.bag.addLast(allTiles.stream().filter(tile -> tile.letter == bagLetter.charAt(0)).findFirst().orElse(null));
        });

        for (String tilePosition : fixedTiles) {
            String[] parts = tilePosition.split("#");
            int row = Integer.parseInt(parts[0]) - 1;
            int col = Integer.parseInt(parts[1]) - 1;
            char letter = parts[2].charAt(0);

            ScrabbleTile fixedTile = allTiles.stream().filter(tile -> tile.letter == letter).findFirst().orElse(null);
            scrabbleBoard.gameBoard[row][col].scrabbleTile = fixedTile;
            scrabbleBoard.gameBoard[row][col].setSquareStateToOccupied();
            this.placedTilesForSendGameData.add(new TileWithPosition(fixedTile.letter, row + 1, col + 1));
        }

        for (String tilePosition : movedTiles) {
            String[] parts = tilePosition.split("#");
            int row = Integer.parseInt(parts[0]) - 1;
            int col = Integer.parseInt(parts[1]) - 1;
            char letter = parts[2].charAt(0);

            ScrabbleTile movedTile = allTiles.stream().filter(tile -> tile.letter == letter).findFirst().orElse(null);
            scrabbleBoard.gameBoard[row][col].scrabbleTile = movedTile;
            scrabbleBoard.gameBoard[row][col].setSquareStateToOccupied();
            this.placedTilesForSendGameData.add(new TileWithPosition(movedTile.letter, row + 1, col + 1));
        }

        for (int i = 0; i < scores.size(); i++) {
            players.get(i).score = scores.get(i);
        }

        for (int i = 0; i < rackTiles.size(); i++) {
            String rackTilesPerUser = rackTiles.get(i);
            String[] parts = rackTilesPerUser.split("#");
            for (String rackLetter : parts) {
                ScrabbleTile rackTileForUser = allTiles.stream()
                        .filter(tile -> tile.letter == rackLetter.charAt(0)).findFirst().orElse(null);
                players.get(i).rackTiles.addLast(rackTileForUser);
            }
        }

        for (int i = 0; i < swapTiles.size(); i++) {
            String swapTilesPerUser = swapTiles.get(i);
            String[] parts = swapTilesPerUser.split("#");
            for (String swapLetter : parts) {
                ScrabbleTile swapTileForUser = allTiles.stream()
                        .filter(tile -> tile.letter == swapLetter.charAt(0)).findFirst().orElse(null);
                players.get(i).swapTiles.addLast(swapTileForUser);
            }
        }
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

    boolean playerHasNoRackTilesLeft() {
        return players.stream().anyMatch(player -> player.rackTiles.isEmpty());

    }

    void returnPlacedAndSwapTilesToRack() {
        if (this.gameState.equals(GameState.SWAP)) {
            this.currentPlayer.rackTiles.addAll(currentPlayer.swapTiles);
        } else if (this.gameState.equals(GameState.PLACE) || this.gameState.equals(GameState.VOTE)) {
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

    boolean isSquareFree(TileWithPosition tileWithPosition) {
        return scrabbleBoard.isSquareFree(tileWithPosition.row() - 1, tileWithPosition.column() - 1
        );
    }

    boolean hasNeighbour(TileWithPosition tileWithPosition) {
        return scrabbleBoard.hasNeighbour(tileWithPosition.row() - 1, tileWithPosition.column() - 1
        );
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
        scrabbleBoard.placeTile(tileWithPosition.row() - 1, tileWithPosition.column() - 1, foundTile);
        this.placedTilesForSendGameData.add(tileWithPosition);
    }

    void swapTile(char letter) {
        ScrabbleTile foundTile = getRackTileWithLetter(letter);
        this.currentPlayer.rackTiles.remove(foundTile);
        this.currentPlayer.swapTiles.add(foundTile);
    }

    void endTurnSwap() {
        //System.out.println("Bag Size before draw: " + this.bag.size());
        this.drawFromBag(this.currentPlayer);
        //System.out.println("Bag Size before addAll: " + this.bag.size());
        this.bag.addAll(this.currentPlayer.swapTiles);
        this.currentPlayer.swapTiles.clear();
        Collections.shuffle(this.bag);
        //System.out.println("Bag Size after addAll: " + this.bag.size());
        this.gameState = GameState.PLAY;
        this.switchCurrentPlayerToNext();
        //System.out.println("Passcounter before reset: " + this.passCounter);
        this.passCounter = 0;
    }

    void endTurnPlace() {
        this.gameState = GameState.VOTE;
        this.placedWordsInThisTurn = this.scrabbleBoard.getPlacedWords();
        placedWordsInThisTurn.forEach(word -> {
            System.out.println("\nPlaced Words in this turn: " + word);
        });
    }

    void endTurnPass() {
        this.passCounter++;
        this.switchCurrentPlayerToNext();
        this.gameState = GameState.PLAY;
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


    void setPlayerStateWithPlayerVote(PlayerVote playerVote, String username) {
        switch (playerVote) {
            case REJECTED -> this.getPlayerByUsername(username).playerState = PlayerState.REJECTED;
            case CONFIRMED -> this.getPlayerByUsername(username).playerState = PlayerState.CONFIRMED;
        }
    }

    public ArrayList<String> getPlacedWords() {
        return new ArrayList<>(this.placedWordsInThisTurn.stream().map(Word::word).toList());
    }

    void endVotePlacedWordsOk() {
        int wordSum = this.placedWordsInThisTurn.stream().mapToInt(Word::score).sum();
        if (this.currentPlayer.rackTiles.isEmpty()) {
            wordSum += 50;
        }
        this.currentPlayer.score += wordSum;

        this.players.stream()
                .filter(player -> player.playerState.equals(PlayerState.REJECTED))
                .forEach(player -> player.score -= 5);

        this.scrabbleBoard.setAllMoveTilesToOccupied();

        this.gameState = GameState.PLAY;
        this.placedWordsInThisTurn.clear();
        players.forEach(player -> player.playerState = PlayerState.NOT_VOTED);
        this.drawFromBag(this.currentPlayer);
        this.switchCurrentPlayerToNext();
    }

    void endVotePlacedWordsNotOk() {
        this.returnPlacedAndSwapTilesToRack();

        this.gameState = GameState.PLAY;
        this.placedWordsInThisTurn.clear();
        players.forEach(player -> player.playerState = PlayerState.NOT_VOTED);
        this.switchCurrentPlayerToNext();
    }

    public void printGameBoard() {
        this.scrabbleBoard.printGameBoardInThePrettiestWayPossiblePlease();
    }
}
