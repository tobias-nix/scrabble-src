package edu.unibw.se.scrabble.server.scom;

import edu.unibw.se.scrabble.common.base.ActionState;
import edu.unibw.se.scrabble.common.base.ReturnValues;
import edu.unibw.se.scrabble.common.base.TileWithPosition;
import edu.unibw.se.scrabble.common.scom.NetworkConnect;
import edu.unibw.se.scrabble.server.auth.Credentials;
import edu.unibw.se.scrabble.server.logic.ServerConnect;
import edu.unibw.se.scrabble.server.logic.ServerConnectCallback;
import org.junit.jupiter.api.*;

import javax.naming.OperationNotSupportedException;
import java.util.Date;
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

    public abstract ServerCommunication getServerCommunication();

    @BeforeAll
    void init() {
        serverCommunication = getServerCommunication();

        serverConnectTest = new ServerConnectTest();
        serverCommunication.setServerConnect(serverConnectTest);

        credentialsTest = new CredentialsTest();
        serverCommunication.setCredentials(credentialsTest);

        networkConnect = serverCommunication.getNetworkConnect();
    }

    @Nested
    class Tests {
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
            assertEquals("ralf", credentialsTest.transferredUsername);
            assertEquals("ralfralf1!", credentialsTest.transferredPassword);
        }

        @Test
        public void registerUserNullAsUsername() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser(null, "ralfralf1!");
            assertEquals(ReturnValues.ReturnRegisterUser.FAILURE, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertNull(credentialsTest.transferredUsername);
            assertEquals("ralfralf1!", credentialsTest.transferredPassword);
        }

        @Test
        public void registerUserNullAsPassword() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("ralf", null);
            assertEquals(ReturnValues.ReturnRegisterUser.FAILURE, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("ralf", credentialsTest.transferredUsername);
            assertNull(credentialsTest.transferredPassword);
        }

        @Test
        public void registerUserUsernameAlreadyExists() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("karl", "karlkarl1!");
            assertEquals(ReturnValues.ReturnRegisterUser.USERNAME_ALREADY_EXISTS, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("karl", credentialsTest.transferredUsername);
            assertEquals("karlkarl1!", credentialsTest.transferredPassword);
        }

        @Test
        public void registerUserDatabaseFailure() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("inge", "ingeinge1!");
            assertEquals(ReturnValues.ReturnRegisterUser.DATABASE_FAILURE, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("inge", credentialsTest.transferredUsername);
            assertEquals("ingeinge1!", credentialsTest.transferredPassword);
        }

        @Test
        public void registerUserInvalidUsernameTooShort() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("123", "ingeinge1!");
            assertEquals(ReturnValues.ReturnRegisterUser.INVALID_USERNAME, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("123", credentialsTest.transferredUsername);
            assertEquals("ingeinge1!", credentialsTest.transferredPassword);
        }

        @Test
        public void registerUserInvalidUsernameTooLong() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("1234567890123456", "ingeinge1!");
            assertEquals(ReturnValues.ReturnRegisterUser.INVALID_USERNAME, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("1234567890123456", credentialsTest.transferredUsername);
            assertEquals("ingeinge1!", credentialsTest.transferredPassword);
        }

        @Test
        public void registerUserInvalidPasswordTooShort() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("ralf", "123");
            assertEquals(ReturnValues.ReturnRegisterUser.INVALID_PASSWORD, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("ralf", credentialsTest.transferredUsername);
            assertEquals("123", credentialsTest.transferredPassword);
        }

        @Test
        public void registerUserInvalidPasswordTooLong() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("ralf", "123456789012345678901");
            assertEquals(ReturnValues.ReturnRegisterUser.INVALID_PASSWORD, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("ralf", credentialsTest.transferredUsername);
            assertEquals("123456789012345678901", credentialsTest.transferredPassword);
        }

        @Test
        public void registerUserInvalidPasswordNoSpecialCharacter() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("ralf", "ralfralf1");
            assertEquals(ReturnValues.ReturnRegisterUser.INVALID_PASSWORD, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("ralf", credentialsTest.transferredUsername);
            assertEquals("ralfralf1", credentialsTest.transferredPassword);
        }

        @Test
        public void registerUserInvalidPasswordNoNumber() {
            ReturnValues.ReturnRegisterUser returnRegisterUser =
                    networkConnect.registerUser("ralf", "ralfralf!");
            assertEquals(ReturnValues.ReturnRegisterUser.INVALID_PASSWORD, returnRegisterUser);
            assertTrue(credentialsTest.registerUserCalled);
            assertEquals("ralf", credentialsTest.transferredUsername);
            assertEquals("ralfralf!", credentialsTest.transferredPassword);
        }
    }

    static class CredentialsTest implements Credentials {
        private static final Pattern usernamePattern = Pattern.compile("[a-zA-Z0-9]{4,15}");
        private static final Pattern passwordPattern = Pattern.compile(
                "^(?=.*?[A-Za-z])(?=.*?[0-9])(?=.*?[?!$%&/()*]).{8,20}$");

        @Override
        public ReturnValues.ReturnLoginUser loginUser(String username, String password) {
            return null;
        }

        private boolean registerUserCalled = false;
        private String transferredUsername = "";
        private String transferredPassword = "";

        @Override
        public ReturnValues.ReturnRegisterUser registerUser(String username, String password) {
            registerUserCalled = true;
            transferredUsername = username;
            transferredPassword = password;
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

        @Override
        public void setServerConnectCallback(ServerConnectCallback serverConnectCallback) {

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
    }
}