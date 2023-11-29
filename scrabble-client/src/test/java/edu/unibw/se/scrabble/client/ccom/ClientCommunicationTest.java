package edu.unibw.se.scrabble.client.ccom;

import edu.unibw.se.scrabble.common.base.*;
import edu.unibw.se.scrabble.common.scom.NetworkConnect;
import edu.unibw.se.scrabble.common.scom.ToClient;
import edu.unibw.se.scrabble.common.scom.ToServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test-Class to test interface ClientConnect
 * <p>
 * Tests:
 * 1. initCheck : Tests the correct initialization of ClientCommunication and ClientConnect
 * 2. testLogin : Tests the login process
 * 3. testRegister : Tests the register process
 * 4. testGetUserStatistics : Tests retrieving user statistics
 * 5. testCreateSession : Tests creating a new game session
 * 6. testJoinSession : Tests joining a game session
 * 7. testStartGame : Tests launching a game
 * 8. testSelectActionPlace : Tests the selection of the “Place” action in the game
 * 9. testSelectActionSwap : Tests the selection of the “Swap” action in the game
 * 10. testSelectActionPass : Tests the selection of the “Pass” action in the game
 * 11. testPlaceTile : Tests dropping a letter on the field
 * 12. testSwapTile : Tests swapping a letter in the game
 * 13. testEndTurn : Tests the end of a turn.
 *
 * @author Nix
 */

public abstract class ClientCommunicationTest {

    protected abstract ClientCommunication getClientCommunication();

    protected abstract NetworkConnect getNetworkConnect(); //TODO

    /*
    gameId = 12345
    user = karl(host) , paul
    password = password1!, password2!
    statistics = 0,0,0,0
     */

    private static NetworkConnectTest nct = null;
    private static ClientConnectCallbackTest ccct = null;
    private static ClientConnect cc = null;
    private static ClientCommunication ccomm = null;


    static int gameId = 12345;
    static String username = "karl";
    static String password = "password1!";
    static Statistics statistics = new Statistics(11, 1, 111, 1111);
    static TileWithPosition tile = new TileWithPosition('N', 7, 8);
    static String[] usernameS = {"karl", "paul", "berta", "anna"}; //TODO: Callback Check joinSession: usersInSession
    static String currentPlayer = "karl";
    static char[] rackTiles = {'N', 'A', 'E'}; //TODO: sendGameData
    static char[] swapTiles = {'U', 'S', 'R', 'T'};
    //static GameData gameData = new GameData(gameId, usernameS,currentPlayer,statistics,)

    char letter = 'N';

    @BeforeEach
    public void init() {
        //clientCommunication = getClientCommunication();
        //clientConnect = clientCommunication.getClientConnect();
        //getClientCommunication().setNetworkConnect(getNetworkConnect());

        nct = new NetworkConnectTest();
        nct.username = this.username;
        nct.password = this.password;
        getClientCommunication().setNetworkConnect(nct);

        cc = getClientCommunication().getClientConnect();
        ccct = new ClientConnectCallbackTest();
        cc.setClientConnectCallback(ccct);

        ccomm = getClientCommunication();

    }

    @Test
    void initCheck() {
        assertNotNull(ccomm);
        assertNotNull(cc);
        assertNotNull(nct);
        assertNotNull(ccct);
    }

