package edu.unibw.se.scrabble.server.logic;

import edu.unibw.se.scrabble.common.base.*;
import edu.unibw.se.scrabble.server.data.ScrabbleData;
import org.junit.jupiter.api.*;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test-Class to test interface ServerConnect
 * @author Kompalka
 */

@DisplayName("Server Communication Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ServerLogicTest {

    private static ScrabbleDataTest scrabbleDataTest;
    private static ServerLogic serverLogic;
    private static ServerConnect serverConnect;
    private static ServerConnectCallbackTest serverConnectCallbackTest;
    public static String getUserStatisticsUsername = "";

    public abstract ServerLogic getServerLogic();

    @BeforeEach
    void init() {
        serverLogic = getServerLogic();

        scrabbleDataTest = new ScrabbleDataTest();
        serverLogic.setScrabbleData(scrabbleDataTest);

        serverConnect = serverLogic.getServerConnect();

        serverConnectCallbackTest = new ServerConnectCallbackTest();
        serverConnect.setServerConnectCallback(serverConnectCallbackTest);
    }

    @Test
    public void getUserStatisticsSuccessful() {
        ReturnValues.ReturnStatistics returnStatistics =
                serverConnect.getUserStatistics("karl");
        assertEquals(returnStatistics.state(), ReturnValues.ReturnStatisticsState.SUCCESSFUL);
        assertEquals(getUserStatisticsUsername, "karl");
        assertEquals(returnStatistics.userStatistics(),
                new Statistics(1, 2, 3, 4));
        assertTrue(scrabbleDataTest.getUserStatisticsCalled);
    }

    @Test
    public void joinSessionSuccessful() {
        ReturnValues.ReturnJoinSession returnJoinSession =
                serverConnect.joinSession(12345, "karl");
        assertEquals(ReturnValues.ReturnJoinSession.SUCCESSFUL, returnJoinSession);
        assertTrue(serverConnectCallbackTest.usersInSessionCalled);
    }

    @Test
    public void createSessionSuccessful() {
        serverLogic.setServerState(0);
        ReturnValues.ReturnCreateSession returnCreateSession =
                serverConnect.createSession("karl");
        assertEquals(returnCreateSession.state(), ReturnValues.ReturnCreateSessionState.SUCCESSFUL);
        assertEquals(returnCreateSession.gameID(), 77777);
    }

    @Test
    public void createSessionFailedSessionLimitReached() {
        serverLogic.setServerState(10);
        ReturnValues.ReturnCreateSession returnCreateSession =
                serverConnect.createSession("karl");
        assertEquals(returnCreateSession.state(), ReturnValues.ReturnCreateSessionState.SESSION_LIMIT_REACHED);
        assertEquals(returnCreateSession.gameID(), -1);
    }

    @Test
    public void startGameSuccessful() {
        serverLogic.setServerState(10); //TODO
        ReturnValues.ReturnStartGame returnStartGame = serverConnect.startGame("karl");
        assertEquals(returnStartGame, ReturnValues.ReturnStartGame.SUCCESSFUL);
        assertTrue(serverConnectCallbackTest.sendGameDataCalled);
    }

    @Test
    public void selectActionSuccessfulPass() {
        serverLogic.setServerState(0); // TODO
        ReturnValues.ReturnSelectAction returnSelectAction = serverConnect.selectAction(
                ActionState.PASS, "karl"
        );
        assertEquals(returnSelectAction, ReturnValues.ReturnSelectAction.SUCCESSFUL);
        assertTrue(serverConnectCallbackTest.sendGameDataCalled);
    }

    @Test
    public void selectActionSuccessfulPlace() {
        serverLogic.setServerState(0); // TODO
        ReturnValues.ReturnSelectAction returnSelectAction = serverConnect.selectAction(
                ActionState.PLACE, "karl"
        );
        assertEquals(returnSelectAction, ReturnValues.ReturnSelectAction.SUCCESSFUL);
        assertTrue(serverConnectCallbackTest.sendGameDataCalled);
    }

    @Test
    public void selectActionSuccessfulSwap() {
        serverLogic.setServerState(0); // TODO
        ReturnValues.ReturnSelectAction returnSelectAction = serverConnect.selectAction(
                ActionState.SWAP, "karl"
        );
        assertEquals(returnSelectAction, ReturnValues.ReturnSelectAction.SUCCESSFUL);
        assertTrue(serverConnectCallbackTest.sendGameDataCalled);
    }

    @Test
    public void placeTileSuccessful() {
        serverLogic.setServerState(0); // TODO
        ReturnValues.ReturnPlaceTile returnPlaceTile = serverConnect.placeTile(
                new TileWithPosition('E', 5, 5), "karl"
        );
        assertEquals(returnPlaceTile, ReturnValues.ReturnPlaceTile.SUCCESSFUL);
        assertTrue(serverConnectCallbackTest.sendGameDataCalled);
    }

    @Test
    public void placeSwapSuccessful() {
        serverLogic.setServerState(0); // TODO
        ReturnValues.ReturnSwapTile returnSwapTile = serverConnect.swapTile(
               'E', "karl"
        );
        assertEquals(returnSwapTile, ReturnValues.ReturnSwapTile.SUCCESSFUL);
        assertTrue(serverConnectCallbackTest.sendGameDataCalled);
    }

    static class ScrabbleDataTest implements ScrabbleData {

        public boolean getUserStatisticsCalled = false;

        @Override
        public Statistics getUserStatistics(String username) {
            getUserStatisticsCalled = true;
            getUserStatisticsUsername = username;
            if (Objects.equals(username, "karl")) {
                return new Statistics(1, 2, 3, 4);
            }
            return null;
        }

        public boolean saveUserStatisticsCalled = false;

        @Override
        public boolean saveUserStatistics(String username, Statistics statistics) {
            return false;
        }
    }

    static class ServerConnectCallbackTest implements ServerConnectCallback {

        public boolean usersInSessionCalled = false;
        @Override
        public void usersInSession(String[] usernames) {
            usersInSessionCalled = true;
        }

        public boolean sendGameDataCalled = false;
        @Override
        public void sendGameState(String username, char[] rackTiles, char[] swapTiles, GameData gameData) {
            sendGameDataCalled = true;
        }

        public boolean voteCalled = false;
        @Override
        public void vote(String username, String[] placedWords) {
            voteCalled = true;
        }
    }
}