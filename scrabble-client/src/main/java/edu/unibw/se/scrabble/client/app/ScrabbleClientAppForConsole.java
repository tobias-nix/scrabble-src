package edu.unibw.se.scrabble.client.app;

import edu.unibw.se.scrabble.client.ccom.ClientCommunication;
import edu.unibw.se.scrabble.client.ccom.ClientConnect;
import edu.unibw.se.scrabble.client.ccom.ClientConnectCallback;
import edu.unibw.se.scrabble.client.ccom.impl.ClientCommunicationImpl;
import edu.unibw.se.scrabble.common.base.*;
import edu.unibw.se.scrabble.common.scom.NetworkConnect;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.Naming;
import java.util.*;

import static edu.unibw.se.scrabble.common.base.Colors.*;

public class ScrabbleClientAppForConsole {
    private static final int PORT = 1099;
    private static final Scanner in = new Scanner(System.in);
    private static ClientConnect clientConnect = null;
    private static String username = null;
    private static final HashMap<Character, Integer> mapLetterToValue = getLetterSetHashMap();

    public static void main(String[] args) {

        ClientCommunication clientCommunication = new ClientCommunicationImpl();
        clientConnect = clientCommunication.getClientConnect();
        clientConnect.setClientConnectCallback(new ClientConnectCallbackForConsole());

        NetworkConnect networkConnect = null;
        try {
            networkConnect = (NetworkConnect) Naming.lookup("//127.0.0.1:" + PORT + "/scrabble-server");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        clientCommunication.setNetworkConnect(networkConnect);

        playScrabble();

        showGameBoard(new char[]{}, new char[]{}, GameData.TEST_GAMEDATA);
    }

    private static void playScrabble() {
        System.out.println("Login for Scrabble\nWrite 'LOGIN' or 'REGISTER'");
        String userIn = in.nextLine().toLowerCase();
        if (userIn.equals("garfield")) {
            if (!clientConnect.loginUser("Garfield", "Garfield1!").equals(ReturnValues.ReturnLoginUser.SUCCESSFUL)) {
                System.out.println("Failure");
                playScrabble();
            }
            username = "Garfield";
            for (; ; ) {
            }
        } else if (userIn.equals("odie")) {
            if (!clientConnect.loginUser("Odie", "OdieOdie1!").equals(ReturnValues.ReturnLoginUser.SUCCESSFUL)) {
                System.out.println("Failure");
                playScrabble();
            }
            username = "Odie";
            for (; ; ) {
            } // wait for callback
        }
        switch (userIn) {
            case "login":
                login();
            case "register":
                System.out.println("Register not implemented in this terminal version");
                login();
            default:
                System.out.println("Wrong input'");
                playScrabble();
        }
    }

    private static void login() {
        System.out.println("Login User\nUsername: ");
        String userIn = in.nextLine();
        System.out.println("Password: ");
        String password = in.nextLine();
        switch (clientConnect.loginUser(userIn, password)) {
            case SUCCESSFUL:
                System.out.println("Login Successful");
                username = userIn;
                mainMenu();
            case WRONG_PASSWORD:
                System.out.println("Wrong Password");
                login();
            default:
                System.out.println("Failure");
                login();
        }
    }

    private static void mainMenu() {
        System.out.println("Main Menu\nWrite 'CREATE' or 'JOIN'");
        switch (in.nextLine().toLowerCase()) {
            case "create":
                createSession();
            case "join":
                joinSession();
        }
    }

    private static void createSession() {
        System.out.println("Create Session");
        ReturnValues.ReturnCreateSession ret = clientConnect.createSession(LanguageSetting.GERMAN);
        switch (ret.state()) {
            case SUCCESSFUL:
                System.out.println("Created Game " + ret.gameID());
                for (; ; ) {
                } // wait for callback
            case FAILURE:
                mainMenu();
        }

    }

    private static void joinSession() {
        System.out.println("Join Session\nEnter Game ID: ");
        int gameId = Integer.parseInt(in.nextLine());
        switch (clientConnect.joinSession(gameId)) {
            case SUCCESSFUL:
                System.out.printf("Joined Game " + gameId);
                for (; ; ) {
                } // wait for callback
            case FAILURE:
                mainMenu();
        }
    }

    private static void lobby(String[] usernames) {
        System.out.println("In Lobby with users: " + Arrays.toString(usernames));
        if (username.equals(usernames[0])) {
            System.out.println("Write 'START' to start game.");
        } else {
            System.out.println("Waiting for host to start game.");
            for (; ; ) {
            } // wait for callback
        }
        String userIn = in.nextLine();
        if (userIn.equalsIgnoreCase("start")) {
            switch (clientConnect.startGame()) {
                case SUCCESSFUL:
                    for (; ; ) {
                    } // wait for callback
                case USER_ALONE_IN_SESSION:
                    System.out.println("Minimum of 2 players required.");
                default:
                    System.out.println("Failure");
                    lobby(usernames);
            }
        }
    }

    private static void play(char[] rackTiles, char[] swapTiles, GameData gameData) {
        System.out.println("What's your move? 'PLACE', 'SWAP', 'PASS'");
        switch (in.nextLine().toLowerCase()) {
            case "place":
                selectAction(ActionState.PLACE, rackTiles, swapTiles, gameData);
            case "swap":
                selectAction(ActionState.SWAP, rackTiles, swapTiles, gameData);
            case "pass":
                selectAction(ActionState.PASS, rackTiles, swapTiles, gameData);
            default:
                play(rackTiles, swapTiles, gameData);
        }
    }

    private static void selectAction(ActionState actionState, char[] rackTiles, char[] swapTiles, GameData gameData) {
        switch (clientConnect.selectAction(actionState)) {
            case SUCCESSFUL:
                for (; ; ) {
                } // wait for callback
            case LESS_THAN_SEVEN_TILES_IN_BAG:
                System.out.println("No swap possible, less than 7 tiles in the bag!");
                play(rackTiles, swapTiles, gameData);
            default:
                System.out.println("Failure");
                play(rackTiles, swapTiles, gameData);
        }
    }

    private static void place(char[] rackTiles, char[] swapTiles, GameData gameData) {
        System.out.println("Place selected");
        System.out.println("Do you want to end your turn? ('YES'/'NO')");
        if (in.nextLine().equalsIgnoreCase("yes")) {
            endTurn(rackTiles, swapTiles, gameData);
        }

        System.out.println("Place a tile: [LETTER, ROW, COLUMN]");
        String tileIn = in.nextLine();
        String[] inArray = tileIn.split(",");
        if (inArray.length != 3) {
            System.out.println("Wrong Input");
            play(rackTiles, swapTiles, gameData);
        }

        switch (clientConnect.placeTile(new TileWithPosition(inArray[0].toUpperCase().charAt(0),
                Integer.parseInt(inArray[1].trim()),
                Integer.parseInt(inArray[2].trim()))
        )) {
            case SUCCESSFUL:
                for (; ; ) {
                }  // wait for callback
            case POSITION_NOT_ALLOWED:
                System.out.println("Position not allowed");
                place(rackTiles, swapTiles, gameData);
            case TILE_NOT_ON_RACK:
                System.out.println("Tile not on rack");
                place(rackTiles, swapTiles, gameData);
            case SQUARE_OCCUPIED:
                System.out.println("Square occupied");
                place(rackTiles, swapTiles, gameData);
            default:
                System.out.println("Failure");
                place(rackTiles, swapTiles, gameData);
        }
    }

    private static void swap(char[] rackTiles, char[] swapTiles, GameData gameData) {
        System.out.println("Swap selected");
        System.out.println("Do you want to end your turn? ('YES'/'NO')");
        if (in.nextLine().equalsIgnoreCase("yes")) {
            endTurn(rackTiles, swapTiles, gameData);
        }
        System.out.println("Select a tile to place on the swap bench:");
        String tileIn = in.nextLine();
        switch (clientConnect.swapTile(tileIn.charAt(0))) {
            case SUCCESSFUL:
                for (; ; ) {
                }  // wait for callback
            case TILE_NOT_ON_RACK:
                System.out.println("Tile not on rack");
                swap(rackTiles, swapTiles, gameData);
            default:
                System.out.println("Failure");
                swap(rackTiles, swapTiles, gameData);
        }
    }

    private static void pass(char[] rackTiles, char[] swapTiles, GameData gameData) {
        System.out.println("Pass selected");
        System.out.println("Do you want to end your turn? ('YES'/'NO')");
        if (in.nextLine().equalsIgnoreCase("yes")) {
            endTurn(rackTiles, swapTiles, gameData);
        }
        play(rackTiles, swapTiles, gameData);
    }

    private static void endTurn(char[] rackTiles, char[] swapTiles, GameData gameData) {
        System.out.println("Ending Turn");
        switch (clientConnect.endTurn()) {
            case SUCCESSFUL:
                for (; ; ) {
                }  // wait for callback
            default:
                System.out.println("Failure");
                play(rackTiles, swapTiles, gameData);
        }
    }

    private static void vote(String[] placedWords) {
        System.out.println("Current player ended his turn and placed the following words:\n"
                + Arrays.toString(placedWords) + "\nDo you confirm or reject?('C'/'R')");
        String userIn = in.nextLine().toUpperCase();

        if (!userIn.equals("C") && !userIn.equals("R")) {
            vote(placedWords);
        }
        ReturnValues.ReturnSendPlayerVote ret = null;
        if (userIn.equals("C")) {
            ret = clientConnect.sendPlayerVote(PlayerVote.CONFIRMED);
        } else if (userIn.equals("R")) {
            ret = clientConnect.sendPlayerVote(PlayerVote.REJECTED);
        }

        switch (ret) {
            case SUCCESSFUL:
                for (; ; ) {
                }  // wait for callback
            case null:
                System.out.println("Failure");
            default:
                System.out.println("Failure");
        }
    }

    private static void gameOver() {
        System.out.println("Game Over");
    }

    private static DisplaySquare[][] createGameBoard() {
        DisplaySquare[][] gameBoard = new DisplaySquare[15][15];
        String filePath = Objects.requireNonNull(ScrabbleClientAppForConsole.class.getResource("/boardValues.csv")).getFile();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            bufferedReader.readLine(); //Erste Format LINE
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineAsArray = line.split(",");
                Arrays.stream(lineAsArray).forEach(scrabbleSquare -> {
                    int[] scrabbleSquareAsArray =
                            Arrays.stream(scrabbleSquare.split("#")).mapToInt(Integer::parseInt).toArray();
                    gameBoard[scrabbleSquareAsArray[0] - 1][scrabbleSquareAsArray[1] - 1] =
                            new DisplaySquare(scrabbleSquareAsArray[2], scrabbleSquareAsArray[3]);
                });
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return gameBoard;
    }


    private static void showGameBoard(char[] rackTiles, char[] swapTiles, GameData gameData) {
        DisplaySquare[][] gameBoard = createGameBoard();

        gameData.tilesWithPositions.forEach(tile ->
                gameBoard[tile.row() - 1][tile.column() - 1].displayTile =
                        new DisplayTile(tile.letter(), mapLetterToValue.get(tile.letter())));

        printGameBoardInThePrettiestWayPossiblePlease(gameBoard);
        System.out.println("Players: " + gameData.usernames.toString());
        System.out.println(gameData.currentPlayer + " is the current player in game state " + gameData.state + ".");
        System.out.println("Your Rack Tiles are: " + Arrays.toString(rackTiles));
        System.out.println("Your Swap Tiles are: " + Arrays.toString(swapTiles));
        System.out.println("Current Scores: " + gameData.score.toString());
    }

    private static void printGameBoardInThePrettiestWayPossiblePlease(DisplaySquare[][] gameBoard) {
        System.out.println(Arrays.deepToString(gameBoard)
                .replace("], ", "]\n")
                .replace("null", "|_/_|")
                .replace("[[", "\n[")
                .replace("]]", "]\n"));
        System.out.println(BLACK_TEXT + YELLOW_BG + "Double Word" + RESET + "  "
                + BLACK_TEXT + RED_BG + "Triple Word" + RESET + "  "
                + BLACK_TEXT + LIGHT_BLUE_BG + "Double Letter" + RESET + "  "
                + BLACK_TEXT + DARK_BLUE_BG + "Triple Letter" + RESET);
    }

    private static class DisplaySquare {
        DisplayTile displayTile = null;
        int wordFactor;
        int letterFactor;

        DisplaySquare(int wordFactor, int letterFactor) {
            this.wordFactor = wordFactor;
            this.letterFactor = letterFactor;
        }

        @Override
        public String toString() {
            String factor = " ";
            if (wordFactor == 2) {
                factor = YELLOW_BG + BLACK_TEXT + "2" + RESET;
            } else if (wordFactor == 3) {
                factor = RED_BG + BLACK_TEXT + "3" + RESET;
            } else if (letterFactor == 2) {
                factor = LIGHT_BLUE_BG + BLACK_TEXT + "2" + RESET;
            } else if (letterFactor == 3) {
                factor = DARK_BLUE_BG + BLACK_TEXT + "3" + RESET;
            }
            return "(" + factor + this.displayTile + ")";
        }
    }

    private static class DisplayTile {
        char letter;
        int value;

        DisplayTile(char letter, int value) {
            this.letter = letter;
            this.value = value;
        }

        @Override
        public String toString() {
            return "|" + CYAN_BG + BLACK_TEXT + letter + "/" + value + RESET + "|";
        }
    }


    static class ClientConnectCallbackForConsole implements ClientConnectCallback {

        @Override
        public void usersInSession(String[] usernames) {
            lobby(usernames);
        }

        @Override
        public void sendGameData(char[] rackTiles, char[] swapTiles, GameData gameData) {
            System.out.println("\n\n" + gameData);
            showGameBoard(rackTiles, swapTiles, gameData);
            if (!gameData.currentPlayer.equals(username)) {
                System.out.println("\nWaiting for next move of " + gameData.currentPlayer);
                for (; ; ) {
                }  // wait for callback
            }
            switch (gameData.state) {
                case PLAY:
                    play(rackTiles, swapTiles, gameData);
                case PLACE:
                    place(rackTiles, swapTiles, gameData);
                case SWAP:
                    swap(rackTiles, swapTiles, gameData);
                case PASS:
                    pass(rackTiles, swapTiles, gameData);
                case GAME_OVER:
                    gameOver();
                default:
                    System.out.println("Failure in sendGameData.");
            }
        }

        @Override
        public void vote(String[] placedWords) {
            ScrabbleClientAppForConsole.vote(placedWords);
        }
    }

    static HashMap<Character, Integer> getLetterSetHashMap() {
        String filePath = Objects.requireNonNull(
                ScrabbleClientAppForConsole.class.getResource("/letterSetGerman.csv")).getFile();

        HashMap<Character, Integer> map = new HashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineAsArray = line.split(",");
                Arrays.stream(lineAsArray).forEach(tileInformationAsArray -> {
                    String[] tileInformationSplit = tileInformationAsArray.split(";");
                    map.put(
                            tileInformationSplit[0].toUpperCase().charAt(0),
                            Integer.parseInt(tileInformationSplit[1]));
                });

            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return map;
    }
}