    @Test
    void testLogin() {
        ReturnValues.ReturnLoginUser rlu = cc.loginUser(username, password);
        assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL, rlu);
    }

    @Test
    void testRegister() {
        ReturnValues.ReturnRegisterUser rru = cc.registerUser(username, password);
        assertEquals(ReturnValues.ReturnRegisterUser.SUCCESSFUL, rru);
    }

    @Test
    void testGetUserStatistics() {
        ReturnValues.ReturnLoginUser rlu = cc.loginUser(username, password);
        assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL, rlu);

        ReturnValues.ReturnStatistics rss = cc.getUserStatistics();
        assertEquals(ReturnValues.ReturnStatisticsState.SUCCESSFUL, rss.state());
        assertEquals(statistics, rss.userStatistics());
        assertTrue(nct.toServer.getUserStatisticsCalled);
    }

    @Nested
    class SessionTests {
        @BeforeEach
        public void login() {
            ReturnValues.ReturnLoginUser rlu = cc.loginUser(username, password);
            assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL, rlu);
        }

        @Test
        void testCreateSession() {
            ReturnValues.ReturnCreateSession rcs = cc.createSession();
            assertEquals(ReturnValues.ReturnCreateSessionState.SUCCESSFUL, rcs.state());
            assertEquals(gameId, rcs.gameID());
            assertTrue(nct.toServer.creatSessionCalled);
        }

        @Test
        void testJoinSession() {
            ReturnValues.ReturnJoinSession rjs = cc.joinSession(gameId);
            assertEquals(ReturnValues.ReturnJoinSession.SUCCESSFUL, rjs);
            assertTrue(nct.toServer.joinedSessionCalled);
            assertTrue(ccct.userInSessionCalled); //ccct.userInSessionCalled
        }
    }

    @Nested
    class GameTests {
        @BeforeEach
        public void loginAndJoinSession() {
            ReturnValues.ReturnLoginUser rlu = cc.loginUser(username, password);
            assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL, rlu);

            ReturnValues.ReturnJoinSession rjs = cc.joinSession(gameId);
            assertEquals(ReturnValues.ReturnJoinSession.SUCCESSFUL, rjs);
            assertTrue(nct.toServer.joinedSessionCalled);
            assertTrue(ccct.userInSessionCalled);
        }

        @Test
        void testStartGame() {
            ReturnValues.ReturnStartGame rsg = cc.startGame();
            assertEquals(ReturnValues.ReturnStartGame.SUCCESSFUL, rsg);

            //TODO: Callback sendGameData
        }

        @Test
        void testSelectActionPlace() {
            ReturnValues.ReturnSelectAction rsa = cc.selectAction(ActionState.PLACE);
            assertEquals(ReturnValues.ReturnSelectAction.SUCCESSFUL, rsa);
        }

        @Test
        void testSelectActionSwap() {
            ReturnValues.ReturnSelectAction rsa = cc.selectAction(ActionState.SWAP);
            assertEquals(ReturnValues.ReturnSelectAction.SUCCESSFUL, rsa);
        }

        @Test
        void testSelectActionPass() {
            ReturnValues.ReturnSelectAction rsa = cc.selectAction(ActionState.PASS);
            assertEquals(ReturnValues.ReturnSelectAction.SUCCESSFUL, rsa);
        }


        @Test
        void testPlaceTile() {
            ReturnValues.ReturnPlaceTile rpt = cc.placeTile(tile);
            assertEquals(ReturnValues.ReturnPlaceTile.SUCCESSFUL, rpt);
        }

        @Test
        void testSwapTile() {
            ReturnValues.ReturnSwapTile rst = cc.swapTile(letter);
            assertEquals(ReturnValues.ReturnSwapTile.SUCCESSFUL, rst);
        }

        @Test
        void testEndTurn() {
            ReturnValues.ReturnEndTurn ret = cc.endTurn();
            assertEquals(ReturnValues.ReturnEndTurn.SUCCESSFUL, ret);
        }

    }


    static class NetworkConnectTest implements NetworkConnect {
        public ToServerTest toServer = null; //+toClient
        public ToClient toClient = null;
        public String username;
        public String password;

        @Override
        public ReturnLoginNetwork loginUser(String username, String password, ToClient toClient) {
            if (Objects.equals(username, this.username) && Objects.equals(password, this.password)) {
                toClient = this.toClient;
                toServer = new ToServerTest();
                toServer.setToClient(toClient);
                return new ReturnLoginNetwork(ReturnValues.ReturnLoginUser.SUCCESSFUL, toServer);
            }
            return ReturnLoginNetwork.NETWORK_FAILURE;
        }

        @Override
        public ReturnValues.ReturnRegisterUser registerUser(String username, String password) {
            return ReturnValues.ReturnRegisterUser.SUCCESSFUL;
        }
    }

    static class ToServerTest implements ToServer {
        public ToClient toClient = null;
        public boolean getUserStatisticsCalled = false;
        public boolean creatSessionCalled = false;
        public boolean startGameCalled = false;

        public ClientConnectCallbackTest callback = null;

        public void setToClient(ToClient tc) {
            this.toClient = tc;
        }

        @Override
        public ReturnValues.ReturnStatistics getUserStatistics() throws RemoteException {
            getUserStatisticsCalled = true; //ToServerImpl.get...
            return new ReturnValues.ReturnStatistics(ReturnValues.ReturnStatisticsState.SUCCESSFUL, new Statistics(11, 1, 111, 1111));
        }

        @Override
        public ReturnValues.ReturnCreateSession createSession() throws RemoteException {
            creatSessionCalled = true;
            return new ReturnValues.ReturnCreateSession(ReturnValues.ReturnCreateSessionState.SUCCESSFUL, 12345);
        }

        public boolean joinedSessionCalled = false;

        @Override
        public ReturnValues.ReturnJoinSession joinSession(int gameID) throws RemoteException {
            joinedSessionCalled = true;
            if (gameID == 12345) {
                this.toClient.usersInSession(usernameS);
                return ReturnValues.ReturnJoinSession.SUCCESSFUL;
            }
            return ReturnValues.ReturnJoinSession.NETWORK_FAILURE;
        }

        @Override
        public ReturnValues.ReturnStartGame startGame() throws RemoteException {
            startGameCalled = true;
            this.toClient.sendGameState(rackTiles,swapTiles,gameState);
            return ReturnValues.ReturnStartGame.SUCCESSFUL;
        }

        @Override
        public ReturnValues.ReturnSelectAction selectAction(ActionState actionState) throws RemoteException {
            return null;
        }

        @Override
        public ReturnValues.ReturnPlaceTile placeTile(TileWithPosition tileWithPosition) throws RemoteException {
            return null;
        }

        @Override
        public ReturnValues.ReturnSwapTile swapTile(char letter) {
            return null;
        }

        @Override
        public ReturnValues.ReturnEndTurn endTurn() throws RemoteException {
            return null;
        }

        @Override
        public ReturnValues.ReturnSendPlayerVote sendPlayerVote(PlayerVote playerVote) throws RemoteException {
            return null;
        }
    }

    static class ToClientTest implements ToClient { //TODO: delete
        public final ClientConnectCallbackTest callback;
        public ToClientTest(ClientConnectCallbackTest callback) throws RemoteException {
            this.callback = callback;
        }

        @Override
        public void usersInSession(String[] usernames) throws RemoteException {
            callback.usersInSession(usernames);
        }

        @Override
        public void sendGameState(char[] rackTiles, char[] swapTiles, GameData gameData) throws RemoteException {

        }

        @Override
        public void vote(String[] placedWords) throws RemoteException {

        }
    }

    public static class ClientConnectCallbackTest implements ClientConnectCallback {
        public boolean userInSessionCalled = false;
        public boolean sendGameStateCalled = false;

        @Override
        public void usersInSession(String[] usernames) {
            this.userInSessionCalled = true;
        }

        @Override
        public void sendGameState(char[] rackTiles, char[] swapTiles, GameData gameData) {
            this.sendGameStateCalled = true;
        }

        @Override
        public void vote(String[] placedWords) {

        }
    }
}