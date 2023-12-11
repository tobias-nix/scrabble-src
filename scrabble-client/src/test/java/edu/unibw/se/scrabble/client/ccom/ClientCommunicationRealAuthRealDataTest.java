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
import org.junit.jupiter.api.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Client Communication Test with real Data, real Authentication, real Server Communication and real Server Logic.
 */
public abstract class ClientCommunicationRealAuthRealDataTest {
    protected abstract ClientCommunication getClientCommunication();
    private static ServerCommunication serverCommunication;
    private static Authentication authentication;
    private static SpringScrabbleData springDatabase;

    private Client[] clients;

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
    class CreateSessionTests {
        @BeforeEach
        public void beforeSessionTests() {
            clients = new Client[]{new Client("Odie", "OdieOdie1!", getClientCommunication()),
                    new Client("Garfield", "Garfield1!", getClientCommunication())};

            try {
                serverCommunication = new ServerCommunicationImpl();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            ServerLogic serverLogic = new ServerLogicImpl();
            ServerConnect serverConnectReal = serverLogic.getServerConnect();
            serverCommunication.setServerConnect(serverConnectReal);

            authentication = new AuthenticationImpl();
            springDatabase = new SpringScrabbleData();
            //springDatabaseReal.fill();
            springDatabase.clear();

            authentication.setAuthData(springDatabase.getAuthData());
            serverCommunication.setCredentials(authentication.getCredentials());

            Arrays.stream(clients).forEach(client -> {
                client.clientCommunication.setNetworkConnect(serverCommunication.getNetworkConnect());
            });

            Arrays.stream(clients).forEach(client -> {
                springDatabase.getAuthData().createUser(client.username, client.password);
                ReturnValues.ReturnLoginUser returnLoginUser =
                        client.clientCommunication.getClientConnect().loginUser(client.username, client.password);
                assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL, returnLoginUser,
                        "Before Each - Login User");
            });
        }

        @Test
        void initTest() {
            assertNotNull(serverCommunication);
        }

        @Test
        public void createSessionSuccess() {
            ReturnValues.ReturnCreateSession returnCreateSession =
                    clients[0].clientCommunication.getClientConnect().createSession(LanguageSetting.GERMAN);
            assertEquals(ReturnValues.ReturnCreateSessionState.SUCCESSFUL, returnCreateSession.state());
            System.out.println("Session ID: " + returnCreateSession.gameID());
            assertTrue(returnCreateSession.gameID() < 100000 && returnCreateSession.gameID() > 0);
            assertTrue(clients[0].clientConnectCallbackTest.usersInSessionCalled);
            assertArrayEquals(clients[0].clientConnectCallbackTest.usersInSessionCalledTransferredUsernames, new String[]{"Odie"});
        }
    }

    @Nested
    class JoinSessionTests {
        int createdGameId;

        @Test
        public void joinSessionSuccessValidInputOdieCreatesSessionGarfieldJoins() {
            clients = new Client[]{new Client("Odie", "OdieOdie1!", getClientCommunication()),
                    new Client("Garfield", "Garfield1!", getClientCommunication())};

            try {
                serverCommunication = new ServerCommunicationImpl();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            ServerLogic serverLogic = new ServerLogicImpl();
            ServerConnect serverConnectReal = serverLogic.getServerConnect();
            serverCommunication.setServerConnect(serverConnectReal);

            authentication = new AuthenticationImpl();
            springDatabase = new SpringScrabbleData();
            //springDatabaseReal.fill();
            springDatabase.clear();

            authentication.setAuthData(springDatabase.getAuthData());
            serverCommunication.setCredentials(authentication.getCredentials());

            Arrays.stream(clients).forEach(client -> {
                client.clientCommunication.setNetworkConnect(serverCommunication.getNetworkConnect());
            });

            Arrays.stream(clients).forEach(client -> {
                springDatabase.getAuthData().createUser(client.username, client.password);
                ReturnValues.ReturnLoginUser returnLoginUser =
                        client.clientCommunication.getClientConnect().loginUser(client.username, client.password);
                assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL, returnLoginUser,
                        "Before Each - Login User");
            });
            ReturnValues.ReturnCreateSession returnCreateSession =
                    clients[0].clientCommunication.getClientConnect().createSession(LanguageSetting.GERMAN);
            assertEquals(ReturnValues.ReturnCreateSessionState.SUCCESSFUL, returnCreateSession.state());
            System.out.println("Session ID: " + returnCreateSession.gameID());
            assertTrue(returnCreateSession.gameID() < 100000 && returnCreateSession.gameID() > 0);
            createdGameId = returnCreateSession.gameID();

            ReturnValues.ReturnJoinSession returnJoinSession =
                    clients[1].clientCommunication.getClientConnect().joinSession(createdGameId);
            System.out.println("Garfield joined GameID " + createdGameId);
            assertEquals(ReturnValues.ReturnJoinSession.SUCCESSFUL, returnJoinSession);

            Arrays.stream(clients).forEach(client -> {
                assertTrue(client.clientConnectCallbackTest.usersInSessionCalled, "usersInSessionCalled");
                String[] usernames = Arrays.stream(clients).map(e -> e.username).toArray(String[]::new);
                assertArrayEquals(usernames,
                        client.clientConnectCallbackTest.usersInSessionCalledTransferredUsernames,
                        "usersInSessionCalledTransferredUsernames");
            });
        }

