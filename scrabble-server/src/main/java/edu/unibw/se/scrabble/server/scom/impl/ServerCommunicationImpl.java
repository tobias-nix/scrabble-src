package edu.unibw.se.scrabble.server.scom.impl;

import edu.unibw.se.scrabble.common.base.ReturnValues;
import edu.unibw.se.scrabble.common.scom.NetworkConnect;
import edu.unibw.se.scrabble.common.scom.ToClient;
import edu.unibw.se.scrabble.server.auth.Credentials;
import edu.unibw.se.scrabble.server.logic.ServerConnect;
import edu.unibw.se.scrabble.server.scom.ServerCommunication;

import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * @author Seegerer
 */
public class ServerCommunicationImpl implements ServerCommunication, NetworkConnect {

    private Credentials credentials;
    private ServerConnect serverConnect;
    private final HashMap<String, ToServerImpl> mapNameToSession = new HashMap<>();

    @Override
    public NetworkConnect getNetworkConnect() {
        return this;
    }

    @Override
    public void setServerConnect(ServerConnect serverConnect) {
        this.serverConnect = serverConnect;
        //serverConnect.setServerConnectCallback(this); //implements ServerConnectCallback
    }

    synchronized void addSession(String username, ToServerImpl toServer) {
        mapNameToSession.put(username, toServer);
    }

    synchronized void removeSession(String username) {
        mapNameToSession.remove(username);
    }

    public void sendUsersInSessionToClient(String username, String[] usernames) {
        if (username == null || usernames == null || !mapNameToSession.containsKey(username)) {
            return;
        }
        ToServerImpl toServer = mapNameToSession.get(username);
        (new Thread(() -> {
            try {
                toServer.toClient.usersInSession(usernames);
            } catch (RemoteException e) {
                this.removeSession(username);
            }
        })).start();
    }
    // TODO: Hier dann die anderen Methoden aus dem Callback?
    //  Was ruft die ServerConnectCallback auf? Direkt toClient oder hier was in der ServerCommunication?
    //  Woher kennt die ServerConnectCallback die ServerCommunication?


    @Override
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public ReturnLoginNetwork loginUser(String username, String password, ToClient toClient) throws RemoteException {
        if (this.credentials == null || username == null || password == null ||
                toClient == null || this.serverConnect == null) {
            return ReturnLoginNetwork.FAILURE;
        }

        ReturnValues.ReturnLoginUser credentialsReturnLoginUser = credentials.loginUser(username, password);

        switch (credentialsReturnLoginUser) {
            case INVALID_PASSWORD:
                return ReturnLoginNetwork.INVALID_PASSWORD;
            case INVALID_USERNAME:
                return ReturnLoginNetwork.INVALID_USERNAME;
            case DATABASE_FAILURE:
                return ReturnLoginNetwork.DATABASE_FAILURE;
            case USERNAME_NOT_IN_DATABASE:
                return ReturnLoginNetwork.USERNAME_NOT_IN_DATABASE;
            case WRONG_PASSWORD:
                return ReturnLoginNetwork.WRONG_PASSWORD;
            case FAILURE:
                return ReturnLoginNetwork.FAILURE;
        }

        ToServerImpl toServer = new ToServerImpl(username, toClient, this, this.serverConnect);
        this.addSession(username, toServer);

        serverConnect.informAboutUserLogin(username);

        return new ReturnLoginNetwork(ReturnValues.ReturnLoginUser.SUCCESSFUL, toServer);
    }

    @Override
    public ReturnValues.ReturnRegisterUser registerUser(String username, String password) {
        if (this.credentials == null || username == null || password == null) {
            return ReturnValues.ReturnRegisterUser.FAILURE;
        }

        return this.credentials.registerUser(username, password);
    }
}
