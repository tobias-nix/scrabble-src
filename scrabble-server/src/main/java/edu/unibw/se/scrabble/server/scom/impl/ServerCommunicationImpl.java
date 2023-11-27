package edu.unibw.se.scrabble.server.scom.impl;

import edu.unibw.se.scrabble.common.base.ReturnValues;
import edu.unibw.se.scrabble.common.scom.NetworkConnect;
import edu.unibw.se.scrabble.common.scom.ToClient;
import edu.unibw.se.scrabble.server.auth.Credentials;
import edu.unibw.se.scrabble.server.logic.ServerConnect;
import edu.unibw.se.scrabble.server.logic.ServerConnectCallback;
import edu.unibw.se.scrabble.server.scom.ServerCommunication;

public class ServerCommunicationImpl implements ServerCommunication, NetworkConnect {

    private Credentials credentials;

    @Override
    public NetworkConnect getNetworkConnect() {
        return this;
    }

    @Override
    public void setServerConnect(ServerConnect serverConnect) {
        //serverConnect.setServerConnectCallback(this); //implements ServerConnectCallback
    }

    @Override
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public ReturnLoginNetwork loginUser(String username, String password, ToClient toClient) {
        return null;
    }

    @Override
    public ReturnValues.ReturnRegisterUser registerUser(String username, String password) {
        return credentials.registerUser(username, password);
    }
}