        @Test
        public void joinSessionSuccessValidInputOdieCreatesSessionGarfieldJoinsThenNermalJoins() {
            clients = new Client[]{new Client("Odie", "OdieOdie1!", getClientCommunication()),
                    new Client("Garfield", "Garfield1!", getClientCommunication()),
                    new Client("Nermal", "Nermal123!", getClientCommunication())};

            ArrayList<Client> alreadyJoined = new ArrayList<>();

            try {
                serverCommunication = new ServerCommunicationImpl();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            ServerLogic serverLogic = new ServerLogicImpl();
            ServerConnect serverConnectReal = serverLogic.getServerConnect();
            serverCommunication.setServerConnect(serverConnectReal);

            authentication = new AuthenticationImpl();
            springDatabase = new SpringScrabbleData();
            //springDatabaseReal.fill();
            springDatabase.clear();

            authentication.setAuthData(springDatabase.getAuthData());
            serverCommunication.setCredentials(authentication.getCredentials());

            Arrays.stream(clients).forEach(client -> {
                client.clientCommunication.setNetworkConnect(serverCommunication.getNetworkConnect());
            });

            Arrays.stream(clients).forEach(client -> {
                springDatabase.getAuthData().createUser(client.username, client.password);
                ReturnValues.ReturnLoginUser returnLoginUser =
                        client.clientCommunication.getClientConnect().loginUser(client.username, client.password);
                assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL, returnLoginUser,
                        "Before Each - Login User");
            });

            ReturnValues.ReturnCreateSession returnCreateSession =
                    clients[0].clientCommunication.getClientConnect().createSession(LanguageSetting.GERMAN);
            assertEquals(ReturnValues.ReturnCreateSessionState.SUCCESSFUL, returnCreateSession.state());
            System.out.println("Session ID: " + returnCreateSession.gameID());
            assertTrue(returnCreateSession.gameID() < 100000 && returnCreateSession.gameID() > 0);
            createdGameId = returnCreateSession.gameID();
            alreadyJoined.add(clients[0]);

            ReturnValues.ReturnJoinSession returnJoinSession =
                    clients[1].clientCommunication.getClientConnect().joinSession(createdGameId);
            System.out.println("Garfield joined GameID " + createdGameId);
            assertEquals(ReturnValues.ReturnJoinSession.SUCCESSFUL, returnJoinSession);
            alreadyJoined.add(clients[1]);

            alreadyJoined.forEach(client -> {
                assertTrue(client.clientConnectCallbackTest.usersInSessionCalled, "usersInSessionCalled");
                String[] usernames = alreadyJoined.stream().map(e -> e.username).toArray(String[]::new);
                System.out.println(Arrays.toString(usernames));
                assertArrayEquals(usernames,
                        client.clientConnectCallbackTest.usersInSessionCalledTransferredUsernames,
                        "usersInSessionCalledTransferredUsernames");
            });

            returnJoinSession =
                    clients[2].clientCommunication.getClientConnect().joinSession(createdGameId);
            System.out.println("Nermal joined GameID " + createdGameId);
            assertEquals(ReturnValues.ReturnJoinSession.SUCCESSFUL, returnJoinSession);
            alreadyJoined.add(clients[2]);

            alreadyJoined.forEach(client -> {
                assertTrue(client.clientConnectCallbackTest.usersInSessionCalled, "usersInSessionCalled");
                String[] usernames = alreadyJoined.stream().map(e -> e.username).toArray(String[]::new);
                System.out.println(Arrays.toString(usernames));
                assertArrayEquals(usernames,
                        client.clientConnectCallbackTest.usersInSessionCalledTransferredUsernames,
                        "usersInSessionCalledTransferredUsernames");
            });
        }
    }

