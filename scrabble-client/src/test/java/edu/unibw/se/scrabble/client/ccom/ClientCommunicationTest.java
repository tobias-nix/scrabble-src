package edu.unibw.se.scrabble.client.ccom;

import edu.unibw.se.scrabble.common.base.ActionState;
import edu.unibw.se.scrabble.common.base.GameData;
import edu.unibw.se.scrabble.common.base.ReturnValues;
import edu.unibw.se.scrabble.common.base.TileWithPosition;
import edu.unibw.se.scrabble.common.scom.NetworkConnect;
import edu.unibw.se.scrabble.common.scom.ToClient;
import edu.unibw.se.scrabble.common.scom.ToServer;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ClientCommunicationTest {
    public abstract ClientCommunication getClientCommunication();
    public abstract NetworkConnect getNetworkConnect();

 /*
 gameId= 12345
 user = karl , paul(host)
 password = password1, password2
 statistics = 0,0,0,0
  */
    @Test
    void testJoinSession(){
        //getClientCommunication().setNetworkConnect(getNetworkConnect());
        NetworkConnectTest nct = new NetworkConnectTest();
        getClientCommunication().setNetworkConnect(nct);

        ClientConnect cc=getClientCommunication().getClientConnect();
        ClientConnectCallbackTest ccct = new ClientConnectCallbackTest();
        cc.setClientConnectCallback(ccct);

        ReturnValues.ReturnLoginUser rlu = cc.loginUser("karl", "password1");
        assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL, rlu);
        int gameId = 12345;
        ReturnValues.ReturnJoinSession rjs = cc.joinSession(gameId);
        assertEquals(ReturnValues.ReturnJoinSession.SUCCESSFUL, rjs);
        assertTrue(nct.toServer.joinedSessionCalled);
        assertTrue(ccct.userInSessionCalled);
    }
    class NetworkConnectTest implements NetworkConnect {
        public ToServerTest toServer = null;


        @Override
        public ReturnLoginNetwork loginUser(String username, String password, ToClient toClient) {
            if (Objects.equals(username, "karl") && Objects.equals(password, "password1")) {
                toServer = new ToServerTest();
                return new ReturnLoginNetwork(ReturnValues.ReturnLoginUser.SUCCESSFUL, toServer);
            }
            return ReturnLoginNetwork.NETWORK_FAILURE;
        }

        @Override
        public ReturnValues.ReturnRegisterUser registerUser(String username, String password) {
            return null;
        }
    }

    class ToServerTest implements ToServer {

        @Override
        public ReturnValues.ReturnStatistics getUserStatistics() throws RemoteException {
            return null;
        }

        @Override
        public ReturnValues.ReturnCreateSession createSession() throws RemoteException {
            return null;
        }
public boolean joinedSessionCalled = false;
        @Override
        public ReturnValues.ReturnJoinSession joinSession(int gameID) throws RemoteException {
            joinedSessionCalled = true;
            if (gameID == 12345) {
                return ReturnValues.ReturnJoinSession.SUCCESSFUL;
            }
            return ReturnValues.ReturnJoinSession.NETWORK_FAILURE;
        }

        @Override
        public ReturnValues.ReturnStartGame startGame() throws RemoteException {
            return null;
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
    }

    class ToClientTest implements ToClient {

        @Override
        public void usersInSession(String[] usernames) throws RemoteException {

        }

        @Override
        public void sendGameState(char[] rackTiles, char[] swapTiles, GameData gameData) throws RemoteException {

        }

        @Override
        public ReturnValues.ReturnPlayerVote vote(String[] placedWords) throws RemoteException {
            return null;
        }
    }
    class ClientConnectCallbackTest implements ClientConnectCallback {
public boolean userInSessionCalled = false;
        @Override
        public void usersInSession(String[] usernames) {
            userInSessionCalled = true;
        }

        @Override
        public void sendGameState(char[] rackTiles, char[] swapTiles, GameData gameData) {

        }

        @Override
        public ReturnValues.ReturnPlayerVote vote(String[] placedWords) {
            return null;
        }
    }
}