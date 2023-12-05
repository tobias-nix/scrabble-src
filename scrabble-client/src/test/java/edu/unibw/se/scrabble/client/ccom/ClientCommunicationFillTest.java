package edu.unibw.se.scrabble.client.ccom;

import edu.unibw.se.scrabble.common.base.*;
import edu.unibw.se.scrabble.server.auth.Authentication;
import edu.unibw.se.scrabble.server.auth.impl.AuthenticationImpl;
import edu.unibw.se.scrabble.server.data.impl.spring.SpringScrabbleData;
import edu.unibw.se.scrabble.server.logic.ServerConnect;
import edu.unibw.se.scrabble.server.logic.ServerLogic;
import edu.unibw.se.scrabble.server.logic.impl.ServerLogicImpl;
import edu.unibw.se.scrabble.server.scom.ServerCommunication;
import edu.unibw.se.scrabble.server.scom.impl.ServerCommunicationImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ClientCommunicationFillTest {
    protected abstract ClientCommunication getClientCommunication();

    private static class Client {
        String username;
        String password;
        ClientCommunication clientCommunication;
        ClientConnectCallbackTest clientConnectCallbackTest;

        Client(String username, String password, ClientCommunication clientCommunication) {
            this.username = username;
            this.password = password;
            this.clientCommunication = clientCommunication;
            this.clientConnectCallbackTest = new ClientConnectCallbackTest();
            this.clientCommunication.getClientConnect().setClientConnectCallback(clientConnectCallbackTest);
        }

        @Override
        public String toString() {
            return username;
        }
    }

    @Nested
    class StartGameTests {
        @Test
        public void startGameSuccess() {
            ArrayList<Client> clientList = setUpClientList();

            checkIfSendGameDataWasCalledAndPrint(clientList);
        }
    }

    @Nested
    class SelectActionTests {
        @Test
        public void selectActionSuccessTileIsPlacedBack() {
            ArrayList<Client> clientList = setUpClientList();

            checkIfSendGameDataWasCalledAndPrint(clientList);

            ReturnValues.ReturnPlaceTile returnPlaceTile =
                    clientList.getFirst().clientCommunication.getClientConnect().placeTile(new TileWithPosition('N', 9, 12));
            assertEquals(ReturnValues.ReturnPlaceTile.SUCCESSFUL, returnPlaceTile);

            checkIfSendGameDataWasCalledAndPrint(clientList);


            ReturnValues.ReturnSelectAction returnSelectAction =
                    clientList.getFirst().clientCommunication.getClientConnect().selectAction(ActionState.PASS);
            assertEquals(ReturnValues.ReturnSelectAction.SUCCESSFUL, returnSelectAction);

            checkIfSendGameDataWasCalledAndPrint(clientList);
        }
    }

    @Nested
    class PlaceTileTests {
        @Test
        public void placeTileSuccess() {
            ArrayList<Client> clientList = setUpClientList();

            checkIfSendGameDataWasCalledAndPrint(clientList);

            ReturnValues.ReturnPlaceTile returnPlaceTile =
                    clientList.getFirst().clientCommunication.getClientConnect().placeTile(new TileWithPosition('N', 9, 12));
            assertEquals(ReturnValues.ReturnPlaceTile.SUCCESSFUL, returnPlaceTile);

            checkIfSendGameDataWasCalledAndPrint(clientList);
        }

        @Test
        public void placeTileFailurePositionNotAllowed() {
            ArrayList<Client> clientList = setUpClientList();

            checkIfSendGameDataWasCalledAndPrint(clientList);

            ReturnValues.ReturnPlaceTile returnPlaceTile =
                    clientList.getFirst().clientCommunication.getClientConnect().placeTile(new TileWithPosition('N', 9, 13));
            assertEquals(ReturnValues.ReturnPlaceTile.POSITION_NOT_ALLOWED, returnPlaceTile);

            checkIfSendGameDataWasCalledAndPrint(clientList);
        }

        @Test
        public void placeTileFailureSquareOccupied() {
            ArrayList<Client> clientList = setUpClientList();

            checkIfSendGameDataWasCalledAndPrint(clientList);

            ReturnValues.ReturnPlaceTile returnPlaceTile =
                    clientList.getFirst().clientCommunication.getClientConnect().placeTile(new TileWithPosition('N', 8, 8));
            assertEquals(ReturnValues.ReturnPlaceTile.SQUARE_OCCUPIED, returnPlaceTile);

            checkIfSendGameDataWasCalledAndPrint(clientList);
        }

        @Test
        public void placeTileFailureTileNotOnRack() {
            ArrayList<Client> clientList = setUpClientList();

            checkIfSendGameDataWasCalledAndPrint(clientList);

            ReturnValues.ReturnPlaceTile returnPlaceTile =
                    clientList.getFirst().clientCommunication.getClientConnect().placeTile(new TileWithPosition('I', 8, 12));
            assertEquals(ReturnValues.ReturnPlaceTile.TILE_NOT_ON_RACK, returnPlaceTile);

            checkIfSendGameDataWasCalledAndPrint(clientList);
        }
    }

    @Nested
    class SwapTileTests {
        @Test
        public void swapTileSuccess() {
            ArrayList<Client> clientList = setUpClientList();

            checkIfSendGameDataWasCalledAndPrint(clientList);

            ReturnValues.ReturnSelectAction returnSelectAction =
                    clientList.getFirst().clientCommunication.getClientConnect().selectAction(ActionState.SWAP);
            assertEquals(ReturnValues.ReturnSelectAction.SUCCESSFUL, returnSelectAction);

            checkIfSendGameDataWasCalledAndPrint(clientList);

            ReturnValues.ReturnSwapTile returnSwapTile =
                    clientList.getFirst().clientCommunication.getClientConnect().swapTile('Y');
            assertEquals(ReturnValues.ReturnSwapTile.SUCCESSFUL, returnSwapTile);

            checkIfSendGameDataWasCalledAndPrint(clientList);
        }

        @Test
        public void swapTileFailureTileNotOnRack() {
            ArrayList<Client> clientList = setUpClientList();

            checkIfSendGameDataWasCalledAndPrint(clientList);

            ReturnValues.ReturnSelectAction returnSelectAction =
                    clientList.getFirst().clientCommunication.getClientConnect().selectAction(ActionState.SWAP);
            assertEquals(ReturnValues.ReturnSelectAction.SUCCESSFUL, returnSelectAction);

            checkIfSendGameDataWasCalledAndPrint(clientList);

            ReturnValues.ReturnSwapTile returnSwapTile =
                    clientList.getFirst().clientCommunication.getClientConnect().swapTile('A');
            assertEquals(ReturnValues.ReturnSwapTile.TILE_NOT_ON_RACK, returnSwapTile);

            checkIfSendGameDataWasCalledAndPrint(clientList);
        }
    }

    @Nested
    class EndTurnTests {
        @Test
        public void endTurnSuccessPass() {
            ArrayList<Client> clientList = setUpClientList();

            checkIfSendGameDataWasCalledAndPrint(clientList);

            ReturnValues.ReturnSelectAction returnSelectAction =
                    clientList.getFirst().clientCommunication.getClientConnect().selectAction(ActionState.PASS);
            assertEquals(ReturnValues.ReturnSelectAction.SUCCESSFUL, returnSelectAction);

            checkIfSendGameDataWasCalledAndPrint(clientList);

            ReturnValues.ReturnEndTurn returnEndTurn =
                    clientList.getFirst().clientCommunication.getClientConnect().endTurn();
            assertEquals(ReturnValues.ReturnEndTurn.SUCCESSFUL, returnEndTurn);

            checkIfSendGameDataWasCalledAndPrint(clientList);
        }

        @Test
        public void endTurnSuccessPassGameOver() {
            ArrayList<Client> clientList = setUpClientList();

            checkIfSendGameDataWasCalledAndPrint(clientList);

            System.out.println("\nUser statistics before game\n");
            clientList.forEach(client -> {
                System.out.println(client.clientCommunication.getClientConnect().getUserStatistics());
            });


            for (int i = 0; i < 8; i++) {
                ReturnValues.ReturnSelectAction returnSelectAction =
                        clientList.get(i % clientList.size()).clientCommunication.getClientConnect().selectAction(ActionState.PASS);
                assertEquals(ReturnValues.ReturnSelectAction.SUCCESSFUL, returnSelectAction);

                checkIfSendGameDataWasCalledAndPrint(clientList);

                ReturnValues.ReturnEndTurn returnEndTurn =
                        clientList.get(i % clientList.size()).clientCommunication.getClientConnect().endTurn();
                assertEquals(ReturnValues.ReturnEndTurn.SUCCESSFUL, returnEndTurn);

                checkIfSendGameDataWasCalledAndPrint(clientList);
            }

            System.out.println("\nUser statistics after game\n");
            clientList.forEach(client -> {
                System.out.println(client.clientCommunication.getClientConnect().getUserStatistics());
            });
        }

        @Test
        public void endTurnSuccessSwapPassCounterReset() {
            ArrayList<Client> clientList = setUpClientList();

            checkIfSendGameDataWasCalledAndPrint(clientList);

            ReturnValues.ReturnSelectAction returnSelectAction =
                    clientList.get(0).clientCommunication.getClientConnect().selectAction(ActionState.PASS);
            assertEquals(ReturnValues.ReturnSelectAction.SUCCESSFUL, returnSelectAction);

            checkIfSendGameDataWasCalledAndPrint(clientList);

            ReturnValues.ReturnEndTurn returnEndTurn =
                    clientList.get(0).clientCommunication.getClientConnect().endTurn();
            assertEquals(ReturnValues.ReturnEndTurn.SUCCESSFUL, returnEndTurn);

            checkIfSendGameDataWasCalledAndPrint(clientList);


            returnSelectAction =
                    clientList.get(1).clientCommunication.getClientConnect().selectAction(ActionState.SWAP);
            assertEquals(ReturnValues.ReturnSelectAction.SUCCESSFUL, returnSelectAction);

            checkIfSendGameDataWasCalledAndPrint(clientList);

            char[] swapLetters = "LASAGNE".toCharArray();
            for (char swapLetter : swapLetters) {
                ReturnValues.ReturnSwapTile returnSwapTile =
                        clientList.get(1).clientCommunication.getClientConnect().swapTile(swapLetter);
                assertEquals(ReturnValues.ReturnSwapTile.SUCCESSFUL, returnSwapTile);

                checkIfSendGameDataWasCalledAndPrint(clientList);
            }

            returnEndTurn =
                    clientList.get(1).clientCommunication.getClientConnect().endTurn();
            assertEquals(ReturnValues.ReturnEndTurn.SUCCESSFUL, returnEndTurn);

            checkIfSendGameDataWasCalledAndPrint(clientList);
        }

        @Test
        public void endTurnSuccessPlace() {
            ArrayList<Client> clientList = setUpClientList();

            checkIfSendGameDataWasCalledAndPrint(clientList);

            for (int i = 0; i < 2; i++) {
                ReturnValues.ReturnSelectAction returnSelectAction =
                        clientList.get(i).clientCommunication.getClientConnect().selectAction(ActionState.PASS);
                assertEquals(ReturnValues.ReturnSelectAction.SUCCESSFUL, returnSelectAction);
                ReturnValues.ReturnEndTurn returnEndTurn =
                        clientList.get(i).clientCommunication.getClientConnect().endTurn();
                assertEquals(ReturnValues.ReturnEndTurn.SUCCESSFUL, returnEndTurn);
            }

            ReturnValues.ReturnSelectAction returnSelectAction =
                    clientList.get(2).clientCommunication.getClientConnect().selectAction(ActionState.PLACE);
            assertEquals(ReturnValues.ReturnSelectAction.SUCCESSFUL, returnSelectAction);
            char[] placeLetters = "ER".toCharArray();
            for (int i = 0; i < placeLetters.length; i++) {
                ReturnValues.ReturnPlaceTile returnPlaceTile =
                        clientList.get(2).clientCommunication.getClientConnect().placeTile(new TileWithPosition(placeLetters[i], 6 - i, 8));
                assertEquals(ReturnValues.ReturnPlaceTile.SUCCESSFUL, returnPlaceTile, "Place Tile");

                checkIfSendGameDataWasCalledAndPrint(clientList);
            }

            ReturnValues.ReturnEndTurn returnEndTurn =
                    clientList.get(2).clientCommunication.getClientConnect().endTurn();
            assertEquals(ReturnValues.ReturnEndTurn.SUCCESSFUL, returnEndTurn, "End Turn");

            checkIfSendGameDataWasCalledAndPrint(clientList);
            assertTrue(clientList.get(0).clientConnectCallbackTest.voteCalled, "vote called");
            assertArrayEquals(new String[]{"REICH"}, clientList.get(0).clientConnectCallbackTest.voteTransferredPlacedWords, "voteTransferredPlacedWords");
        }
    }

    ArrayList<Client> setUpClientList() {
        ArrayList<Client> clientList = new ArrayList<>(List.of(
                new Client("Odie", "OdieOdie1!", getClientCommunication()),
                new Client("Garfield", "Garfield1!", getClientCommunication()),
                new Client("Nermal", "Nermal123!", getClientCommunication()),
                new Client("JonA", "JonA1234!", getClientCommunication())));

        ServerCommunication serverCommunication;
        try {
            serverCommunication = new ServerCommunicationImpl();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        ServerLogic serverLogic = new ServerLogicImpl();
        ServerConnect serverConnect = serverLogic.getServerConnect();
        serverCommunication.setServerConnect(serverConnect);

        Authentication authentication = new AuthenticationImpl();
        SpringScrabbleData springDatabase = new SpringScrabbleData();
        springDatabase.clear();
        springDatabase.fill();

        authentication.setAuthData(springDatabase.getAuthData());
        serverLogic.setScrabbleData(springDatabase.getScrabbleData());
        serverCommunication.setCredentials(authentication.getCredentials());

        clientList.forEach(client -> {
            client.clientCommunication.setNetworkConnect(serverCommunication.getNetworkConnect());
        });

        clientList.forEach(client -> {
            ReturnValues.ReturnLoginUser returnLoginUser =
                    client.clientCommunication.getClientConnect().loginUser(client.username, client.password);
            assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL, returnLoginUser,
                    "Login User");
        });

        serverLogic.setServerState();

        return clientList;
    }

    void checkIfSendGameDataWasCalledAndPrint(ArrayList<Client> clientList) {
        System.out.println("Sent GameData:");
        clientList.forEach(client -> {
            assertTrue(client.clientConnectCallbackTest.sendGameStateCalled, client.username + " sendGameStateCalled");
            System.out.println(client.username + " RackTiles:" + Arrays.toString(client.clientConnectCallbackTest.sendGameStateTransferredRackTiles));
            System.out.println(client.username + " SwapTiles:" + Arrays.toString(client.clientConnectCallbackTest.sendGameStateTransferredSwapTiles));
            System.out.println(client.username + " GameData: " + client.clientConnectCallbackTest.sendGameStateTransferredGameData);
        });
    }

    static class ClientConnectCallbackTest implements ClientConnectCallback {

        public boolean usersInSessionCalled = false;
        public String[] usersInSessionCalledTransferredUsernames = {};

        @Override
        public void usersInSession(String[] usernames) {
            usersInSessionCalled = true;
            usersInSessionCalledTransferredUsernames = usernames;
        }

        public boolean sendGameStateCalled = false;
        public char[] sendGameStateTransferredRackTiles = {};
        public char[] sendGameStateTransferredSwapTiles = {};
        public GameData sendGameStateTransferredGameData = null;

        @Override
        public void sendGameData(char[] rackTiles, char[] swapTiles, GameData gameData) {
            sendGameStateCalled = true;
            sendGameStateTransferredRackTiles = rackTiles;
            sendGameStateTransferredSwapTiles = swapTiles;
            sendGameStateTransferredGameData = gameData;
        }

        public boolean voteCalled = false;
        public String[] voteTransferredPlacedWords = {};

        @Override
        public void vote(String[] placedWords) {
            voteCalled = true;
            voteTransferredPlacedWords = placedWords;
            System.out.println(Arrays.toString(placedWords));
        }
    }
}