    @Nested
    class StartGameTests {
        @Test
        public void startGameSuccess() {
            ArrayList<Client> clientList = new ArrayList<>(List.of(
                    new Client("Odie", "OdieOdie1!", getClientCommunication()),
                    new Client("Garfield", "Garfield1!", getClientCommunication()),
                    new Client("Nermal", "Nermal123!", getClientCommunication()),
                    new Client("JonA", "JonA1234!", getClientCommunication())));

            try {
                serverCommunication = new ServerCommunicationImpl();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            ServerLogic serverLogic = new ServerLogicImpl();
            ServerConnect serverConnectReal = serverLogic.getServerConnect();
            serverCommunication.setServerConnect(serverConnectReal);

            authentication = new AuthenticationImpl();
            springDatabase = new SpringScrabbleData();
            springDatabase.clear();

            authentication.setAuthData(springDatabase.getAuthData());
            serverCommunication.setCredentials(authentication.getCredentials());

            clientList.forEach(client -> {
                client.clientCommunication.setNetworkConnect(serverCommunication.getNetworkConnect());
            });

            clientList.forEach(client -> {
                springDatabase.getAuthData().createUser(client.username, client.password);
                ReturnValues.ReturnLoginUser returnLoginUser =
                        client.clientCommunication.getClientConnect().loginUser(client.username, client.password);
                assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL, returnLoginUser,
                        "Login User");
            });

            ReturnValues.ReturnCreateSession returnCreateSession =
                    clientList.getFirst().clientCommunication.getClientConnect().createSession(LanguageSetting.GERMAN);
            assertEquals(ReturnValues.ReturnCreateSessionState.SUCCESSFUL, returnCreateSession.state());
            System.out.println(clientList.getFirst() + " created session with GameID " + returnCreateSession.gameID());
            assertTrue(returnCreateSession.gameID() < 100000 && returnCreateSession.gameID() > 0);
            int createdGameId = returnCreateSession.gameID();


            Client tmpStorageForFirstWhileTheOthersJoin = clientList.removeFirst();

            clientList.forEach(client -> {
                ReturnValues.ReturnJoinSession returnJoinSession =
                        client.clientCommunication.getClientConnect().joinSession(createdGameId);
                System.out.println(client.username + " joined GameID " + createdGameId);
                assertEquals(ReturnValues.ReturnJoinSession.SUCCESSFUL, returnJoinSession);
            });

            clientList.add(0, tmpStorageForFirstWhileTheOthersJoin);

            clientList.forEach(client -> {
                assertTrue(client.clientConnectCallbackTest.usersInSessionCalled, "usersInSessionCalled");
                String[] usernames = clientList.stream().map(e -> e.username).toArray(String[]::new);
                assertArrayEquals(usernames,
                        client.clientConnectCallbackTest.usersInSessionCalledTransferredUsernames,
                        "usersInSessionCalledTransferredUsernames");
            });

            System.out.println("Users in session: " + clientList + "\n");

            ReturnValues.ReturnStartGame returnStartGame =
                    clientList.getFirst().clientCommunication.getClientConnect().startGame();
            assertEquals(ReturnValues.ReturnStartGame.SUCCESSFUL, returnStartGame);
            System.out.println("Game started successfully\n");

            System.out.println("Sent GameData:");
            clientList.forEach(client -> {
                assertTrue(client.clientConnectCallbackTest.sendGameStateCalled, client.username + " sendGameStateCalled");
                System.out.println(client.username + " RackTiles:" + Arrays.toString(client.clientConnectCallbackTest.sendGameStateTransferredRackTiles));
                System.out.println(client.username + " SwapTiles:" + Arrays.toString(client.clientConnectCallbackTest.sendGameStateTransferredSwapTiles));
                System.out.println(client.username + " GameData: " + client.clientConnectCallbackTest.sendGameStateTransferredGameData);
            });
        }
    }

