package edu.unibw.se.scrabble.server.scom;

import edu.unibw.se.scrabble.common.base.ActionState;
import edu.unibw.se.scrabble.common.base.GameData;
import edu.unibw.se.scrabble.common.base.ReturnValues;
import edu.unibw.se.scrabble.common.base.TileWithPosition;
import edu.unibw.se.scrabble.common.scom.NetworkConnect;
import edu.unibw.se.scrabble.common.scom.ToClient;
import edu.unibw.se.scrabble.server.auth.Credentials;
import edu.unibw.se.scrabble.server.logic.ServerConnect;
import edu.unibw.se.scrabble.server.logic.ServerConnectCallback;
import org.junit.jupiter.api.*;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Server Communication Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ServerCommunicationTest {
    private static CredentialsTest credentialsTest;
    private static ServerConnectTest serverConnectTest;
    private static NetworkConnect networkConnect;
    private static ServerCommunication serverCommunication;
    private static ServerConnectCallbackTest serverConnectCallbackTest;

    public abstract ServerCommunication getServerCommunication();

    @Nested
    class RegisterUserTests {
        @BeforeEach
        void init() {
            serverCommunication = getServerCommunication();

            serverConnectTest = new ServerConnectTest();
            serverCommunication.setServerConnect(serverConnectTest);

            credentialsTest = new CredentialsTest();
            serverCommunication.setCredentials(credentialsTest);

            networkConnect = serverCommunication.getNetworkConnect();
        }

        @Test
        void initTest() {
            assertNotNull(serverCommunication);
            assertNotNull(credentialsTest);
            assertNotNull(serverConnectTest);
            assertNotNull(networkConnect);
        }

        @Test
        public void registerUserValidInput() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("ralf", "ralfralf1!");
            assertEquals(ReturnValues.ReturnRegisterUser.SUCCESSFUL, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("ralf", credentialsTest.registerTransferredUsername);
            assertEquals("ralfralf1!", credentialsTest.registerTransferredPassword);
        }

        @Test
        public void registerUserNullAsUsername() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser(null, "ralfralf1!");
            assertEquals(ReturnValues.ReturnRegisterUser.FAILURE, returnRegisterUser);
            assertFalse(credentialsTest.registerUserCalled);
        }

        @Test
        public void registerUserNullAsPassword() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("ralf", null);
            assertEquals(ReturnValues.ReturnRegisterUser.FAILURE, returnRegisterUser);
            assertFalse(credentialsTest.registerUserCalled);
        }

        @Test
        public void registerUserUsernameAlreadyExists() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("karl", "karlkarl1!");
            assertEquals(ReturnValues.ReturnRegisterUser.USERNAME_ALREADY_EXISTS, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("karl", credentialsTest.registerTransferredUsername);
            assertEquals("karlkarl1!", credentialsTest.registerTransferredPassword);
        }

        @Test
        public void registerUserDatabaseFailure() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("inge", "ingeinge1!");
            assertEquals(ReturnValues.ReturnRegisterUser.DATABASE_FAILURE, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("inge", credentialsTest.registerTransferredUsername);
            assertEquals("ingeinge1!", credentialsTest.registerTransferredPassword);
        }

        @Test
        public void registerUserInvalidUsernameTooShort() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("123", "ingeinge1!");
            assertEquals(ReturnValues.ReturnRegisterUser.INVALID_USERNAME, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("123", credentialsTest.registerTransferredUsername);
            assertEquals("ingeinge1!", credentialsTest.registerTransferredPassword);
        }

        @Test
        public void registerUserInvalidUsernameTooLong() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("1234567890123456", "ingeinge1!");
            assertEquals(ReturnValues.ReturnRegisterUser.INVALID_USERNAME, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("1234567890123456", credentialsTest.registerTransferredUsername);
            assertEquals("ingeinge1!", credentialsTest.registerTransferredPassword);
        }

        @Test
        public void registerUserInvalidPasswordTooShort() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("ralf", "123");
            assertEquals(ReturnValues.ReturnRegisterUser.INVALID_PASSWORD, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("ralf", credentialsTest.registerTransferredUsername);
            assertEquals("123", credentialsTest.registerTransferredPassword);
        }

        @Test
        public void registerUserInvalidPasswordTooLong() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("ralf", "123456789012345678901");
            assertEquals(ReturnValues.ReturnRegisterUser.INVALID_PASSWORD, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("ralf", credentialsTest.registerTransferredUsername);
            assertEquals("123456789012345678901", credentialsTest.registerTransferredPassword);
        }

        @Test
        public void registerUserInvalidPasswordNoSpecialCharacter() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("ralf", "ralfralf1");
            assertEquals(ReturnValues.ReturnRegisterUser.INVALID_PASSWORD, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("ralf", credentialsTest.registerTransferredUsername);
            assertEquals("ralfralf1", credentialsTest.registerTransferredPassword);
        }

        @Test
        public void registerUserInvalidPasswordNoNumber() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("ralf", "ralfralf!");
            assertEquals(ReturnValues.ReturnRegisterUser.INVALID_PASSWORD, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("ralf", credentialsTest.registerTransferredUsername);
            assertEquals("ralfralf!", credentialsTest.registerTransferredPassword);
        }
    }

    @Nested
    class LoginUserTests {
        @BeforeEach
        void init() {
            serverCommunication = getServerCommunication();

            serverConnectTest = new ServerConnectTest();
            serverConnectCallbackTest = new ServerConnectCallbackTest();
            serverConnectTest.setServerConnectCallback(serverConnectCallbackTest);
            serverCommunication.setServerConnect(serverConnectTest);

            credentialsTest = new CredentialsTest();
            serverCommunication.setCredentials(credentialsTest);


            networkConnect = serverCommunication.getNetworkConnect();
        }

        @Test
        void initTest() {
            assertNotNull(serverCommunication);
            assertNotNull(credentialsTest);
            assertNotNull(serverConnectTest);
            assertNotNull(networkConnect);
        }

        @Test
        public void loginUserSuccessfulValidInputUserNotInSession() {
            String usernameTest = "test";
            String passwordTest = "test123!";
            ToClientTest toClientTest = new ToClientTest();
            NetworkConnect.ReturnLoginNetwork returnLoginUser = null;
            try {
                returnLoginUser = networkConnect.loginUser(usernameTest, passwordTest, toClientTest);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL, returnLoginUser.state);
            assertNotNull(returnLoginUser.toServer);
            assertTrue(credentialsTest.loginUserCalled);
            assertEquals(usernameTest, credentialsTest.loginTransferredUsername);
            assertEquals(passwordTest, credentialsTest.loginTransferredPassword);

            assertTrue(serverConnectTest.informAboutUserLoginCalled);
            assertEquals(usernameTest, serverConnectTest.informAboutUserLoginUsernameTransferred);
            assertFalse(toClientTest.sendGameStateCalled);
        }

        @Test
        public void loginUserSuccessfulValidInputUserAlreadyInSession() {
            String usernameTest = "testSession";
            String passwordTest = "test123!";
            ToClientTest toClientTest = new ToClientTest();
            ((ServerConnectCallbackTest) serverConnectTest.serverConnectCallback).setToClient(toClientTest);
            NetworkConnect.ReturnLoginNetwork returnLoginUser = null;
            try {
                returnLoginUser = networkConnect.loginUser(usernameTest, passwordTest, toClientTest);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL, returnLoginUser.state);
            assertNotNull(returnLoginUser.toServer);
            assertTrue(credentialsTest.loginUserCalled);
            assertEquals(usernameTest, credentialsTest.loginTransferredUsername);
            assertEquals(passwordTest, credentialsTest.loginTransferredPassword);

            assertTrue(serverConnectTest.informAboutUserLoginCalled);
            assertEquals(usernameTest, serverConnectTest.informAboutUserLoginUsernameTransferred);
            assertTrue(toClientTest.sendGameStateCalled);
        }
    }

    static class CredentialsTest implements Credentials {
        private static final Pattern usernamePattern = Pattern.compile("[a-zA-Z0-9]{4,15}");
        private static final Pattern passwordPattern = Pattern.compile(
                "^(?=.*?[A-Za-z])(?=.*?[0-9])(?=.*?[?!$%&/()*]).{8,20}$");


        private boolean loginUserCalled = false;
        private String loginTransferredUsername = "";
        private String loginTransferredPassword = "";

        @Override
        public ReturnValues.ReturnLoginUser loginUser(String username, String password) {
            String[] usernames = {"test", "testSession"};
            loginUserCalled = true;
            loginTransferredUsername = username;
            loginTransferredPassword = password;
            if (Arrays.asList(usernames).contains(username) && Objects.equals("test123!", password)) {
                return ReturnValues.ReturnLoginUser.SUCCESSFUL;
            }
            return null;
        }

        private boolean registerUserCalled = false;
        private String registerTransferredUsername = "";
        private String registerTransferredPassword = "";

        @Override
        public ReturnValues.ReturnRegisterUser registerUser(String username, String password) {
            registerUserCalled = true;
            registerTransferredUsername = username;
            registerTransferredPassword = password;
            if (username == null || password == null) {
                return ReturnValues.ReturnRegisterUser.FAILURE;
            }
            if (!usernamePattern.matcher(username).matches()) {
                return ReturnValues.ReturnRegisterUser.INVALID_USERNAME;
            }
            if (!passwordPattern.matcher(password).matches()) {
                return ReturnValues.ReturnRegisterUser.INVALID_PASSWORD;
            }
            if (Objects.equals(username, "inge") && Objects.equals(password, "ingeinge1!")) {
                return ReturnValues.ReturnRegisterUser.DATABASE_FAILURE;
            }
            if (Objects.equals(username, "karl") && Objects.equals(password, "karlkarl1!")) {
                return ReturnValues.ReturnRegisterUser.USERNAME_ALREADY_EXISTS;
            }
            if (Objects.equals(username, "ralf") && Objects.equals(password, "ralfralf1!")) {
                return ReturnValues.ReturnRegisterUser.SUCCESSFUL;
            }
            throw new UnsupportedOperationException();
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

        @Override
        public ReturnValues.ReturnJoinSession joinSession(int gameID, String username) {
            return null;
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

        public boolean informAboutUserLoginCalled = false;
        public String informAboutUserLoginUsernameTransferred = null;

        @Override
        public void informAboutUserLogin(String username) {
            informAboutUserLoginCalled = true;
            informAboutUserLoginUsernameTransferred = username;
            if (Objects.equals("testSession", username)) {
                this.serverConnectCallback.sendGameData(null, null, null, null);
            }
        }
    }

    static class ToClientTest implements ToClient {

        @Override
        public void usersInSession(String[] usernames) throws RemoteException {

        }

        public boolean sendGameStateCalled = false;

        @Override
        public void sendGameState(char[] rackTiles, char[] swapTiles, GameData gameData) throws RemoteException {
            sendGameStateCalled = true;
        }

        @Override
        public ReturnValues.ReturnPlayerVote vote(String[] placedWords) throws RemoteException {
            return null;
        }
    }

    static class ServerConnectCallbackTest implements ServerConnectCallback {
        public ToClient toClient = null;

        public void setToClient(ToClient toClient) {
            this.toClient = toClient;
        }

        @Override
        public void usersInSession(String[] usernames) {

        }

        @Override
        public void sendGameData(String username, char[] rackTiles, char[] swapTiles, GameData gameData) {
            try {
                toClient.sendGameState(rackTiles, swapTiles, gameData);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public ReturnValues.ReturnPlayerVote vote(String username, String[] placedWords) {
            return null;
        }
    }
}