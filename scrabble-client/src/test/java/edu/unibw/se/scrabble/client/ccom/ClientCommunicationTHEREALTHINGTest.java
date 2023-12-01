package edu.unibw.se.scrabble.client.ccom;

import edu.unibw.se.scrabble.common.base.*;
import edu.unibw.se.scrabble.server.auth.Authentication;
import edu.unibw.se.scrabble.server.auth.impl.AuthenticationImpl;
import edu.unibw.se.scrabble.server.data.impl.spring.SpringScrabbleData;
import edu.unibw.se.scrabble.server.logic.ServerConnect;
import edu.unibw.se.scrabble.server.logic.ServerConnectCallback;
import edu.unibw.se.scrabble.server.scom.ServerCommunication;
import edu.unibw.se.scrabble.server.scom.impl.ServerCommunicationImpl;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Client Communication Test with real Data, real Authentication and real Server Communication.
 * Server Connect is implemented as ServerConnectTest as a nested class.
 */
public abstract class ClientCommunicationTHEREALTHINGTest {
    protected abstract ClientCommunication getClientCommunication();

    private static ClientCommunication clientCommunication;
    private static ClientConnect clientConnect;
    private static ClientConnectCallbackTest clientConnectCallbackTest;
    private static ServerCommunication serverCommunication;
    private static ServerConnectTest serverConnectTest;
    private static Authentication authentication;
    private static SpringScrabbleData springDatabase;