    @Nested
    class SelectActionTests {
        @Test
        public void selectActionSuccess() {
            ArrayList<Client> clientList = new ArrayList<>(List.of(
                    new Client("Odie", "OdieOdie1!", getClientCommunication()),
                    new Client("Garfield", "Garfield1!", getClientCommunication()),
                    new Client("Nermal", "Nermal123!", getClientCommunication()),
                    new Client("JonA", "JonA1234!", getClientCommunication())));

            try {
                serverCommunication = new ServerCommunicationImpl();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            ServerLogic serverLogic = new ServerLogicImpl();
            ServerConnect serverConnectReal = serverLogic.getServerConnect();
            serverCommunication.setServerConnect(serverConnectReal);

            authentication = new AuthenticationImpl();
            springDatabase = new SpringScrabbleData();
            springDatabase.clear();

            authentication.setAuthData(springDatabase.getAuthData());
            serverCommunication.setCredentials(authentication.getCredentials());

            clientList.forEach(client -> {
                client.clientCommunication.setNetworkConnect(serverCommunication.getNetworkConnect());
            });

            clientList.forEach(client -> {
                springDatabase.getAuthData().createUser(client.username, client.password);
                ReturnValues.ReturnLoginUser returnLoginUser =
                        client.clientCommunication.getClientConnect().loginUser(client.username, client.password);
                assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL, returnLoginUser,
                        "Login User");
            });

            ReturnValues.ReturnCreateSession returnCreateSession =
                    clientList.getFirst().clientCommunication.getClientConnect().createSession(LanguageSetting.GERMAN);
            assertEquals(ReturnValues.ReturnCreateSessionState.SUCCESSFUL, returnCreateSession.state());
            System.out.println(clientList.getFirst() + " created session with GameID " + returnCreateSession.gameID());
            assertTrue(returnCreateSession.gameID() < 100000 && returnCreateSession.gameID() > 0);
            int createdGameId = returnCreateSession.gameID();


            Client tmpStorageForFirstWhileTheOthersJoin = clientList.removeFirst();

            clientList.forEach(client -> {
                ReturnValues.ReturnJoinSession returnJoinSession =
                        client.clientCommunication.getClientConnect().joinSession(createdGameId);
                System.out.println(client.username + " joined GameID " + createdGameId);
                assertEquals(ReturnValues.ReturnJoinSession.SUCCESSFUL, returnJoinSession);
            });

            clientList.add(0, tmpStorageForFirstWhileTheOthersJoin);

            clientList.forEach(client -> {
                assertTrue(client.clientConnectCallbackTest.usersInSessionCalled, "usersInSessionCalled");
                String[] usernames = clientList.stream().map(e -> e.username).toArray(String[]::new);
                assertArrayEquals(usernames,
                        client.clientConnectCallbackTest.usersInSessionCalledTransferredUsernames,
                        "usersInSessionCalledTransferredUsernames");
            });

            System.out.println("Users in session: " + clientList + "\n");

            ReturnValues.ReturnStartGame returnStartGame =
                    clientList.getFirst().clientCommunication.getClientConnect().startGame();
            assertEquals(ReturnValues.ReturnStartGame.SUCCESSFUL, returnStartGame);
            System.out.println("Game started successfully\n");

            System.out.println("Sent GameData:");
            clientList.forEach(client -> {
                assertTrue(client.clientConnectCallbackTest.sendGameStateCalled, client.username + " sendGameStateCalled");
                System.out.println(client.username + " RackTiles:" + Arrays.toString(client.clientConnectCallbackTest.sendGameStateTransferredRackTiles));
                System.out.println(client.username + " SwapTiles:" + Arrays.toString(client.clientConnectCallbackTest.sendGameStateTransferredSwapTiles));
                System.out.println(client.username + " GameData: " + client.clientConnectCallbackTest.sendGameStateTransferredGameData);
            });
        }
    }

    @Nested
    class GetUserStatisticsTest {
        @Test
        public void getUserStatisticsTest() {
            Client client = new Client("Garfield", "Garfield1!", getClientCommunication());

            try {
                serverCommunication = new ServerCommunicationImpl();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            ServerLogic serverLogic = new ServerLogicImpl();
            ServerConnect serverConnect = serverLogic.getServerConnect();

            authentication = new AuthenticationImpl();
            springDatabase = new SpringScrabbleData();
            springDatabase.clear();
            serverCommunication.setServerConnect(serverConnect);

            serverLogic.setScrabbleData(springDatabase.getScrabbleData());

            authentication.setAuthData(springDatabase.getAuthData());
            serverCommunication.setCredentials(authentication.getCredentials());

            client.clientCommunication.setNetworkConnect(serverCommunication.getNetworkConnect());

            springDatabase.getAuthData().createUser(client.username, client.password);
            ReturnValues.ReturnLoginUser returnLoginUser =
                    client.clientCommunication.getClientConnect().loginUser(client.username, client.password);
            assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL, returnLoginUser,
                    "Login User");

            ReturnValues.ReturnStatistics returnGetUserStatistics =
                    client.clientCommunication.getClientConnect().getUserStatistics();
            assertEquals(ReturnValues.ReturnStatisticsState.SUCCESSFUL, returnGetUserStatistics.state());
            assertEquals(new Statistics(0,0,0,0),
                    returnGetUserStatistics.userStatistics(), "User statistics equals");
        }
    }

    static class ClientConnectCallbackTest implements ClientConnectCallback {

        public boolean usersInSessionCalled = false;
        public String[] usersInSessionCalledTransferredUsernames = {};

        @Override
        public void usersInSession(String[] usernames) {
            usersInSessionCalled = true;
            usersInSessionCalledTransferredUsernames = usernames;
            System.out.println(Arrays.toString(usernames));
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

        @Override
        public void vote(String[] placedWords) {

        }
    }
}