    private Client[] clients;
    private static final char[] testRackTiles = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
    private static final char[] testSwapTiles = {'h', 'i', 'j', 'k', 'l', 'm', 'n'};
    private static final String[] testOneUsersInSession = {"Garfield50"};
    private static final String[] testTwoUsersInSession = {"Odie", "Garfield"};
    private static final String[] testThreeUsersInSession = {"Odie", "JonA", "Nermal"};

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
    }

    @BeforeEach
    void init() {
        clients = new Client[]{new Client("Odie", "OdieOdie1!", getClientCommunication()),
                new Client("Garfield", "Garfield1!", getClientCommunication())};

        serverCommunication = new ServerCommunicationImpl();
        serverConnectTest = new ServerConnectTest();
        serverCommunication.setServerConnect(serverConnectTest);

        authentication = new AuthenticationImpl();
        springDatabase = new SpringScrabbleData();
        //springDatabaseReal.fill();
        springDatabase.clear();

        authentication.setAuthData(springDatabase.getAuthData());
        serverCommunication.setCredentials(authentication.getCredentials());

        Arrays.stream(clients).forEach(client -> {
            client.clientCommunication.setNetworkConnect(serverCommunication.getNetworkConnect());
        });
    }

    /*
    @BeforeEach
    void init() {
        Client client = new Client(getClientCommunication());

        clientCommunication = getClientCommunication();
        clientConnectCallbackTest = new ClientConnectCallbackTest();
        clientCommunication.getClientConnect().setClientConnectCallback(clientConnectCallbackTest);

        serverCommunication = new ServerCommunicationImpl();
        serverConnectTest = new ServerConnectTest();
        serverCommunication.setServerConnect(serverConnectTest);

        authentication = new AuthenticationImpl();
        springDatabase = new SpringScrabbleData();
        //springDatabaseReal.fill();
        springDatabase.clear();

        authentication.setAuthData(springDatabase.getAuthData());
        serverCommunication.setCredentials(authentication.getCredentials());

        clientCommunication.setNetworkConnect(serverCommunication.getNetworkConnect());
        clientConnect = clientCommunication.getClientConnect();
    }
     */

    @Test
    void initTest() {/*
        assertNotNull(clientCommunication);
        assertNotNull(clientConnect);
        assertNotNull(clientConnectCallbackTest);
        assertNotNull(serverCommunication);
        assertNotNull(serverConnectTest);
        assertNotNull(authentication);
        assertNotNull(springDatabase);*/
    }

    @Nested
    class RegisterUserTests {
        @Test
        void registerUserSuccessValidInputUserDoesNotYetExist() {
            String username = "Garfield1";
            String password = "Garfield1!";
            springDatabase.deleteUser(username);

            ReturnValues.ReturnRegisterUser returnRegisterUser = clientConnect.registerUser(username, password);
            assertEquals(ReturnValues.ReturnRegisterUser.SUCCESSFUL, returnRegisterUser);

            assertTrue(springDatabase.getAuthData().usernameExists(username));
            assertEquals(password, springDatabase.getAuthData().getPassword(username));
        }

        @Test
        void registerUserFailureValidInputUserAlreadyExists() {
            String username = "Garfield2";
            String password = "Garfield2!";
            String existingPassword = "OdieOdie1!";
            springDatabase.getAuthData().createUser(username, existingPassword);

            ReturnValues.ReturnRegisterUser returnRegisterUser = clientConnect.registerUser(username, password);
            assertEquals(ReturnValues.ReturnRegisterUser.USERNAME_ALREADY_EXISTS, returnRegisterUser);

            assertTrue(springDatabase.getAuthData().usernameExists(username));
            assertEquals(existingPassword, springDatabase.getAuthData().getPassword(username));
        }
    }


    @Nested
    class LoginUserTests {
        @Test
        void loginUserSuccessfulValidInputUserNotInSession() {
            String username = "Garfield99";
            String password = "Garfield99!";
            springDatabase.getAuthData().createUser(username, password);

            ReturnValues.ReturnLoginUser returnLoginUser = clientConnect.loginUser(username, password);
            assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL, returnLoginUser);

            assertTrue(serverConnectTest.informAboutUserLoginCalled, "informAboutUserLoginCalled");
            assertEquals(username, serverConnectTest.informAboutUserLoginUsernameTransferred);

            assertFalse(clientConnectCallbackTest.sendGameStateCalled, "sendGameStateCalled");
        }

        @Test
        void loginUserSuccessfulValidInputUserAlreadyInSession() {
            String username = "Garfield98";
            String password = "Garfield98!";
            springDatabase.getAuthData().createUser(username, password);

            ReturnValues.ReturnLoginUser returnLoginUser = clientConnect.loginUser(username, password);
            assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL, returnLoginUser);

            assertTrue(serverConnectTest.informAboutUserLoginCalled, "informAboutUserLoginCalled");
            assertEquals(username, serverConnectTest.informAboutUserLoginUsernameTransferred);

            assertTrue(clientConnectCallbackTest.sendGameStateCalled, "sendGameStateCalled");
            assertArrayEquals(testRackTiles, clientConnectCallbackTest.sendGameStateTransferredRackTiles, "rackTiles");
            assertArrayEquals(testSwapTiles, clientConnectCallbackTest.sendGameStateTransferredSwapTiles, "swapTiles");
            assertEquals(GameData.TEST_GAMEDATA, clientConnectCallbackTest.sendGameStateTransferredGameData, "gameData");
        }

        @Test
        void loginUserFailureValidInputWrongPassword() {
            String username = "Garfield97";
            String password = "Garfield97!";
            String wrongPassword = "JonJon123!";
            springDatabase.getAuthData().createUser(username, password);

            ReturnValues.ReturnLoginUser returnLoginUser = clientConnect.loginUser(username, wrongPassword);
            assertEquals(ReturnValues.ReturnLoginUser.WRONG_PASSWORD, returnLoginUser);

            assertFalse(serverConnectTest.informAboutUserLoginCalled, "informAboutUserLoginCalled");

            assertFalse(clientConnectCallbackTest.sendGameStateCalled, "sendGameStateCalled");
        }
    }

    @Nested
    class JoinSessionTests {
        @BeforeEach
        public void beforeSessionTests() {
            Arrays.stream(clients).forEach(client -> {
                springDatabase.getAuthData().createUser(client.username, client.password);
                ReturnValues.ReturnLoginUser returnLoginUser =
                        client.clientCommunication.getClientConnect().loginUser(client.username, client.password);
                assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL, returnLoginUser,
                        "Before Each - Login User");
            });
        }

        @Test
        public void joinSessionSuccessValidInputOdieAlreadyInSessionGarfieldJoins() {
            int testGameId = 99999;
            ReturnValues.ReturnJoinSession returnJoinSession =
                    clients[1].clientCommunication.getClientConnect().joinSession(testGameId);
            assertEquals(ReturnValues.ReturnJoinSession.SUCCESSFUL, returnJoinSession);

            assertTrue(serverConnectTest.joinSessionCalled, "joinSessionCalled");
            assertEquals(testGameId, serverConnectTest.joinSessionTransferredGameId,
                    "joinSessionTransferredGameId");
            assertEquals(clients[1].username, serverConnectTest.joinSessionTransferredUsername,
                    "joinSessionTransferredUsername");

            Arrays.stream(clients).forEach(client -> {
                assertTrue(client.clientConnectCallbackTest.usersInSessionCalled, "usersInSessionCalled");
                assertArrayEquals(testTwoUsersInSession,
                        client.clientConnectCallbackTest.usersInSessionCalledTransferredUsernames,
                        "usersInSessionCalledTransferredUsernames");
            });
        }
    }

    static class ServerConnectTest implements ServerConnect {
        public ServerConnectCallback serverConnectCallback = null;

        @Override
        public void setServerConnectCallback(ServerConnectCallback serverConnectCallback) {
            this.serverConnectCallback = serverConnectCallback;
        }

        @Override
        public ReturnValues.ReturnStatistics getUserStatistics(String username) {
            return null;
        }

        @Override
        public ReturnValues.ReturnCreateSession createSession(String username) {
            return null;
        }


        public boolean joinSessionCalled = false;
        public String joinSessionTransferredUsername = null;
        public int joinSessionTransferredGameId = -1;

        @Override
        public ReturnValues.ReturnJoinSession joinSession(int gameID, String username) {
            joinSessionCalled = true;
            joinSessionTransferredUsername = username;
            joinSessionTransferredGameId = gameID;
            if (gameID == 99999) {
                this.serverConnectCallback.usersInSession(testTwoUsersInSession);
                return ReturnValues.ReturnJoinSession.SUCCESSFUL;
            }
            return ReturnValues.ReturnJoinSession.FAILURE;
        }

        @Override
        public ReturnValues.ReturnStartGame startGame(String username) {
            return null;
        }

        @Override
        public ReturnValues.ReturnSelectAction selectAction(ActionState actionState, String username) {
            return null;
        }

        @Override
        public ReturnValues.ReturnPlaceTile placeTile(TileWithPosition tileWithPosition, String username) {
            return null;
        }

        @Override
        public ReturnValues.ReturnSwapTile swapTile(char letter, String username) {
            return null;
        }

        @Override
        public ReturnValues.ReturnEndTurn endTurn(String username) {
            return null;
        }

        @Override
        public ReturnValues.ReturnSendPlayerVote sendPlayerVote(PlayerVote playerVote) {
            return null;
        }

        public boolean informAboutUserLoginCalled = false;
        public String informAboutUserLoginUsernameTransferred = null;

        @Override
        public void informAboutUserLogin(String username) {
            informAboutUserLoginCalled = true;
            informAboutUserLoginUsernameTransferred = username;
            if (Objects.equals("Garfield98", username)) {
                this.serverConnectCallback.sendGameState(username, testRackTiles, testSwapTiles, GameData.TEST_GAMEDATA);
            }
        }
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
        public void sendGameState(char[] rackTiles, char[] swapTiles, GameData gameData) {
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
